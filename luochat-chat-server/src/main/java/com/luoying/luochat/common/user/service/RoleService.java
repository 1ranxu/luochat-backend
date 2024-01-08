package com.luoying.luochat.common.user.service;

import com.luoying.luochat.common.user.domain.enums.RoleEnum;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author <a href="https://github.com/1ranxu">luoying</a>
 * @since 2024-01-08
 */
public interface RoleService {
    /**
     * 是否有某个权限，临时做法
     *
     * @return
     */
    RoleEnum hasWhatPower(Long uid);
}
