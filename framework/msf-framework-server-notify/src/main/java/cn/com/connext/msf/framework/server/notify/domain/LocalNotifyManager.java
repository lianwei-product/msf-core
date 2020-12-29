package cn.com.connext.msf.framework.server.notify.domain;

import cn.com.connext.msf.framework.server.notify.event.SaasNotificationEvent;
import cn.com.connext.msf.framework.server.notify.processor.NotificationSendEventProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import java.util.List;


public class LocalNotifyManager implements NotifyManager {
    private final List<NotificationSendEventProcessor> processChain;

    public LocalNotifyManager(List<NotificationSendEventProcessor> processChain) {
        this.processChain = processChain;
    }


    public void sendNotify(SaasNotificationEvent saasNotificationEvent) {
        for (NotificationSendEventProcessor processor : processChain) {
            if (processor.isMatch(saasNotificationEvent.getMessageType())) {
                processor.process(saasNotificationEvent);
            }
        }
    }
}
