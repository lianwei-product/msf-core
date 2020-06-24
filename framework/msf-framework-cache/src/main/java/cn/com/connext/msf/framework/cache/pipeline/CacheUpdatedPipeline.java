package cn.com.connext.msf.framework.cache.pipeline;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface CacheUpdatedPipeline {
    String SEND = "application_cache_updated_event_send";
    String RECV = "application_cache_updated_event_recv";

    @Output(SEND)
    MessageChannel sendChannel();

    @Input(RECV)
    SubscribableChannel recvChannel();
}
