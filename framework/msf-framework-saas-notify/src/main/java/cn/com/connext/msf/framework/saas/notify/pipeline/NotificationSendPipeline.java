package cn.com.connext.msf.framework.saas.notify.pipeline;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface NotificationSendPipeline {

    String Recv = "saas_notification_event_recv";

    @Input(Recv)
    MessageChannel recvChannel();
}
