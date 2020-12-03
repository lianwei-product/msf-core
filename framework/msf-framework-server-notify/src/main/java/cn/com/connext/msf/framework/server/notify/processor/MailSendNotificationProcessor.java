package cn.com.connext.msf.framework.server.notify.processor;

import cn.com.connext.msf.framework.mail.domain.MailManager;
import cn.com.connext.msf.framework.notify.event.SaasNotificationEvent;
import cn.com.connext.msf.framework.notify.model.MailNotificationInfo;
import cn.com.connext.msf.framework.utils.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "mail.enable", havingValue = "true", matchIfMissing = false)
public class MailSendNotificationProcessor implements NotificationSendEventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MailSendNotificationProcessor.class);

    private MailManager mailManager;

    public MailSendNotificationProcessor(MailManager mailManager) {
        this.mailManager = mailManager;
    }


    @Override
    public boolean isMatch(SaasNotificationEvent.MessageType type) {
        return (SaasNotificationEvent.MessageType.MAIL == type || SaasNotificationEvent.MessageType.EXIST == type);
    }

    @Override
    public void process(SaasNotificationEvent saasNotificationEvent) {
        String content = saasNotificationEvent.getContent();
        MailNotificationInfo mailNotificationInfo = null;
        try {
            mailNotificationInfo = JSON.parseObject(content, MailNotificationInfo.class);
        } catch (Exception e) {
            logger.error("MailNotificationInfo 转换失败,content：" + content);
        }
        if (mailNotificationInfo == null) return;
        try {
            mailManager.sendTemplateMail(
                    mailNotificationInfo.getSubject(),
                    mailNotificationInfo.getFrom(),
                    mailNotificationInfo.getTo(),
                    mailNotificationInfo.getContent(),
                    mailNotificationInfo.getTemplatePath(),
                    mailNotificationInfo.getTemplateFileName()
            );
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
}
