package cn.com.connext.msf.server.management.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

//@Component
//@ConditionalOnMissingBean(ManagedSendNotificationProcessor.class)
public class BaseManagedSendNotificationProcessor implements ManagedSendNotificationProcessor {
    private static final Logger logger = LoggerFactory.getLogger(BaseManagedSendNotificationProcessor.class);

    @Override
    public void process(String content) {
        logger.error(content);
    }
}
