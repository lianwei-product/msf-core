package cn.com.connext.msf.framework.concurrent;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TimeWaiterTest {

    private static final Logger logger = LoggerFactory.getLogger(TimeWaiterTest.class);

    @Test
    public void test() {
        TimeWaiter timeWaiter = new TimeWaiter();

        long begin = System.currentTimeMillis();
        timeWaiter.wait(1, TimeUnit.SECONDS);
        logger.info("finish,use time: " + (System.currentTimeMillis() - begin));
    }
}