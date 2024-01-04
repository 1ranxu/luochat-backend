package com.luoying.luochat.common.websocket.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.luoying.luochat.common.user.dao.UserDao;
import com.luoying.luochat.common.user.domain.entity.User;
import com.luoying.luochat.common.user.service.LoginService;
import com.luoying.luochat.common.websocket.domain.dto.WSChannelExtraDTO;
import com.luoying.luochat.common.websocket.domain.vo.resp.WSBaseResp;
import com.luoying.luochat.common.websocket.service.WebSocketService;
import com.luoying.luochat.common.websocket.service.adapter.WebSocketAdapter;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/3 14:56
 * description：专门管理 websocket 的逻辑，包括推拉
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {
    @Resource
    @Lazy
    private WxMpService wxMpService;

    @Resource
    private UserDao userDao;

    @Resource
    private LoginService loginService;

    /**
     * 管理所有用户的连接（已登录的用户/未登录的游客）
     */
    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();

    public static final Duration DURATION = Duration.ofSeconds(1000);

    public static final int MAXIMUM_SIZE = 10000;

    /**
     * 临时保存登录code和channel的映射关系
     */
    private static final Cache<Integer, Channel> WAIT_LOGIN_MAP = Caffeine.newBuilder().maximumSize(MAXIMUM_SIZE).expireAfterWrite(DURATION).build();

    @Override
    public void connect(Channel channel) {
        ONLINE_WS_MAP.put(channel, new WSChannelExtraDTO());
    }

    @SneakyThrows
    @Override
    public void handleLoginRequest(Channel channel) {
        // 生成随机码
        Integer code = generateLoginCode(channel);
        // 向微信申请带参二维码
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, 1000);
        // 返回二维码给前端
        sendMsg(channel, WebSocketAdapter.buildResp(wxMpQrCodeTicket));
    }

    @Override
    public void remove(Channel channel) {
        ONLINE_WS_MAP.remove(channel);
        // todo 用户下线
    }

    @Override
    public void scanLoginSuccess(Integer code, Long uid) {
        // 确认连接存储在本地
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if (Objects.isNull(channel)) {
            return;
        }
        User user = userDao.getById(uid);
        // 移除code
        WAIT_LOGIN_MAP.invalidate(code);
        // 调用登录模块获取token
        String token = loginService.login(uid);
        // 用户登录成功
        loginSuccess(channel, user, token);
    }

    @Override
    public void waitAuthorize(Integer code) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if (Objects.isNull(channel)) {
            return;
        }
        sendMsg(channel, WebSocketAdapter.buildWaitAuthorizeResp());
    }

    @Override
    public void authorize(Channel channel, String token) {
        Long validUid = loginService.getValidUid(token);
        if (Objects.nonNull(validUid)) {
            User user = userDao.getById(validUid);
            loginSuccess(channel, user, token);
        } else {
            sendMsg(channel, WebSocketAdapter.buildInvalidTokenResp());
        }
    }

    private void loginSuccess(Channel channel, User user, String token) {
        // 保存channel的uid
        WSChannelExtraDTO wsChannelExtraDTO = ONLINE_WS_MAP.get(channel);
        wsChannelExtraDTO.setUid(user.getId());
        // 推送用户登录成功消息
        sendMsg(channel, WebSocketAdapter.buildResp(user, token));
    }

    private void sendMsg(Channel channel, WSBaseResp<?> resp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(resp)));
    }

    private Integer generateLoginCode(Channel channel) {
        Integer code;
        do {
            code = RandomUtil.randomInt(Integer.MAX_VALUE);
        } while (Objects.nonNull(WAIT_LOGIN_MAP.asMap().putIfAbsent(code, channel)));
        // putIfAbsent方法主要是在向ConcurrentHashMap中添加键—值对的时候，它会先判断该键值对是否已经存在。
        // 如果不存在（新的entry），那么会向map中添加该键值对，并返回null。
        // 如果已经存在，那么不会覆盖已有的值，直接返回已经存在的值。
        // 这里之所以用do while循环，是解决随机生成重复code的问题，如果重复了就再随机生成一次
        return code;
    }
}
