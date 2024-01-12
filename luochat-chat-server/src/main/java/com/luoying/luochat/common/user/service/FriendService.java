package com.luoying.luochat.common.user.service;

import com.luoying.luochat.common.common.domain.vo.req.CursorPageBaseReq;
import com.luoying.luochat.common.common.domain.vo.resp.CursorPageBaseResp;
import com.luoying.luochat.common.user.domain.vo.resp.FriendResp;

/**
 * @description : 好友
 */
public interface FriendService {
    /**
     * 获取好友列表
     * @param uid
     * @param request
     * @return
     */
    CursorPageBaseResp<FriendResp> friendList(Long uid, CursorPageBaseReq request);
}
