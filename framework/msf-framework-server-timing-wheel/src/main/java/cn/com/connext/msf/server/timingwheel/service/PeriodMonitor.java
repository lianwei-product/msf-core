package cn.com.connext.msf.server.timingwheel.service;

import cn.com.connext.msf.server.timingwheel.repository.TimingWheelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;

class PeriodMonitor {
    private final Logger logger = LoggerFactory.getLogger(PeriodMonitor.class);
    private final Semaphore semaphore = new Semaphore(1, true);
    private final ClusterLock clusterLock;
    private final PeriodTrigger periodTrigger;
    private final TimingWheelRepository timingWheelRepository;

    private boolean isRunning = false;
    private long periodUpdateTag = 0;
    private int periodUpdateWatchTimeSpan;


    PeriodMonitor(
            int periodUpdateWatchTimeSpan,
            ClusterLock clusterLock,
            PeriodTrigger periodTrigger,
            TimingWheelRepository timingWheelRepository) {
        this.periodUpdateWatchTimeSpan = periodUpdateWatchTimeSpan;
        this.clusterLock = clusterLock;
        this.periodTrigger = periodTrigger;
        this.timingWheelRepository = timingWheelRepository;
    }

    void start() {
        isRunning = true;
        timingWheelRepository.initLatestPeriodUpdateTag();
        Thread thread = new Thread(() -> {
            while (isRunning) {
                try {
                    if (clusterLock.hadClusterLock()) {
                        semaphore.acquireUninterruptibly();
                        watchPeriodUpdate();
                    }
                    Thread.sleep(periodUpdateWatchTimeSpan);
                } catch (Exception ex) {
                    logger.error("Watch timing-wheel period update error.", ex);
                } finally {
                    if (clusterLock.hadClusterLock()) {
                        semaphore.release();
                    }
                }
            }
        });
        thread.setName("period-monitor");
        thread.start();
    }


    void stop() {
        isRunning = false;
        semaphore.acquireUninterruptibly();
    }

    private void watchPeriodUpdate() {
        long latestPeriodUpdateTag = timingWheelRepository.findLatestPeriodUpdateTag();
        if (periodUpdateTag != latestPeriodUpdateTag) {
            logger.info("Detect timing-wheel period update, latestPeriodUpdateTag: " + Long.toString(latestPeriodUpdateTag));
            periodUpdateTag = latestPeriodUpdateTag;
            periodTrigger.reloadPeriod();
        }
    }
}
