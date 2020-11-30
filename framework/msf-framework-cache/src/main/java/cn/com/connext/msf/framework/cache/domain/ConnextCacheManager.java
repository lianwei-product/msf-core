package cn.com.connext.msf.framework.cache.domain;

import cn.com.connext.msf.framework.cache.AutoExpireCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ConnextCacheManager extends CaffeineCacheManager {

    private final Logger logger = LoggerFactory.getLogger(ConnextCacheManager.class);
    private final Map<String, AutoExpireCache> cacheMap;

    public ConnextCacheManager(@Autowired(required = false) List<AutoExpireCache> cacheList) {
        this.cacheMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(cacheList)) {
            cacheList.forEach(cache -> {
                cacheMap.put(cache.getCacheName(), cache);
            });
        }
    }

    @Override
    protected Cache createCaffeineCache(String name) {
        return new CaffeineCache(name, createNativeCaffeineCache(name), isAllowNullValues());
    }

    @Override
    protected com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(String name) {
        AutoExpireCache autoExpireCache = cacheMap.get(name);
        if (autoExpireCache == null) {
            logger.info("Init non expire cache, cache name: {}", name);
            return Caffeine.newBuilder().recordStats().build();
        }

        return autoExpireCache.isExpireAfterAccessMode()
                ? createExpireAfterAccessCache(autoExpireCache)
                : createExpireAfterWriteCache(autoExpireCache);
    }

    private com.github.benmanes.caffeine.cache.Cache<Object, Object> createExpireAfterAccessCache(AutoExpireCache autoExpireCache) {
        logger.info("Init auto expire after access cache, cache name: {}", autoExpireCache.getCacheName());

        if (autoExpireCache.getExpireAfterAccess() <= 0) {
            throw new RuntimeException("Cache " + autoExpireCache.getCacheName() + " expireAfterAccess must greater then zero.");
        }

        return Caffeine.newBuilder().recordStats()
                .maximumSize(autoExpireCache.getMaximumSize())
                .expireAfterAccess(autoExpireCache.getExpireAfterAccess(), TimeUnit.SECONDS)
                .removalListener(new ConnextCacheRemovalListener(autoExpireCache.getCacheName()))
                .build();
    }

    private com.github.benmanes.caffeine.cache.Cache<Object, Object> createExpireAfterWriteCache(AutoExpireCache autoExpireCache) {
        logger.info("Init auto expire after write cache, cache name: {}", autoExpireCache.getCacheName());

        if (autoExpireCache.getExpireAfterWrite() <= 0) {
            throw new RuntimeException("Cache " + autoExpireCache.getCacheName() + " expireAfterWrite must greater then zero.");
        }

        return Caffeine.newBuilder().recordStats()
                .maximumSize(autoExpireCache.getMaximumSize())
                .expireAfterWrite(autoExpireCache.getExpireAfterWrite(), TimeUnit.SECONDS)
                .removalListener(new ConnextCacheRemovalListener(autoExpireCache.getCacheName()))
                .build();
    }

}
