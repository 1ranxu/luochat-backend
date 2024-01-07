package com.luoying.luochat.common.common.aspect;

import cn.hutool.core.util.StrUtil;
import com.luoying.luochat.common.common.annotation.RedissonLock;
import com.luoying.luochat.common.common.service.LockServcie;
import com.luoying.luochat.common.common.utils.SpElUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/7 18:00
 */
@Component
@Aspect
@Order(0) // 确保比事务注解@Transactional先执行，也就是先加锁，然后开启事务，再提交事务，最后释放锁
public class RedissonAspect {

    @Resource
    private LockServcie lockServcie;

    @Around("@annotation(redissonLock)")
    public Object around(ProceedingJoinPoint joinPoint,RedissonLock redissonLock) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String prefix = StrUtil.isBlank(redissonLock.prefix()) ? SpElUtils.getPrefixByMethod(method) : redissonLock.prefix();
        String key = SpElUtils.getKeyByParseSpEl(method, joinPoint.getArgs(), redissonLock.key());
        return lockServcie.executeWithLock(prefix + ":" + key, redissonLock.waitTime(), redissonLock.timeUnit(), joinPoint::proceed);
    }
}
