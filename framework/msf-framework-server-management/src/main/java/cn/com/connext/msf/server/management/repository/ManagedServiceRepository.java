package cn.com.connext.msf.server.management.repository;

import cn.com.connext.msf.data.mongo.ConnextMongoRepository;
import cn.com.connext.msf.server.management.entity.ManagedService;
import org.springframework.stereotype.Repository;

/**
 * 托管服务 - 数据访问接口定义
 * 开发人员: 程瀚
 * 修订日期: 2019-05-30 22:36:20
 */
@Repository
public interface ManagedServiceRepository extends ConnextMongoRepository<ManagedService, String> {

    /**
     * 该方法用于检测“托管服务”中“服务名称”属性的值，是否已经被其它记录所使用。
     *
     * @param name 服务名称
     * @param id   唯一标识
     * @return 返回与指定“服务名称”相同的记录总数。
     */
    long countByNameAndIdNot(String name, String id);

}

