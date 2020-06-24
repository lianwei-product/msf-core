package cn.com.connext.msf.framework.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TimeWaiter {

    private final static Logger logger = LoggerFactory.getLogger(TimeWaiter.class);
    private final Lock waitLock = new ReentrantLock();
    private final Lock pauseLock = new ReentrantLock();
    private final Condition available = waitLock.newCondition();
    private boolean needResetWaiter = false;

    public void wait(long time, TimeUnit timeUnit) {
        waitLock.lock();
        try {
            available.await(time, timeUnit);
            pauseLock.lock();
            if (needResetWaiter) {
                available.await(time, timeUnit);
                needResetWaiter = false;
            }
        } catch (Exception e) {
            logger.error("Thread interrupted", e);
        } finally {
            waitLock.unlock();
            pauseLock.unlock();
        }
    }

    public void pause() {
        try {
            pauseLock.lock();
            needResetWaiter = true;
        } catch (Exception e) {
            logger.error("Thread interrupted", e);
        }
    }

    public void reset() {
        pauseLock.unlock();
    }
}
