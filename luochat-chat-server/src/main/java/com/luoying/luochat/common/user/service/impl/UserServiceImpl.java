package com.luoying.luochat.common.user.service.impl;

import com.luoying.luochat.common.common.exception.BusinessException;
import com.luoying.luochat.common.user.dao.UserBackpackDao;
import com.luoying.luochat.common.user.dao.UserDao;
import com.luoying.luochat.common.user.domain.entity.User;
import com.luoying.luochat.common.user.domain.enums.ItemEnum;
import com.luoying.luochat.common.user.domain.vo.resp.UserInfoResp;
import com.luoying.luochat.common.user.service.UserService;
import com.luoying.luochat.common.user.service.adapter.UserAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

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
    public void modifyName(Long uid, String name) {
        User user = userDao.getByName(name);
        if (Objects.nonNull(user)){
            throw new BusinessException("名字重复，换个名字再尝试吧~~");
        }
    }
}
