package com.luoying.luochat.common.user.domain.vo.resp;

import com.luoying.luochat.common.user.domain.enums.ChatActiveStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Description: 好友校验
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendResp {

    @ApiModelProperty("好友uid")
    private Long uid;
    @ApiModelProperty("好友名称")
    private String name;
    @ApiModelProperty("好友头像")
    private String avatar;

    /**
     * @see ChatActiveStatusEnum
     */
    @ApiModelProperty("在线状态 1在线 2离线")
    private Integer activeStatus;
}
