package cn.com.connext.msf.server.timingwheel.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 周期时间更新标识
 */
@Document(collection = "timing_wheel_period_update_tag")
public class TimingWheelPeriodUpdateTag {

    @Id
    private String id;

    private long updateTag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getUpdateTag() {
        return updateTag;
    }

    public void setUpdateTag(long updateTag) {
        this.updateTag = updateTag;
    }
}
