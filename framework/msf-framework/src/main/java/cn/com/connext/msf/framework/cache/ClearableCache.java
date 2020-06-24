package cn.com.connext.msf.framework.cache;

public interface ClearableCache {

    /**
     * @return 缓存名称
     */
    String getCacheName();

    /**
     * 根据缓存标识，清除指定缓存
     *
     * @param cacheId 缓存标识
     */
    void clear(String cacheId);

}
