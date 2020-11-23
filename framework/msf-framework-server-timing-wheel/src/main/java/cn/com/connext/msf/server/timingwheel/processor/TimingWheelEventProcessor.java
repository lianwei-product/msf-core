package cn.com.connext.msf.server.timingwheel.processor;

import cn.com.connext.msf.server.timingwheel.entity.TimingWheelEvent;

public interface TimingWheelEventProcessor {
    void process(TimingWheelEvent timingWheelEvent);
}
