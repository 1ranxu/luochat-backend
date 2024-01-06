package com.luoying.luochat.common.common.exception;

import com.luoying.luochat.common.common.domain.vo.resp.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/6 15:14
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        StringBuilder errorMsg = new StringBuilder();
        // 从MethodArgumentNotValidException中获取所有FieldErrors
        // 然后遍历获取field名称和具体出错信息，设置到errorMsg中
        exception
                .getBindingResult()
                .getFieldErrors()
                .forEach(x -> errorMsg
                        .append(x.getField())
                        .append(x.getDefaultMessage())
                        .append(",")
                );
        String msg = errorMsg.substring(0,errorMsg.length()-1);
        // 返回给前端
        return ApiResult.fail(CommonErrorEnum.PARAM_INVALID.getCode(), msg);
    }

    /**
     * 业务异常处理
     */
    @ExceptionHandler(value = BusinessException.class)
    public ApiResult<?> handleBusinessException(BusinessException e) {
        log.info("Business Exception! The reason is: {}",e.getMessage());
        return ApiResult.fail(e.getErrorCode(),e.getErrorMsg());
    }

    @ExceptionHandler(value = Throwable.class)
    public ApiResult<?> handleThrowable(Throwable throwable) {
        log.error("System Exception! The reason is: {}",throwable.getMessage(),throwable);
        return ApiResult.fail(CommonErrorEnum.SYSTEM_ERROR);
    }
}
