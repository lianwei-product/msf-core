package cn.com.connext.msf.server.timingwheel.service;

import cn.com.connext.msf.framework.exception.BusinessException;
import cn.com.connext.msf.framework.utils.CronExpression;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelEvent;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelPeriod;
import cn.com.connext.msf.server.timingwheel.processor.TimingWheelEventProcessor;
import cn.com.connext.msf.server.timingwheel.repository.TimingWheelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class TimingWheelService {

    private final Logger logger = LoggerFactory.getLogger(TimingWheelService.class);

    private final TimingWheelRepository timingWheelRepository;
    private final ClusterLock clusterLock;
    private final EventTrigger eventTrigger;
    private final PeriodTrigger periodTrigger;
    private final PeriodMonitor periodMonitor;
    private final boolean defaultPause;

    public TimingWheelService(
            @Value("${msf.timing-wheel.renewal-time-span:10000}") int renewalTimeSpan,
            @Value("${msf.timing-wheel.expires-time-span:30000}") int expiresTimeSpan,
            @Value("${msf.timing-wheel.batch-event-size:2000}") int batchEventSize,
            @Value("${msf.timing-wheel.event-time-span:1000}") int triggerEventTimeSpan,
            @Value("${msf.timing-wheel.period-update-Watch-time-span:30000}") int periodUpdateWatchTimeSpan,
            @Value("${msf.timing-wheel.default-pause:false}") boolean defaultPause,
            TimingWheelRepository timingWheelRepository,
            TimingWheelEventProcessor timingWheelEventProcessor) {

        this.timingWheelRepository = timingWheelRepository;
        this.clusterLock = new ClusterLock(renewalTimeSpan, expiresTimeSpan, timingWheelRepository);
        this.eventTrigger = new EventTrigger(batchEventSize, triggerEventTimeSpan, clusterLock, timingWheelRepository, timingWheelEventProcessor);
        this.periodTrigger = new PeriodTrigger(clusterLock, timingWheelRepository);
        this.periodMonitor = new PeriodMonitor(periodUpdateWatchTimeSpan, clusterLock, periodTrigger, timingWheelRepository);
        this.defaultPause = defaultPause;

        logger.info("TimingWheel config params, renewalTimeSpan:" + renewalTimeSpan + ", expiresTimeSpan:" + expiresTimeSpan + ", batchEventSize:" + batchEventSize + ", triggerEventTimeSpan:" + triggerEventTimeSpan + ", periodUpdateWatchTimeSpan:" + periodUpdateWatchTimeSpan);
    }

    public void start() {
        logger.info("Start timing-wheel service");
        if (defaultPause) {
            pause();
        }
        clusterLock.start();
        eventTrigger.start();
        periodMonitor.start();
        periodTrigger.start();
    }

    public void stop() {
        periodMonitor.stop();
        periodTrigger.stop();
        eventTrigger.stop();
        clusterLock.stop();
        logger.info("Stop timing-wheel service");
    }

    public void pause() {
        eventTrigger.pause();
        periodTrigger.pause();
    }

    public void resume() {
        eventTrigger.resume();
        periodTrigger.resume();
    }

    public void config(int batchEventSize, int triggerEventTimeSpan) {
        this.eventTrigger.config(batchEventSize, triggerEventTimeSpan);
        this.periodTrigger.config(triggerEventTimeSpan);
        logger.info("Config timing-wheel service: batchEventSize={}, triggerEventTimeSpan={}",
                batchEventSize, triggerEventTimeSpan);
    }

    public String status() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("TimingWheel Status=====");
        stringBuilder.append("\n\t").append(eventTrigger.status());
        stringBuilder.append("\n\t").append(periodTrigger.status());
        return stringBuilder.toString();
    }

    public void appendEvent(TimingWheelEvent timingWheelEvent) {
        timingWheelRepository.appendEvent(timingWheelEvent);
    }

    public void appendPeriod(TimingWheelPeriod timingWheelPeriod) {
        if (!CronExpression.isValidExpression(timingWheelPeriod.getExpression()))
            throw new BusinessException("Invalid cron expression.");

        timingWheelRepository.appendPeriod(timingWheelPeriod);
        periodTrigger.reloadPeriod();
    }

    public void removePeriod(String id) {
        timingWheelRepository.removePeriodInfo(id);
        periodTrigger.reloadPeriod();
    }

    public TimingWheelPeriod findPeriod(String id) {
        return timingWheelRepository.findPeriodInfo(id);
    }

    public boolean hadClusterLock() {
        return clusterLock.hadClusterLock();
    }

    public int getPeriodCount() {
        return periodTrigger.getPeriodCount();
    }

}
