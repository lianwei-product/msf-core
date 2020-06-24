package cn.com.connext.msf.framework.entity;

import java.time.LocalDateTime;

/**
 * 生日范围模型
 */
public class DataRangeModel {

    private String startExp;
    private String endExp;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public DataRangeModel() {
        startExp = null;
        endExp = null;
        startTime = null;
        endTime = null;
    }

    public String getStartExp() {
        return startExp;
    }

    public void setStartExp(String startExp) {
        this.startExp = startExp;
    }

    public String getEndExp() {
        return endExp;
    }

    public void setEndExp(String endExp) {
        this.endExp = endExp;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
