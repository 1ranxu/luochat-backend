package com.luoying.luochat.common.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/6 15:32
 */
@AllArgsConstructor
@Getter
public enum CommonErrorEnum implements ErrorEnum{
    BUSINESS_ERROR(0,"{}"),
    SYSTEM_ERROR(-1,"系统出小差了，请稍后再尝试哦~~"),
    PARAM_INVALID(-2, "参数校验失败");

    private final Integer code;

    private final String desc;

    @Override
    public Integer getErrorCode() {
        return code;
    }

    @Override
    public String getErrorMsg() {
        return desc;
    }
}