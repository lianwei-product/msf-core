package cn.com.connext.msf.data.mongo.provider;

import org.springframework.data.mongodb.core.MongoOperations;

public interface MongoOperationsProvider {

    MongoOperations getMongoOperations(String shardCode);

}
