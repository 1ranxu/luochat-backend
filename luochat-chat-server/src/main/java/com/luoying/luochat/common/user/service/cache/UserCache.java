package com.luoying.luochat.common.user.service.cache;

import com.luoying.luochat.common.user.dao.BlackDao;
import com.luoying.luochat.common.user.dao.UserRoleDao;
import com.luoying.luochat.common.user.domain.entity.Black;
import com.luoying.luochat.common.user.domain.entity.UserRole;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description: 用户相关缓存
 *
 * @Author 落樱的悔恨
 * @Date 2024/1/8 17:08
 */
@Component
public class UserCache {
    @Resource
    private UserRoleDao userRoleDao;

    @Resource
    private BlackDao blackDao;

    /**
     * 获取角色缓存
     */
    @Cacheable(cacheNames = "user", key = "'roles'+#uid")
    public Set<Long> getRoleSet(Long uid) {
        List<UserRole> userRoles = userRoleDao.listByUid(uid);
        return userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toSet());
    }

    /**
     *获取黑名单缓存
     */
    @Cacheable(cacheNames = "user", key = "'blackMap'")
    public Map<Integer, Set<String>> getBlackMap() {
        Map<Integer, List<Black>> map = blackDao.list().stream().collect(Collectors.groupingBy(Black::getType));
        Map<Integer, Set<String>> resultMap = new HashMap<>();
        map.forEach((type, list) -> {
            resultMap.put(type, list.stream().map(black -> black.getTarget()).collect(Collectors.toSet()));
        });
        return resultMap;
    }

    /**
     * 清空黑名单缓存
     * @return
     */
    @CacheEvict(cacheNames = "user", key = "'blackList'")
    public Map<Integer, Set<String>> evictBlackMap() {
        return null;
    }
}
