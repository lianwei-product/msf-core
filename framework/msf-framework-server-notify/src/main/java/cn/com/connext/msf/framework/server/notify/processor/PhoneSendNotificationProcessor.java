package cn.com.connext.msf.framework.server.notify.processor;

import cn.com.connext.msf.framework.notify.event.SaasNotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PhoneSendNotificationProcessor implements NotificationSendEventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(PhoneSendNotificationProcessor.class);

    @Override
    public boolean isMatch(SaasNotificationEvent.MessageType type) {
        return (SaasNotificationEvent.MessageType.PHONE == type || SaasNotificationEvent.MessageType.EXIST == type);
    }

    @Override
    public void process(SaasNotificationEvent saasNotificationEvent) {

    }
}
