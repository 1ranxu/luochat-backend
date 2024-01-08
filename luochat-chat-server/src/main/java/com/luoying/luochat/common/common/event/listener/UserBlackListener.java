package com.luoying.luochat.common.common.event.listener;

import com.luoying.luochat.common.common.event.UserBlackEvent;
import com.luoying.luochat.common.user.dao.UserDao;
import com.luoying.luochat.common.user.domain.entity.User;
import com.luoying.luochat.common.user.service.cache.UserCache;
import com.luoying.luochat.common.websocket.service.WebSocketService;
import com.luoying.luochat.common.websocket.service.adapter.WebSocketAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;

/**
 * 用户拉黑监听器
 * @Author 落樱的悔恨
 * @Date 2024/1/8 19:05
 */
@Slf4j
@Component
public class UserBlackListener {
    @Resource
    private WebSocketService webSocketService;

    @Resource
    private UserDao userDao;

    @Resource
    private UserCache userCache;

    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class,phase = TransactionPhase.AFTER_COMMIT)
    public void sendMsg(UserBlackEvent event) {
        User user = event.getUser();
        // 向所有在线用户发送拉黑消息
        webSocketService.sendMsgToAll(WebSocketAdapter.buildBlcakResp(user));
    }
    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class,phase = TransactionPhase.AFTER_COMMIT)
    public void changeUserStatus(UserBlackEvent event) {
        // 把该用户的状态改为拉黑
        userDao.invalidUid(event.getUser().getId());
    }

    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class,phase = TransactionPhase.AFTER_COMMIT)
    public void evictCache(UserBlackEvent event) {
        // 清空黑名单缓存，因为黑名单又新增了，旧的就需要清除
        userCache.evictBlackMap();
    }


}
