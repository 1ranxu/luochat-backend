package com.luoying.luochat.common.user.domain.entity;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * 用户ip信息
 *
 * @Author 落樱的悔恨
 * @Date 2024/1/8 9:43
 */
@Data
public class IpInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    //注册时的ip
    private String createIp;
    //注册时的ip详情
    private IpDetail createIpDetail;
    //最新登录的ip
    private String updateIp;
    //最新登录的ip详情
    private IpDetail updateIpDetail;

    public void refreshIp(String ip) {
        // 如果传进来的ip是空的就不更新了
        if (StringUtils.isEmpty(ip)) {
            return;
        }
        // 用户第一次注册，createIp没有才更新
        if (StrUtil.isBlank(createIp)) {
            createIp = ip;
        }
        // updateIp是无论如何都要更新的
        updateIp = ip;
    }

    /**
     * 判断updateIp是否需要刷新就行，
     * 1.如果是用户第一次注册时，createIp和updateIp是相同的，但updateIpDetail为null，updateIp与updateIpDetail中的ip不相等，
     * notNeedRefresh为false，返回updateIp，根据updateIp查出ipDetail，调用refreshIpDetail方法
     * 然后更新createIpDetail和updateIpDetail
     * 2.如果是登录，updateIpDetail不为null，updateIp与updateIpDetail中的ip不相等，notNeedRefresh为false，
     * 返回updateIp，根据updateIp查出ipDetail，调用refreshIpDetail方法，更新updateIpDetail，但不更新createIpDetail
     * 3.如果是登录，updateIpDetail不为null，updateIp与updateIpDetail中的ip相等，notNeedRefresh为true
     * 返回null，就什么也不更新。同时实现了createIpDetail只会在注册时更新
     *
     * @return
     */
    public String needRefreshUpdateIp() {
        boolean notNeedRefresh = Optional.ofNullable(updateIpDetail)
                .map(IpDetail::getIp)
                .filter(ip -> Objects.equals(ip, updateIp))
                .isPresent();
        return notNeedRefresh ? null : updateIp;
    }

    public void refreshIpDetail(IpDetail ipDetail) {
        if (Objects.equals(createIp, ipDetail.getIp())) {
            createIpDetail = ipDetail;
        }
        if (Objects.equals(updateIp, ipDetail.getIp())) {
            updateIpDetail = ipDetail;
        }
    }
}