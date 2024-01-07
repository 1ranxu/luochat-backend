package com.luoying.luochat.common.websocket;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.luoying.luochat.common.websocket.domain.enums.WSReqTypeEnum;
import com.luoying.luochat.common.websocket.domain.vo.req.WSBaseReq;
import com.luoying.luochat.common.websocket.service.WebSocketService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/1 12:37
 */
@Slf4j
@Sharable
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private WebSocketService webSocketService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        webSocketService = SpringUtil.getBean(WebSocketService.class);
        // 保存连接
        webSocketService.connect(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        userOffline(ctx.channel());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {// 监听握手完成事件
            System.out.println("握手完成");
            String token = NettyUtil.getValueInAttr(ctx.channel(), NettyUtil.TOKEN);
            // 有些用户是第一次登录，没有token
            if (StrUtil.isNotBlank(token)) {
                webSocketService.authorize(ctx.channel(), token);
            }
        } else if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                System.out.println("读空闲");
                //userOffline(ctx.channel());
            }
        }
    }

    /**
     * 用户下线统一处理
     *
     * @param channel
     */
    private void userOffline(Channel channel) {
        webSocketService.remove(channel);
        channel.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 获取消息
        String text = msg.text();
        // 转换为WSBaseReq请求对象
        WSBaseReq wsBaseReq = JSONUtil.toBean(text, WSBaseReq.class);
        // 进行判断
        switch (WSReqTypeEnum.of(wsBaseReq.getType())) {
            // 登录
            case LOGIN:
                // 向前端写回消息
                webSocketService.handleLoginRequest(ctx.channel());
                break;
            // 心跳检测
            case HEARTBEAT:
                break;
            // 用户认证
            case AUTHORIZE:
                webSocketService.authorize(ctx.channel(), wsBaseReq.getData());
                break;
        }
    }
}
