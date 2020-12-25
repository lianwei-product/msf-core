package cn.com.connext.msf.framework.log.entity;

import java.util.List;

/**
 * 系统日志实体
 */
public class SystemLogInfo {

    private List<String> axisNameList;

    private List<Integer> infoCountList;

    private List<Integer> debugCountList;

    private List<Integer> warnCountList;

    private List<Integer> errorCountList;

    public List<String> getAxisNameList() {
        return axisNameList;
    }

    public void setAxisNameList(List<String> axisNameList) {
        this.axisNameList = axisNameList;
    }

    public List<Integer> getInfoCountList() {
        return infoCountList;
    }

    public void setInfoCountList(List<Integer> infoCountList) {
        this.infoCountList = infoCountList;
    }

    public List<Integer> getDebugCountList() {
        return debugCountList;
    }

    public void setDebugCountList(List<Integer> debugCountList) {
        this.debugCountList = debugCountList;
    }

    public List<Integer> getWarnCountList() {
        return warnCountList;
    }

    public void setWarnCountList(List<Integer> warnCountList) {
        this.warnCountList = warnCountList;
    }

    public List<Integer> getErrorCountList() {
        return errorCountList;
    }

    public void setErrorCountList(List<Integer> errorCountList) {
        this.errorCountList = errorCountList;
    }
}