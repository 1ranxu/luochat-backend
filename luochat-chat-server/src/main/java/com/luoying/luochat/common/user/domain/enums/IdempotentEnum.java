package com.luoying.luochat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/7 12:10
 */
@AllArgsConstructor
@Getter
public enum IdempotentEnum {

    UID(1,"uid"),
    MSG_ID(2,"消息id");


    private final Integer type;

    private final String desc;
}
