package com.luoying.luochat.common.websocket.service.adapter;

import com.luoying.luochat.common.user.domain.entity.User;
import com.luoying.luochat.common.websocket.domain.enums.WSRespTypeEnum;
import com.luoying.luochat.common.websocket.domain.vo.resp.WSBaseResp;
import com.luoying.luochat.common.websocket.domain.vo.resp.WSLoginSuccess;
import com.luoying.luochat.common.websocket.domain.vo.resp.WSLoginUrl;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/3 15:58
 */
public class WebSocketAdapter {
    public static WSBaseResp<?> buildResp(WxMpQrCodeTicket wxMpQrCodeTicket) {
        WSBaseResp<WSLoginUrl> resp = new WSBaseResp<>();
        WSLoginUrl wsLoginUrl = new WSLoginUrl();
        wsLoginUrl.setLoginUrl(wxMpQrCodeTicket.getUrl());
        resp.setType(WSRespTypeEnum.LOGIN_URL.getType());
        resp.setData(wsLoginUrl);
        return resp;
    }

    public static WSBaseResp<?> buildResp(User user, String token) {
        WSBaseResp<WSLoginSuccess> resp = new WSBaseResp<>();
        WSLoginSuccess wsLoginSuccess = WSLoginSuccess.builder()
                .uid(user.getId())
                .avatar(user.getAvatar())
                .token(token)
                .name(user.getName()).build();
        resp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        resp.setData(wsLoginSuccess);
        return resp;
    }

    public static WSBaseResp<?> buildWaitAuthorizeResp() {
        WSBaseResp<Object> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_SCAN_SUCCESS.getType());
        return resp;
    }
}
