package com.luoying.luochat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description: 拉黑目标类型
 * @Author 落樱的悔恨
 * @Date 2024/1/8 18:47
 */
@AllArgsConstructor
@Getter
public enum BlackTypeEnum {
    IP(1),
    UID(2),
    ;

    private final Integer type;

}
