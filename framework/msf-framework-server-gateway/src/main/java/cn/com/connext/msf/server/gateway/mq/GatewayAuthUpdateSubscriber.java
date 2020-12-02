package cn.com.connext.msf.server.gateway.mq;

import cn.com.connext.msf.framework.route.event.GatewayAuthUpdateEvent;
import cn.com.connext.msf.server.gateway.cache.ClientRoleCache;
import cn.com.connext.msf.server.gateway.cache.ExpiredTokenCache;
import cn.com.connext.msf.server.gateway.cache.RoleAuthoritiesCache;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;

@EnableBinding({GatewayAuthUpdatePipeline.class})
public class GatewayAuthUpdateSubscriber {

    private final ClientRoleCache clientRoleCache;
    private final RoleAuthoritiesCache roleAuthoritiesCache;
    private final ExpiredTokenCache expiredTokenCache;

    public GatewayAuthUpdateSubscriber(ClientRoleCache clientRoleCache,
                                       RoleAuthoritiesCache roleAuthoritiesCache,
                                       ExpiredTokenCache expiredTokenCache) {
        this.clientRoleCache = clientRoleCache;
        this.roleAuthoritiesCache = roleAuthoritiesCache;
        this.expiredTokenCache = expiredTokenCache;
    }

    @StreamListener(GatewayAuthUpdatePipeline.RECV)
    public void receiver(@Payload GatewayAuthUpdateEvent event) {
        switch (event.getMessageType()) {
            case ClientRolesUpdated:
                clientRoleCache.clear(event.getClientId());
                break;

            case RoleAuthoritiesUpdated:
                roleAuthoritiesCache.clear(event.getRoleId());
                break;

            case TokenExpired:
                expiredTokenCache.append(event);
                break;
        }
    }
}
