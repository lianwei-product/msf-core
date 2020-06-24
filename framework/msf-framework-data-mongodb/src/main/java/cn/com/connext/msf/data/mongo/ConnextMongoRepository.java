package cn.com.connext.msf.data.mongo;

import cn.com.connext.msf.framework.query.QueryInfo;
import cn.com.connext.msf.framework.repository.ConnextRepository;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface ConnextMongoRepository<T, ID extends Serializable>
        extends MongoRepository<T, ID>, ConnextRepository<T, ID> {

    long update(Query query, Update update);

    long update(QueryInfo queryInfo, Update update);
}
