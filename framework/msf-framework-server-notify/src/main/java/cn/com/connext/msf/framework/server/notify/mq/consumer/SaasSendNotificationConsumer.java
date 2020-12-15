package cn.com.connext.msf.framework.server.notify.mq.consumer;


import cn.com.connext.msf.framework.server.notify.event.SaasNotificationEvent;
import cn.com.connext.msf.framework.server.notify.mq.pipeline.NotificationSendPipeline;
import cn.com.connext.msf.framework.server.notify.processor.NotificationSendEventProcessor;
import cn.com.connext.msf.framework.utils.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.List;

@EnableBinding(NotificationSendPipeline.class)
public class SaasSendNotificationConsumer {

    private final Logger logger = LoggerFactory.getLogger(SaasSendNotificationConsumer.class);

    private final List<NotificationSendEventProcessor> processChain;

    public SaasSendNotificationConsumer(List<NotificationSendEventProcessor> processChain) {
        this.processChain = processChain;
    }

    @StreamListener(NotificationSendPipeline.Recv)
    public void receiver(@Payload SaasNotificationEvent saasNotificationEvent) {

        if (logger.isDebugEnabled()) {
            logger.debug("SaaS Send notification: " + JSON.toIndentJsonString(saasNotificationEvent));
        }
        for (NotificationSendEventProcessor processor : processChain) {
            if (processor.isMatch(saasNotificationEvent.getMessageType())) {
                processor.process(saasNotificationEvent);
            }
        }

    }
}
