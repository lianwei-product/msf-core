package cn.com.connext.msf.server.timingwheel.entity;


import cn.com.connext.msf.framework.utils.Base58UUID;
import cn.com.connext.msf.framework.utils.JSON;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * 定时事件实体
 */
@Document(collection = "timing_wheel_event")
public class TimingWheelEvent {


    @Id
    private String id;

    /**
     * 事件触发时间，如：20181001090000 代表在 2018-10-01 09:00:00 时触发该事件
     */
    @Indexed
    private Date eventTime;

    /**
     * 事件类型，如：订单状态检查、会员到期提醒等
     */
    private String eventType;

    /**
     * 事件参数信息，可以为任意对象序列化后的JSON
     */
    private String eventParm;


    public TimingWheelEvent() {
        id = Base58UUID.newBase58UUID();
    }

    public static TimingWheelEvent from(LocalDateTime eventTime, String eventType, String eventParm) {
        return from(Date.from(eventTime.atZone(ZoneId.systemDefault()).toInstant()), eventType, eventParm);
    }

    public static TimingWheelEvent from(Date eventTime, String eventType, String eventParm) {
        TimingWheelEvent timingWheelEvent = new TimingWheelEvent();
        timingWheelEvent.setEventTime(eventTime);
        timingWheelEvent.setEventType(eventType);
        timingWheelEvent.setEventParm(eventParm);
        return timingWheelEvent;
    }

    public <T> T parseObject(Class<T> valueType) {
        return JSON.parseObject(eventParm, valueType);
    }

    public <T> List<T> parseList(Class<T> valueType) {
        return JSON.parseList(eventParm, valueType);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventParm() {
        return eventParm;
    }

    public void setEventParm(String eventParm) {
        this.eventParm = eventParm;
    }
}
