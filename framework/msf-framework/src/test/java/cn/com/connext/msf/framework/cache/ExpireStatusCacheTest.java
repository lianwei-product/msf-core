package cn.com.connext.msf.framework.cache;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ExpireStatusCacheTest {

    private final ExpireStatusCache<Date> expireStatusCache;

    public ExpireStatusCacheTest() {
        expireStatusCache = new ExpireStatusCache<>(3, TimeUnit.SECONDS, 5);
    }

    @Test
    public void expired() {

    }

    @Test
    public void append() throws InterruptedException {
        int count = 10;
        for (int i = 0; i < count; i++) {
            expireStatusCache.put(Integer.toString(i), new Date());
        }

        for (int i = 0; i < count; i++) {
            System.out.println(Integer.toString(i) + ": " + expireStatusCache.get(Integer.toString(i)));
        }


        Thread.sleep(5000);

        for (int i = 0; i < count; i++) {
            System.out.println(Integer.toString(i) + ": " +expireStatusCache.get(Integer.toString(i)));
        }
    }
}