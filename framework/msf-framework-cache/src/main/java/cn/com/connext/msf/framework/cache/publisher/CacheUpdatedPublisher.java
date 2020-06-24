package cn.com.connext.msf.framework.cache.publisher;

import cn.com.connext.msf.framework.cache.event.CacheUpdatedEvent;
import cn.com.connext.msf.framework.cache.pipeline.CacheUpdatedPipeline;
import cn.com.connext.msf.framework.utils.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

@EnableBinding(CacheUpdatedPipeline.class)
public class CacheUpdatedPublisher {
    private final Logger logger = LoggerFactory.getLogger(CacheUpdatedPublisher.class);
    private final MessageChannel sendChannel;

    public CacheUpdatedPublisher(@Qualifier(CacheUpdatedPipeline.SEND) MessageChannel sendChannel) {
        this.sendChannel = sendChannel;
    }

    public void send(CacheUpdatedEvent cacheUpdatedEvent) {
        Message<CacheUpdatedEvent> message = MessageBuilder.withPayload(cacheUpdatedEvent).build();
        if (logger.isDebugEnabled()) {
            logger.debug("CacheUpdatePublisher send cache updated event: " + JSON.toJsonString(cacheUpdatedEvent));
        }
        sendChannel.send(message);
    }
}
