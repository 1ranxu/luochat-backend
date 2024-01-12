package com.luoying.luochat.common.chat.service;

import com.luoying.luochat.common.user.domain.entity.RoomFriend;

import java.util.List;

/**
 * <p>
 * 房间表 服务类
 * </p>
 *
 * @author <a href="https://github.com/1ranxu">luoying</a>
 * @since 2024-01-11
 */
public interface RoomService {
    /**
     * 创建一个单聊房间
     */
    RoomFriend createFriendRoom(List<Long> uidList);
}
