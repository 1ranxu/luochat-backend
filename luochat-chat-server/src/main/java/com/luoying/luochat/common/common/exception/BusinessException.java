package com.luoying.luochat.common.common.exception;

import lombok.Data;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/6 16:41
 */
@Data
public class BusinessException extends RuntimeException {
    protected Integer errorCode;

    protected String errorMsg;

    public BusinessException(String errorMsg) {
        super(errorMsg);
        this.errorCode=CommonErrorEnum.BUSINESS_ERROR.getErrorCode();
        this.errorMsg = errorMsg;
    }

    public BusinessException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

}
