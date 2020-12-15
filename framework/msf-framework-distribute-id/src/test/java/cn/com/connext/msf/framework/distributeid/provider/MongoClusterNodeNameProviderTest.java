package cn.com.connext.msf.framework.distributeid.provider;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.com.connext.msf.framework.distributeid.entity.ClusterNodeRenewInfo;
import cn.com.connext.msf.framework.distributeid.exception.NodeNameAllInUseException;
import cn.com.connext.msf.framework.distributeid.exception.NodeNameException;
import cn.com.connext.msf.framework.distributeid.repository.ClusterNodeRenewInfoRepository;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

public class MongoClusterNodeNameProviderTest {

    private final Logger logger = LoggerFactory.getLogger(MongoClusterNodeNameProviderTest.class);

    private final MongoOperations mongoOperations;
    private final ClusterNodeRenewInfoRepository repository;

    public MongoClusterNodeNameProviderTest() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("root").setLevel(Level.WARN);
        loggerContext.getLogger(MongoClusterNodeNameProvider.class).setLevel(Level.DEBUG);

        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://saas:123456@192.168.0.10:27017/ec"));
        mongoOperations = new MongoTemplate(mongoClient, "ec");
        repository = new ClusterNodeRenewInfoRepository(99, mongoOperations);
    }

    @Test
    public void init() {
        MongoClusterNodeNameProvider provider = new MongoClusterNodeNameProvider(999, 86400, repository);
        provider.init();
    }

    @Test
    public void hold() {
        clearTestData();
        String nodeName;

        MongoClusterNodeNameProvider provider01 = new MongoClusterNodeNameProvider(999, 86400, repository);
        MongoClusterNodeNameProvider provider02 = new MongoClusterNodeNameProvider(999, 86400, repository);
        MongoClusterNodeNameProvider provider03 = new MongoClusterNodeNameProvider(999, 86400, repository);

        provider01.hold();
        nodeName = provider01.getClusterNodeName();
        Assert.assertEquals("001", nodeName);

        provider02.hold();
        nodeName = provider02.getClusterNodeName();
        Assert.assertEquals("002", nodeName);

        provider03.hold();
        nodeName = provider03.getClusterNodeName();
        Assert.assertEquals("003", nodeName);

        for (int i = 4; i < 100; i++) {
            MongoClusterNodeNameProvider provider = new MongoClusterNodeNameProvider(999, 86400, repository);
            provider.hold();
            nodeName = provider.getClusterNodeName();
            Assert.assertEquals(StringUtils.leftPad(Integer.toString(i), 3, "0"), nodeName);
        }

        MongoClusterNodeNameProvider provider100 = new MongoClusterNodeNameProvider(999, 86400, repository);
        try {
            provider100.hold();
        } catch (Exception e) {
            boolean result = e instanceof NodeNameAllInUseException;
            Assert.assertEquals(true, result);
        }
    }

    @Test
    public void renew() {
        clearTestData();
        MongoClusterNodeNameProvider provider = new MongoClusterNodeNameProvider(999, 86400, repository);
        provider.hold();
        provider.renew();
    }

    @Test
    public void getClusterNodeName() {
        try {
            MongoClusterNodeNameProvider provider = new MongoClusterNodeNameProvider(999, 86400, repository);
            provider.getClusterNodeName();
        } catch (Exception e) {
            boolean result = e instanceof NodeNameException;
            Assert.assertEquals(true, result);
        }
    }

    private void clearTestData() {
        mongoOperations.findAllAndRemove(new Query(), ClusterNodeRenewInfo.class);
    }
}