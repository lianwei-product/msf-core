package cn.com.connext.msf.data.mongo;

import cn.com.connext.msf.data.mongo.provider.MongoOperationsProvider;
import cn.com.connext.msf.framework.query.QueryInfo;
import com.google.common.collect.Lists;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class ConnextShardRepository<T, ID extends Serializable> {

    private final MongoOperationsProvider mongoOperationsProvider;
    private final Class<T> entityClass;
    private final String baseCollectionName;
    private final String idName;

    public ConnextShardRepository(MongoOperationsProvider mongoOperationsProvider) {
        this.mongoOperationsProvider = mongoOperationsProvider;
        this.entityClass = getEntityClass();
        this.baseCollectionName = getBaseCollectionName();
        this.idName = getIdName();
    }

    @SuppressWarnings("unchecked")
    private Class<T> getEntityClass() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<T>) pt.getActualTypeArguments()[0];
    }

    private String getBaseCollectionName() {
        ShardDocument document = entityClass.getAnnotation(ShardDocument.class);
        if (document == null || StringUtils.isBlank(document.collection())) {
            throw new RuntimeException(entityClass.getName() + " must have ShardDocument annotation.");
        }
        return document.collection();
    }


    public boolean initShard(String shardCode) {
        MongoOperations mongoOperations = getMongoOperations(shardCode);
        String collectionName = getCollectionName(shardCode);
        if (isCollectionExist(shardCode, collectionName)) {
            return false;
        }
        mongoOperations.createCollection(collectionName);
        MongoCollection<Document> collection = mongoOperations.getCollection(collectionName);

        List<Field> fieldList = getFieldList();
        for (Field field : fieldList) {
            Indexed indexed = field.getAnnotation(Indexed.class);
            if (indexed != null) {
                String fieldName = field.getName();
                if (indexed.unique()) {
                    collection.createIndex(new Document(fieldName, 1), new IndexOptions().name("uni_".concat(fieldName)).background(true).unique(true));
                } else {
                    if (indexed.expireAfterSeconds() == -1) {
                        collection.createIndex(new Document(fieldName, 1), new IndexOptions().name("idx_".concat(fieldName)).background(true));
                    } else {
                        collection.createIndex(new Document(fieldName, 1), new IndexOptions().name("idx_".concat(fieldName)).expireAfter((long) indexed.expireAfterSeconds(), TimeUnit.SECONDS));
                    }
                }
            }
        }
        return true;
    }

    public void dropShard(String shardCode) {
        MongoOperations mongoOperations = getMongoOperations(shardCode);
        String collectionName = getCollectionName(shardCode);
        if (isCollectionExist(shardCode, collectionName)) {
            mongoOperations.dropCollection(collectionName);
        }
    }

    public <S extends T> S create(String shardCode, S entity) {
        MongoOperations mongoOperations = getMongoOperations(shardCode);
        String collectionName = getCollectionName(shardCode);
        mongoOperations.insert(entity, collectionName);
        return entity;
    }

    public <S extends T> void batchCreate(String shardCode, List<S> entityList, int batchSize) {
        MongoOperations mongoOperations = getMongoOperations(shardCode);
        String collectionName = getCollectionName(shardCode);
        List<S> batchList = new ArrayList<>();
        for (int i = 0; i < entityList.size(); i++) {
            batchList.add(entityList.get(i));
            if ((i + 1) % batchSize == 0) {
                mongoOperations.insert(batchList, collectionName);
                batchList.clear();
            }
        }
        if (!batchList.isEmpty()) {
            mongoOperations.insert(batchList, collectionName);
            batchList.clear();
        }
    }

    public <S extends T> S modify(String shardCode, S entity) {
        MongoOperations mongoOperations = getMongoOperations(shardCode);
        String collectionName = getCollectionName(shardCode);
        mongoOperations.save(entity, collectionName);
        return entity;
    }

    public <S extends T> S save(String shardCode, S entity) {
        MongoOperations mongoOperations = getMongoOperations(shardCode);
        String collectionName = getCollectionName(shardCode);
        mongoOperations.save(entity, collectionName);
        return entity;
    }

    public long update(String shardCode, QueryInfo queryInfo, Update update) {
        MongoOperations mongoOperations = getMongoOperations(shardCode);
        String collectionName = getCollectionName(shardCode);
        Query query = QueryBuilder.build(entityClass, queryInfo);
        return mongoOperations.updateFirst(query, update, entityClass, collectionName).getModifiedCount();
    }

    public long update(String shardCode, Query query, Update update) {
        MongoOperations mongoOperations = getMongoOperations(shardCode);
        String collectionName = getCollectionName(shardCode);
        return mongoOperations.updateFirst(query, update, entityClass, collectionName).getModifiedCount();
    }

    public long updateMulti(String shardCode, QueryInfo queryInfo, Update update) {
        MongoOperations mongoOperations = getMongoOperations(shardCode);
        String collectionName = getCollectionName(shardCode);
        Query query = QueryBuilder.build(entityClass, queryInfo);
        return mongoOperations.updateMulti(query, update, entityClass, collectionName).getModifiedCount();
    }

    public long updateMulti(String shardCode, Query query, Update update) {
        MongoOperations mongoOperations = getMongoOperations(shardCode);
        String collectionName = getCollectionName(shardCode);
        return mongoOperations.updateMulti(query, update, entityClass, collectionName).getModifiedCount();
    }

    public long upsert(String shardCode, Query query, Update update) {
        MongoOperations mongoOperations = getMongoOperations(shardCode);
        String collectionName = getCollectionName(shardCode);
        return mongoOperations.upsert(query, update, entityClass, collectionName).getModifiedCount();
    }

    public T findOne(String shardCode, ID id) {
        return findItem(shardCode, id);
    }

    public T findItem(String shardCode, ID id) {
        MongoOperations mongoOperations = getMongoOperations(shardCode);
        String collectionName = getCollectionName(shardCode);
        return mongoOperations.findById(id, entityClass, collectionName);
    }

    public T findFirst(String shardCode, QueryInfo queryInfo) {
        queryInfo.setPageable(PageRequest.of(0, 1));
        Page<T> page = findPage(shardCode, queryInfo);
        if (page.getContent().size() > 0) {
            return page.getContent().get(0);
        }
        return null;
    }

    public boolean exists(String shardCode, ID id) {
        return findItem(shardCode, id) != null;
    }

    public boolean exists(String shardCode, QueryInfo queryInfo) {
        return findFirst(shardCode, queryInfo) != null;
    }

    public void delete(String shardCode, ID id) {
        MongoOperations mongoOperations = getMongoOperations(shardCode);
        String collectionName = getCollectionName(shardCode);
        mongoOperations.remove(getIdQuery(id), entityClass, collectionName);
    }

    public long delete(String shardCode, QueryInfo queryInfo) {
        MongoOperations mongoOperations = getMongoOperations(shardCode);
        String collectionName = getCollectionName(shardCode);
        Query query = QueryBuilder.build(entityClass, queryInfo);
        if (query != null) {
            return mongoOperations.remove(query, entityClass, collectionName).getDeletedCount();
        }
        return 0;
    }

    public Page<T> findPage(String shardCode, Pageable pageable) {
        MongoOperations mongoOperations = getMongoOperations(shardCode);
        String collectionName = getCollectionName(shardCode);
        Long count = count(shardCode);
        List<T> list = mongoOperations.find(new Query().with(pageable), entityClass, collectionName);
        return new PageImpl<T>(list, pageable, count);
    }

    public Page<T> findPage(String shardCode, QueryInfo queryInfo) {
        if (queryInfo == null) {
            return findPage(shardCode, PageRequest.of(0, 20));
        }

        String collectionName = getCollectionName(shardCode);
        Query query = QueryBuilder.build(entityClass, queryInfo);
        query.with(queryInfo.getPageable());

        List<T> list = getMongoOperations(shardCode).find(query, entityClass, collectionName);
        long count = getMongoOperations(shardCode).count(query, entityClass, collectionName);
        return new PageImpl<>(list, queryInfo.getPageable(), count);
    }

    public List<T> findList(String shardCode) {
        String collectionName = getCollectionName(shardCode);
        return getMongoOperations(shardCode).find(new Query(), entityClass, collectionName);
    }

    public List<T> findList(String shardCode, QueryInfo queryInfo) {
        String collectionName = getCollectionName(shardCode);
        Query query = QueryBuilder.build(entityClass, queryInfo);
        if (queryInfo != null && queryInfo.getPageable() != null) {
            query.with(queryInfo.getPageable().getSort());
        }
        return getMongoOperations(shardCode).find(query, entityClass, collectionName);
    }


    public long count(String shardCode) {
        String collectionName = getCollectionName(shardCode);
        return getMongoOperations(shardCode).getCollection(collectionName).count();
    }

    public long count(String shardCode, QueryInfo queryInfo) {
        String collectionName = getCollectionName(shardCode);
        Query query = QueryBuilder.build(entityClass, queryInfo);
        return getMongoOperations(shardCode).count(query, entityClass, collectionName);
    }

    public long count(String shardCode, Query query) {
        String collectionName = getCollectionName(shardCode);
        return getMongoOperations(shardCode).count(query, entityClass, collectionName);
    }

    protected boolean isCollectionExist(String shardCode, String collectionName) {
        Set<String> collectionNames = getMongoOperations(shardCode).getCollectionNames();
        return collectionNames.contains(collectionName);
    }

    protected String getCollectionName(String shardCode) {
        return shardCode.concat("_").concat(baseCollectionName);
    }

    protected MongoOperations getMongoOperations(String shardCode) {
        return mongoOperationsProvider.getMongoOperations(shardCode);
    }

    private Query getIdQuery(ID id) {
        return new Query(Criteria.where(idName).is(id));
    }

    private List<Field> getFieldList() {
        List<Field> fieldList = Lists.newArrayList(entityClass.getDeclaredFields());
        Class superClass = entityClass.getSuperclass();
        while (superClass != null) {
            fieldList.addAll(Lists.newArrayList(superClass.getDeclaredFields()));
            superClass = superClass.getSuperclass();
        }
        return fieldList;
    }

    private String getIdName() {
        List<Field> fieldList = getFieldList();

        for (Field field : fieldList) {
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                return field.getName();
            }
        }
        throw new RuntimeException(entityClass.getName() + " must have Id annotation.");
    }
}
