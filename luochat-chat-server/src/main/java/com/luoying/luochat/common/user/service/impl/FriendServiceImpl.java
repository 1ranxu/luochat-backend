package com.luoying.luochat.common.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.luoying.luochat.common.chat.service.RoomService;
import com.luoying.luochat.common.common.annotation.RedissonLock;
import com.luoying.luochat.common.common.domain.vo.req.CursorPageBaseReq;
import com.luoying.luochat.common.common.domain.vo.req.PageBaseReq;
import com.luoying.luochat.common.common.domain.vo.resp.CursorPageBaseResp;
import com.luoying.luochat.common.common.domain.vo.resp.PageBaseResp;
import com.luoying.luochat.common.common.event.UserApplyEvent;
import com.luoying.luochat.common.common.utils.AssertUtil;
import com.luoying.luochat.common.user.dao.UserApplyDao;
import com.luoying.luochat.common.user.dao.UserDao;
import com.luoying.luochat.common.user.dao.UserFriendDao;
import com.luoying.luochat.common.user.domain.entity.RoomFriend;
import com.luoying.luochat.common.user.domain.entity.User;
import com.luoying.luochat.common.user.domain.entity.UserApply;
import com.luoying.luochat.common.user.domain.entity.UserFriend;
import com.luoying.luochat.common.user.domain.enums.ApplyStatusEnum;
import com.luoying.luochat.common.user.domain.vo.req.FriendApplyReq;
import com.luoying.luochat.common.user.domain.vo.req.FriendApproveReq;
import com.luoying.luochat.common.user.domain.vo.req.FriendCheckReq;
import com.luoying.luochat.common.user.domain.vo.resp.FriendApplyResp;
import com.luoying.luochat.common.user.domain.vo.resp.FriendCheckResp;
import com.luoying.luochat.common.user.domain.vo.resp.FriendResp;
import com.luoying.luochat.common.user.domain.vo.resp.FriendUnreadResp;
import com.luoying.luochat.common.user.service.FriendService;
import com.luoying.luochat.common.user.service.adapter.FriendAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @description : 好友
 */
@Slf4j
@Service
public class FriendServiceImpl implements FriendService {

    @Resource
    private UserFriendDao userFriendDao;

    @Resource
    private UserDao userDao;

    @Resource
    private UserApplyDao userApplyDao;

    @Resource
    private RoomService roomService;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;


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
    public void apply(Long uid, FriendApplyReq request) {
        // 是否已经有好友关系
        UserFriend friend = userFriendDao.getByFriend(uid, request.getTargetUid());
        AssertUtil.isEmpty(friend, "你们已经是好友了");
        // 是否已经有自己的待审批的申请记录
        UserApply selfApproving = userApplyDao.getFriendApproving(uid, request.getTargetUid());
        if (Objects.nonNull(selfApproving)) {
            log.info("已有好友申请记录,uid:{}, targetId:{}", uid, request.getTargetUid());
            return;
        }
        // 是否已经有别人请求自己的待审批的申请记录(防止别人请求我，我又去请求别人，没必要生再成一条申请记录)
        UserApply friendApproving = userApplyDao.getFriendApproving(request.getTargetUid(), uid);
        if (Objects.nonNull(friendApproving)) {// 存在，直接帮我同意别人的请求
            ((FriendService) AopContext.currentProxy()).applyApprove(uid, new FriendApproveReq(friendApproving.getId()));
            return;
        }
        // 构造好友申请
        UserApply insert = FriendAdapter.buildFriendApply(uid, request);
        // 保存好友申请
        userApplyDao.save(insert);
        // 发送申请事件
        applicationEventPublisher.publishEvent(new UserApplyEvent(this, insert));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedissonLock(key = "#uid")
    public void applyApprove(Long uid, FriendApproveReq request) {
        // 查询根据申请id查询是否有申请记录
        UserApply userApply = userApplyDao.getById(request.getApplyId());
        // 校验
        AssertUtil.isNotEmpty(userApply, "不存在申请记录");
        // 判断申请记录的targetId是否是自己
        AssertUtil.equal(userApply.getTargetId(), uid, "不存在申请记录");
        // 判断申请记录是否是待审批状态
        AssertUtil.equal(userApply.getStatus(), ApplyStatusEnum.WAIT_APPROVAL.getCode(), "已同意好友申请");
        // 同意申请
        userApplyDao.agree(request.getApplyId());
        // 创建双方好友关系
        createFriend(uid, userApply.getUid());
        // 创建一个聊天房间
        RoomFriend roomFriend = roomService.createFriendRoom(Arrays.asList(uid, userApply.getUid()));
        // todo 发送一条同意消息。。我们已经是好友了，开始聊天吧
    }

    /**
     * 申请未读数
     */
    @Override
    public FriendUnreadResp unread(Long uid) {
        Integer unReadCount = userApplyDao.getUnReadCount(uid);
        return new FriendUnreadResp(unReadCount);
    }

    /**
     * 分页查询好友申请
     */
    @Override
    public PageBaseResp<FriendApplyResp> pageApplyFriend(Long uid, PageBaseReq request) {
        // 分页查询user_apply表中申请目标为自己的记录
        IPage<UserApply> userApplyIPage = userApplyDao.friendApplyPage(uid, request.plusPage());
        // 简单校验
        if (CollectionUtil.isEmpty(userApplyIPage.getRecords())) {
            return PageBaseResp.empty();
        }
        // 将这些申请记录设置为已读
        readApplications(uid, userApplyIPage);
        // 返回好友申请列表
        return PageBaseResp.init(userApplyIPage, FriendAdapter.buildFriendApplyList(userApplyIPage.getRecords()));
    }

    /**
     * 删除好友
     *
     * @param uid       uid
     * @param friendUid 朋友uid
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFriend(Long uid, Long friendUid) {
        List<UserFriend> userFriends = userFriendDao.getUserFriend(uid, friendUid);
        // 简单校验
        if (CollectionUtil.isEmpty(userFriends)) {
            log.info("没有好友关系：{},{}", uid, friendUid);
            return;
        }
        // 遍历userFriends，获取记录的id集合
        List<Long> friendRecordIds = userFriends.stream().map(UserFriend::getId).collect(Collectors.toList());
        // 根据id集合逻辑删除记录
        userFriendDao.removeByIds(friendRecordIds);
        // 禁用房间
        roomService.disableFriendRoom(Arrays.asList(uid, friendUid));
    }

    private void readApplications(Long uid, IPage<UserApply> userApplyIPage) {
        // 遍历userApplyIPage获取所有的申请id
        List<Long> applyIds = userApplyIPage.getRecords()
                .stream().map(UserApply::getId)
                .collect(Collectors.toList());
        // 把目标用户为自己且未读的申请记录设置为已读状态
        userApplyDao.readApplications(uid, applyIds);
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

    /**
     * 需要在user_friend表中添加两条记录，维护两者之间的好友关系
     * @param uid
     * @param targetUid
     */
    private void createFriend(Long uid, Long targetUid) {
        UserFriend userFriend1 = new UserFriend();
        userFriend1.setUid(uid);
        userFriend1.setFriendUid(targetUid);
        UserFriend userFriend2 = new UserFriend();
        userFriend2.setUid(targetUid);
        userFriend2.setFriendUid(uid);
        userFriendDao.saveBatch(Lists.newArrayList(userFriend1, userFriend2));
    }
}
