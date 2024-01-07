package com.luoying.luochat.common.user.service.impl;

import com.luoying.luochat.common.common.domain.enums.YesOrNoEnum;
import com.luoying.luochat.common.common.utils.AssertUtil;
import com.luoying.luochat.common.user.dao.UserBackpackDao;
import com.luoying.luochat.common.user.domain.entity.UserBackpack;
import com.luoying.luochat.common.user.domain.enums.IdempotentEnum;
import com.luoying.luochat.common.user.service.UserBackpackService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/7 12:21
 */
@Service
public class UserBackpackServiceImpl implements UserBackpackService {
    @Resource
    private RedissonClient redissonClient;

    @Resource
    private UserBackpackDao userBackpackDao;

    @Override
    public void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        // 组装幂等号
        String idempotent = getIdempotent(itemId, idempotentEnum, businessId);
        // 加锁
        RLock lock = redissonClient.getLock("acquireItem_" + idempotent);
        boolean b = lock.tryLock();
        AssertUtil.isTrue(b, "请求太频繁了");
        try {
            // 幂等判断
            UserBackpack userBackpack = userBackpackDao.getbyIdempotent(idempotent);
            if (Objects.nonNull(userBackpack)) {
                return;
            }
            // todo 业务检查
            // 发放物品
            UserBackpack insert = UserBackpack.builder()
                    .uid(uid)
                    .itemId(itemId)
                    .status(YesOrNoEnum.NO.getStatus())
                    .idempotent(idempotent).build();
            userBackpackDao.save(insert);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 组装幂等号
     *
     * @param itemId         物品id
     * @param idempotentEnum 幂等类型
     * @param businessId     业务唯一标识
     * @return
     */
    private String getIdempotent(Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        return String.format("%d_%d_%s", itemId, idempotentEnum.getType(), businessId);
    }
}
