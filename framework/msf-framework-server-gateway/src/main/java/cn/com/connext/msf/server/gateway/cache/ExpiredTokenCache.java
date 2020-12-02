package cn.com.connext.msf.server.gateway.cache;

import cn.com.connext.msf.framework.cache.ExpireStatusCache;
import cn.com.connext.msf.framework.route.event.GatewayAuthUpdateEvent;
import cn.com.connext.msf.framework.utils.JSON;
import cn.com.connext.msf.server.gateway.client.GatewayAuthClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class ExpiredTokenCache {

    private final Logger logger = LoggerFactory.getLogger(ExpiredTokenCache.class);
    private final int tokenExpireSeconds;
    private final ExpireStatusCache<Date> expireStatusCache;
    private final GatewayAuthClient gatewayAuthClient;

    public ExpiredTokenCache(GatewayAuthClient gatewayAuthClient) {
        this.gatewayAuthClient = gatewayAuthClient;
        this.tokenExpireSeconds = this.gatewayAuthClient.getExpiration();
        this.expireStatusCache = new ExpireStatusCache<>(tokenExpireSeconds, TimeUnit.SECONDS, 10000);
    }

    public void append(GatewayAuthUpdateEvent event) {
        expireStatusCache.put(event.getClientId(), event.getTime());
        if (logger.isDebugEnabled()) {
            logger.debug("Receive GatewayAuthUpdateEvent: {}.", JSON.toJsonString(event));
        }
    }

    public boolean expired(String clientId, long tokenExpireTime) {
        Date expireTime = expireStatusCache.get(clientId);
        if (expireTime == null) {
            return false;
        }

        long expireTimestamp = expireTime.getTime();
        long tokenCreateTime = tokenExpireTime - tokenExpireSeconds * 1000;

        return expireTimestamp > tokenCreateTime;
    }

}
