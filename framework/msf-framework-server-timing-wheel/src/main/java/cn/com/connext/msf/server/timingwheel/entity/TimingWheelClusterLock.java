package cn.com.connext.msf.server.timingwheel.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 分布式全局锁，多实例部署情况下，保证只有一个实例可以触发定时事件。
 */
@Document(collection = "timing_wheel_cluster_lock")
public class TimingWheelClusterLock {

    /**
     * 锁的编号，默认为TimingWheelService
     */
    @Id
    private String id;

    /**
     * 当前锁的持有者
     */
    private String owner;

    /**
     * 续约时间，在指定时间仍未续约后，将会失去对锁的持有权。
     */
    private Date expires;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
