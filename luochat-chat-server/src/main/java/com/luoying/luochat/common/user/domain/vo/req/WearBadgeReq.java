package com.luoying.luochat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/6 20:56
 */
@Data
public class WearBadgeReq {
    @ApiModelProperty("徽章id")
    @NotNull
    private Long itemId;
}
