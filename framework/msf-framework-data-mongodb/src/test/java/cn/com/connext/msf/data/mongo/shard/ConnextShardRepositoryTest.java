package cn.com.connext.msf.data.mongo.shard;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.com.connext.msf.data.mongo.ConnextMongoOperationManager;
import cn.com.connext.msf.data.mongo.entity.RequestLimiting;
import cn.com.connext.msf.data.mongo.provider.MongoOperationsProvider;
import cn.com.connext.msf.data.mongo.provider.SimpleMongoOperationsProvider;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;

public class ConnextShardRepositoryTest {

    private final RequestLimitingRepository requestLimitingRepository;

    public ConnextShardRepositoryTest() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("root").setLevel(Level.WARN);
        loggerContext.getLogger("cn.com.connext").setLevel(Level.INFO);

        ConnextMongoOperationManager mongoOperationManager = ConnextMongoOperationManager.create();
        MongoOperations mongoOperations = mongoOperationManager.getMongoOperations("mongodb://saas:123456@192.168.0.10:27017/test");
        MongoOperationsProvider mongoOperationsProvider = new SimpleMongoOperationsProvider(mongoOperations);
        requestLimitingRepository = new RequestLimitingRepository(mongoOperationsProvider);
    }

    @Test
    public void init() {
        requestLimitingRepository.initShard("store01");
    }

    @Test
    public void create() {
        RequestLimiting requestLimiting = RequestLimiting.from("C01", "ORDER", 5);
        requestLimitingRepository.create("store01", requestLimiting);
    }

    @Test
    public void lock() {
        boolean lock = false;
        RequestLimiting requestLimiting = RequestLimiting.from("C01", "ORDER", 5);
        try {
            requestLimitingRepository.create("store01", requestLimiting);
            throw new RuntimeException("abc");
        } finally {
            requestLimitingRepository.delete("store01", requestLimiting.getId());
            System.out.println("lock: " + lock);
        }


    }
}
