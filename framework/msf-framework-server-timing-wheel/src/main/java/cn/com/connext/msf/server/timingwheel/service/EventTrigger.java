package cn.com.connext.msf.server.timingwheel.service;

import cn.com.connext.msf.framework.utils.JSON;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelEvent;
import cn.com.connext.msf.server.timingwheel.processor.TimingWheelEventProcessor;
import cn.com.connext.msf.server.timingwheel.repository.TimingWheelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.Semaphore;

class EventTrigger {
    private final Logger logger = LoggerFactory.getLogger(EventTrigger.class);
    private final Semaphore semaphore = new Semaphore(1, true);
    private final ClusterLock clusterLock;
    private final TimingWheelRepository timingWheelRepository;
    private final TimingWheelEventProcessor timingWheelEventProcessor;

    private boolean isRunning = false;
    private boolean isPause = false;
    private int batchEventSize;
    private int triggerEventTimeSpan;


    EventTrigger(int batchEventSize,
                 int triggerEventTimeSpan,
                 ClusterLock clusterLock,
                 TimingWheelRepository timingWheelRepository,
                 TimingWheelEventProcessor timingWheelEventProcessor) {
        this.batchEventSize = batchEventSize;
        this.triggerEventTimeSpan = triggerEventTimeSpan;
        this.clusterLock = clusterLock;
        this.timingWheelRepository = timingWheelRepository;
        this.timingWheelEventProcessor = timingWheelEventProcessor;
    }

    void start() {
        isRunning = true;
        Thread thread = new Thread(() -> {
            while (isRunning) {
                try {
                    Thread.sleep(triggerEventTimeSpan);
                    if (isPause) {
                        continue;
                    }
                    if (clusterLock.hadClusterLock()) {
                        semaphore.acquireUninterruptibly();
                        triggerEvent();
                    }
                } catch (Exception ex) {
                    logger.error("Trigger timing-wheel event error.", ex);
                } finally {
                    if (clusterLock.hadClusterLock()) {
                        semaphore.release();
                    }
                }
            }
        });
        thread.setName("event-trigger");
        thread.start();
    }

    void pause() {
        isPause = true;
    }

    void resume() {
        isPause = false;
    }

    void stop() {
        isRunning = false;
        semaphore.acquireUninterruptibly();
    }

    void config(int batchEventSize, int triggerEventTimeSpan) {
        this.batchEventSize = batchEventSize;
        this.triggerEventTimeSpan = triggerEventTimeSpan;
    }

    String status() {
        StringBuilder result = new StringBuilder();
        result.append("EventTrigger Status:");
        result.append("\n\tisRunning:").append(isRunning);
        result.append("\n\tisPause:").append(isPause);
        result.append("\n\tbatchEventSize:").append(batchEventSize);
        result.append("\n\ttriggerEventTimeSpan:").append(triggerEventTimeSpan);
        return result.toString();
    }

    private void triggerEvent() {
        long eventCount = 0;
        long beginTime = System.currentTimeMillis();
        List<TimingWheelEvent> eventList = timingWheelRepository.takeEventList(batchEventSize);
        while (eventList.size() > 0) {
            eventList.forEach(eventInfo -> {
                try {
                    timingWheelEventProcessor.process(eventInfo);
                } catch (Exception ex) {
                    logger.error("Process error, event:" + JSON.toIndentJsonString(eventInfo), ex);
                }
            });
            eventCount += eventList.size();
            eventList = timingWheelRepository.takeEventList(batchEventSize);
        }

        if (logger.isDebugEnabled() && eventCount > 0) {
            logger.debug(MessageFormat.format("Trigger timing event Count:{0}, time taken: {1}ms", eventCount, (System.currentTimeMillis() - beginTime)));
        }
    }

}
