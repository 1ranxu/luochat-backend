package com.luoying.luochat.common.common.event;

import com.luoying.luochat.common.user.domain.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/7 20:58
 */
@Getter
public class UserRegisterEvent extends ApplicationEvent {
    private User user;

    public UserRegisterEvent(Object source, User user) {// source就是事件发送者
        super(source);
        this.user = user;
    }
}
