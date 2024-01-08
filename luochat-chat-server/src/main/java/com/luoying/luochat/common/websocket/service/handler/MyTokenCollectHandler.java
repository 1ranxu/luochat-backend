package com.luoying.luochat.common.websocket.service.handler;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.StrUtil;
import com.luoying.luochat.common.websocket.NettyUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

import java.net.InetSocketAddress;
import java.util.Optional;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/5 13:29
 */
public class MyTokenCollectHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof HttpRequest) { // 如果是http请求，说明需要进行握手升级
            final HttpRequest request = (HttpRequest) msg;
            UrlBuilder urlBuilder = UrlBuilder.ofHttp(request.getUri());
            Optional<String> tokenOptional = Optional.ofNullable(urlBuilder)
                    .map(UrlBuilder::getQuery)
                    .map(urlQuery -> urlQuery.get("token"))
                    .map(CharSequence::toString);
            // 如果token存在，保存token
            tokenOptional.ifPresent(s -> NettyUtil.setValueToAttr(ctx.channel(), NettyUtil.TOKEN, s));
            // 还原request的路径
            request.setUri(urlBuilder.getPath().toString());
            // 取用户的ip
            String ip = request.headers().get("X-Real-IP");
            if (StrUtil.isBlank(ip)) {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                ip = address.getAddress().getHostAddress();
            }
            // 保存到channel
            NettyUtil.setValueToAttr(ctx.channel(), NettyUtil.IP, ip);
            ctx.channel().pipeline().remove(this);
        }
        ctx.fireChannelRead(msg);
    }
}