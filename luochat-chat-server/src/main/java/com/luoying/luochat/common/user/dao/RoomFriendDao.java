package com.luoying.luochat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoying.luochat.common.common.domain.enums.NormalOrNoEnum;
import com.luoying.luochat.common.user.domain.entity.RoomFriend;
import com.luoying.luochat.common.user.mapper.RoomFriendMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 单聊房间表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/1ranxu">luoying</a>
 * @since 2024-01-11
 */
@Service
public class RoomFriendDao extends ServiceImpl<RoomFriendMapper, RoomFriend>  {
    /**
     * 根据roomKey获取房间
     */
    public RoomFriend getByKey(String key) {
        return lambdaQuery().eq(RoomFriend::getRoomKey, key).one();
    }

    /**
     * 将房间的状态恢复为正常
     */
    public void restoreRoom(Long id) {
        lambdaUpdate()
                .eq(RoomFriend::getId, id)
                .set(RoomFriend::getStatus, NormalOrNoEnum.NORMAL.getStatus())
                .update();
    }


}
