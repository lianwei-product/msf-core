package cn.com.connext.msf.server.gateway.cache;

import cn.com.connext.msf.framework.cache.AutoExpireCache;
import cn.com.connext.msf.server.gateway.client.GatewayAuthClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleAuthoritiesCache implements AutoExpireCache {

    private final Logger logger = LoggerFactory.getLogger(ClientRoleCache.class);

    private final String cacheName = "GatewayAuthAuthoritiesCache";
    private final int maximumSize;
    private final int expireAfterWrite;

    private final GatewayAuthClient gatewayAuthClient;

    public RoleAuthoritiesCache(@Value("${gateway.cache.roleAuthorities.maxSize:1000}") int maximumSize,
                                @Value("${gateway.cache.roleAuthorities.expireAfterWrite:72000}") int expireAfterWrite,
                                GatewayAuthClient gatewayAuthClient) {
        this.maximumSize = maximumSize;
        this.expireAfterWrite = expireAfterWrite;
        this.gatewayAuthClient = gatewayAuthClient;
    }

    @Cacheable(sync = true, cacheNames = cacheName, key = "#roleId")
    public synchronized List<String> getRoleAuthorities(String roleId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Init {} cache, roleId: {}.", cacheName, roleId);
        }

        return gatewayAuthClient.getRoleAuthorities(roleId);
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
    @CacheEvict(cacheNames = cacheName, key = "#roleId")
    public synchronized void clear(String roleId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Clear {} cache, roleId: {}.", cacheName, roleId);
        }
    }
}
