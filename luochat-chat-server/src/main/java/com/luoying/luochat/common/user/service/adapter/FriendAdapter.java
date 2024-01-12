package com.luoying.luochat.common.user.service.adapter;

import com.luoying.luochat.common.user.domain.entity.User;
import com.luoying.luochat.common.user.domain.entity.UserApply;
import com.luoying.luochat.common.user.domain.entity.UserFriend;
import com.luoying.luochat.common.user.domain.enums.ApplyReadStatusEnum;
import com.luoying.luochat.common.user.domain.enums.ApplyStatusEnum;
import com.luoying.luochat.common.user.domain.enums.ApplyTypeEnum;
import com.luoying.luochat.common.user.domain.vo.req.FriendApplyReq;
import com.luoying.luochat.common.user.domain.vo.resp.FriendApplyResp;
import com.luoying.luochat.common.user.domain.vo.resp.FriendResp;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Description: 好友适配器
 */
public class FriendAdapter {

    public static List<FriendResp> buildFriend(List<UserFriend> userFriendList, List<User> userList) {
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, user -> user));
        // 遍历userFriendList，组装List<FriendResp>
        return userFriendList.stream().map(userFriend -> {
            // 组装FriendResp
            FriendResp resp = new FriendResp();
            // 设置uid
            resp.setUid(userFriend.getFriendUid());
            // 根据uid获取用户
            // userList可能不包含userFriendList中的好友，因为userList比userFriendList新，可能在userList查出来前有用户删号了
            User user = userMap.get(userFriend.getFriendUid());
            if (Objects.nonNull(user)) {
                // 设置在线状态
                resp.setActiveStatus(user.getActiveStatus());
                // 设置用户名
                resp.setName(user.getName());
                // 设置头像
                resp.setAvatar(user.getAvatar());
            }
            // 返回
            return resp;
        }).collect(Collectors.toList());
    }

    public static UserApply buildFriendApply(Long uid, FriendApplyReq request) {
        // 构建好友申请
        UserApply userApplyNew = new UserApply();
        // 设置申请人uid
        userApplyNew.setUid(uid);
        // 设置申请人的申请消息
        userApplyNew.setMsg(request.getMsg());
        // 设置申请类型为添加好友
        userApplyNew.setType(ApplyTypeEnum.ADD_FRIEND.getCode());
        // 设置申请的目标用户
        userApplyNew.setTargetId(request.getTargetUid());
        // 设置申请状态为待审批状态
        userApplyNew.setStatus(ApplyStatusEnum.WAIT_APPROVAL.getCode());
        // 设置申请的阅读状态为未读状态
        userApplyNew.setReadStatus(ApplyReadStatusEnum.UNREAD.getCode());
        // 返回
        return userApplyNew;
    }

    public static List<FriendApplyResp> buildFriendApplyList(List<UserApply> records) {
        // 构造好友申请列表
        return records.stream().map(userApply -> {
            // 构造好友申请
            FriendApplyResp friendApplyResp = new FriendApplyResp();
            // 设置申请人id
            friendApplyResp.setUid(userApply.getUid());
            // 设置申请类型
            friendApplyResp.setType(userApply.getType());
            // 设置申请id
            friendApplyResp.setApplyId(userApply.getId());
            // 设置申请消息
            friendApplyResp.setMsg(userApply.getMsg());
            // 设置申请状态
            friendApplyResp.setStatus(userApply.getStatus());
            return friendApplyResp;
        }).collect(Collectors.toList());
    }
}
