package com.luoying.luochat.common.common.event;

import com.luoying.luochat.common.user.domain.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/8 19:05
 */
@Getter
public class UserBlackEvent extends ApplicationEvent {
    private final User user;

    public UserBlackEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}