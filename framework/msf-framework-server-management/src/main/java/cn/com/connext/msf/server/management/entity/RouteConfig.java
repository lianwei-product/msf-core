package cn.com.connext.msf.server.management.entity;

import io.swagger.annotations.ApiModelProperty;

public class RouteConfig {

    @ApiModelProperty(value = "日志级别", required = true, allowableValues = "trace, debug, info, none")
    private String logLevel;

    public RouteConfig() {
        this.logLevel = "none";
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
}
