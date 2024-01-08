package com.luoying.luochat.common.common.event.listener;

import com.luoying.luochat.common.common.event.UserOnlineEvent;
import com.luoying.luochat.common.common.event.UserRegisterEvent;
import com.luoying.luochat.common.user.dao.UserDao;
import com.luoying.luochat.common.user.domain.entity.User;
import com.luoying.luochat.common.user.domain.enums.UserActiveStatusEnum;
import com.luoying.luochat.common.user.service.IpService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/8 10:09
 */
@Component
public class UserOnlineListener {
    @Resource
    private IpService ipService;
    @Resource
    private UserDao userDao;

    @Async
    @TransactionalEventListener(classes = UserOnlineEvent.class, phase = TransactionPhase.AFTER_COMMIT,fallbackExecution = true)
    public void updateDB(UserOnlineEvent event) {
        User user = event.getUser();
        User update = new User();
        update.setId(user.getId());
        // 更新用户上线时间
        update.setLastOptTime(user.getLastOptTime());
        // 更新用户的ip信息
        update.setIpInfo(user.getIpInfo());
        // 更新用户的活跃状态
        update.setActiveStatus(UserActiveStatusEnum.ONLINE.getStatus());
        userDao.updateById(update);
        // 我们只设置了ip，ip的详情还未设置，接下来设置用户ip详情
        ipService.refreshIpDetailAsync(user.getId());
    }
}
