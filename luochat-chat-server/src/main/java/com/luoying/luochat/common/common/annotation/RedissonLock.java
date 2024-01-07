package com.luoying.luochat.common.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * description：分布式注解
 *
 * @Author 落樱的悔恨
 * @Date 2024/1/7 17:35
 */
@Retention(RetentionPolicy.RUNTIME)// 运行时生效
@Target({ElementType.METHOD}) // 作用在方法上
public @interface RedissonLock {

    /**
     * key的前缀,取方法的全限定名（例如：class java.lang.String.toUpperCase）
     * 除非我们需要在不同方法对同一个资源做分布式锁，就可以自己指定
     */
    String prefix() default "";

    /**
     * 支持SpringEL表达式的key
     */
    String key();

    /**
     * 重试等待时间，默认重试快速失败
     */
    int waitTime() default -1;

    /**
     * 时间单位，默认毫秒
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
