package com.luoying.luochat.common.user.controller;


import com.luoying.luochat.common.common.domain.vo.req.CursorPageBaseReq;
import com.luoying.luochat.common.common.domain.vo.resp.ApiResult;
import com.luoying.luochat.common.common.domain.vo.resp.CursorPageBaseResp;
import com.luoying.luochat.common.common.utils.RequestHolder;
import com.luoying.luochat.common.user.domain.vo.req.FriendApplyReq;
import com.luoying.luochat.common.user.domain.vo.req.FriendApproveReq;
import com.luoying.luochat.common.user.domain.vo.req.FriendCheckReq;
import com.luoying.luochat.common.user.domain.vo.resp.FriendCheckResp;
import com.luoying.luochat.common.user.domain.vo.resp.FriendResp;
import com.luoying.luochat.common.user.domain.vo.resp.FriendUnreadResp;
import com.luoying.luochat.common.user.service.FriendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * <p>
 * 好友相关接口
 * </p>
 *
 */
@RestController
@RequestMapping("/capi/user/friend")
@Api(tags = "好友相关接口")
@Slf4j
public class FriendController {
    @Resource
    private FriendService friendService;

    @GetMapping("/check")
    @ApiOperation("批量判断是否是自己好友")
    public ApiResult<FriendCheckResp> check(@Valid FriendCheckReq request) {
        // 当前登录用户的uid
        Long uid = RequestHolder.get().getUid();
        // 批量判断是否是自己好友
        return ApiResult.success(friendService.check(uid, request));
    }

    @PostMapping("/apply")
    @ApiOperation("申请好友")
    public ApiResult<Void> apply(@Valid @RequestBody FriendApplyReq request) {
        // 获取当前登录用户uid
        Long uid = RequestHolder.get().getUid();
        // 根据uid, request申请好友
        friendService.apply(uid, request);
        return ApiResult.success();
    }

    @PutMapping("/apply")
    @ApiOperation("审批同意")
    public ApiResult<Void> applyApprove(@Valid @RequestBody FriendApproveReq request) {
        // 根据uid, request审批同意申请
        friendService.applyApprove(RequestHolder.get().getUid(), request);
        return ApiResult.success();
    }

    @GetMapping("/apply/unread")
    @ApiOperation("申请未读数")
    public ApiResult<FriendUnreadResp> unread() {
        // 获取当前登录用户uid
        Long uid = RequestHolder.get().getUid();
        // 根据查询登录用户的申请未读数
        return ApiResult.success(friendService.unread(uid));
    }

    @GetMapping("/page")
    @ApiOperation("联系人列表")
    public ApiResult<CursorPageBaseResp<FriendResp>> friendList(@Valid CursorPageBaseReq request) {
        // 当前登录用户的uid
        Long uid = RequestHolder.get().getUid();
        // 获取当前登录用户的联系人列表
        return ApiResult.success(friendService.friendList(uid, request));
    }
}

