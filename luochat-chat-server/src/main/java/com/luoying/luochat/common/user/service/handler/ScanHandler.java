package com.luoying.luochat.common.user.service.handler;

import com.luoying.luochat.common.user.service.adapter.TextBuilder;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ScanHandler extends AbstractHandler {


    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map,
                                    WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        String code = wxMpXmlMessage.getEventKey();
        String openId = wxMpXmlMessage.getFromUser();
        // todo 扫码
        return TextBuilder.build("你好", wxMpXmlMessage);
    }

}