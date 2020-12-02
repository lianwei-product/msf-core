package cn.com.connext.msf.framework.dynamic.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class ApplicationLog {

    @ApiModelProperty(value = "应用")
    @JsonProperty("app_name")
    private String appName;

    @ApiModelProperty(value = "日志")
    @JsonProperty("logger_name")
    private String loggerName;

    @ApiModelProperty(value = "类型")
    private String level;

    @ApiModelProperty(value = "时间")
    @JsonProperty("@timestamp")
    private Date time;

    public String getAppName() {
        return appName;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public String getLevel() {
        return level;
    }

    public Date getTime() {
        return time;
    }
}
