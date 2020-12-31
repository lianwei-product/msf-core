package cn.com.connext.msf.framework.notify.event;


public class SaasNotificationEvent {

    private String applicationName;
    private MessageType messageType;
    private String content;

    public static SaasNotificationEvent from(MessageType messageType, String content, String applicationName) {
        SaasNotificationEvent saasNotificationEvent = new SaasNotificationEvent();
        saasNotificationEvent.setMessageType(messageType);
        saasNotificationEvent.setContent(content);
        saasNotificationEvent.setApplicationName(applicationName);
        return saasNotificationEvent;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public enum MessageType {
        //服务中存在的都发
        EXIST,
        MAIL, WECHAT, DINGTALK, PHONE
    }
}
