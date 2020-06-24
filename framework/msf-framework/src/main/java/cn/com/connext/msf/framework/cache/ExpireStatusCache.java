package cn.com.connext.msf.framework.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;

import java.util.concurrent.TimeUnit;

public class ExpireStatusCache<T> {

    private final Cache<String, T> cache;

    public ExpireStatusCache(long duration, TimeUnit unit, int maximumSize) {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(duration, unit)
                .executor(Runnable::run)
                .maximumSize(maximumSize)
                .build();
    }

    public void put(String clientId, T t) {
        cache.put(clientId, t);
    }

    public T get(String id) {
        return cache.getIfPresent(id);
    }

    public CacheStats status() {
        return cache.stats();
    }
}
