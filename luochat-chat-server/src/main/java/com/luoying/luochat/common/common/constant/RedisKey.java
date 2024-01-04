package com.luoying.luochat.common.common.constant;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/4 14:54
 */
public class RedisKey {
    private static final String BASE_KEY = "luochat:chat:";

    /**
     * 用户token的key
     */
    public static final String USER_TOKEN_STRING = "userToken:uid_%d";

    public static String getKey(String key, Object... o) {
        return BASE_KEY + String.format(key, o);
    }
}
