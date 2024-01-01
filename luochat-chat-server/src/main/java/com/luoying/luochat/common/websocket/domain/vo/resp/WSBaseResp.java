package com.luoying.luochat.common.websocket.domain.vo.resp;

import lombok.Data;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/1 21:30
 */
@Data
public class WSBaseResp<T> {
    /**
     * @see com.luoying.luochat.common.websocket.domain.enums.WSRespTypeEnum
     */
    private Integer type;

    private T data;
}
