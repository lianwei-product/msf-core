package cn.com.connext.msf.framework.partition;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "partition_info")
public class DynamicPartitionInfo {

    @Id
    private String id;
    @Indexed
    private String destination;

    private int partitionCount;

    private List<ConsumerInfo> consumers = new ArrayList<>();

    @Version
    private Long version;

    public void addConsumerInfo(ConsumerInfo consumerInfo){
        consumers.add(consumerInfo);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getPartitionCount() {
        return partitionCount;
    }

    public void setPartitionCount(int partitionCount) {
        this.partitionCount = partitionCount;
    }

    public List<ConsumerInfo> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<ConsumerInfo> consumers) {
        this.consumers = consumers;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
