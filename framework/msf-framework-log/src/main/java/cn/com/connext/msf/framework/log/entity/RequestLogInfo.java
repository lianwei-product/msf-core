package cn.com.connext.msf.framework.log.entity;

import java.util.List;

/**
 * 请求日志实体
 */
public class RequestLogInfo {

    private List<String> axisNameByDayList;

    private List<Integer> resultCountByDayList;

    private List<String> axisNameBySumList;

    private List<Integer> resultCountBySumList;

    public List<String> getAxisNameByDayList() {
        return axisNameByDayList;
    }

    public static RequestLogInfo from(List<String> axisNameByDayList, List<Integer> resultCountByDayList, List<String> axisNameBySumList, List<Integer> resultCountBySumList) {
        RequestLogInfo requestLogInfo = new RequestLogInfo();
        requestLogInfo.setAxisNameByDayList(axisNameByDayList);
        requestLogInfo.setAxisNameBySumList(axisNameBySumList);
        requestLogInfo.setResultCountByDayList(resultCountByDayList);
        requestLogInfo.setResultCountBySumList(resultCountBySumList);
        return requestLogInfo;
    }

    public void setAxisNameByDayList(List<String> axisNameByDayList) {
        this.axisNameByDayList = axisNameByDayList;
    }

    public List<Integer> getResultCountByDayList() {
        return resultCountByDayList;
    }

    public void setResultCountByDayList(List<Integer> resultCountByDayList) {
        this.resultCountByDayList = resultCountByDayList;
    }

    public List<String> getAxisNameBySumList() {
        return axisNameBySumList;
    }

    public void setAxisNameBySumList(List<String> axisNameBySumList) {
        this.axisNameBySumList = axisNameBySumList;
    }

    public List<Integer> getResultCountBySumList() {
        return resultCountBySumList;
    }

    public void setResultCountBySumList(List<Integer> resultCountBySumList) {
        this.resultCountBySumList = resultCountBySumList;
    }
}
