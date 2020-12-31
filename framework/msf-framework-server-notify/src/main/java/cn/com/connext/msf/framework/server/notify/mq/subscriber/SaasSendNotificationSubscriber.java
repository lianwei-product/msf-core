package cn.com.connext.msf.framework.server.notify.mq.subscriber;


import cn.com.connext.msf.framework.notify.event.SaasNotificationEvent;
import cn.com.connext.msf.framework.server.notify.mq.pipeline.NotificationSendPipeline;
import cn.com.connext.msf.framework.server.notify.processor.NotificationSendEventProcessor;
import cn.com.connext.msf.framework.utils.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.List;

@ConditionalOnClass(MessageChannel.class)
@EnableBinding(NotificationSendPipeline.class)
public class SaasSendNotificationSubscriber {

    private final Logger logger = LoggerFactory.getLogger(SaasSendNotificationSubscriber.class);

    private final List<NotificationSendEventProcessor> processChain;
    private final String applicationName;

    public SaasSendNotificationSubscriber(@Value("${msf.application.name}") String applicationName,
                                          List<NotificationSendEventProcessor> processChain) {
        this.processChain = processChain;
        this.applicationName = applicationName;
    }

    @StreamListener(NotificationSendPipeline.Recv)
    public void receiver(@Payload SaasNotificationEvent saasNotificationEvent) {

        if (logger.isDebugEnabled()) {
            logger.debug("SaaS Send notification: " + JSON.toIndentJsonString(saasNotificationEvent));
        }
        if (!applicationName.equals(saasNotificationEvent.getApplicationName())) return;

        for (NotificationSendEventProcessor processor : processChain) {
            if (processor.isMatch(saasNotificationEvent.getMessageType())) {
                processor.process(saasNotificationEvent);
            }
        }

    }
}
