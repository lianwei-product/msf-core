package cn.com.connext.msf.framework.distributeid.repository;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.com.connext.msf.framework.distributeid.entity.ClusterNodeRenewInfo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;

public class ClusterNodeRenewInfoRepositoryTest {

    private final Logger logger = LoggerFactory.getLogger(ClusterNodeRenewInfoRepositoryTest.class);

    private final MongoOperations mongoOperations;
    private final ClusterNodeRenewInfoRepository repository;

    public ClusterNodeRenewInfoRepositoryTest() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("root").setLevel(Level.WARN);
        loggerContext.getLogger(getClass()).setLevel(Level.INFO);

        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://saas:123456@192.168.0.10:27017/ec"));
        mongoOperations = new MongoTemplate(mongoClient, "ec");
        repository = new ClusterNodeRenewInfoRepository(99, mongoOperations);
    }

    @Test
    public void init() {
        repository.init();
    }

    @Test
    public void create() {
        clearTestData();
        ClusterNodeRenewInfo renewalInfo = ClusterNodeRenewInfo.from(1, "test_owner01", 10);
        boolean result;

        result = repository.create(renewalInfo);
        Assert.assertEquals(true, result);

        result = repository.create(renewalInfo);
        Assert.assertEquals(false, result);
    }


    @Test
    public void update() throws InterruptedException {
        create();
        boolean result;

        logger.info("Renew by owner node, will be success.");
        ClusterNodeRenewInfo renewalInfo01 = ClusterNodeRenewInfo.from(1, "test_owner01", 1);
        result = repository.update(renewalInfo01);
        Assert.assertEquals(true, result);

        logger.info("Renew by other node, will be failure.");
        ClusterNodeRenewInfo renewalInfo02 = ClusterNodeRenewInfo.from(1, "test_owner02", 30);
        result = repository.update(renewalInfo02);
        Assert.assertEquals(false, result);

        Thread.sleep(1500);

        logger.info("Owner node expired, will be success.");
        ClusterNodeRenewInfo renewalInfo03 = ClusterNodeRenewInfo.from(1, "test_owner03", 30);
        result = repository.update(renewalInfo03);
        Assert.assertEquals(true, result);
    }

    @Test
    public void findNewNodeName() {
        clearTestData();
        int newNodeName;
        newNodeName = repository.findNewNodeName();
        Assert.assertEquals(1, newNodeName);

        repository.create(ClusterNodeRenewInfo.from(1, "test_owner01", 30));
        newNodeName = repository.findNewNodeName();
        Assert.assertEquals(2, newNodeName);
    }

    @Test
    public void findExpireRenewInfo() {
        clearTestData();

        repository.create(ClusterNodeRenewInfo.from(1, "test_owner01", DateUtils.addSeconds(new Date(), -10)));
        repository.create(ClusterNodeRenewInfo.from(2, "test_owner02", DateUtils.addSeconds(new Date(), -20)));
        repository.create(ClusterNodeRenewInfo.from(3, "test_owner03", DateUtils.addSeconds(new Date(), -10)));

        ClusterNodeRenewInfo expireRenewInfo = repository.findExpireRenewInfo();
        Assert.assertEquals("test_owner02", expireRenewInfo.getOwner());
    }

    private void clearTestData() {
        mongoOperations.findAllAndRemove(new Query(), ClusterNodeRenewInfo.class);
    }
}