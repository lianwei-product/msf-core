package cn.com.connext.msf.framework.notify.model;

public class MailNotificationInfo {

    private String notifyType;

    private String from;

    private String to;

    private String[] cc;

    private String[] bcc;

    private Object content;

    private String subject;

    private String templatePath;

    private String templateFileName;

    public static MailNotificationInfo from(String notifyType,
                                            String from,
                                            String to,
                                            String[] cc,
                                            String[] bcc,
                                            Object content,
                                            String subject,
                                            String templatePath,
                                            String templateFileName) {
        MailNotificationInfo mailNotificationInfo=new MailNotificationInfo();
        mailNotificationInfo.notifyType = notifyType;
        mailNotificationInfo.from = from;
        mailNotificationInfo.to = to;
        mailNotificationInfo.cc = cc;
        mailNotificationInfo.bcc = bcc;
        mailNotificationInfo.content = content;
        mailNotificationInfo.subject = subject;
        mailNotificationInfo.templatePath = templatePath;
        mailNotificationInfo.templateFileName = templateFileName;
        return mailNotificationInfo;
    }


    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String[] getBcc() {
        return bcc;
    }

    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getTemplateFileName() {
        return templateFileName;
    }

    public void setTemplateFileName(String templateFileName) {
        this.templateFileName = templateFileName;
    }
}
