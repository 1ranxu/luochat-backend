package com.luoying.luochat.common.user.service.impl;

import com.luoying.luochat.common.common.utils.AssertUtil;
import com.luoying.luochat.common.user.dao.UserBackpackDao;
import com.luoying.luochat.common.user.dao.UserDao;
import com.luoying.luochat.common.user.domain.entity.User;
import com.luoying.luochat.common.user.domain.entity.UserBackpack;
import com.luoying.luochat.common.user.domain.enums.ItemEnum;
import com.luoying.luochat.common.user.domain.vo.resp.UserInfoResp;
import com.luoying.luochat.common.user.service.UserService;
import com.luoying.luochat.common.user.service.adapter.UserAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/3 17:41
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Resource
    private UserBackpackDao userBackpackDao;

    @Override
    @Transactional
    public Long register(User user) {
        userDao.save(user);
        // todo 用户注册的事件
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
    public void modifyName(Long uid, String name) {
        User user = userDao.getByName(name);
        // 使用AssertUtil的isEmpty方法判断user是否为空，不为空就会抛出BusinessException
        // errorMsg就是isEmpty方法的第二个参数
        AssertUtil.isEmpty(user,"名字重复，换个名字再尝试吧~~");
        // 获取该用户第一个可用的改名卡，然后使用掉
        UserBackpack modifyNameItem = userBackpackDao.getFirstValidItem(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        AssertUtil.isNotEmpty(modifyNameItem,"无可用改名卡哦");
        // 使用改名卡
        boolean success = userBackpackDao.useItem(modifyNameItem);
        if (success){ // 改名
            userDao.modifyName(uid,name);
        }
    }
}
