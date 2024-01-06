package com.luoying.luochat.common.user.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.luoying.luochat.common.common.domain.enums.YesOrNoEnum;
import com.luoying.luochat.common.user.domain.entity.ItemConfig;
import com.luoying.luochat.common.user.domain.entity.User;
import com.luoying.luochat.common.user.domain.entity.UserBackpack;
import com.luoying.luochat.common.user.domain.vo.resp.BadgeResp;
import com.luoying.luochat.common.user.domain.vo.resp.UserInfoResp;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static UserInfoResp buildUserInfoResp(User user, Integer modifyNameCount) {
        UserInfoResp userInfoResp = new UserInfoResp();
        BeanUtil.copyProperties(user, userInfoResp);
        userInfoResp.setModifyNameChance(modifyNameCount);
        return userInfoResp;
    }

    public static List<BadgeResp> buildBadgesResp(List<ItemConfig> itemConfigs, List<UserBackpack> backpacks, Long itemId) {
        Set<Long> obtainItemSet = backpacks.stream().map(UserBackpack::getItemId).collect(Collectors.toSet());
        return itemConfigs.stream().map(itemConfig -> {
            BadgeResp badgeResp = new BadgeResp();
            BeanUtil.copyProperties(itemConfig, badgeResp);
            badgeResp.setObtain(obtainItemSet.contains(badgeResp.getId()) ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus());
            badgeResp.setWearing(Objects.equals(badgeResp.getId(), itemId) ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus());
            return badgeResp;
        }).sorted(
                Comparator
                 // 先根据是否拥有排序，0否1是，会把未拥有的排到前面，所以需要反转一下顺序
                .comparing(BadgeResp::getObtain,Comparator.reverseOrder())
                 // 再根据是否佩戴排序，0否1是，会把未佩戴的排到前面，所以需要反转一下顺序
                .thenComparing(BadgeResp::getWearing,Comparator.reverseOrder())
        ).collect(Collectors.toList());
    }
}
