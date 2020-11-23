package cn.com.connext.msf.server.timingwheel.config;

import cn.com.connext.msf.server.timingwheel.service.TimingWheelService;
import org.springframework.context.SmartLifecycle;

import java.util.concurrent.atomic.AtomicBoolean;

public class TimingWheelBootstrapper implements SmartLifecycle {

    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final TimingWheelService timingWheelService;


    public TimingWheelBootstrapper(TimingWheelService timingWheelService) {
        this.timingWheelService = timingWheelService;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable runnable) {
        timingWheelService.stop();
        isRunning.compareAndSet(true, false);
        runnable.run();
    }

    @Override
    public void start() {
        if (isRunning.compareAndSet(false, true)) {
            timingWheelService.start();
        }
    }

    @Override
    public void stop() {
        // only Lifecycle child will execute this method.
    }

    @Override
    public boolean isRunning() {
        return isRunning.get();
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

}
