package cn.com.connext.msf.server.management.domain;

import cn.com.connext.msf.server.management.constant.TaskTypes;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelPeriod;
import cn.com.connext.msf.server.timingwheel.service.TimingWheelService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ManagedServiceTaskManager {

    private final TimingWheelService timingWheelService;
    private final String timingWheelIdentify;
    private final String timeInterval;

    public ManagedServiceTaskManager(TimingWheelService timingWheelService,
                                     @Value("${managed.timeInterval:10}") String timeInterval) {
        this.timingWheelService = timingWheelService;
        this.timingWheelIdentify = TaskTypes.CHECK_SERVICE_HEALTH;
        this.timeInterval = timeInterval;
    }

    public void create() {
        TimingWheelPeriod oldTimingWheelPeriod = timingWheelService.findPeriod(timingWheelIdentify);
        if (oldTimingWheelPeriod == null) {
            String expression = "0 0/" + timeInterval + " * * * ? ";
            TimingWheelPeriod timingWheelPeriod = TimingWheelPeriod.from(timingWheelIdentify, TaskTypes.CHECK_SERVICE_HEALTH, expression, "");
            timingWheelService.appendPeriod(timingWheelPeriod);
        }
    }
}
