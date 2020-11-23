package cn.com.connext.msf.server.timingwheel.repository;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.com.connext.msf.data.mongo.QueryBuilder;
import cn.com.connext.msf.framework.utils.JSON;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelClusterLock;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelEvent;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelPeriod;
import cn.com.connext.msf.server.timingwheel.entity.TimingWheelPeriodUpdateTag;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.List;


public class TimingWheelMongoRepositoryTest {

    private static MongoClient mongoClient;
    private static MongoOperations mongoOperations;
    private static TimingWheelMongoRepository timingWheelMongoRepository;
    private static Query all;
    private static String owner = "test-owner";


    @BeforeClass
    public static void initTest() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("root").setLevel(Level.INFO);

        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://saas:SAAS2018connext%40Dmcs@106.14.192.217:27017/cdp"));
        mongoOperations = new MongoTemplate(mongoClient, "cdp");
        timingWheelMongoRepository = new TimingWheelMongoRepository(mongoOperations);
        all = new Query();
    }

    @Before
    public void clearAllTestData() {
//        mongoOperations.remove(all, TimingWheelClusterLock.class);
//        mongoOperations.remove(all, TimingWheelEvent.class);
//        mongoOperations.remove(all, TimingWheelPeriod.class);
//        mongoOperations.remove(all, TimingWheelPeriodUpdateTag.class);
    }

    @Test
    public void initialLock() throws Exception {
        Assert.assertEquals(0, mongoOperations.find(all, TimingWheelClusterLock.class).size());
        timingWheelMongoRepository.initialLock(owner);
        timingWheelMongoRepository.initialLock(owner);
        Assert.assertEquals(1, mongoOperations.find(all, TimingWheelClusterLock.class).size());
    }

    @Test
    public void acquireLock() throws Exception {
        Assert.assertTrue(timingWheelMongoRepository.acquireLock(owner, new Date(new Date().getTime() + 10 * 1000)));
        Assert.assertFalse(timingWheelMongoRepository.acquireLock("another-owner", new Date()));
    }

    @Test
    public void releaseLock() throws Exception {
        timingWheelMongoRepository.acquireLock(owner, new Date(new Date().getTime() + 10 * 1000));
        timingWheelMongoRepository.releaseLock(owner);
        Assert.assertTrue(timingWheelMongoRepository.acquireLock("another-owner", new Date()));
    }

    @Test
    public void appendEvent() throws Exception {
        timingWheelMongoRepository.appendEvent(TimingWheelEvent.from(new Date(), "test", null));
        Assert.assertEquals(1, mongoOperations.find(all, TimingWheelEvent.class).size());
    }

    @Test
    public void appendPeriod() throws Exception {
        timingWheelMongoRepository.appendPeriod(TimingWheelPeriod.from("test", "test", "0 0 0 * * ? 2019-2020"));
        Assert.assertEquals(1, mongoOperations.find(all, TimingWheelPeriod.class).size());
    }

    @Test
    public void removePeriodInfo() throws Exception {
        timingWheelMongoRepository.appendPeriod(TimingWheelPeriod.from("testId", "test", "test", "0 0 0 * * ? 2019-2020"));
        Assert.assertEquals(1, mongoOperations.find(all, TimingWheelPeriod.class).size());

        timingWheelMongoRepository.removePeriodInfo("testId");
        Assert.assertEquals(0, mongoOperations.find(all, TimingWheelPeriod.class).size());
    }

    @Test
    public void takeEventList() throws Exception {
        for (int i = 0; i < 10; i++) {
            timingWheelMongoRepository.appendEvent(TimingWheelEvent.from(new Date(), "test", null));
        }
        Assert.assertEquals(10, timingWheelMongoRepository.takeEventList(10).size());
    }

    @Test
    public void takePeriodList() throws Exception {
        for (int i = 0; i < 10; i++) {
            timingWheelMongoRepository.appendPeriod(TimingWheelPeriod.from("test", "test", "0 0 0 * * ? 2019-2020"));
        }
        Assert.assertEquals(10, timingWheelMongoRepository.takePeriodList().size());
    }

    @Test
    public void findPeriodInfo() throws Exception {
        timingWheelMongoRepository.appendPeriod(TimingWheelPeriod.from("testId", "test", "test", "0 0 0 * * ? 2019-2020"));
        Assert.assertEquals("testId", timingWheelMongoRepository.findPeriodInfo("testId").getId());
    }

    @Test
    public void initLatestPeriodUpdateTag() throws Exception {
        Assert.assertEquals(0, mongoOperations.find(all, TimingWheelPeriodUpdateTag.class).size());
        timingWheelMongoRepository.initLatestPeriodUpdateTag();
        timingWheelMongoRepository.initLatestPeriodUpdateTag();
        Assert.assertEquals(1, mongoOperations.find(all, TimingWheelPeriodUpdateTag.class).size());
    }

    @Test
    public void findLatestPeriodUpdateTag() throws Exception {
        Assert.assertEquals(0, timingWheelMongoRepository.findLatestPeriodUpdateTag());
        timingWheelMongoRepository.initLatestPeriodUpdateTag();
        Assert.assertNotEquals(0, timingWheelMongoRepository.findLatestPeriodUpdateTag());
    }

    @Test
    public void updateLatestPeriodUpdateTag() throws Exception {
        timingWheelMongoRepository.updateLatestPeriodUpdateTag();
        Assert.assertNotEquals(0, timingWheelMongoRepository.findLatestPeriodUpdateTag());

        long latestTag = timingWheelMongoRepository.findLatestPeriodUpdateTag();
        Thread.sleep(10);
        timingWheelMongoRepository.updateLatestPeriodUpdateTag();
        Assert.assertNotEquals(latestTag, timingWheelMongoRepository.findLatestPeriodUpdateTag());

    }

    @Test
    public void queryTest() throws Exception {
        Query query = QueryBuilder.build(TimingWheelClusterLock.class, "owner like $L");
        List<TimingWheelClusterLock> list = mongoOperations.find(query, TimingWheelClusterLock.class);
        System.out.println("$L-(:" + JSON.toIndentJsonString(list));

        query = QueryBuilder.build(TimingWheelClusterLock.class, "owner like $R");
        list = mongoOperations.find(query, TimingWheelClusterLock.class);
        System.out.println("$R-):" + JSON.toIndentJsonString(list));

        query = QueryBuilder.build(TimingWheelClusterLock.class, "owner like *");
        list = mongoOperations.find(query, TimingWheelClusterLock.class);
        System.out.println("*:" + JSON.toIndentJsonString(list));

        query = QueryBuilder.build(TimingWheelClusterLock.class, "owner like ?");
        list = mongoOperations.find(query, TimingWheelClusterLock.class);
        System.out.println("?:" + JSON.toIndentJsonString(list));

        query = QueryBuilder.build(TimingWheelClusterLock.class, "owner like $A");
        list = mongoOperations.find(query, TimingWheelClusterLock.class);
        System.out.println("AND:" + JSON.toIndentJsonString(list));

        query = QueryBuilder.build(TimingWheelClusterLock.class, "owner like $O");
        list = mongoOperations.find(query, TimingWheelClusterLock.class);
        System.out.println("OR:" + JSON.toIndentJsonString(list));

        query = QueryBuilder.build(TimingWheelClusterLock.class, "owner like \\");
        list = mongoOperations.find(query, TimingWheelClusterLock.class);
        System.out.println("\\:" + JSON.toIndentJsonString(list));

        query = QueryBuilder.build(TimingWheelClusterLock.class, "owner like $");
        list = mongoOperations.find(query, TimingWheelClusterLock.class);
        System.out.println("$:" + JSON.toIndentJsonString(list));


        query = QueryBuilder.build(TimingWheelClusterLock.class, "owner like +");
        list = mongoOperations.find(query, TimingWheelClusterLock.class);
        System.out.println("+:" + JSON.toIndentJsonString(list));

        query = QueryBuilder.build(TimingWheelClusterLock.class, "owner like .");
        list = mongoOperations.find(query, TimingWheelClusterLock.class);
        System.out.println(".:" + JSON.toIndentJsonString(list));


        query = QueryBuilder.build(TimingWheelClusterLock.class, "owner like ]");
        list = mongoOperations.find(query, TimingWheelClusterLock.class);
        System.out.println("]:" + JSON.toIndentJsonString(list));

        query = QueryBuilder.build(TimingWheelClusterLock.class, "owner like [");
        list = mongoOperations.find(query, TimingWheelClusterLock.class);
        System.out.println("[:" + JSON.toIndentJsonString(list));

        query = QueryBuilder.build(TimingWheelClusterLock.class, "owner like ^");
        list = mongoOperations.find(query, TimingWheelClusterLock.class);
        System.out.println("^:" + JSON.toIndentJsonString(list));

//        query = QueryBuilder.build(TimingWheelClusterLock.class, "owner like {");
//        list = mongoOperations.find(query, TimingWheelClusterLock.class);
//        System.out.println("{:" + JSON.toIndentJsonString(list));

        query = QueryBuilder.build(TimingWheelClusterLock.class, "owner like }");
        list = mongoOperations.find(query, TimingWheelClusterLock.class);
        System.out.println("}:" + JSON.toIndentJsonString(list));

        query = QueryBuilder.build(TimingWheelClusterLock.class, "owner like |");
        list = mongoOperations.find(query, TimingWheelClusterLock.class);
        System.out.println("|:" + JSON.toIndentJsonString(list));

        query = QueryBuilder.build(TimingWheelClusterLock.class, "owner like test");
        list = mongoOperations.find(query, TimingWheelClusterLock.class);
        System.out.println("test:" + JSON.toIndentJsonString(list));

    }

}