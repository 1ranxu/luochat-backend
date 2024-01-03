package com.luoying.luochat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoying.luochat.common.user.domain.entity.User;
import com.luoying.luochat.common.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/1ranxu">luoying</a>
 * @since 2024-01-02
 */
@Service
public class UserDao extends ServiceImpl<UserMapper, User> {

    public User getByOpneId(String openId) {
        return lambdaQuery().eq(User::getOpenId, openId).one();
    }
}
