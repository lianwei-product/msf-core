package cn.com.connext.msf.server.timingwheel.repository;

import cn.com.connext.msf.framework.utils.JSON;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelClusterLock;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelEvent;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelPeriod;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelPeriodUpdateTag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimingWheelMemoryRepository implements TimingWheelRepository {

    private final String globalLockId;
    private TimingWheelClusterLock globalLock = null;
    private TimingWheelPeriodUpdateTag updateTag = null;
    private List<TimingWheelEvent> eventList = new ArrayList<>();
    private List<TimingWheelPeriod> periodList = new ArrayList<>();

    public TimingWheelMemoryRepository() {
        this.globalLockId = "timing-wheel";
    }

    @Override
    public synchronized void initialLock(String owner) {
        if (globalLock == null) {
            globalLock = new TimingWheelClusterLock();
            globalLock.setId(globalLockId);
            globalLock.setOwner(owner);
            globalLock.setExpires(new Date(0));
        }
    }

    @Override
    public synchronized boolean acquireLock(String owner, Date expiresTime) {
        if (globalLock.getOwner().equals(owner) || globalLock.getExpires().before(new Date())) {
            globalLock.setOwner(owner);
            globalLock.setExpires(expiresTime);
            return true;
        }

        return false;
    }

    @Override
    public synchronized void releaseLock(String owner) {
        if (globalLock.getOwner().equals(owner)) {
            globalLock.setExpires(new Date(0));
        }
    }

    @Override
    public synchronized void appendEvent(TimingWheelEvent timingWheelEvent) {
        eventList.add(timingWheelEvent);
    }

    @Override
    public synchronized void appendPeriod(TimingWheelPeriod timingWheelPeriod) {
        periodList.add(timingWheelPeriod);
        updateLatestPeriodUpdateTag();
    }

    @Override
    public synchronized void removePeriodInfo(String id) {
        int pos = -1;
        for (int i = 0; i < periodList.size(); i++) {
            TimingWheelPeriod timingWheelPeriod = periodList.get(i);
            if (timingWheelPeriod.getId().equals(id)) {
                pos = i;
            }
        }

        if (pos != -1) {
            periodList.remove(pos);
            updateLatestPeriodUpdateTag();
        }
    }

    @Override
    public synchronized List<TimingWheelEvent> takeEventList(int size) {
        List<TimingWheelEvent> targetList = new ArrayList<>();
        int count = 0;
        long currentTime = new Date().getTime();
        for (TimingWheelEvent timingWheelEvent : eventList) {
            if (timingWheelEvent.getEventTime().getTime() <= currentTime) {
                targetList.add(timingWheelEvent);

                if (count < size) {
                    count++;
                } else {
                    break;
                }
            }
        }

        eventList.removeAll(targetList);
        return targetList;
    }

    @Override
    public synchronized List<TimingWheelPeriod> takePeriodList() {
        return JSON.parseList(JSON.toJsonString(periodList), TimingWheelPeriod.class);
    }


    @Override
    public TimingWheelPeriod findPeriodInfo(String id) {
        for (TimingWheelPeriod period : periodList) {
            if (period.getId().equals(id)) return period;
        }
        return null;
    }

    @Override
    public synchronized void initLatestPeriodUpdateTag() {
        if (updateTag == null) {
            updateTag = new TimingWheelPeriodUpdateTag();
            updateTag.setId(globalLockId);
            updateTag.setUpdateTag(0);
        }
    }

    @Override
    public synchronized long findLatestPeriodUpdateTag() {
        return updateTag.getUpdateTag();
    }

    @Override
    public synchronized void updateLatestPeriodUpdateTag() {
        if (updateTag == null) {
            updateTag = new TimingWheelPeriodUpdateTag();
            updateTag.setId(globalLockId);
        }
        updateTag.setUpdateTag(new Date().getTime());
    }


}
