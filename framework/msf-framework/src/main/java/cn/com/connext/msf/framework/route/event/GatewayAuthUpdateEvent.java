package cn.com.connext.msf.framework.route.event;

import java.util.Date;

public class GatewayAuthUpdateEvent {
    private MessageType messageType;
    private String clientId;
    private String roleId;
    private Date time;

    public static GatewayAuthUpdateEvent tokenExpiredEvent(String clientId) {
        return from(MessageType.TokenExpired, clientId, null);
    }

    public static GatewayAuthUpdateEvent clientUpdatedEvent(String clientId) {
        return from(MessageType.ClientRolesUpdated, clientId, "");
    }

    public static GatewayAuthUpdateEvent roleUpdatedEvent(String roleId) {
        return from(MessageType.RoleAuthoritiesUpdated, "", roleId);
    }

    public static GatewayAuthUpdateEvent from(MessageType messageType, String clientId, String roleId) {
        GatewayAuthUpdateEvent event = new GatewayAuthUpdateEvent();
        event.messageType = messageType;
        event.clientId = clientId;
        event.roleId = roleId;
        event.time = new Date();
        return event;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRoleId() {
        return roleId;
    }

    public Date getTime() {
        return time;
    }

    public enum MessageType {
        ClientRolesUpdated, RoleAuthoritiesUpdated, TokenExpired, ClientUserLoginToken
    }
}
