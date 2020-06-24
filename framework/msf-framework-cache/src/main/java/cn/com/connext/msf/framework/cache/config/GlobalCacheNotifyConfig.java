package cn.com.connext.msf.framework.cache.config;

import cn.com.connext.msf.framework.cache.publisher.CacheUpdatedPublisher;
import cn.com.connext.msf.framework.cache.subscriber.CacheUpdatedSubscriber;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.MessageChannel;

@Configuration
@ConditionalOnClass(MessageChannel.class)
@Import({
        CacheUpdatedPublisher.class,
        CacheUpdatedSubscriber.class
})
public class GlobalCacheNotifyConfig {

    public GlobalCacheNotifyConfig() {
        LoggerFactory.getLogger(GlobalCacheNotifyConfig.class).info("Init cache notify by message queue.");
    }

}