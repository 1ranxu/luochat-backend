package com.luoying.luochat.common.common.config;

import com.luoying.luochat.common.common.interceptor.CollectorInterceptor;
import com.luoying.luochat.common.common.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/6 12:10
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private TokenInterceptor tokenInterceptor;

    @Resource
    private CollectorInterceptor collectorInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {// 注意拦截器的添加顺序
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/capi/**");
        registry.addInterceptor(collectorInterceptor)
                .addPathPatterns("/capi/**");
    }
}
