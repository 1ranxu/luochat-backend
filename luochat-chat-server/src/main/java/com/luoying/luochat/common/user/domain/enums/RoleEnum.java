package com.luoying.luochat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 角色枚举
 * @Author 落樱的悔恨
 * @Date 2024/1/8 16:51
 */
@AllArgsConstructor
@Getter
public enum RoleEnum {
    SUPER_ADMIN(1L, "超级管理员"),
    GENERAL_MANAGER(2L, "普通管理员"),
    ;

    private final Long id;
    private final String desc;

    private static Map<Long, RoleEnum> cache;

    static {
        cache = Arrays.stream(RoleEnum.values()).collect(Collectors.toMap(RoleEnum::getId, Function.identity()));
    }

    public static RoleEnum of(Long type) {
        return cache.get(type);
    }
}
