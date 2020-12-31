package cn.com.connext.msf.framework.notify.model;

import cn.com.connext.msf.framework.notify.constant.DingtalkMessageType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DingtalkNotificationInfo {

    @JsonProperty("msgtype")
    private String notifyType;
    private DingtalkTextInfo text;
    private DingtalkAtInfo at;

    private String token;
    private String secret;

    public static DingtalkNotificationInfo from(String messageType, String content, String[] phoneList, boolean isAtAll, String token, String secret) {
        switch (messageType) {
            case DingtalkMessageType.TEXT:
                return buildTextMessage(content, phoneList, isAtAll, token, secret);

            default:
                throw new RuntimeException("消息类型暂时不支持发送。");
        }
    }

    private static DingtalkNotificationInfo buildTextMessage(String content, String[] phoneList, boolean isAtAll, String token, String secret) {
        DingtalkNotificationInfo sendMessage = new DingtalkNotificationInfo();
        sendMessage.notifyType = DingtalkMessageType.TEXT;
        sendMessage.text = new DingtalkTextInfo();
        sendMessage.text.setContent(content);
        sendMessage.at = new DingtalkAtInfo();
        sendMessage.at.setAtMobiles(phoneList);
        sendMessage.at.setAtAll(isAtAll);
        sendMessage.token = token;
        sendMessage.secret = secret;

        return sendMessage;
    }


    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public DingtalkTextInfo getText() {
        return text;
    }

    public void setText(DingtalkTextInfo text) {
        this.text = text;
    }

    public DingtalkAtInfo getAt() {
        return at;
    }

    public void setAt(DingtalkAtInfo at) {
        this.at = at;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public static class DingtalkAtInfo {

        private String[] atMobiles;

        private boolean isAtAll;

        public String[] getAtMobiles() {
            return atMobiles;
        }

        public void setAtMobiles(String[] atMobiles) {
            this.atMobiles = atMobiles;
        }

        public boolean isAtAll() {
            return isAtAll;
        }

        public void setAtAll(boolean atAll) {
            isAtAll = atAll;
        }
    }

    public static class DingtalkTextInfo {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
