package com.luoying.luochat.common.user.service;

import com.luoying.luochat.common.user.domain.entity.User;
import com.luoying.luochat.common.user.domain.vo.resp.UserInfoResp;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author <a href="https://github.com/1ranxu">luoying</a>
 * @since 2024-01-02
 */
public interface UserService {

    Long register(User user);

    UserInfoResp getUserInfo(Long uid);

    void modifyName(Long uid, String name);
}
