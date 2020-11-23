package cn.com.connext.msf.server.management.service;

import cn.com.connext.msf.framework.query.QueryInfo;
import cn.com.connext.msf.server.management.entity.RouteApi;
import cn.com.connext.msf.server.management.mq.GatewayRouteUpdatePublisher;
import cn.com.connext.msf.server.management.repository.RouteApiMethodRepository;
import cn.com.connext.msf.server.management.repository.RouteApiRepository;
import cn.com.connext.msf.server.management.validator.RouteApiValidator;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 网关路由接口 - 业务服务对象
 * 开发人员: 程瀚
 * 修订日期: 2019-06-03 14:10:36
 */
@Service
public class RouteApiService {

    private final RouteApiValidator routeApiValidator;
    private final RouteApiRepository routeApiRepository;
    private final RouteApiMethodRepository routeApiMethodRepository;
    private final GatewayRouteUpdatePublisher gatewayRouteUpdatePublisher;

    /**
     * 默认实例化方法。
     *
     * @param routeApiValidator  实体验证器
     * @param routeApiRepository 实体持久层
     */
    public RouteApiService(
            RouteApiValidator routeApiValidator,
            RouteApiRepository routeApiRepository,
            RouteApiMethodRepository routeApiMethodRepository,
            GatewayRouteUpdatePublisher gatewayRouteUpdatePublisher) {
        this.routeApiValidator = routeApiValidator;
        this.routeApiRepository = routeApiRepository;
        this.routeApiMethodRepository = routeApiMethodRepository;
        this.gatewayRouteUpdatePublisher = gatewayRouteUpdatePublisher;
    }

    /**
     * 创建新的网关路由接口。
     *
     * @param routeApi 所要创建的网关路由接口对象实例
     * @return 返回所创建的对象实例
     */
    public RouteApi create(RouteApi routeApi) {
        routeApiValidator.createValidate(routeApi);
        return routeApiRepository.create(routeApi);
    }

    /**
     * 修改已有网关路由接口。
     *
     * @param routeApi 所要修改的网关路由接口对象实例
     * @return 返回修改后对象实例
     */
    public RouteApi modify(RouteApi routeApi) {
        routeApiValidator.modifyValidate(routeApi);
        return routeApiRepository.modify(routeApi);
    }

    /**
     * 根据主键删除网关路由接口。
     *
     * @param id 主键 - 唯一标识
     */
    public void delete(String id) {
        RouteApi routeApi = routeApiValidator.deleteValidate(id);
        routeApiRepository.delete(id);

        QueryInfo queryInfo = QueryInfo.from("routeApiId eq {0}", routeApi.getId());
        routeApiMethodRepository.delete(queryInfo);
        gatewayRouteUpdatePublisher.send(id);
    }

    /**
     * 根据主键查找网关路由接口。
     *
     * @param id 主键 - 唯一标识
     * @return 返回所匹配的网关路由接口对象实例
     */
    public RouteApi findItem(String id) {
        return routeApiRepository.findOne(id);
    }

    /**
     * 根据查询条件，分页查询匹配的记录。
     *
     * @param queryInfo 查询条件
     * @return 返回数据分页信息
     */
    public Page<RouteApi> findPage(QueryInfo queryInfo) {
        return routeApiRepository.findPage(queryInfo);
    }

    /**
     * 根据查询条件，查询并返回所有记录列表信息。
     *
     * @param queryInfo 查询条件
     * @return 返回数据集合信息
     */
    public List<RouteApi> findList(QueryInfo queryInfo) {
        return routeApiRepository.findList(queryInfo);
    }

}

