package cn.com.connext.msf.framework.partition;

import org.slf4j.Logger;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;

public class DynamicPartitionInfoRepository {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DynamicPartitionInfoRepository.class);

    private final MongoOperations mongoOperations;

    public DynamicPartitionInfoRepository(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public void save(DynamicPartitionInfo info) {
        this.mongoOperations.save(info);
    }

    public DynamicPartitionInfo findItem(String destination) {
        Query query = Query.query(Criteria.where("destination").is(destination));
        return this.mongoOperations.findOne(query, DynamicPartitionInfo.class);
    }

    public boolean updateTimestamp(String destination, String host, Date date) {
        try {
            Query query = Query.query(Criteria.where("destination").is(destination));
            DynamicPartitionInfo info = this.mongoOperations.findOne(query, DynamicPartitionInfo.class);

            info.getConsumers().stream().filter(it -> host.equals(it.getHost())).forEach(it -> it.setTimestamp(date));
            mongoOperations.save(info);
            return true;
        } catch (OptimisticLockingFailureException e) {
            log.warn("初始化队列分区[{}]乐观锁失败，重试初始化队列", destination);
            return false;
        }
    }
}
