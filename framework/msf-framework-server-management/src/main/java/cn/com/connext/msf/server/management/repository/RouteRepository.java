package cn.com.connext.msf.server.management.repository;

import cn.com.connext.msf.data.mongo.ConnextMongoRepository;
import cn.com.connext.msf.server.management.entity.Route;
import org.springframework.stereotype.Repository;

/**
 * 网关路由 - 数据访问接口定义
 * 开发人员: 程瀚
 * 修订日期: 2019-06-02 21:10:58
 */
@Repository
public interface RouteRepository extends ConnextMongoRepository<Route, String> {

    /**
     * 该方法用于检测“网关路由”中“路由路径”属性的值，是否已经被其它记录所使用。
     *
     * @param path 路由路径
     * @param id   唯一标识
     * @return 返回与指定“路由路径”相同的记录总数。
     */
    long countByPathAndIdNot(String path, String id);

}

