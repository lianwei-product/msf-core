package cn.com.connext.msf.server.timingwheel.service;

import cn.com.connext.msf.framework.utils.CronExpression;
import cn.com.connext.msf.framework.utils.JSON;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelEvent;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelPeriod;
import cn.com.connext.msf.server.timingwheel.repository.TimingWheelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

class PeriodTrigger {

    private final Logger logger = LoggerFactory.getLogger(PeriodTrigger.class);
    private final Semaphore semaphore = new Semaphore(1, true);
    private final ClusterLock clusterLock;
    private final TimingWheelRepository timingWheelRepository;

    private List<TimingWheelPeriod> periodListCache = new ArrayList<>();
    private int triggerPeriodTimeSpan = 1000;
    private boolean isRunning = false;
    private boolean isPause = false;

    PeriodTrigger(ClusterLock clusterLock, TimingWheelRepository timingWheelRepository) {
        this.clusterLock = clusterLock;
        this.timingWheelRepository = timingWheelRepository;
    }

    void start() {
        isRunning = true;
        Thread thread = new Thread(() -> {
            while (isRunning) {
                try {
                    Thread.sleep(triggerPeriodTimeSpan);
                    if (isPause) {
                        continue;
                    }
                    if (clusterLock.hadClusterLock()) {
                        semaphore.acquireUninterruptibly();
                        triggerPeriod();
                    }
                } catch (Exception ex) {
                    logger.error("Trigger timing-wheel period error.", ex);
                } finally {
                    if (clusterLock.hadClusterLock()) {
                        semaphore.release();
                    }
                }
            }
        });
        thread.setName("period-trigger");
        thread.start();
    }


    void stop() {
        isRunning = false;
        semaphore.acquireUninterruptibly();
    }

    void pause() {
        isPause = true;
    }

    void resume() {
        isPause = false;
    }

    void config(int triggerPeriodTimeSpan) {
        this.triggerPeriodTimeSpan = triggerPeriodTimeSpan;
    }

    String status() {
        StringBuilder result = new StringBuilder();
        result.append("PeriodTrigger Status:");
        result.append("\n\tisRunning:").append(isRunning);
        result.append("\n\tisPause:").append(isPause);
        result.append("\n\ttriggerPeriodTimeSpan:").append(triggerPeriodTimeSpan);
        return result.toString();
    }


    void reloadPeriod() {
        List<TimingWheelPeriod> tmpPeriodList = timingWheelRepository.takePeriodList();
        Date currentTime = new Date();
        tmpPeriodList.forEach(period -> {
            resetNextTriggerTime(period, currentTime);
        });
        tmpPeriodList.sort(Comparator.comparing(TimingWheelPeriod::getNextValidTime));
        periodListCache = tmpPeriodList;
        logger.info("Refresh timing wheel period list.");
        if (logger.isDebugEnabled()) {
            logger.debug("timing wheel period:" + JSON.toIndentJsonString(tmpPeriodList));
        }
    }

    int getPeriodCount() {
        return periodListCache.size();
    }

    private void triggerPeriod() {
        List<TimingWheelPeriod> tmpPeriodList = periodListCache;

        long beginTime = System.currentTimeMillis();
        int triggerCount = 0;
        Date currentTime = new Date();

        for (TimingWheelPeriod period : tmpPeriodList) {
            if (period.getNextValidTime().getTime() > currentTime.getTime()) {
                break;
            } else {
                resetNextTriggerTime(period, currentTime);
                if (isTimeValidPeriod(period, currentTime)) {
                    triggerCount++;
                    timingWheelRepository.appendEvent(TimingWheelEvent.from(currentTime, period.getEventType(), period.getEventParm()));
                }
            }
        }

        if (triggerCount > 0) {
            periodListCache.sort(Comparator.comparing(TimingWheelPeriod::getNextValidTime));
        }

//        if (logger.isDebugEnabled() && triggerCount > 0) {
//            logger.debug(MessageFormat.format("Count:{0}, time taken: {1}ms", triggerCount, (System.currentTimeMillis() - beginTime)));
//        }
    }

    private void resetNextTriggerTime(TimingWheelPeriod period, Date currentTime) {
        try {
            CronExpression cronExpression = new CronExpression(period.getExpression());
            period.setNextValidTime(cronExpression.getNextValidTimeAfter(currentTime));
        } catch (Exception e) {
            logger.error("CronExpression error, periodInfo:" + JSON.toIndentJsonString(period), e);
        }
    }

    private boolean isTimeValidPeriod(TimingWheelPeriod period, Date currentTime) {
        long number = currentTime.getTime();
        return period.getStartTime().getTime() <= number && period.getEndTime().getTime() >= number;
    }
}
