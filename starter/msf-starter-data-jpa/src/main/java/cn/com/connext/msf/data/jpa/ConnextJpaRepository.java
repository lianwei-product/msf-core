package cn.com.connext.msf.data.jpa;

import cn.com.connext.msf.framework.repository.ConnextRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * 对象名称：Connext 关系数据库 访问接口
 * 摘要信息：封装关系数据库常用数据访问方法，继承自JpaRepository和JpaSpecificationExecutor。
 * 开发人员: 程瀚
 * 修订日期: 2018-09-04 13:06:01
 */
@NoRepositoryBean
public interface ConnextJpaRepository<T, ID extends Serializable>
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>, ConnextRepository<T, ID> {

    Page<T> findPage(Specification<T> specification, Pageable pageable);

    List<T> findList(Specification<T> specification);

    List<T> findList(Specification<T> specification, Sort sort);
}
