package cn.com.connext.msf.framework.mail.domain;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

public class MailManager {

    private final Logger logger = LoggerFactory.getLogger(MailManager.class);

    private final JavaMailSender javaMailSender;
    private final String ccName;
    private final String bccName;

    public MailManager(JavaMailSender javaMailSender,
                       @Value("${mail.ccName:#{null}}") String ccName,
                       @Value("${mail.bccName:#{null}}") String bccName) {
        this.javaMailSender = javaMailSender;
        this.ccName = ccName;
        this.bccName = bccName;
    }

    public void sendSimpleMail(String subject, String from, String to, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setFrom(from);
        message.setTo(to);
        message.setSentDate(new Date());
        message.setText(content);
        if (ccName != null) message.setCc(ccName.split(","));
        if (bccName != null) message.setBcc(bccName.split(","));

        javaMailSender.send(message);
    }

    public void sendTemplateMail(String subject, String from, String to, Object object,
                                 String templatePath, String templateFileName)
            throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject(subject);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSentDate(new Date());
        if (ccName != null) helper.setCc(ccName.split(","));
        if (bccName != null) helper.setBcc(bccName.split(","));

        Template template = getBodyTemplate(templatePath, templateFileName);
        if (template == null) return;
        String text = getBodyText(template, object);
        if (text == null) return;
        helper.setText(text, true);
        javaMailSender.send(mimeMessage);
    }

    private Template getBodyTemplate(String templatePath, String templateFileName) {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
        configuration.setClassForTemplateLoading(this.getClass(), templatePath);
        try {
            return configuration.getTemplate(templateFileName);
        } catch (IOException e) {
            logger.error("Message template not found error: {}", e.getLocalizedMessage());
        }
        return null;
    }

    private String getBodyText(Template template, Object object) {
        StringWriter out = new StringWriter();
        try {
            template.process(object, out);
            return out.toString();
        } catch (Exception e) {
            logger.error("Message template structure error: {}", e.getLocalizedMessage());
        }
        return null;
    }
}
