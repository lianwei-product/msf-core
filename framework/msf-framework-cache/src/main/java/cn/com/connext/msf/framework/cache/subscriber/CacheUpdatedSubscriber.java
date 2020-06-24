package cn.com.connext.msf.framework.cache.subscriber;

import cn.com.connext.msf.framework.cache.ClearableCache;
import cn.com.connext.msf.framework.cache.event.CacheUpdatedEvent;
import cn.com.connext.msf.framework.cache.pipeline.CacheUpdatedPipeline;
import cn.com.connext.msf.framework.utils.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.List;
import java.util.Objects;

@EnableBinding({CacheUpdatedPipeline.class})
public class CacheUpdatedSubscriber {

    private final Logger logger = LoggerFactory.getLogger(CacheUpdatedSubscriber.class);
    private final List<ClearableCache> clearableCacheList;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    public CacheUpdatedSubscriber(@Autowired(required = false) List<ClearableCache> clearableCacheList) {
        this.clearableCacheList = clearableCacheList;
    }

    @StreamListener(CacheUpdatedPipeline.RECV)
    public void receiver(@Payload CacheUpdatedEvent cacheUpdatedEvent) {
        if (logger.isDebugEnabled()) {
            logger.debug("CacheUpdateSubscriber received cache updated event: " + JSON.toJsonString(cacheUpdatedEvent));
        }
        if (clearableCacheList == null) {
            return;
        }
        for (ClearableCache cache : clearableCacheList) {
            if (Objects.equals(cache.getCacheName(), cacheUpdatedEvent.getCacheName())) {
                cache.clear(cacheUpdatedEvent.getCacheId());
            }
        }
    }
}
