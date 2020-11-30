package cn.com.connext.msf.framework.cache;

public interface AutoExpireCache extends ClearableCache {

    /**
     * @return 缓存最大容量（缓存对象个数）
     */
    int getMaximumSize();

    /**
     * @return 缓存写入后过期时间（指定秒数）
     */
    default int getExpireAfterWrite() {
        return 0;
    }

    /**
     * @return 缓存读取后过期时间（指定秒数）
     */
    default int getExpireAfterAccess() {
        return 0;
    }

    /**
     * @return 是否使用expireAfterAccess过期方式
     * False: expireAfterAccess(访问后隔断时间过期)
     * True: expireAfterWrite(写入后隔段时间过期）
     */
    default boolean isExpireAfterAccessMode() {
        return false;
    }
}
