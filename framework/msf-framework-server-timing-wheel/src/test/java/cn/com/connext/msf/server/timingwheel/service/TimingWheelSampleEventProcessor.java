package cn.com.connext.msf.server.timingwheel.service;

import cn.com.connext.msf.framework.utils.JSON;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelEvent;
import cn.com.connext.msf.server.timingwheel.processor.TimingWheelEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimingWheelSampleEventProcessor implements TimingWheelEventProcessor {

    private Logger logger = LoggerFactory.getLogger(TimingWheelSampleEventProcessor.class);
    private int count = 0;

    @Override
    public void process(TimingWheelEvent timingWheelEvent) {
        count++;
        logger.debug(JSON.toJsonString(timingWheelEvent));
    }

    public int getCount() {
        return count;
    }
}
