package com.luoying.luochat.common.user.service.adapter;

import com.luoying.luochat.common.user.domain.entity.User;
import com.luoying.luochat.common.user.domain.entity.UserFriend;
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
}
