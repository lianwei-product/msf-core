package cn.com.connext.msf.framework.cache.domain;

import cn.com.connext.msf.framework.cache.AutoExpireCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ConnextCacheManager extends CaffeineCacheManager {

    private final Logger logger = LoggerFactory.getLogger(ConnextCacheManager.class);
    private final List<AutoExpireCache> cacheList;

    public ConnextCacheManager(@Autowired(required = false) List<AutoExpireCache> cacheList) {
        this.cacheList = cacheList;
    }

    @Override
    protected Cache createCaffeineCache(String name) {
        return new CaffeineCache(name, createNativeCaffeineCache(name), isAllowNullValues());
    }

    @Override
    protected com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(String name) {
        if (cacheList != null && cacheList.size() > 0) {
            for (AutoExpireCache autoExpireCache : cacheList) {
                if (Objects.equals(name, autoExpireCache.getCacheName())) {
                    logger.info("Init auto expire cache, cache name: {}", name);
                    return Caffeine.newBuilder().recordStats()
                            .maximumSize(autoExpireCache.getMaximumSize())
                            .expireAfterWrite(autoExpireCache.getExpireAfterWrite(), TimeUnit.SECONDS)
                            .build();
                }
            }
        }

        logger.info("Init non expire cache, cache name: {}", name);
        return Caffeine.newBuilder().recordStats().build();
    }
}
