package cn.com.connext.msf.framework.notify.event;


public class SaasNotificationEvent {

    private MessageType messageType;
    private String content;

    public static SaasNotificationEvent from(MessageType messageType, String content) {
        SaasNotificationEvent saasNotificationEvent = new SaasNotificationEvent();
        saasNotificationEvent.setMessageType(messageType);
        saasNotificationEvent.setContent(content);
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

    public enum MessageType {
        //服务中存在的都发
        EXIST,
        MAIL, WECHAT, DINGTALK, PHONE
    }
}
