package com.luoying.luochat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


/**
 * Description: 申请好友信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendDeleteReq {

    @NotNull
    @ApiModelProperty("好友uid")
    private Long targetUid;

}
