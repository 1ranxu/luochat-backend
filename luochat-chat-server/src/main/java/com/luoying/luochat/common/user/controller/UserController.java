package com.luoying.luochat.common.user.controller;


import com.luoying.luochat.common.common.domain.vo.resp.ApiResult;
import com.luoying.luochat.common.common.utils.AssertUtil;
import com.luoying.luochat.common.common.utils.RequestHolder;
import com.luoying.luochat.common.user.domain.enums.RoleEnum;
import com.luoying.luochat.common.user.domain.vo.req.BlackReq;
import com.luoying.luochat.common.user.domain.vo.req.ModifyNameReq;
import com.luoying.luochat.common.user.domain.vo.req.WearBadgeReq;
import com.luoying.luochat.common.user.domain.vo.resp.BadgeResp;
import com.luoying.luochat.common.user.domain.vo.resp.UserInfoResp;
import com.luoying.luochat.common.user.service.RoleService;
import com.luoying.luochat.common.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

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

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @GetMapping("/userInfo")
    @ApiOperation("获取用户个人信息")
    public ApiResult<UserInfoResp> getUserInfo() {
        return ApiResult.success(userService.getUserInfo(RequestHolder.get().getUid()));
    }

    @PutMapping("/name")
    @ApiOperation("修改用户名")
    public ApiResult<Void> modifyName(@Valid @RequestBody ModifyNameReq modifyNameReq) {
        userService.modifyName(RequestHolder.get().getUid(), modifyNameReq.getName());
        return ApiResult.success(null);
    }

    @GetMapping("/badges")
    @ApiOperation("可选徽章预览")
    public ApiResult<List<BadgeResp>> badges() {
        return ApiResult.success(userService.badges(RequestHolder.get().getUid()));
    }

    @PutMapping("/wearBadge")
    @ApiOperation("佩戴徽章")
    public ApiResult<Void> wearBage(@Valid @RequestBody WearBadgeReq wearBadgeReq) {
        return ApiResult.success(userService.wearBage(RequestHolder.get().getUid(), wearBadgeReq.getItemId()));
    }

    @PutMapping("/black")
    @ApiOperation("拉黑用户")
    public ApiResult<Void> black(@Valid @RequestBody BlackReq blackReq) {
        // 获取当前登录用户uid
        Long uid = RequestHolder.get().getUid();
        // 获取当前登录用户的角色
        RoleEnum roleEnum = roleService.hasWhatPower(uid);
        // 判断角色是否为超级管理员
        AssertUtil.isNotEmpty(roleEnum,"无权限");
        AssertUtil.isTrue(RoleEnum.SUPER_ADMIN.getId().equals(roleEnum.getId()), "无权限");
        // 拉黑
        return ApiResult.success(userService.black(blackReq));
    }


}

