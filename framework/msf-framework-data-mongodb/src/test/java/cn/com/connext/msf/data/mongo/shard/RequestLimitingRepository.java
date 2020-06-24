package cn.com.connext.msf.data.mongo.shard;

import cn.com.connext.msf.data.mongo.ConnextShardRepository;
import cn.com.connext.msf.data.mongo.entity.RequestLimiting;
import cn.com.connext.msf.data.mongo.provider.MongoOperationsProvider;
import org.springframework.stereotype.Repository;

@Repository
public class RequestLimitingRepository extends ConnextShardRepository<RequestLimiting, String> {

    public RequestLimitingRepository(MongoOperationsProvider mongoOperationsProvider) {
        super(mongoOperationsProvider);
    }

}