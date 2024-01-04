package com.luoying.luochat.common.common.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/4 17:46
 */
@Slf4j
public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Exception ", e);
    }
}
