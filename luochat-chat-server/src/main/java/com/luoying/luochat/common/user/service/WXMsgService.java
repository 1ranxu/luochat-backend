package com.luoying.luochat.common.user.service;

import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/3 17:10
 */
public interface WXMsgService {
    /**
     * 用户扫码成功
     *
     * @param wxMpXmlMessage
     * @return
     */
    WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage);

    /**
     * 用户授权
     * @param userInfo
     */
    void authorize(WxOAuth2UserInfo userInfo);
}
