package cn.com.connext.msf.framework.cache;

public interface AutoExpireCache extends ClearableCache {

    /**
     * @return 缓存最大容量（缓存对象个数）
     */
    int getMaximumSize();


    /**
     * @return 缓存过期时间（指定秒数）
     */
    int getExpireAfterWrite();

}
