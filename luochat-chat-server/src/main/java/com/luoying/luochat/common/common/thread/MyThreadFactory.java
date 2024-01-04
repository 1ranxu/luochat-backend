package com.luoying.luochat.common.common.thread;

import lombok.AllArgsConstructor;

import java.util.concurrent.ThreadFactory;

@AllArgsConstructor
public class MyThreadFactory implements ThreadFactory {

    public static final MyUncaughtExceptionHandler MYUNCAUGHTEXCEPTIONHANDLER = new MyUncaughtExceptionHandler();
    private ThreadFactory original;

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = original.newThread(r); // 执行原来的ThreadFactory创建线程的逻辑
        // 为创建出来的线程额外装饰上我们需要的异常捕获功能
        thread.setUncaughtExceptionHandler(MYUNCAUGHTEXCEPTIONHANDLER);//异常捕获
        return thread;
    }
}