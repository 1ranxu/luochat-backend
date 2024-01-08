package com.luoying.luochat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.luoying.luochat.common.common.annotation.RedissonLock;
import com.luoying.luochat.common.common.event.UserBlackEvent;
import com.luoying.luochat.common.common.event.UserRegisterEvent;
import com.luoying.luochat.common.common.utils.AssertUtil;
import com.luoying.luochat.common.user.dao.BlackDao;
import com.luoying.luochat.common.user.dao.ItemConfigDao;
import com.luoying.luochat.common.user.dao.UserBackpackDao;
import com.luoying.luochat.common.user.dao.UserDao;
import com.luoying.luochat.common.user.domain.entity.Black;
import com.luoying.luochat.common.user.domain.entity.ItemConfig;
import com.luoying.luochat.common.user.domain.entity.User;
import com.luoying.luochat.common.user.domain.entity.UserBackpack;
import com.luoying.luochat.common.user.domain.enums.BlackTypeEnum;
import com.luoying.luochat.common.user.domain.enums.ItemEnum;
import com.luoying.luochat.common.user.domain.enums.ItemTypeEnum;
import com.luoying.luochat.common.user.domain.vo.req.BlackReq;
import com.luoying.luochat.common.user.domain.vo.resp.BadgeResp;
import com.luoying.luochat.common.user.domain.vo.resp.UserInfoResp;
import com.luoying.luochat.common.user.service.UserService;
import com.luoying.luochat.common.user.service.adapter.UserAdapter;
import com.luoying.luochat.common.user.service.cache.ItemCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/3 17:41
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Resource
    private UserBackpackDao userBackpackDao;

    @Resource
    private ItemCache itemCache;

    @Resource
    private ItemConfigDao itemConfigDao;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private BlackDao blackDao;

    @Override
    @Transactional
    public Long register(User user) {
        userDao.save(user);
        // 用户注册的事件
        applicationEventPublisher.publishEvent(new UserRegisterEvent(this,user));
        return user.getId();
    }

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User user = userDao.getById(uid);
        // 获取该用户未使用的改名卡个数
        Integer modifyNameCount = userBackpackDao.getCountByItemId(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        return UserAdapter.buildUserInfoResp(user, modifyNameCount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedissonLock(key = "#uid")
    public void modifyName(Long uid, String name) {
        User user = userDao.getByName(name);
        // 使用AssertUtil的isEmpty方法判断user是否为空，不为空就会抛出BusinessException
        // errorMsg就是isEmpty方法的第二个参数
        AssertUtil.isEmpty(user, "名字重复，换个名字再尝试吧~~");
        // 获取该用户第一个可用的改名卡，然后使用掉
        UserBackpack modifyNameItem = userBackpackDao.getFirstValidItem(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        AssertUtil.isNotEmpty(modifyNameItem, "无可用改名卡哦");
        // 使用改名卡
        boolean success = userBackpackDao.useItem(modifyNameItem);
        if (success) { // 改名
            userDao.modifyName(uid, name);
        }
    }

    @Override
    public List<BadgeResp> badges(Long uid) {
        // 查询所有徽章
        List<ItemConfig> itemConfigs = itemCache.getByType(ItemTypeEnum.BADGE.getType());
        // 查询用户拥有的徽章
        List<Long> itemIds = itemConfigs.stream().map(ItemConfig::getId).collect(Collectors.toList());
        List<UserBackpack> backpacks = userBackpackDao.getByItemIds(uid, itemIds);
        // 查询用户佩戴的徽章
        User user = userDao.getById(uid);

        return UserAdapter.buildBadgesResp(itemConfigs, backpacks, user.getItemId());
    }

    @Override
    public Void wearBage(Long uid, Long itemId) {
        // 确保拥有徽章
        UserBackpack validItem = userBackpackDao.getFirstValidItem(uid, itemId);
        AssertUtil.isNotEmpty(validItem, "暂未获得该徽章哦~~");
        // 确保这个物品是徽章
        ItemConfig itemConfig = itemConfigDao.getById(itemId);
        AssertUtil.equal(itemConfig.getType(), ItemTypeEnum.BADGE.getType(), "只有徽章才能佩戴，不要尝试佩戴奇怪的东西哦~~");
        // 佩戴徽章
        userDao.wearBadge(uid, itemId);
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Void black(BlackReq blackReq) {
        // 获取要拉黑的uid
        Long uid = blackReq.getUid();
        // 拉黑uid
        Black black = new Black();
        black.setType(BlackTypeEnum.UID.getType());
        black.setTarget(uid.toString());
        blackDao.save(black);
        User user = userDao.getById(uid);
        // 拉黑ip
        blackIp(user.getIpInfo().getCreateIp());
        blackIp(user.getIpInfo().getUpdateIp());
        // 发送用户拉黑事件
        applicationEventPublisher.publishEvent(new UserBlackEvent(this,user));
        return null;
    }

    public void blackIp(String ip) {
        if (StrUtil.isBlank(ip)) { // ip有可能为空
            return;
        }
        try {
            // 拉黑ip
            Black black = new Black();
            black.setType(BlackTypeEnum.IP.getType());
            black.setTarget(ip);
            blackDao.save(black);
        } catch (Exception e) {
            log.error("duplicate black ip:{}", ip);
        }
    }
}
