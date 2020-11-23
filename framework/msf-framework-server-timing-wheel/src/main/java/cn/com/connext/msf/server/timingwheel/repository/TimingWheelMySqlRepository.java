package cn.com.connext.msf.server.timingwheel.repository;

import cn.com.connext.msf.server.timingwheel.entity.TimingWheelEvent;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelPeriod;

import java.util.Date;
import java.util.List;

public class TimingWheelMySqlRepository implements TimingWheelRepository {

    public TimingWheelMySqlRepository() {

    }

    @Override
    public void initialLock(String owner) {

    }

    @Override
    public boolean acquireLock(String owner, Date expiresTime) {
        return false;
    }

    @Override
    public void releaseLock(String owner) {

    }

    @Override
    public void appendEvent(TimingWheelEvent timingWheelEvent) {

    }

    @Override
    public void appendPeriod(TimingWheelPeriod timingWheelPeriod) {

    }

    @Override
    public void removePeriodInfo(String id) {

    }

    @Override
    public List<TimingWheelEvent> takeEventList(int size) {
        return null;
    }

    @Override
    public List<TimingWheelPeriod> takePeriodList() {
        return null;
    }

    @Override
    public void initLatestPeriodUpdateTag() {

    }

    @Override
    public long findLatestPeriodUpdateTag() {
        return 0;
    }

    @Override
    public void updateLatestPeriodUpdateTag() {

    }

    @Override
    public TimingWheelPeriod findPeriodInfo(String id) {
        return null;
    }
}
