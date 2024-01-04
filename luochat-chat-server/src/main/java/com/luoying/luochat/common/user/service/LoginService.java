package com.luoying.luochat.common.user.service;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/3 20:45
 */
public interface LoginService {

    /**
     * 刷新token有效期
     *
     * @param token
     */
    void renewalTokenIfNecessary(String token);

    /**
     * 登录成功，获取token
     *
     * @param uid
     * @return 返回token
     */
    String login(Long uid);

    /**
     * 如果token有效，返回uid
     *
     * @param oldToken
     * @return
     */
    Long getValidUid(String oldToken);
}
