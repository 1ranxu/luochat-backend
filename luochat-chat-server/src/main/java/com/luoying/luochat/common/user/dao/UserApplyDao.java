package com.luoying.luochat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoying.luochat.common.user.domain.entity.UserApply;
import com.luoying.luochat.common.user.domain.enums.ApplyStatusEnum;
import com.luoying.luochat.common.user.domain.enums.ApplyTypeEnum;
import com.luoying.luochat.common.user.mapper.UserApplyMapper;
import org.springframework.stereotype.Service;

import static com.luoying.luochat.common.user.domain.enums.ApplyReadStatusEnum.UNREAD;

/**
 * <p>
 * 用户申请表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/1ranxu">luoying</a>
 * @since 2024-01-11
 */
@Service
public class UserApplyDao extends ServiceImpl<UserApplyMapper, UserApply> {
    /**
     * 查询uid用户是否已经向targetUid目标用户提交好友申请
     * @param uid
     * @param targetUid
     * @return
     */
    public UserApply getFriendApproving(Long uid, Long targetUid) {
        return lambdaQuery()
                .eq(UserApply::getUid, uid)
                .eq(UserApply::getTargetId, targetUid)
                .eq(UserApply::getStatus, ApplyStatusEnum.WAIT_APPROVAL)
                .eq(UserApply::getType, ApplyTypeEnum.ADD_FRIEND.getCode())
                .one();
    }

    /**
     * 查询所有对我的申请，且未读
     * @param targetId
     * @return
     */
    public Integer getUnReadCount(Long targetId) {
        return lambdaQuery().eq(UserApply::getTargetId, targetId)
                .eq(UserApply::getReadStatus, UNREAD.getCode())
                .count();
    }

    /**
     * 修改申请记录的状态为已同意
     * @param applyId
     */
    public void agree(Long applyId) {
        lambdaUpdate().set(UserApply::getStatus, ApplyStatusEnum.AGREE.getCode())
                .eq(UserApply::getId, applyId)
                .update();
    }
}
