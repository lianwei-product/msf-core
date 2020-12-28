package cn.com.connext.msf.framework.server.notify.mq.pipeline;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

@ConditionalOnClass(MessageChannel.class)
public interface NotificationSendPipeline {

    String SEND = "saas_notification_event_send";
    String Recv = "saas_notification_event_recv";

    @Output(SEND)
    MessageChannel sendChannel();

    @Input(Recv)
    MessageChannel recvChannel();
}
