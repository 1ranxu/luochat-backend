package com.luoying.luochat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 拉黑请求
 * @Author 落樱的悔恨
 * @Date 2024/1/8 17:40
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlackReq {
    @NotNull
    @ApiModelProperty("拉黑目标uid")
    private Long uid;
}
