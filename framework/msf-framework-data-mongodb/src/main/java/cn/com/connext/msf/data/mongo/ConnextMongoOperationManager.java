package cn.com.connext.msf.data.mongo;

import cn.com.connext.msf.data.mongo.converter.BigDecimalConverter;
import cn.com.connext.msf.data.mongo.converter.JSR310DateConverter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Map;

public class ConnextMongoOperationManager {

    private final Logger logger = LoggerFactory.getLogger(ConnextMongoOperationManager.class);
    private final Object lock = new Object();
    private final Map<String, MongoOperations> mongoOperationsMap;
    private final MongoCustomConversions mongoCustomConversions;

    public ConnextMongoOperationManager(MongoCustomConversions mongoCustomConversions) {
        this.mongoOperationsMap = Maps.newHashMap();
        this.mongoCustomConversions = mongoCustomConversions;
    }

    public static ConnextMongoOperationManager create() {
        return new ConnextMongoOperationManager(new MongoCustomConversions(Lists.newArrayList(
                JSR310DateConverter.DateToZonedDateTimeConverter.INSTANCE,
                JSR310DateConverter.ZonedDateTimeToDateConverter.INSTANCE,
                JSR310DateConverter.LocalDateTimeToDateConverter.INSTANCE,
                JSR310DateConverter.DateToLocalDateConverter.INSTANCE,
                BigDecimalConverter.Decimal128ToBigDecimalConverter.INSTANCE,
                BigDecimalConverter.BigDecimalToDecimal128Converter.INSTANCE
        )));
    }

    public MongoOperations getMongoOperations(String uri) {
        MongoOperations mongoOperations = mongoOperationsMap.get(uri);
        if (mongoOperations == null) {
            synchronized (lock) {
                mongoOperations = mongoOperationsMap.get(uri);
                if (mongoOperations == null) {
                    mongoOperations = build(uri);
                    mongoOperationsMap.put(uri, mongoOperations);
                }
            }
        }
        return mongoOperations;
    }

    private MongoOperations build(String uri) {
        logger.info("Build new mongo connections, uri: {}", uri);
        MongoClientURI mongoClientURI = new MongoClientURI(uri);
        MongoClient mongoClient = new MongoClient(mongoClientURI);
        MongoTemplate mongoTemplate = new MongoTemplate(new SimpleMongoDbFactory(mongoClient, mongoClientURI.getDatabase()));
        MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
        mongoMapping.setCustomConversions(mongoCustomConversions); // tell mongodb to use the custom converters
        mongoMapping.afterPropertiesSet();
        return mongoTemplate;
    }
}
