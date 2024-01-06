package com.luoying.luochat.common.common.interceptor;

import com.luoying.luochat.common.common.exception.HttpErrorEnum;
import com.luoying.luochat.common.user.service.LoginService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/6 10:25
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_SCHEMA = "Bearer ";
    public static final String UID = "uid";

    @Resource
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = getToken(request);
        Long validUid = loginService.getValidUid(token);
        if (Objects.nonNull(validUid)) {// 用户有登录态
            request.setAttribute(UID, validUid);
        } else {// 用户未登录
            boolean isPublic = isPublicURI(request);
            if (!isPublic) {// 拦截非public权限的路径，因为未登录只能访问public权限的路径
                HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
                return false;
            }
        }
        return true;
    }

    /**
     * 判断路径是否为public权限
     * @param request
     * @return
     */
    private static boolean isPublicURI(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        boolean isPublic = split.length > 3 && "public".equals(split[3]);
        return isPublic;
    }

    /**
     * 从Authorization请求头中，获取token
     * @param request
     * @return
     */
    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        return Optional.ofNullable(authorization)
                .filter(header -> header.startsWith(AUTHORIZATION_SCHEMA))
                .map(header -> header.replaceFirst(AUTHORIZATION_SCHEMA, ""))
                .orElse(null);
    }
}
