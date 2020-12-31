package cn.com.connext.msf.framework.server.notify.domain;

import cn.com.connext.msf.framework.notify.domain.NotifyManager;
import cn.com.connext.msf.framework.notify.event.SaasNotificationEvent;
import cn.com.connext.msf.framework.server.notify.processor.NotificationSendEventProcessor;

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
