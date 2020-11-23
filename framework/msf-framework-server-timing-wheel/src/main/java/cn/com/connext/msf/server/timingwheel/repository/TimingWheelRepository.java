package cn.com.connext.msf.server.timingwheel.repository;

import cn.com.connext.msf.server.timingwheel.entity.TimingWheelEvent;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelPeriod;

import java.util.Date;
import java.util.List;

public interface TimingWheelRepository {

    /**
     * Initial timing wheel cluster global lock
     */
    void initialLock(String owner);

    /**
     * Acquire global lock and renew expires time
     */
    boolean acquireLock(String owner, Date expiresTime);


    /**
     * Release global lock
     */
    void releaseLock(String owner);


    /**
     * Append new timingWheelEventInfo to database
     */
    void appendEvent(TimingWheelEvent timingWheelEvent);

    /**
     * Append new timingWheelPeriodInfo to database
     */
    void appendPeriod(TimingWheelPeriod timingWheelPeriod);

    /**
     * Remove timingWheelPeriodInfo form database
     */
    void removePeriodInfo(String id);

    List<TimingWheelEvent> takeEventList(int size);

    /**
     * Get all timingWheelPeriodInfo list from database
     */
    List<TimingWheelPeriod> takePeriodList();


    void initLatestPeriodUpdateTag();

    /**
     * long eventTime
     * Find timingWheelEventInfo last update tag from database
     */
    long findLatestPeriodUpdateTag();

    void updateLatestPeriodUpdateTag();

    TimingWheelPeriod findPeriodInfo(String id);
}
