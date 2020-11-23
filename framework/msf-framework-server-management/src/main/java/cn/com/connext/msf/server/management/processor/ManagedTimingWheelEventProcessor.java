package cn.com.connext.msf.server.management.processor;

import cn.com.connext.msf.framework.utils.JSON;
import cn.com.connext.msf.server.management.constant.TaskTypes;
import cn.com.connext.msf.server.management.service.ManagedServiceService;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelEvent;
import cn.com.connext.msf.server.timingwheel.processor.TimingWheelEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ManagedTimingWheelEventProcessor implements TimingWheelEventProcessor {

    private final Logger logger = LoggerFactory.getLogger(ManagedTimingWheelEventProcessor.class);

    private final ManagedServiceService managedServiceService;

    public ManagedTimingWheelEventProcessor(ManagedServiceService managedServiceService) {
        this.managedServiceService = managedServiceService;
    }

    @Override
    public void process(TimingWheelEvent timingWheelEvent) {
        if (logger.isDebugEnabled()) {
            logger.info("Process timingWheelEvent triggered, timingWheelEvent:" + JSON.toIndentJsonString(timingWheelEvent));
        }
        try {
            String eventType = timingWheelEvent.getEventType();
            switch (eventType) {
                case TaskTypes.CHECK_SERVICE_HEALTH:
                    managedServiceService.checkServiceHealth();
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            logger.error("Process timingWheelEvent error, timingWheelEvent:" + JSON.toIndentJsonString(timingWheelEvent), e);
        }
    }
}
