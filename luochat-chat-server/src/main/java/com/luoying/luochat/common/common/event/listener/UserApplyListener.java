package com.luoying.luochat.common.common.event.listener;

import com.luoying.luochat.common.common.event.UserApplyEvent;
import com.luoying.luochat.common.user.dao.UserApplyDao;
import com.luoying.luochat.common.user.domain.entity.UserApply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;

/**
 * 好友申请监听器
 */
@Slf4j
@Component
public class UserApplyListener {
    @Resource
    private UserApplyDao userApplyDao;


    @Async
    @TransactionalEventListener(classes = UserApplyEvent.class, fallbackExecution = true)
    public void notifyFriend(UserApplyEvent event) {
        // 获取好友申请
        UserApply userApply = event.getUserApply();
        // 获取申请中的目标用户的未读申请数
        Integer unReadCount = userApplyDao.getUnReadCount(userApply.getTargetId());
        // todo 发送申请通知
    }

}
