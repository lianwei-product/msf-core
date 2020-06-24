package cn.com.connext.msf.data.mongo.provider;


import org.springframework.data.mongodb.core.MongoOperations;

public class SimpleMongoOperationsProvider implements MongoOperationsProvider {

    private final MongoOperations mongoOperations;

    public SimpleMongoOperationsProvider(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public MongoOperations getMongoOperations(String shardCode) {
        return mongoOperations;
    }

}
