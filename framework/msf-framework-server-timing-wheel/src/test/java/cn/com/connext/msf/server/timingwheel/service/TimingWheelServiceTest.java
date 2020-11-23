package cn.com.connext.msf.server.timingwheel.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelEvent;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelPeriod;
import cn.com.connext.msf.server.timingwheel.processor.TimingWheelEventProcessor;
import cn.com.connext.msf.server.timingwheel.repository.TimingWheelMemoryRepository;
import cn.com.connext.msf.server.timingwheel.repository.TimingWheelRepository;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TimingWheelServiceTest {

    @Test
    public void testClusterLock() throws Exception {
        TimingWheelRepository timingWheelRepository = new TimingWheelMemoryRepository();

        TimingWheelService timingWheelService01 = createTimingWheelService(timingWheelRepository, null);
        TimingWheelService timingWheelService02 = createTimingWheelService(timingWheelRepository, null);

        timingWheelService01.start();
        Thread.sleep(1000);
        Assert.assertEquals(true, timingWheelService01.hadClusterLock());

        timingWheelService02.start();
        Thread.sleep(1000);
        Assert.assertEquals(false, timingWheelService02.hadClusterLock());

        timingWheelService01.stop();
        Thread.sleep(2000);
        Assert.assertEquals(false, timingWheelService01.hadClusterLock());
        Assert.assertEquals(true, timingWheelService02.hadClusterLock());

        timingWheelService02.stop();
    }

    @Test
    public void testTriggerEvent() throws Exception {
        TimingWheelRepository timingWheelRepository = new TimingWheelMemoryRepository();
        TimingWheelSampleEventProcessor timingWheelEventProcessor = new TimingWheelSampleEventProcessor();
        TimingWheelService timingWheelService = createTimingWheelService(timingWheelRepository, timingWheelEventProcessor);

        long currentTime = new Date().getTime();
        timingWheelService.appendEvent(TimingWheelEvent.from(new Date(currentTime - 1000), "event01", "must trigger"));
        timingWheelService.appendEvent(TimingWheelEvent.from(new Date(currentTime - 1000), "event02", "must trigger"));
        timingWheelService.appendEvent(TimingWheelEvent.from(new Date(currentTime + 2000), "event03", "must trigger"));
        timingWheelService.appendEvent(TimingWheelEvent.from(new Date(currentTime + 10000), "event04", "must not trigger"));

        timingWheelService.start();
        Thread.sleep(3000);
        timingWheelService.stop();

        Assert.assertEquals(3, timingWheelEventProcessor.getCount());
    }

    @Test
    public void testPeriodMonitor() throws Exception {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("root").setLevel(Level.INFO);

        TimingWheelRepository timingWheelRepository = new TimingWheelMemoryRepository();

        TimingWheelService timingWheelService = createTimingWheelService(timingWheelRepository, null);
        timingWheelService.start();
        Assert.assertEquals(0, timingWheelService.getPeriodCount());

        timingWheelService.appendPeriod(TimingWheelPeriod.from("period-event1", null, "0 0 * * * ?"));
        Thread.sleep(2000);
        Assert.assertEquals(1, timingWheelService.getPeriodCount());

        timingWheelService.appendPeriod(TimingWheelPeriod.from("period-event2", null, "0 0 * * * ?"));
        Thread.sleep(2000);
        Assert.assertEquals(2, timingWheelService.getPeriodCount());

        timingWheelService.stop();
    }

    @Test
    public void testPeriodTrigger() throws Exception {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("cn.com.connext.msf.server.timingwheel.service.EventTrigger").setLevel(Level.INFO);

        TimingWheelRepository timingWheelRepository = new TimingWheelMemoryRepository();
        TimingWheelSampleEventProcessor timingWheelEventProcessor = new TimingWheelSampleEventProcessor();

        TimingWheelService timingWheelService = createTimingWheelService(timingWheelRepository, timingWheelEventProcessor);

        long currentTime = new Date().getTime();
        timingWheelService.appendPeriod(TimingWheelPeriod.from(new Date(currentTime + 3000), new Date(currentTime + 8000), "event1", "will trigger 5 times", "* * * * * ?"));
        timingWheelService.start();

        Thread.sleep(2 * 1000);
        Assert.assertEquals(1, timingWheelService.getPeriodCount());

        currentTime = new Date().getTime();
        timingWheelService.appendPeriod(TimingWheelPeriod.from(new Date(currentTime + 3000), new Date(currentTime + 8000), "event2", "will trigger 5 times", "* * * * * ?"));
        Thread.sleep(10 * 1000);

        Assert.assertEquals(2, timingWheelService.getPeriodCount());
        Assert.assertTrue(10 <= timingWheelEventProcessor.getCount());

        timingWheelService.stop();
    }


    @Test
    public void testLoopListOnMultiThread() throws Exception {

        class Test {
            private List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));

            private void start() {
                new Thread(() -> {
                    for (; ; ) {
                        List<String> tmpList = list;
                        for (String item : tmpList) {
                            System.out.println("list size: " + tmpList.size() + ", item: " + item);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }

            private void changeList() {
                System.out.println("change list.");
                list = new ArrayList<>(Arrays.asList("1", "2", "3", "4"));
            }
        }

        Test test = new Test();
        test.start();
        Thread.sleep(1000);
        test.changeList();
        Thread.sleep(5000);
    }

    private TimingWheelService createTimingWheelService(
            TimingWheelRepository timingWheelRepository,
            TimingWheelEventProcessor timingWheelEventProcessor) {

        int renewalTimeSpan = 1000;
        int expiresTimeSpan = 3000;
        int batchEventSize = 1000;
        int triggerEventTimeSpan = 1000;
        int periodUpdateWatchTimeSpan = 1000;
        boolean defaultPause = false;

        return new TimingWheelService(
                renewalTimeSpan,
                expiresTimeSpan,
                batchEventSize,
                triggerEventTimeSpan,
                periodUpdateWatchTimeSpan,
                defaultPause,
                timingWheelRepository,
                timingWheelEventProcessor);
    }
}