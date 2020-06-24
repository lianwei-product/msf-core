package cn.com.connext.msf.data.mongo;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.com.connext.msf.data.mongo.entity.*;
import cn.com.connext.msf.framework.utils.JSON;
import cn.com.connext.msf.framework.utils.Time;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MongoQueryTest {

    private static Logger logger = LoggerFactory.getLogger(MongoQueryTest.class);
    private static MongoClient mongoClient;
    private static MongoOperations mongoOperations;

    @BeforeClass
    public static void initTest() {
        mongoClient = new MongoClient(new MongoClientURI("mongodb://106.14.192.217/cdp"));
        mongoOperations = new MongoTemplate(new SimpleMongoDbFactory(mongoClient, "cdp"));
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("root").setLevel(Level.INFO);

    }

    @AfterClass
    public static void destroy() {
        mongoClient.close();
    }


    @Test
    public void testFindAll() {
        List<DataCollection> collectionList = mongoOperations.findAll(DataCollection.class);
        logger.info(JSON.toIndentJsonString(collectionList));
    }


    @Test
    public void testFindQuery() {
        Query query = new Query();
        Criteria criteria01 = Criteria.where("tenantId").is("SampleTenant01");
        Criteria criteria02 = Criteria.where("id").is("connext_brand_a");
//        Criteria criteria03 = new Criteria()
//                .orOperator(Criteria.where("tenantId").is("0001"), Criteria.where("tenantId").is("SampleTenant01"))
//                .andOperator(new Criteria().orOperator(Criteria.where("id").is("connext_brand_a"),
//                        new Criteria().andOperator(Criteria.where("id").is("connext_brand_b"), Criteria.where("name").is("connext_brand_b"))));

        Criteria criteria03 = new Criteria()
                .andOperator(Criteria.where("tenantId").is("0001"));
        //query.addCriteria(criteria01);
        //query.addCriteria(criteria02);
        query.addCriteria(criteria03);

        List<DataCollection> collectionList = mongoOperations.find(query, DataCollection.class);

        logger.info(query.toString());
        logger.info(JSON.toIndentJsonString(collectionList));
    }

    @Test
    public void testNesting() {
        mongoOperations.getConverter();
        mongoOperations.insert(buildUser("Jack", "1", "Connext", "CN"));
        mongoOperations.insert(buildUser("Sony", "2", "NAS", "USA"));

        logger.info("Query by Nesting field=========================");
        Query query = QueryBuilder.build(User.class, "org.id eq 1");
        List<User> users = mongoOperations.find(query, User.class);
        logger.info(query.toString());
        logger.info(JSON.toIndentJsonString(users));

        logger.info("Query by field=========================");
        query = QueryBuilder.build(User.class, "addressList.name eq CN");
        users = mongoOperations.find(query, User.class);
        logger.info(query.toString());
        logger.info(JSON.toIndentJsonString(users));

        logger.info("Query by List Nesting field=========================");
        query = QueryBuilder.build(User.class, "name eq Sony");
        users = mongoOperations.find(query, User.class);
        logger.info(query.toString());
        logger.info(JSON.toIndentJsonString(users));
    }

    private User buildUser(String name, String orgId, String orgName, String addressName) {
        User user = new User();
        user.setName(name);
        Org org = new Org();
        org.setId(orgId);
        org.setName(orgName);
        user.setOrg(org);
        List<Address> addresses = new ArrayList<>();
        Address address = new Address();
        address.setName(addressName);
        addresses.add(address);
        user.setAddressList(addresses);
        return user;
    }

    @Test
    public void testQuery() {
        mongoOperations.getConverter();
        mongoOperations.remove(new Query(), Order.class);
        mongoOperations.insert(Order.from("0001", "0001", 10, 20.5,false, Time.parseDate("2018-01-20"), LocalDateTime.of(2018, 2, 10, 15, 30)));
        mongoOperations.insert(Order.from("0002", "0002", 20, 41,true, Time.parseDate("2018-03-27"), LocalDateTime.of(2019, 6, 23, 10, 0)));
        mongoOperations.insert(Order.from("0003", "0003", 40, 82,true, Time.parseDate("2019-06-13"), LocalDateTime.of(2019, 8, 22, 15, 0)));

        logger.info("########## test String ##########");
        Query query = QueryBuilder.build(Order.class, "id in 0001,0002");
        List<Order> orderList = mongoOperations.find(query, Order.class);
        logger.info(query.toString());
        logger.info(JSON.toIndentJsonString(orderList));
        Assert.assertEquals(orderList.size(), 2);

        query = QueryBuilder.build(Order.class, "id in 0003");
        orderList = mongoOperations.find(query, Order.class);
        logger.info(query.toString());
        logger.info(JSON.toIndentJsonString(orderList));
        Assert.assertEquals(orderList.size(), 1);

        logger.info("########## test int ##########");
        query = QueryBuilder.build(Order.class, "quantity in 20,40");
        orderList = mongoOperations.find(query, Order.class);
        logger.info(query.toString());
        logger.info(JSON.toIndentJsonString(orderList));
        Assert.assertEquals(orderList.size(), 2);

        logger.info("########## test int ##########");
        query = QueryBuilder.build(Order.class, "totalAmount in 20.5,41,82");
        orderList = mongoOperations.find(query, Order.class);
        logger.info(query.toString());
        logger.info(JSON.toIndentJsonString(orderList));
        Assert.assertEquals(orderList.size(), 3);

        logger.info("########## test Date ##########");
        query = QueryBuilder.build(Order.class, "createTime in 2018-03-27,2019-06-13");
        orderList = mongoOperations.find(query, Order.class);
        logger.info(query.toString());
        logger.info(JSON.toIndentJsonString(orderList));
        Assert.assertEquals(orderList.size(), 2);

        logger.info("########## test boolean ##########");
        query = QueryBuilder.build(Order.class, "realMember in true");
        orderList = mongoOperations.find(query, Order.class);
        logger.info(query.toString());
        logger.info(JSON.toIndentJsonString(orderList));
        Assert.assertEquals(orderList.size(), 2);

        query = QueryBuilder.build(Order.class, "realMember in true,false");
        orderList = mongoOperations.find(query, Order.class);
        logger.info(query.toString());
        logger.info(JSON.toIndentJsonString(orderList));
        Assert.assertEquals(orderList.size(), 3);
    }
}