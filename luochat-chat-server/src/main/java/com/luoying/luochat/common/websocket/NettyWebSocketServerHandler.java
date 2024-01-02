package com.luoying.luochat.common.websocket;

import cn.hutool.json.JSONUtil;
import com.luoying.luochat.common.websocket.domain.enums.WSReqTypeEnum;
import com.luoying.luochat.common.websocket.domain.vo.req.WSBaseReq;
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

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 监听握手完成事件
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            System.out.println("握手完成");
        } else if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                System.out.println("读空闲");
                // todo 用户下线
                ctx.channel().close();
            }
        }
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
                System.out.println("请求二维码");
                // 向前端写回消息
                ctx.channel().writeAndFlush(new TextWebSocketFrame("二维码"));
                break;
            // 心跳检测
            case HEARTBEAT:
                break;
            // 用户认证
            case AUTHORIZE:
                break;
        }
    }
}
