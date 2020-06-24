package cn.com.connext.msf.framework.notify.pipeline;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface NotificationSendPipeline {
    String SEND = "saas_notification_event_send";

    @Output(SEND)
    MessageChannel sendChannel();
}
