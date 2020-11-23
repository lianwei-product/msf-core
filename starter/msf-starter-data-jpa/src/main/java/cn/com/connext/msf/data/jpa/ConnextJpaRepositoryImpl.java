package cn.com.connext.msf.data.jpa;

import cn.com.connext.msf.framework.query.QueryInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiringInspection")
public class ConnextJpaRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements ConnextJpaRepository<T, ID> {

    private final JpaEntityInformation entityInformation;
    private final EntityManager entityManager;

    public ConnextJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.entityManager = entityManager;
    }

    @Transactional
    public <S extends T> S create(S entity) {
        this.entityManager.persist(entity);
        return entity;
    }


    @Transactional
    public <S extends T> void batchCreate(List<S> entityList, int batchSize) {
        for (int i = 0; i < entityList.size(); i++) {
            entityManager.persist(entityList.get(i));
            if ((i + 1) % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
    }

    @Transactional
    public <S extends T> S modify(S entity) {
        this.entityManager.merge(entity);
        return entity;
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
    @Transactional
    public void delete(ID id) {
        deleteById(id);
    }

    @Override
    @Transactional
    public long delete(QueryInfo queryInfo) {
        List<T> list = findList(queryInfo);
        deleteInBatch(list);
        return 0;
    }

    @Override
    public Page<T> findPage(Pageable pageable) {
        return this.findAll(pageable);
    }

    @Override
    public Page<T> findPage(String expression, Pageable pageable) {
        return findPage(QueryInfo.from(pageable, expression));
    }

    @Override
    public Page<T> findPage(QueryInfo queryInfo) {
        return queryInfo == null ?
                findPage(PageRequest.of(0, 20)) :
                findPage(SpecificationBuilder.build(queryInfo), queryInfo.getPageable());
    }

    @Override
    public Page<T> findPage(Specification<T> specification, Pageable pageable) {
        return this.findAll(specification, pageable);
    }

    @Override
    public List<T> findList() {
        return this.findAll();
    }

    @Override
    public List<T> findList(String expression) {
        return findList(QueryInfo.from(expression));
    }

    @Override
    public List<T> findList(QueryInfo queryInfo) {
        return queryInfo == null ?
                findList() :
                findList(SpecificationBuilder.build(queryInfo), queryInfo.getPageable().getSort());
    }

    @Override
    public List<T> findList(Specification<T> specification) {
        return this.findAll(specification);
    }

    @Override
    public List<T> findList(Specification<T> specification, Sort sort) {
        return this.findAll(specification, sort);
    }

}