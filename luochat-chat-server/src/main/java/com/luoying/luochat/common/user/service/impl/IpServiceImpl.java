package com.luoying.luochat.common.user.service.impl;

import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.luoying.luochat.common.common.domain.vo.resp.ApiResult;
import com.luoying.luochat.common.common.utils.JsonUtils;
import com.luoying.luochat.common.user.dao.UserDao;
import com.luoying.luochat.common.user.domain.entity.IpDetail;
import com.luoying.luochat.common.user.domain.entity.IpInfo;
import com.luoying.luochat.common.user.domain.entity.User;
import com.luoying.luochat.common.user.service.IpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/8 10:25
 */
@Service
@Slf4j
public class IpServiceImpl implements IpService, DisposableBean {
    private static ExecutorService executor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(500), new NamedThreadFactory("refresh-ipDetail", false));

    @Resource
    private UserDao userDao;

    @Override
    public void refreshIpDetailAsync(Long uid) {
        // 异步解析ip详情
        executor.execute(() -> {
            // 查询用户
            User user = userDao.getById(uid);
            // 获取ipInfo
            IpInfo ipInfo = user.getIpInfo();
            if (Objects.isNull(ipInfo)) {// 如果ipInfo为null，直接返回，不需要更新
                return;
            }
            // 判断updateIp是否需要刷新，需要则ip不为空
            String ip = ipInfo.needRefreshUpdateIp();
            // ip为空，说明不需要更新
            if (StrUtil.isBlank(ip)) {
                return;
            }
            // 根据ip获取ipDetail，如果失败，休眠2秒，再尝试，加起来总共三次
            IpDetail ipDetail = tryGetIpDetailOrNullThreeTimes(ip);
            if (Objects.nonNull(ipDetail)) {
                // 更新ipInfo中的ipDetail
                ipInfo.refreshIpDetail(ipDetail);
                // 更新数据库
                User update = new User();
                update.setId(uid);
                update.setIpInfo(ipInfo);
                userDao.updateById(update);
            }
        });
    }

    private static IpDetail tryGetIpDetailOrNullThreeTimes(String ip) {
        for (int i = 0; i < 3; i++) {
            // 根据ip获取ipDetail
            IpDetail ipDetail = getIPDetailOrNull(ip);
            // 获取到了就返回
            if (Objects.nonNull(ipDetail)) {
                return ipDetail;
            } else { // 失败就休眠2秒，再尝试
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    log.error("tryGetIpDetailOrNullThreeTimes InterruptedException", e);
                }
            }
        }
        return null;
    }

    private static IpDetail getIPDetailOrNull(String ip) {
        try {
            // 请求淘宝IP解析接口，获取数据
            String url = String.format("https://ip.taobao.com/outGetIpInfo?ip=%s&accessKey=alibaba-inc", ip);
            String data = HttpUtil.get(url);
            // 使用JsonUtils把数据转换成ApiResult类型，因为两者都有一个data字段
            ApiResult<IpDetail> apiResult = JsonUtils.toObj(data, new TypeReference<ApiResult<IpDetail>>() {
            });
            // 返回ipDetail
            return apiResult.getData();
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        // 测试
        AtomicReference<Date> begin = new AtomicReference<>(new Date());
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            executor.execute(() -> {
                IpDetail ipDetail = tryGetIpDetailOrNullThreeTimes("125.80.190.40");
                if (Objects.nonNull(ipDetail)) {
                    Date date = new Date();
                    System.out.println(String.format("第%d次成功，耗时:%dms", finalI, (date.getTime() - begin.get().getTime())));
                    begin.set(date);
                }
            });
        }
    }

    @Override
    public void destroy() throws Exception {
        // 关闭线程池，接下来线程池不会接收新任务，会继续处理未完成的任务
        executor.shutdown();
        if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {//最多处理30秒，处理不完强制关闭
            if (log.isErrorEnabled()) {
                log.error("Timed out while waiting for executor [{}] to terminate", executor);
            }
        }
    }
}
