package cn.com.connext.msf.framework.partition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartitionWatchProcess implements KeepAliveProcess {

    @Autowired
    private DynamicPartitionService dynamicPartitionService;

    @Override
    public void keepAlive() {
        dynamicPartitionService.keepAlive();
    }

}
