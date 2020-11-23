package cn.com.connext.msf.server.timingwheel.service;

import cn.com.connext.msf.framework.utils.Base58UUID;
import cn.com.connext.msf.server.timingwheel.repository.TimingWheelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.Semaphore;

class ClusterLock {

    private final Logger logger = LoggerFactory.getLogger(ClusterLock.class);
    private final Semaphore semaphore = new Semaphore(1, true);
    private final String owner = Base58UUID.newBase58UUID();
    private final TimingWheelRepository timingWheelRepository;

    private boolean isRunning = false;
    private boolean hadClusterLock = false;

    private int renewalTimeSpan;
    private int expiresTimeSpan;


    ClusterLock(int renewalTimeSpan, int expiresTimeSpan, TimingWheelRepository timingWheelRepository) {
        this.renewalTimeSpan = renewalTimeSpan;
        this.expiresTimeSpan = expiresTimeSpan;
        this.timingWheelRepository = timingWheelRepository;
    }

    void start() {
        isRunning = true;
        Thread thread = new Thread(() -> {
            timingWheelRepository.initialLock(owner);
            while (isRunning) {
                try {
                    semaphore.acquireUninterruptibly();
                    acquire();
                } catch (Exception ex) {
                    logger.error("Renewal timing-wheel lock error.", ex);
                } finally {
                    semaphore.release();
                    try {
                        Thread.sleep(renewalTimeSpan);
                    } catch (Exception e) {
                        // do nothing
                    }
                }
            }
        });
        thread.setName("cluster-lock");
        thread.start();
    }

    void stop() {
        isRunning = false;
        semaphore.acquireUninterruptibly();
        release();
    }

    boolean hadClusterLock() {
        return hadClusterLock;
    }

    private void acquire() {
        Date expiresTime = new Date(System.currentTimeMillis() + expiresTimeSpan);
        boolean result = timingWheelRepository.acquireLock(owner, expiresTime);
        if (hadClusterLock != result) {
            hadClusterLock = result;
            if (hadClusterLock) {
                logger.info("Acquire timing-wheel lock success.");
            } else {
                logger.warn("Acquire timing-wheel lock failed.");
            }
        }
    }

    private void release() {
        timingWheelRepository.releaseLock(owner);
        hadClusterLock = false;
        logger.info("Release timing-wheel lock.");
    }
}
