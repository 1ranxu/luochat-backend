package com.luoying.luochat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/7 12:10
 */
@AllArgsConstructor
@Getter
public enum UserActiveStatusEnum {

    ONLINE(1,"在线"),
    OFFLINE(2,"离线");


    private final Integer status;

    private final String desc;
}
