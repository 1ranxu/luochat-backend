package com.luoying.luochat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.luoying.luochat.common.common.constant.RedisKey;
import com.luoying.luochat.common.common.utils.JwtUtils;
import com.luoying.luochat.common.common.utils.RedisUtils;
import com.luoying.luochat.common.user.service.LoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/3 20:46
 */
@Service
public class LoginServiceImpl implements LoginService {
    public static final int TOKEN_EXPIRE_DAYS = 3;
    public static final int TOKEN_RENEWAL_DAYS = 1;
    @Resource
    private JwtUtils jwtUtils;

    @Override
    public void renewalTokenIfNecessary(String token) {
        Long uid = getValidUid(token);
        String userTokenKey = getUserTokenKey(uid);
        Long expireDays = RedisUtils.getExpire(userTokenKey, TimeUnit.DAYS);
        if (expireDays == -2) { // 不存在的key
            return;
        }
        if (expireDays < TOKEN_RENEWAL_DAYS) {
            RedisUtils.expire(getUserTokenKey(uid), TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        }
    }

    @Override
    public String login(Long uid) {
        String token = jwtUtils.createToken(uid);
        RedisUtils.set(getUserTokenKey(uid), token, TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        return token;
    }

    @Override
    public Long getValidUid(String oldToken) {
        Long uid = jwtUtils.getUidOrNull(oldToken);
        if (Objects.isNull(uid)) {
            return null;
        }
        String tokenInRedis = RedisUtils.getStr(getUserTokenKey(uid));
        if (StrUtil.isBlank(tokenInRedis)) {
            return null;
        }
        // oldToken必须与tokenInRedis相同，才能返回uid，如果不做判断直接返回，会导致即使两者不相同也能返回uid
        // 这种情况是不允许发生的，不相同说明用户的oldToken已经过期了且用户又重新登录了一次，此时用户竟然拿着过期的
        // token来测试我们系统的严谨性，当然得让他看看我们的技术了
        return Objects.equals(tokenInRedis, oldToken) ? uid : null;
    }

    private String getUserTokenKey(Long uid) {
        return RedisKey.getKey(RedisKey.USER_TOKEN_STRING, uid);
    }
}
