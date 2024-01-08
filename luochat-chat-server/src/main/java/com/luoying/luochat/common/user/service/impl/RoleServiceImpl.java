package com.luoying.luochat.common.user.service.impl;

import com.luoying.luochat.common.user.domain.enums.RoleEnum;
import com.luoying.luochat.common.user.service.RoleService;
import com.luoying.luochat.common.user.service.cache.UserCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/8 17:08
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private UserCache userCache;

    @Override
    public RoleEnum hasWhatPower(Long uid) {// 后期做成权限 => 资源模式
        // 获取该用户拥有的所有角色
        Set<Long> roleSet = userCache.getRoleSet(uid);
        // 返回该用户拥有的最高权限
        if (roleSet.contains(RoleEnum.SUPER_ADMIN.getId())) {
            return RoleEnum.SUPER_ADMIN;
        } else if (roleSet.contains(RoleEnum.GENERAL_MANAGER.getId())) {
            return RoleEnum.GENERAL_MANAGER;
        } else
            return null;

    }

}
