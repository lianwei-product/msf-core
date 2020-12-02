package cn.com.connext.msf.server.gateway.cache;

import cn.com.connext.msf.framework.cache.AutoExpireCache;
import cn.com.connext.msf.server.gateway.client.GatewayAuthClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientRoleCache implements AutoExpireCache {

    private final Logger logger = LoggerFactory.getLogger(ClientRoleCache.class);

    private final String cacheName = "GatewayAuthRolesCache";
    private final int maximumSize;
    private final int expireAfterWrite;

    private final GatewayAuthClient gatewayAuthClient;

    public ClientRoleCache(@Value("${gateway.cache.clientRole.maxSize:5000}") int maximumSize,
                           @Value("${gateway.cache.clientRole.expireAfterWrite:7200}") int expireAfterWrite,
                           GatewayAuthClient gatewayAuthClient) {
        this.maximumSize = maximumSize;
        this.expireAfterWrite = expireAfterWrite;
        this.gatewayAuthClient = gatewayAuthClient;
    }

    @Cacheable(sync = true, cacheNames = cacheName, key = "#clientId")
    public synchronized List<String> getClientRoles(String clientType, String clientId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Init {} cache, clientType: {}, clientId: {}.", cacheName, clientType, clientId);
        }

        return gatewayAuthClient.getClientRoles(clientType, clientId);
    }

    @Override
    public int getMaximumSize() {
        return maximumSize;
    }

    @Override
    public int getExpireAfterWrite() {
        return expireAfterWrite;
    }

    @Override
    public String getCacheName() {
        return cacheName;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = cacheName, key = "#clientId")
    })
    public synchronized void clear(String clientId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Clear {} cache, clientId: {}.", cacheName, clientId);
        }
    }

}
