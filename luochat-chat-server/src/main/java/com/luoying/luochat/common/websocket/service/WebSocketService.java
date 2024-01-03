package com.luoying.luochat.common.websocket.service;

import io.netty.channel.Channel;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/3 14:55
 */
public interface WebSocketService {
    void connect(Channel channel);

    void handleLoginRequest(Channel channel);

    void remove(Channel channel);

    void scanLoginSuccess(Integer code, Long id);

    void waitAuthorize(Integer code);
}
