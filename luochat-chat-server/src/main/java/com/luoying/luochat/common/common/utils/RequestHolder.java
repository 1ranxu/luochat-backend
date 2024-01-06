package com.luoying.luochat.common.common.utils;

import com.luoying.luochat.common.common.domain.dto.RequestInfo;

/**
 * description 请求上下文
 *
 * @Author 落樱的悔恨
 * @Date 2024/1/6 12:47
 */
public class RequestHolder {
    private static final ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<>();

    public static void set(RequestInfo requestInfo) {
        threadLocal.set(requestInfo);
    }

    public static RequestInfo get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
