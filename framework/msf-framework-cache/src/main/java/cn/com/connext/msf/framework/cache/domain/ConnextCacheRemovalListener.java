package cn.com.connext.msf.framework.cache.domain;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnextCacheRemovalListener implements RemovalListener<Object, Object> {

    private final Logger logger = LoggerFactory.getLogger(ConnextCacheRemovalListener.class);
    private final String cacheName;

    public ConnextCacheRemovalListener(String cacheName) {
        this.cacheName = cacheName;
    }

    public void onRemoval(Object key, Object value, RemovalCause cause) {
        if (logger.isDebugEnabled()) {
            logger.debug("Remove [{}] cache, key: {}, cause: {}", cacheName, key, cause);
        }

        if (value == null) {
            return;
        }

        if (value instanceof AutoCloseable) {
            AutoCloseable autoCloseable = (AutoCloseable) value;
            try {
                autoCloseable.close();
            } catch (Exception e) {
                logger.error("Close AutoCloseable cache error.", e);
            }
        }
    }
}
