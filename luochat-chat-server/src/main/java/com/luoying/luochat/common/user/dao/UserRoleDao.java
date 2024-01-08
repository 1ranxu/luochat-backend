package com.luoying.luochat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoying.luochat.common.user.domain.entity.UserRole;
import com.luoying.luochat.common.user.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色关系表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/1ranxu">luoying</a>
 * @since 2024-01-08
 */
@Service
public class UserRoleDao extends ServiceImpl<UserRoleMapper, UserRole> {

}
