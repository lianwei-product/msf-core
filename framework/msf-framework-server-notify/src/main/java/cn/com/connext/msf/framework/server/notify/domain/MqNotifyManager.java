package cn.com.connext.msf.framework.server.notify.domain;

import cn.com.connext.msf.framework.server.notify.event.SaasNotificationEvent;
import cn.com.connext.msf.framework.server.notify.mq.producer.SaasSendNotificationProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@ConditionalOnClass(MessageChannel.class)
@Component
public class MqNotifyManager implements NotifyManager {
    private final SaasSendNotificationProducer saasSendNotificationProducer;

    public MqNotifyManager(SaasSendNotificationProducer saasSendNotificationProducer) {
        this.saasSendNotificationProducer = saasSendNotificationProducer;
    }

    public void sendNotify(SaasNotificationEvent saasNotificationEvent) {
        saasSendNotificationProducer.send(saasNotificationEvent);
    }
}
