package com.luoying.luochat.common.common.exception;

import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import com.luoying.luochat.common.common.domain.vo.resp.ApiResult;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
public enum HttpErrorEnum implements ErrorEnum {
    ACCESS_DENIED(401, "登录失效，请重新登录"),
    ;
    private Integer httpCode;
    private String msg;

    @Override
    public Integer getErrorCode() {
        return httpCode;
    }

    @Override
    public String getErrorMsg() {
        return msg;
    }

    public void sendHttpError(HttpServletResponse response) throws IOException {
        response.setStatus(this.getErrorCode());
        response.setContentType(ContentType.JSON.toString(StandardCharsets.UTF_8));
        ApiResult responseData = ApiResult.fail(this.getErrorCode(), this.getErrorMsg());
        response.getWriter().write(JSONUtil.toJsonStr(responseData));
    }
}