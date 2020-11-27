package cn.com.connext.msf.framework.mail.config;

import cn.com.connext.msf.framework.mail.domain.MailManager;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnProperty(name = "mail.enable", havingValue = "true")
@Import({MailSenderAutoConfiguration.class, MailManager.class})
public class MailConfig {

    public MailConfig() {
        LoggerFactory.getLogger(MailConfig.class).info("Init smtp mail config...");
    }
}
