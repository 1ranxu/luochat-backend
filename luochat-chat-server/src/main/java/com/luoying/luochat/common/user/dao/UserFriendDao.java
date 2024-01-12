package com.luoying.luochat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoying.luochat.common.common.domain.vo.req.CursorPageBaseReq;
import com.luoying.luochat.common.common.domain.vo.resp.CursorPageBaseResp;
import com.luoying.luochat.common.common.utils.CursorUtils;
import com.luoying.luochat.common.user.domain.entity.UserFriend;
import com.luoying.luochat.common.user.mapper.UserFriendMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户联系人表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/1ranxu">luoying</a>
 * @since 2024-01-11
 */
@Service
public class UserFriendDao extends ServiceImpl<UserFriendMapper, UserFriend> {
    /**
     * 根据uidList查询当前登录用户的好友
     */
    public List<UserFriend> getByFriends(Long uid, List<Long> uidList) {
        return lambdaQuery()
                .eq(UserFriend::getUid, uid)
                .in(UserFriend::getFriendUid, uidList)
                .list();
    }

    /**
     * 查询当前登录用户与目标用户是否为好友关系
     */
    public UserFriend getByFriend(Long uid, Long targetUid) {
        return lambdaQuery()
                .eq(UserFriend::getUid, uid)
                .eq(UserFriend::getFriendUid, targetUid)
                .one();
    }

    /**
     * 使用游标查询当前登录用户的好友
     */
    public CursorPageBaseResp<UserFriend> getFriendPage(Long uid, CursorPageBaseReq cursorPageBaseReq) {
        return CursorUtils.getCursorPageByMysql(this, cursorPageBaseReq,
                wrapper -> wrapper.eq(UserFriend::getUid, uid), UserFriend::getId);
    }

    /**
     * 查看双方是否互相都有好友关系
     * @param uid
     * @param friendUid
     * @return
     */
    public List<UserFriend> getUserFriend(Long uid, Long friendUid) {
        return lambdaQuery()
                .eq(UserFriend::getUid, uid)
                .eq(UserFriend::getFriendUid, friendUid)
                .or()
                .eq(UserFriend::getFriendUid, uid)
                .eq(UserFriend::getUid, friendUid)
                .select(UserFriend::getId)
                .list();
    }
}
