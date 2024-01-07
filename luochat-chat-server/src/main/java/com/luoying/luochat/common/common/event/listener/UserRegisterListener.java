package com.luoying.luochat.common.common.event.listener;

import com.luoying.luochat.common.common.event.UserRegisterEvent;
import com.luoying.luochat.common.user.dao.UserDao;
import com.luoying.luochat.common.user.domain.entity.User;
import com.luoying.luochat.common.user.domain.enums.IdempotentEnum;
import com.luoying.luochat.common.user.domain.enums.ItemEnum;
import com.luoying.luochat.common.user.service.UserBackpackService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/7 21:02
 */
@Component
public class UserRegisterListener {
    @Resource
    private UserBackpackService userBackpackService;

    @Resource
    private UserDao userDao;

    @TransactionalEventListener(classes = UserRegisterEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void sendModifyNameCard(UserRegisterEvent event) {
        User user = event.getUser();
        userBackpackService.acquireItem(user.getId(), ItemEnum.MODIFY_NAME_CARD.getId(), IdempotentEnum.UID, user.getId() + "");
    }

    @TransactionalEventListener(classes = UserRegisterEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void sendBadege(UserRegisterEvent event) {
        User user = event.getUser();
        int registeredCount = userDao.count();
        if (registeredCount < 10)
            userBackpackService.acquireItem(user.getId(), ItemEnum.REG_TOP10_BADGE.getId(), IdempotentEnum.UID, user.getId() + "");
        else if (registeredCount < 100) {
            userBackpackService.acquireItem(user.getId(), ItemEnum.REG_TOP100_BADGE.getId(), IdempotentEnum.UID, user.getId() + "");
        }
    }


}
