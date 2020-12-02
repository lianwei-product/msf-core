package cn.com.connext.msf.data.mongo;

import cn.com.connext.msf.data.mongo.provider.MongoOperationsProvider;
import cn.com.connext.msf.framework.query.QueryInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;
import java.util.List;

public abstract class ConnextTenantShardRepository<T, ID extends Serializable> extends ConnextShardRepository<T, ID> {

    public ConnextTenantShardRepository(MongoOperationsProvider mongoOperationsProvider) {
        super(mongoOperationsProvider);
    }

    public <S extends T> S create(String tenantId, String buCode, S entity) {
        String shardCode = getShardCode(tenantId, buCode);
        return create(shardCode, entity);
    }

    public <S extends T> void batchCreate(String tenantId, String buCode, List<S> entityList, int batchSize) {
        String shardCode = getShardCode(tenantId, buCode);
        batchCreate(shardCode, entityList, batchSize);
    }

    public <S extends T> S modify(String tenantId, String buCode, S entity) {
        String shardCode = getShardCode(tenantId, buCode);
        return modify(shardCode, entity);
    }

    public <S extends T> S save(String tenantId, String buCode, S entity) {
        String shardCode = getShardCode(tenantId, buCode);
        return save(shardCode, entity);
    }

    public long update(String tenantId, String buCode, Query query, Update update) {
        String shardCode = getShardCode(tenantId, buCode);
        return update(shardCode, query, update);
    }

    public long update(String tenantId, String buCode, QueryInfo queryInfo, Update update) {
        String shardCode = getShardCode(tenantId, buCode);
        return update(shardCode, queryInfo, update);
    }

    public long updateMulti(String tenantId, String buCode, Query query, Update update) {
        String shardCode = getShardCode(tenantId, buCode);
        return updateMulti(shardCode, query, update);
    }

    public long updateMulti(String tenantId, String buCode, QueryInfo queryInfo, Update update) {
        String shardCode = getShardCode(tenantId, buCode);
        return updateMulti(shardCode, queryInfo, update);
    }

    public long upsert(String tenantId, String buCode, Query query, Update update) {
        String shardCode = getShardCode(tenantId, buCode);
        return upsert(shardCode, query, update);
    }

    public void delete(String tenantId, String buCode, ID id) {
        String shardCode = getShardCode(tenantId, buCode);
        delete(shardCode, id);
    }

    public T findOne(String tenantId, String buCode, ID id) {
        return findItem(tenantId, buCode, id);
    }

    public T findItem(String tenantId, String buCode, ID id) {
        String shardCode = getShardCode(tenantId, buCode);
        return findItem(shardCode, id);
    }

    public T findFirst(String tenantId, String buCode, QueryInfo queryInfo) {
        String shardCode = getShardCode(tenantId, buCode);
        return findFirst(shardCode, queryInfo);
    }

    public boolean exists(String tenantId, String buCode, ID id) {
        String shardCode = getShardCode(tenantId, buCode);
        return findItem(shardCode, id) != null;
    }

    public boolean exists(String tenantId, String buCode, QueryInfo queryInfo) {
        String shardCode = getShardCode(tenantId, buCode);
        return findFirst(shardCode, queryInfo) != null;
    }

    public long delete(String tenantId, String buCode, QueryInfo queryInfo) {
        String shardCode = getShardCode(tenantId, buCode);
        return delete(shardCode, queryInfo);
    }

    public Page<T> findPage(String tenantId, String buCode, Pageable pageable) {
        String shardCode = getShardCode(tenantId, buCode);
        return findPage(shardCode, pageable);
    }

    public Page<T> findPage(String tenantId, String buCode, QueryInfo queryInfo) {
        String shardCode = getShardCode(tenantId, buCode);
        return findPage(shardCode, queryInfo);
    }

    public List<T> findList(String tenantId, String buCode) {
        String shardCode = getShardCode(tenantId, buCode);
        return findList(shardCode);
    }

    public List<T> findList(String tenantId, String buCode, QueryInfo queryInfo) {
        String shardCode = getShardCode(tenantId, buCode);
        return findList(shardCode, queryInfo);
    }


    public long count(String tenantId, String buCode) {
        String shardCode = getShardCode(tenantId, buCode);
        return count(shardCode);
    }

    public long count(String tenantId, String buCode, QueryInfo queryInfo) {
        String shardCode = getShardCode(tenantId, buCode);
        return count(shardCode, queryInfo);
    }

    public long count(String tenantId, String buCode, Query query) {
        String shardCode = getShardCode(tenantId, buCode);
        return count(shardCode, query);
    }

    protected String getShardCode(String tenantId, String buCode) {
        return tenantId.concat("_").concat(buCode);
    }

}
