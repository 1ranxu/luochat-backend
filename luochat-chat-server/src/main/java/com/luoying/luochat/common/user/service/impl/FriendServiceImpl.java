package com.luoying.luochat.common.user.service.impl;

import com.luoying.luochat.common.common.domain.vo.req.CursorPageBaseReq;
import com.luoying.luochat.common.common.domain.vo.resp.CursorPageBaseResp;
import com.luoying.luochat.common.user.dao.UserDao;
import com.luoying.luochat.common.user.dao.UserFriendDao;
import com.luoying.luochat.common.user.domain.entity.User;
import com.luoying.luochat.common.user.domain.entity.UserFriend;
import com.luoying.luochat.common.user.domain.vo.req.FriendCheckReq;
import com.luoying.luochat.common.user.domain.vo.resp.FriendCheckResp;
import com.luoying.luochat.common.user.domain.vo.resp.FriendResp;
import com.luoying.luochat.common.user.service.FriendService;
import com.luoying.luochat.common.user.service.adapter.FriendAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @description : 好友
 */
@Slf4j
@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private UserFriendDao userFriendDao;

    @Autowired
    private UserDao userDao;


    @Override
    public FriendCheckResp check(Long uid, FriendCheckReq request) {
        // 根据uidList查询当前登录用户的好友friendList
        List<UserFriend> friendList = userFriendDao.getByFriends(uid, request.getUidList());
        // 遍历friendList取出所有的friendUid
        Set<Long> friendUidSet = friendList.stream().map(UserFriend::getFriendUid).collect(Collectors.toSet());
        // 组装friendCheckList
        List<FriendCheckResp.FriendCheck> friendCheckList = request.getUidList().stream().map(friendUid -> {
            FriendCheckResp.FriendCheck friendCheck = new FriendCheckResp.FriendCheck();
            // 设置uid
            friendCheck.setUid(friendUid);
            // 判断friendUidSet是否包含请求中的friendUid，包含则是好友
            friendCheck.setIsFriend(friendUidSet.contains(friendUid));
            return friendCheck;
        }).collect(Collectors.toList());
        // 封装返回
        return new FriendCheckResp(friendCheckList);
    }

    @Override
    public CursorPageBaseResp<FriendResp> friendList(Long uid, CursorPageBaseReq request) {
        // 从用户联系人表（user_friend）中获取当前登录用户的好友列表 friendPage
        CursorPageBaseResp<UserFriend> friendPage = userFriendDao.getFriendPage(uid, request);
        // 简单校验
        if (CollectionUtils.isEmpty(friendPage.getList())) {
            return CursorPageBaseResp.empty();
        }
        // 从friendPage中遍历出该登录用户所有的好友id
        List<Long> friendUids = friendPage.getList()
                .stream().map(UserFriend::getFriendUid)
                .collect(Collectors.toList());
        // 使用friendUids从用户表（user）查询好友具体信息：id、在线状态、名称、头像
        List<User> userList = userDao.getFriendList(friendUids);
        // 使用userlist作为基础数据，组装返回
        return CursorPageBaseResp.init(friendPage, FriendAdapter.buildFriend(friendPage.getList(), userList));
    }

}
