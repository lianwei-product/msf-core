package cn.com.connext.msf.framework.repository;

import cn.com.connext.msf.framework.query.QueryInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface ConnextRepository<T, ID extends Serializable> {

    <S extends T> S create(S entity);

    <S extends T> void batchCreate(List<S> entityList, int batchSize);

    <S extends T> S modify(S entity);

    T findOne(ID id);

    T findItem(ID id);

    T findFirst(QueryInfo queryInfo);

    boolean exists(ID id);

    boolean exists(QueryInfo queryInfo);

    void delete(ID id);

    long count();

    long delete(QueryInfo queryInfo);

    Page<T> findPage(Pageable pageable);

    Page<T> findPage(String expression, Pageable pageable);

    Page<T> findPage(QueryInfo queryInfo);

    List<T> findList();

    List<T> findList(String expression);

    List<T> findList(QueryInfo queryInfo);

}