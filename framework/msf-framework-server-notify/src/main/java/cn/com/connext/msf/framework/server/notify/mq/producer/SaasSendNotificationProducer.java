package cn.com.connext.msf.framework.server.notify.mq.producer;

import cn.com.connext.msf.framework.server.notify.event.SaasNotificationEvent;
import cn.com.connext.msf.framework.server.notify.mq.pipeline.NotificationSendPipeline;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

@EnableBinding(NotificationSendPipeline.class)
public class SaasSendNotificationProducer {

    private final MessageChannel sendChannel;

    public SaasSendNotificationProducer(@Qualifier(NotificationSendPipeline.SEND) MessageChannel sendChannel) {
        this.sendChannel = sendChannel;
    }

    public void send(SaasNotificationEvent saasNotificationEvent) {
        Message<SaasNotificationEvent> message = MessageBuilder.withPayload(saasNotificationEvent).build();
        sendChannel.send(message);
    }
}
