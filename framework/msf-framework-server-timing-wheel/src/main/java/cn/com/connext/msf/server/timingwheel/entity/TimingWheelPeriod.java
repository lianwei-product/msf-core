package cn.com.connext.msf.server.timingwheel.entity;


import cn.com.connext.msf.framework.utils.Base58UUID;
import cn.com.connext.msf.framework.utils.Time;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 周期事件实体（待实现）
 */
@Document(collection = "timing_wheel_period")
public class TimingWheelPeriod {

    @Id
    private String id;

    @Indexed
    private Date startTime;

    @Indexed
    private Date endTime;


    /**
     * 事件类型，如：订单状态检查、会员到期提醒等
     */
    private String eventType;

    /**
     * 事件参数信息，可以为任意对象序列化后的JSON
     */
    private String eventParm;

    private String expression;

    @Transient
    private Date nextValidTime;

    public TimingWheelPeriod() {
        id = Base58UUID.newBase58UUID();
    }

    public static TimingWheelPeriod from(String eventType, String eventParm, String expression) {
        return from(Time.parseDate("1900-01-01"), Time.parseDate("2099-01-01"), eventType, eventParm, expression);
    }

    public static TimingWheelPeriod from(Date startTime, Date endTime, String eventType, String eventParm, String expression) {
        return from(Base58UUID.newBase58UUID(), startTime, endTime, eventType, eventParm, expression);
    }

    public static TimingWheelPeriod from(String id, String eventType, String expression, String eventParm) {
        return from(id, Time.parseDate("1900-01-01"), Time.parseDate("2099-01-01"), eventType, eventParm, expression);
    }

    public static TimingWheelPeriod from(String id, Date startTime, Date endTime, String eventType, String eventParm, String expression) {
        TimingWheelPeriod timingWheelPeriod = new TimingWheelPeriod();
        timingWheelPeriod.setId(id);
        timingWheelPeriod.setStartTime(startTime);
        timingWheelPeriod.setEndTime(endTime);
        timingWheelPeriod.setEventType(eventType);
        timingWheelPeriod.setEventParm(eventParm);
        timingWheelPeriod.setExpression(expression);
        return timingWheelPeriod;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public Date getNextValidTime() {
        return nextValidTime;
    }

    public void setNextValidTime(Date nextValidTime) {
        this.nextValidTime = nextValidTime;
    }
}
