package com.luoying.luochat.common.user.domain.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/6 20:10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="用户信息响应对象", description="用户信息")
public class BadgeResp {
    @ApiModelProperty(value = "徽章id")
    private Long id;
    @ApiModelProperty(value = "徽章图标")
    private String img;
    @ApiModelProperty(value = "徽章描述")
    private String describe;
    @ApiModelProperty(value = "是否拥有 0-否 1-是")
    private Integer obtain;
    @ApiModelProperty(value = "是否佩戴 0-否 1-是")
    private Integer wearing;
}
