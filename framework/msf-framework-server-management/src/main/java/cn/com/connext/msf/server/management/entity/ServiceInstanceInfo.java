package cn.com.connext.msf.server.management.entity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public class ServiceInstanceInfo {

    private ServiceInstance serviceInstance;

    private CpuUsageInfo cpuUsage;

    private MemoryUsedInfo memoryUsed;

    private GcPauseInfo gcPause;

    private HealthInfo healthInfo;

    public static ServiceInstanceInfo from(ServiceInstance serviceInstance, CpuUsageInfo cpuUsage, MemoryUsedInfo memoryUsed, GcPauseInfo gcPause,HealthInfo healthInfo) {
        ServiceInstanceInfo serviceInstanceInfo = new ServiceInstanceInfo();
        serviceInstanceInfo.setServiceInstance(serviceInstance);
        serviceInstanceInfo.setCpuUsage(cpuUsage);
        serviceInstanceInfo.setGcPause(gcPause);
        serviceInstanceInfo.setMemoryUsed(memoryUsed);
        serviceInstanceInfo.setHealthInfo(healthInfo);
        return serviceInstanceInfo;
    }

    public HealthInfo getHealthInfo() {
        return healthInfo;
    }

    public void setHealthInfo(HealthInfo healthInfo) {
        this.healthInfo = healthInfo;
    }

    public ServiceInstance getServiceInstance() {
        return serviceInstance;
    }

    public void setServiceInstance(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public CpuUsageInfo getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(CpuUsageInfo cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public MemoryUsedInfo getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(MemoryUsedInfo memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public GcPauseInfo getGcPause() {
        return gcPause;
    }

    public void setGcPause(GcPauseInfo gcPause) {
        this.gcPause = gcPause;
    }

    public static class HealthInfo{
        private String status;
        private ObjectNode details;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public ObjectNode getDetails() {
            return details;
        }

        public void setDetails(ObjectNode details) {
            this.details = details;
        }
    }

    public static class CpuUsageInfo {
        private List<Measurement> measurements;

        public CpuUsageInfo() {
        }

        public List<Measurement> getMeasurements() {
            return measurements;
        }

        public void setMeasurements(List<Measurement> measurements) {
            this.measurements = measurements;
        }

    }

    public static class MemoryUsedInfo {
        private List<Measurement> measurements;

        public MemoryUsedInfo() {
        }

        public List<Measurement> getMeasurements() {
            return measurements;
        }

        public void setMeasurements(List<Measurement> measurements) {
            this.measurements = measurements;
        }
    }

    public static class GcPauseInfo {
        private List<Measurement> measurements;

        public GcPauseInfo() {
        }

        public List<Measurement> getMeasurements() {
            return measurements;
        }

        public void setMeasurements(List<Measurement> measurements) {
            this.measurements = measurements;
        }
    }


    public static class Measurement {
        private String statistic;
        private String value;

        public Measurement() {
        }

        public String getStatistic() {
            return statistic;
        }

        public void setStatistic(String statistic) {
            this.statistic = statistic;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
