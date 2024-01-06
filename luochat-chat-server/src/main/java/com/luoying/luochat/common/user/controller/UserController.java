package com.luoying.luochat.common.user.controller;


import com.luoying.luochat.common.common.domain.dto.RequestInfo;
import com.luoying.luochat.common.common.domain.vo.resp.ApiResult;
import com.luoying.luochat.common.common.interceptor.TokenInterceptor;
import com.luoying.luochat.common.common.utils.RequestHolder;
import com.luoying.luochat.common.user.domain.vo.resp.UserInfoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author <a href="https://github.com/1ranxu">luoying</a>
 * @since 2024-01-02
 */
@RestController
@RequestMapping("/capi/user")
@Api(tags = "用户相关接口")
public class UserController {

    @GetMapping("/userInfo")
    @ApiOperation("获取用户个人信息")
    public ApiResult<UserInfoResp> getUserInfo() {
        // 使用RequestHolder从ThreadLocal中取出requestInfo
        RequestInfo requestInfo = RequestHolder.get();
        System.out.println(requestInfo.getUid());
        return null;
    }
}

