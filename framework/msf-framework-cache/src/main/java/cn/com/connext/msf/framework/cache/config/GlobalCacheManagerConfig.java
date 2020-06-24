package cn.com.connext.msf.framework.cache.config;

import cn.com.connext.msf.framework.cache.AutoExpireCache;
import cn.com.connext.msf.framework.cache.domain.ConnextCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableCaching
@ConditionalOnProperty(name = "msf.globalCache.enable", havingValue = "true", matchIfMissing = true)
public class GlobalCacheManagerConfig {

    @Bean
    public CacheManager cacheManager(@Autowired(required = false) List<AutoExpireCache> cacheList) {
        return new ConnextCacheManager(cacheList);
    }
}
