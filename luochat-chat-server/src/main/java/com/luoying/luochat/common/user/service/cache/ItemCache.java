package com.luoying.luochat.common.user.service.cache;

import com.luoying.luochat.common.user.dao.ItemConfigDao;
import com.luoying.luochat.common.user.domain.entity.ItemConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/6 19:35
 */
@Component
public class ItemCache {
    @Resource
    private ItemConfigDao itemConfigDao;

    @Cacheable(cacheNames = "item", key = "'itemsByType:'+#itemTYpe")
    public List<ItemConfig> getByType(Integer itemType) {
        return itemConfigDao.getByType(itemType);
    }

    @CacheEvict(cacheNames = "item", key = "'itemsByType:'+#itemTYpe")
    public void evictByType(Integer itemType) {
    }
}
