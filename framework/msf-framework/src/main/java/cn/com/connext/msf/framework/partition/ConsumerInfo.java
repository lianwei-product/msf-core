package cn.com.connext.msf.framework.partition;

import java.util.Date;

public class ConsumerInfo {

    private int instanceIndex;
    private String serviceName;
    private String host;
    private Date timestamp;

    public int getInstanceIndex() {
        return instanceIndex;
    }

    public void setInstanceIndex(int instanceIndex) {
        this.instanceIndex = instanceIndex;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
