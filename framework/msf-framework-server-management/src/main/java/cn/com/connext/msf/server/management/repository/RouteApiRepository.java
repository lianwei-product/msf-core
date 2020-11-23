package cn.com.connext.msf.server.management.repository;

import cn.com.connext.msf.data.mongo.ConnextMongoRepository;
import cn.com.connext.msf.server.management.entity.RouteApi;
import org.springframework.stereotype.Repository;

/**
 * 网关路由接口 - 数据访问接口定义
 * 开发人员: 程瀚
 * 修订日期: 2019-06-03 14:10:36
 */
@Repository
public interface RouteApiRepository extends ConnextMongoRepository<RouteApi, String> {

}

