package cn.com.connext.msf.framework.partition;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeepAliveService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(KeepAliveService.class);
    @Autowired
    private List<KeepAliveProcess> processList;
    private long interval = 30000;
    private boolean isRunning;

    public void start() {
        this.isRunning = true;
        Thread thread = new Thread(() -> {
            while(this.isRunning) {
                try {
                    Thread.sleep(interval);
                    keep();
                } catch (Exception var2) {
                    log.error("error", var2);
                }
            }

        });
        thread.setName("keep_alive");
        thread.start();
    }

    private void keep(){
        for (KeepAliveProcess process : processList) {
            process.keepAlive();
        }
    }
}
