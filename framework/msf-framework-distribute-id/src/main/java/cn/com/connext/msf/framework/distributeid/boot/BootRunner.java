package cn.com.connext.msf.framework.distributeid.boot;

import cn.com.connext.msf.framework.distributeid.provider.ClusterNodeNameProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;

import java.util.concurrent.atomic.AtomicBoolean;

public class BootRunner implements SmartLifecycle {

    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final ClusterNodeNameProvider clusterNodeNameProvider;

    /**
     * 节点名称续约间隔时间（单位：秒）
     */
    private final int renewalTimeSpan;

    public BootRunner(ClusterNodeNameProvider clusterNodeNameService,
                      @Value("${msf.distribute-id.renewal-time-span:300}") int renewalTimeSpan) {
        this.clusterNodeNameProvider = clusterNodeNameService;
        this.renewalTimeSpan = renewalTimeSpan * 1000;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void start() {
        if (isRunning.compareAndSet(false, true)) {
            clusterNodeNameProvider.init();
            clusterNodeNameProvider.hold();

            Thread thread = new Thread(() -> {
                while (isRunning.get()) {
                    try {
                        Thread.sleep(renewalTimeSpan);
                        clusterNodeNameProvider.renew();
                    } catch (Exception e) {
                        // do noting
                    }
                }
            });
            thread.setName("distribute-id-renewal");
            thread.start();
        }
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void stop(Runnable callback) {
        isRunning.compareAndSet(true, false);
        callback.run();
    }

    @Override
    public void stop() {
        // do nothing.
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
