package cn.com.connext.msf.data.mongo;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.com.connext.msf.data.mongo.entity.Customer;
import cn.com.connext.msf.framework.query.QueryInfo;
import cn.com.connext.msf.framework.utils.JSON;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.bson.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;


public class QueryBuilderTest {

    private final Logger logger = LoggerFactory.getLogger(QueryBuilderTest.class);
    private final MongoOperations mongoOperations;

    public QueryBuilderTest() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("root").setLevel(Level.WARN);
        loggerContext.getLogger(getClass()).setLevel(Level.INFO);

        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://saas:123456@192.168.0.10:27017/test"));
        mongoOperations = new MongoTemplate(mongoClient, "test");
    }

    @Test
    public void build() throws Exception {
        mongoOperations.dropCollection(Customer.class);
        Customer customer = new Customer();
        mongoOperations.insert(customer);

        QueryInfo queryInfo = QueryInfo.from("id eq {0}", customer.getId());
        Query query = QueryBuilder.build(Customer.class, queryInfo);
        customer = mongoOperations.findOne(query, Customer.class);
        logger.info(JSON.toIndentJsonString(customer));

        query = new Query();
        query.fields().include("realName");
        List<Document> documents = mongoOperations.find(query, Document.class, "customer");
        logger.info(documents.get(0).toJson());
    }

}