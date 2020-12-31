package cn.com.connext.msf.framework.server.notify.mq.publisher;

import cn.com.connext.msf.framework.notify.event.SaasNotificationEvent;
import cn.com.connext.msf.framework.server.notify.mq.pipeline.NotificationSendPipeline;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

@ConditionalOnClass(MessageChannel.class)
@EnableBinding(NotificationSendPipeline.class)
public class SaasSendNotificationPublisher {

    private final MessageChannel sendChannel;

    public SaasSendNotificationPublisher(@Qualifier(NotificationSendPipeline.SEND) MessageChannel sendChannel) {
        this.sendChannel = sendChannel;
    }

    public void send(SaasNotificationEvent saasNotificationEvent) {
        Message<SaasNotificationEvent> message = MessageBuilder.withPayload(saasNotificationEvent).build();
        sendChannel.send(message);
    }
}
