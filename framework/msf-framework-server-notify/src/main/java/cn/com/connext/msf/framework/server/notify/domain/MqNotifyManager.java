package cn.com.connext.msf.framework.server.notify.domain;

import cn.com.connext.msf.framework.notify.domain.NotifyManager;
import cn.com.connext.msf.framework.notify.event.SaasNotificationEvent;
import cn.com.connext.msf.framework.server.notify.mq.publisher.SaasSendNotificationPublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@ConditionalOnClass(MessageChannel.class)
@Component
public class MqNotifyManager implements NotifyManager {
    private final SaasSendNotificationPublisher saasSendNotificationPublisher;

    public MqNotifyManager(SaasSendNotificationPublisher saasSendNotificationPublisher) {
        this.saasSendNotificationPublisher = saasSendNotificationPublisher;
    }

    public void sendNotify(SaasNotificationEvent saasNotificationEvent) {
        saasSendNotificationPublisher.send(saasNotificationEvent);
    }
}
