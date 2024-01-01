package com.luoying.luochat.common.websocket.domain.vo.req;

import lombok.Data;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/1 21:24
 */
@Data
public class WSBaseReq {
    /**
     * @see com.luoying.luochat.common.websocket.domain.enums.WSReqTypeEnum
     */
    private Integer type;

    private String data;
}
