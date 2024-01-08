package com.luoying.luochat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoying.luochat.common.common.domain.enums.YesOrNoEnum;
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

    public User getByName(String name) {
        return lambdaQuery().eq(User::getName, name).one();
    }

    public void modifyName(Long uid, String name) {
        lambdaUpdate().eq(User::getId, uid).set(User::getName, name).update();
    }

    public void wearBadge(Long uid, Long itemId) {
        lambdaUpdate().eq(User::getId,uid)
                .set(User::getItemId,itemId)
                .update();
    }

    public void invalidUid(Long uid) {
        lambdaUpdate()
                .eq(User::getId,uid)
                .set(User::getStatus, YesOrNoEnum.YES.getStatus())
                .update();
    }
}
