package com.luoying.luochat.common.user.service.adapter;

import com.luoying.luochat.common.user.domain.entity.User;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/3 17:37
 */
public class UserAdapter {
    public static User buildUserSave(String openId) {
        return User.builder().openId(openId).build();
    }

    public static User buildAuthorizeUser(Long uid, WxOAuth2UserInfo userInfo) {
        return User.builder()
                .id(uid)
                .name(userInfo.getNickname())
                .avatar(userInfo.getHeadImgUrl()).build();
    }
}
