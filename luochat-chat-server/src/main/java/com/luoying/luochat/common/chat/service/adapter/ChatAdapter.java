package com.luoying.luochat.common.chat.service.adapter;

import com.luoying.luochat.common.chat.domain.enums.HotFlagEnum;
import com.luoying.luochat.common.chat.domain.enums.RoomTypeEnum;
import com.luoying.luochat.common.common.domain.enums.NormalOrNoEnum;
import com.luoying.luochat.common.user.domain.entity.Room;
import com.luoying.luochat.common.user.domain.entity.RoomFriend;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 */
public class ChatAdapter {
    public static final String SEPARATOR = "_";

    /**
     * 使用两个用户的uid生成roomKey，数字小的在前，大的在后
     */
    public static String generateRoomKey(List<Long> uidList) {
        return uidList.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(SEPARATOR));
    }

    /**
     * 根据roomTypeEnum创建房间
     */
    public static Room buildRoom(RoomTypeEnum roomTypeEnum) {
        Room room = new Room();
        // 设置房间的类型
        room.setType(roomTypeEnum.getType());
        // 设置房间是否为热点房间
        room.setHotFlag(HotFlagEnum.NOT.getType());
        return room;
    }

    /**
     * 根据roomId，uidList构建roomFriend
     */
    public static RoomFriend buildFriendRoom(Long roomId, List<Long> uidList) {
        // 单聊房间的用户uid升序排序
        List<Long> collect = uidList.stream().sorted().collect(Collectors.toList());
        // 构造roomFriend
        RoomFriend roomFriend = new RoomFriend();
        // 设置房间id
        roomFriend.setRoomId(roomId);
        // 设置第一个用户
        roomFriend.setUid1(collect.get(0));
        // 设置第二个用户
        roomFriend.setUid2(collect.get(1));
        // 设置roomKey
        roomFriend.setRoomKey(generateRoomKey(uidList));
        // 设置房间状态为正常
        roomFriend.setStatus(NormalOrNoEnum.NORMAL.getStatus());
        // 返回
        return roomFriend;
    }
}
