package com.luoying.luochat.common.websocket;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/5 16:43
 */
public class NettyUtil {
    public static AttributeKey<String> TOKEN = AttributeKey.valueOf("token");
    public static AttributeKey<String> IP = AttributeKey.valueOf("ip");

    public static <T> void setValueToAttr(Channel channel,AttributeKey<T> key,T vlaue){
        Attribute<T> attr = channel.attr(key);
        attr.set(vlaue);
    }

    public static <T> T getValueInAttr(Channel channel,AttributeKey<T> key){
        Attribute<T> attr = channel.attr(key);
        return attr.get();
    }
}
