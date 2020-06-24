package cn.com.connext.msf.framework.cache.event;

public class CacheUpdatedEvent {

    private String cacheName;
    private String cacheId;

    public static CacheUpdatedEvent from(String cacheName, String cacheId) {
        CacheUpdatedEvent cacheUpdatedEvent = new CacheUpdatedEvent();
        cacheUpdatedEvent.cacheName = cacheName;
        cacheUpdatedEvent.cacheId = cacheId;
        return cacheUpdatedEvent;
    }

    public static CacheUpdatedEvent from(String cacheName, String prefix, String cacheId) {
        CacheUpdatedEvent cacheUpdatedEvent = new CacheUpdatedEvent();
        cacheUpdatedEvent.cacheName = cacheName;
        cacheUpdatedEvent.cacheId = prefix.concat("_").concat(cacheId);
        return cacheUpdatedEvent;
    }

    public String getCacheName() {
        return cacheName;
    }

    public String getCacheId() {
        return cacheId;
    }

}
