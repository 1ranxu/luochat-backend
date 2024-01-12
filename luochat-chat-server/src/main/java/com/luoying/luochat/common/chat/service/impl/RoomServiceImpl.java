package com.luoying.luochat.common.chat.service.impl;


import com.luoying.luochat.common.chat.domain.enums.RoomTypeEnum;
import com.luoying.luochat.common.chat.service.RoomService;
import com.luoying.luochat.common.chat.service.adapter.ChatAdapter;
import com.luoying.luochat.common.common.domain.enums.NormalOrNoEnum;
import com.luoying.luochat.common.common.utils.AssertUtil;
import com.luoying.luochat.common.user.dao.RoomDao;
import com.luoying.luochat.common.user.dao.RoomFriendDao;
import com.luoying.luochat.common.user.domain.entity.Room;
import com.luoying.luochat.common.user.domain.entity.RoomFriend;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 */
@Service
public class RoomServiceImpl implements RoomService {

    @Resource
    private RoomFriendDao roomFriendDao;
    @Resource
    private RoomDao roomDao;

    /**
     * 创建单聊房间
     * @param uidList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoomFriend createFriendRoom(List<Long> uidList) {
        AssertUtil.isNotEmpty(uidList, "房间创建失败，好友数量不对");
        AssertUtil.equal(uidList.size(), 2, "房间创建失败，好友数量不对");
        // 生成roomKey
        String key = ChatAdapter.generateRoomKey(uidList);
        // 根据roomKey获取房间
        RoomFriend roomFriend = roomFriendDao.getByKey(key);
        if (Objects.nonNull(roomFriend)) { //如果存在房间就恢复，适用于删除好友后恢复好友或者重新添加好友的场景
            restoreRoomIfNeed(roomFriend);
        } else {// 新建房间
            // 新建room
            Room room = createRoom(RoomTypeEnum.FRIEND);
            // 新建roomFriend
            roomFriend = createFriendRoom(room.getId(), uidList);
        }
        // 返回roomFriend
        return roomFriend;
    }


    private RoomFriend createFriendRoom(Long roomId, List<Long> uidList) {
        // 根据roomId，uidList构建roomFriend
        RoomFriend insert = ChatAdapter.buildFriendRoom(roomId, uidList);
        // 保存roomFriend
        roomFriendDao.save(insert);

        // 返回roomFriend
        return insert;
    }

    private Room createRoom(RoomTypeEnum roomTypeEnum) {
        // 根据roomTypeEnum构建room
        Room insert = ChatAdapter.buildRoom(roomTypeEnum);
        // 保存room
        roomDao.save(insert);
        // 返回room
        return insert;
    }

    private void restoreRoomIfNeed(RoomFriend room) {
        if (Objects.equals(room.getStatus(), NormalOrNoEnum.NOT_NORMAL.getStatus())) {// 如果roomFriend的状态是不正常（已删除）
            // 恢复roomFriend
            roomFriendDao.restoreRoom(room.getId());
        }
    }
}
