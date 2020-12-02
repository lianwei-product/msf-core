package cn.com.connext.msf.server.gateway.cache;

import cn.com.connext.msf.framework.cache.AutoExpireCache;
import cn.com.connext.msf.framework.cache.GlobalCacheNames;
import cn.com.connext.msf.server.gateway.client.GatewayAuthClient;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientTrustedHostsCache implements AutoExpireCache {

    private final Logger logger = LoggerFactory.getLogger(ClientTrustedHostsCache.class);

    private final String cacheName = GlobalCacheNames.PLATFORM_GATEWAY_CLIENT_TRUSTED_HOSTS;
    private final int maximumSize;
    private final int expireAfterWrite;

    private final GatewayAuthClient gatewayAuthClient;

    public ClientTrustedHostsCache(@Value("${gateway.cache.clientTrustedHosts.maxSize:1000}") int maximumSize,
                                   @Value("${gateway.cache.clientTrustedHosts.expireAfterWrite:36000}") int expireAfterWrite,
                                   GatewayAuthClient gatewayAuthClient) {
        this.maximumSize = maximumSize;
        this.expireAfterWrite = expireAfterWrite;
        this.gatewayAuthClient = gatewayAuthClient;
    }

    @Cacheable(sync = true, cacheNames = cacheName, key = "#clientId")
    public synchronized List<String> getTrustedHosts(String clientId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Init {} cache, clientId: {}.", cacheName, clientId);
        }

        String trustedHosts = gatewayAuthClient.findTrustedHosts(clientId);
        return StringUtils.isNotEmpty(trustedHosts) ? Lists.newArrayList(trustedHosts.split(";")) : Lists.newArrayList();
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
