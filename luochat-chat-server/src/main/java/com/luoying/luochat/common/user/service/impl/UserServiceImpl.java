package com.luoying.luochat.common.user.service.impl;

import com.luoying.luochat.common.user.dao.UserDao;
import com.luoying.luochat.common.user.domain.entity.User;
import com.luoying.luochat.common.user.service.UserService;
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

    @Override
    @Transactional
    public Long register(User user) {
        userDao.save(user);
        // todo 用户注册的事件
        return user.getId();
    }
}
