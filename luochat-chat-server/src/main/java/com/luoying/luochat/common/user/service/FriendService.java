package com.luoying.luochat.common.user.service;

import com.luoying.luochat.common.common.domain.vo.req.CursorPageBaseReq;
import com.luoying.luochat.common.common.domain.vo.req.PageBaseReq;
import com.luoying.luochat.common.common.domain.vo.resp.CursorPageBaseResp;
import com.luoying.luochat.common.common.domain.vo.resp.PageBaseResp;
import com.luoying.luochat.common.user.domain.vo.req.FriendApplyReq;
import com.luoying.luochat.common.user.domain.vo.req.FriendApproveReq;
import com.luoying.luochat.common.user.domain.vo.req.FriendCheckReq;
import com.luoying.luochat.common.user.domain.vo.resp.FriendApplyResp;
import com.luoying.luochat.common.user.domain.vo.resp.FriendCheckResp;
import com.luoying.luochat.common.user.domain.vo.resp.FriendResp;
import com.luoying.luochat.common.user.domain.vo.resp.FriendUnreadResp;

/**
 * @description : 好友
 */
public interface FriendService {
    /**
     * 检查是否是自己好友
     * @param request 请求
     * @param uid     uid
     * @return {@link FriendCheckResp}
     */
    FriendCheckResp check(Long uid, FriendCheckReq request);

    /**
     * 申请好友
     *
     * @param request 请求
     * @param uid     uid
     */
    void apply(Long uid, FriendApplyReq request);

    /**
     * 同意好友申请
     *
     * @param uid     uid
     * @param request 请求
     */
    void applyApprove(Long uid, FriendApproveReq request);

    /**
     * 申请未读数
     *
     * @return {@link FriendUnreadResp}
     */
    FriendUnreadResp unread(Long uid);

    /**
     * 分页查询好友申请
     *
     * @param request 请求
     * @return {@link PageBaseResp}<{@link FriendApplyResp}>
     */
    PageBaseResp<FriendApplyResp> pageApplyFriend(Long uid, PageBaseReq request);

    /**
     * 获取好友列表
     * @param uid
     * @param request
     * @return
     */
    CursorPageBaseResp<FriendResp> friendList(Long uid, CursorPageBaseReq request);
}
