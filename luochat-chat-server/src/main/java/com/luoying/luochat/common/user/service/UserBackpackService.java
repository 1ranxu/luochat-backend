package com.luoying.luochat.common.user.service;

import com.luoying.luochat.common.user.domain.enums.IdempotentEnum;

/**
 * <p>
 * 用户背包表 服务类
 * </p>
 *
 * @author <a href="https://github.com/1ranxu">luoying</a>
 * @since 2024-01-05
 */
public interface UserBackpackService {

    /**
     * 给用户发放一个物品
     *
     * @param uid            用户id
     * @param itemId         物品id
     * @param idempotentEnum 幂等类型
     * @param businessId     业务唯一标识
     */
    void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId);

}
