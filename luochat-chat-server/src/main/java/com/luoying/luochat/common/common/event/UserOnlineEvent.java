package com.luoying.luochat.common.common.event;

import com.luoying.luochat.common.user.domain.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/8 9:34
 */
@Getter
public class UserOnlineEvent extends ApplicationEvent {
    private User user;

    public UserOnlineEvent(Object source, User user) {// source就是事件发送者
        super(source);
        this.user = user;
    }
}
