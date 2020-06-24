package cn.com.connext.msf.data.mongo;

import cn.com.connext.msf.framework.query.QueryInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConnextMongoRepositoryImpl<T, ID extends Serializable>
        extends SimpleMongoRepository<T, ID> implements ConnextMongoRepository<T, ID> {

    private final MongoOperations mongoOperations;
    private final MongoEntityInformation<T, ID> entityInformation;

    public ConnextMongoRepositoryImpl(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);

        this.mongoOperations = mongoOperations;
        this.entityInformation = metadata;
    }


    @Override
    public <S extends T> S create(S entity) {
        mongoOperations.insert(entity, entityInformation.getCollectionName());
        return entity;
    }

    @Override
    public <S extends T> void batchCreate(List<S> entityList, int batchSize) {
        List<S> batchList = new ArrayList<>();
        for (int i = 0; i < entityList.size(); i++) {
            batchList.add(entityList.get(i));
            if ((i + 1) % batchSize == 0) {
                mongoOperations.insertAll(batchList);
                batchList.clear();
            }
        }
        if(!batchList.isEmpty()) {
            mongoOperations.insertAll(batchList);
            batchList.clear();
        }
    }

    @Override
    public <S extends T> S modify(S entity) {
        mongoOperations.save(entity, entityInformation.getCollectionName());
        return entity;
    }

    @Override
    public long update(Query query, Update update) {
        return mongoOperations.updateFirst(query, update, entityInformation.getJavaType()).getModifiedCount();
    }

    @Override
    public long update(QueryInfo queryInfo, Update update) {
        Query query = QueryBuilder.build(entityInformation.getJavaType(), queryInfo);
        return mongoOperations.updateFirst(query, update, entityInformation.getJavaType()).getModifiedCount();
    }

    @Override
    public T findOne(ID id) {
        return findItem(id);
    }

    @Override
    public T findItem(ID id) {
        return findById(id).orElse(null);
    }

    @Override
    public T findFirst(QueryInfo queryInfo) {
        queryInfo.setPageable(PageRequest.of(0, 1));
        Page<T> page = findPage(queryInfo);
        if (page.getContent().size() > 0) {
            return page.getContent().get(0);
        }
        return null;
    }

    @Override
    public boolean exists(ID id) {
        return existsById(id);
    }

    @Override
    public boolean exists(QueryInfo queryInfo) {
        return findFirst(queryInfo) != null;
    }

    @Override
    public void delete(ID id) {
        deleteById(id);
    }

    @Override
    public long delete(QueryInfo queryInfo) {
        Query query = QueryBuilder.build(entityInformation.getJavaType(), queryInfo);
        if (query != null) {
            return mongoOperations.remove(query, entityInformation.getJavaType()).getDeletedCount();
        }
        return 0;
    }

    @Override
    public Page<T> findPage(Pageable pageable) {
        return findAll(pageable);
    }

    @Override
    public Page<T> findPage(String expression, Pageable pageable) {
        return findPage(QueryInfo.from(pageable, expression));
    }

    @Override
    public Page<T> findPage(QueryInfo queryInfo) {
        if (queryInfo == null) {
            return findAll(PageRequest.of(0, 20));
        }

        Query query = QueryBuilder.build(entityInformation.getJavaType(), queryInfo);
        if (query == null) {
            query = new Query();
        }
        query.with(queryInfo.getPageable());

        String[] fields = queryInfo.getFieldArray();
        if (fields != null) {
            for (String field : fields) {
                query.fields().include(field);
            }
        }

        List<T> list = mongoOperations.find(query, entityInformation.getJavaType());
        long count = mongoOperations.count(query, entityInformation.getJavaType());
        return new PageImpl<>(list, queryInfo.getPageable(), count);
    }

    @Override
    public List<T> findList() {
        return findAll();
    }

    @Override
    public List<T> findList(String expression) {
        return findList(QueryInfo.from(expression));
    }

    @Override
    public List<T> findList(QueryInfo queryInfo) {
        Query query = QueryBuilder.build(entityInformation.getJavaType(), queryInfo);
        if (query == null) {
            query = new Query();
        }

        if (queryInfo != null && queryInfo.getPageable() != null) {
            query.with(queryInfo.getPageable().getSort());
        }
        return mongoOperations.find(query, entityInformation.getJavaType());
    }
}
