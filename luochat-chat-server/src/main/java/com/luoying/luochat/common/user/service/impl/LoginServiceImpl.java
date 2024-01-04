package com.luoying.luochat.common.user.service.impl;

import com.luoying.luochat.common.common.utils.JwtUtils;
import com.luoying.luochat.common.user.service.LoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/3 20:46
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private JwtUtils jwtUtils;

    @Override
    public void renewalTokenIfNecessary(String token) {

    }

    @Override
    public String login(Long uid) {
        return jwtUtils.createToken(uid);
    }

    @Override
    public Long getValidUid(String token) {
        return null;
    }
}
