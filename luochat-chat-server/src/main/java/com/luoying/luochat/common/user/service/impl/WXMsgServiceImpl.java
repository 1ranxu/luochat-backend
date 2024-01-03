package com.luoying.luochat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.luoying.luochat.common.user.dao.UserDao;
import com.luoying.luochat.common.user.domain.entity.User;
import com.luoying.luochat.common.user.service.UserService;
import com.luoying.luochat.common.user.service.WXMsgService;
import com.luoying.luochat.common.user.service.adapter.TextBuilder;
import com.luoying.luochat.common.user.service.adapter.UserAdapter;
import com.luoying.luochat.common.websocket.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/3 17:11
 */
@Service
@Slf4j
public class WXMsgServiceImpl implements WXMsgService {
    @Resource
    private UserDao userDao;

    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private WxMpService wxMpService;

    @Resource
    private WebSocketService webSocketService;

    @Value("${wx.mp.callback}")
    private String callback;

    private static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

    /**
     * openid和登录code的关系map
     */
    private static final ConcurrentHashMap<String, Integer> WAIT_AUTHORIZE_MAP = new ConcurrentHashMap<>();

    /**
     * 用户扫码成功
     *
     * @param wxMpXmlMessage
     * @return
     */
    @Override
    public WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage) {
        String openId = wxMpXmlMessage.getFromUser();
        Integer code = getEventKey(wxMpXmlMessage);
        if (Objects.isNull(code)) {
            return null;
        }
        User user = userDao.getByOpneId(openId);
        // 用户已经注册并授权
        boolean registered = Objects.nonNull(user);
        boolean authorized = registered && StrUtil.isNotBlank(user.getAvatar());
        if (registered && authorized) {
            // 走登录成功的逻辑，通过code找到channel，给前端推送消息
            webSocketService.scanLoginSuccess(code,user.getId());
        }
        // 未注册，先注册
        if (!registered) {
            userService.register(UserAdapter.buildUserSave(openId));
        }
        // 推送链接让用户授权
        WAIT_AUTHORIZE_MAP.put(openId,code);
        webSocketService.waitAuthorize(code);
        String authorizeUrl = String.format(URL, wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode(callback + "/wx/portal/public/callBack"));
        return TextBuilder.build("请点击链接授权：<a href=\"" + authorizeUrl + "\">登录</a>", wxMpXmlMessage);
    }

    @Override
    public void authorize(WxOAuth2UserInfo userInfo) {
        String openid = userInfo.getOpenid();
        User user = userDao.getByOpneId(openid);
        // 更新用户信息
        if (StrUtil.isBlank(user.getAvatar())) {
            fillUserInfo(user.getId(), userInfo);
        }
        Integer code = WAIT_AUTHORIZE_MAP.remove(openid);
        webSocketService.scanLoginSuccess(code,user.getId());
    }

    private void fillUserInfo(Long uid, WxOAuth2UserInfo userInfo) {
        User user = UserAdapter.buildAuthorizeUser(uid, userInfo);
        userDao.updateById(user);

    }

    private Integer getEventKey(WxMpXmlMessage wxMpXmlMessage) {
        try {
            String eventKey = wxMpXmlMessage.getEventKey();
            String code = eventKey.replace("qrscene_", "");
            return Integer.valueOf(code);
        } catch (NumberFormatException e) {
            log.error("getEventKey error eventKey:{}", wxMpXmlMessage.getEventKey(), e);
            return null;
        }
    }
}
