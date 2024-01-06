package com.luoying.luochat.common.common.interceptor;

import cn.hutool.extra.servlet.ServletUtil;
import com.luoying.luochat.common.common.domain.dto.RequestInfo;
import com.luoying.luochat.common.common.utils.RequestHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/6 12:41
 */
@Component
public class CollectorInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从request的attribute中获取token
        Long uid = Optional.ofNullable(request.getAttribute(TokenInterceptor.UID))
                .map(Object::toString)
                .map(Long::parseLong)
                .get();
        // 获取ip
        String ip = ServletUtil.getClientIP(request);
        // 封装
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUid(uid);
        requestInfo.setIp(ip);
        // 存入ThreadLocal
        RequestHolder.set(requestInfo);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 使用完需要移除
        RequestHolder.remove();
    }
}
