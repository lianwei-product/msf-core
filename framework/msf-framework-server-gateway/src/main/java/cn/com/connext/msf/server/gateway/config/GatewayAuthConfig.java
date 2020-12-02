package cn.com.connext.msf.server.gateway.config;

import cn.com.connext.msf.server.gateway.cache.ClientRoleCache;
import cn.com.connext.msf.server.gateway.cache.ClientTrustedHostsCache;
import cn.com.connext.msf.server.gateway.cache.ExpiredTokenCache;
import cn.com.connext.msf.server.gateway.cache.RoleAuthoritiesCache;
import cn.com.connext.msf.server.gateway.client.GatewayAuthClient;
import cn.com.connext.msf.server.gateway.filter.GatewayAuthFilter;
import cn.com.connext.msf.server.gateway.mq.GatewayAuthUpdateSubscriber;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnExpression("${msf.management.enabled:true}&&${msf.authorization.enabled:true}")
@EnableFeignClients(basePackageClasses = {GatewayAuthClient.class})
@Import({ClientRoleCache.class,
        GatewayAuthFilter.class,
        RoleAuthoritiesCache.class,
        ExpiredTokenCache.class,
        GatewayAuthUpdateSubscriber.class,
        ClientTrustedHostsCache.class})
public class GatewayAuthConfig {

    public GatewayAuthConfig() {
        LoggerFactory.getLogger(GatewayAuthConfig.class).info("Init gateway auth filter.");
    }
}
