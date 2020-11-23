package cn.com.connext.msf.server.management.service;

import cn.com.connext.msf.framework.query.QueryInfo;
import cn.com.connext.msf.server.management.entity.Route;
import cn.com.connext.msf.server.management.entity.RouteApi;
import cn.com.connext.msf.server.management.entity.RouteApiMethod;
import cn.com.connext.msf.server.management.mq.GatewayRouteUpdatePublisher;
import cn.com.connext.msf.server.management.repository.RouteApiMethodRepository;
import cn.com.connext.msf.server.management.repository.RouteApiRepository;
import cn.com.connext.msf.server.management.repository.RouteRepository;
import cn.com.connext.msf.server.management.validator.RouteValidator;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 网关路由 - 业务服务对象
 * 开发人员: 程瀚
 * 修订日期: 2019-06-02 21:10:58
 */
@Service
public class RouteService {

    private final RouteValidator routeValidator;
    private final RouteRepository routeRepository;
    private final RouteApiRepository routeApiRepository;
    private final RouteApiMethodRepository routeApiMethodRepository;
    private final GatewayRouteUpdatePublisher gatewayRouteUpdatePublisher;

    /**
     * 默认实例化方法。
     *
     * @param routeValidator  实体验证器
     * @param routeRepository 实体持久层
     */
    public RouteService(
            RouteValidator routeValidator,
            RouteRepository routeRepository,
            RouteApiRepository routeApiRepository,
            RouteApiMethodRepository routeApiMethodRepository,
            GatewayRouteUpdatePublisher gatewayRouteUpdatePublisher) {
        this.routeValidator = routeValidator;
        this.routeRepository = routeRepository;
        this.routeApiRepository = routeApiRepository;
        this.routeApiMethodRepository = routeApiMethodRepository;
        this.gatewayRouteUpdatePublisher = gatewayRouteUpdatePublisher;
    }

    /**
     * 创建新的网关路由。
     *
     * @param route 所要创建的网关路由对象实例
     * @return 返回所创建的对象实例
     */
    public Route create(Route route) {
        routeValidator.createValidate(route);
        routeRepository.create(route);

        RouteApi routeApi = new RouteApi();
        routeApi.setRouteId(route.getId());
        routeApi.setName("DefaultApi");
        routeApi.setDescription("默认接口");
        routeApiRepository.create(routeApi);

        RouteApiMethod routeApiMethod = new RouteApiMethod();
        routeApiMethod.setRouteId(route.getId());
        routeApiMethod.setRouteApiId(routeApi.getId());
        routeApiMethod.setName("DefaultMethod");
        routeApiMethod.setDescription("默认方法");
        routeApiMethod.setHttpMethod("*");
        routeApiMethod.setNoAuth(false);
        routeApiMethod.setUri(route.getPath());
        routeApiMethod.setAuthority("");
        routeApiMethodRepository.create(routeApiMethod);

        gatewayRouteUpdatePublisher.send(route.getId());
        return route;
    }

    /**
     * 修改已有网关路由。
     *
     * @param route 所要修改的网关路由对象实例
     * @return 返回修改后对象实例
     */
    public Route modify(Route route) {
        routeValidator.modifyValidate(route);
        routeRepository.modify(route);
        gatewayRouteUpdatePublisher.send(route.getId());
        return route;
    }

    /**
     * 根据主键删除网关路由。
     *
     * @param id 主键 - 唯一标识
     */
    public void delete(String id) {
        routeValidator.deleteValidate(id);
        routeRepository.delete(id);

        QueryInfo queryInfo = QueryInfo.from("routeId eq {0}", id);
        routeApiRepository.delete(queryInfo);
        routeApiMethodRepository.delete(queryInfo);
        gatewayRouteUpdatePublisher.send(id);
    }

    /**
     * 根据主键查找网关路由。
     *
     * @param id 主键 - 唯一标识
     * @return 返回所匹配的网关路由对象实例
     */
    public Route findItem(String id) {
        return routeRepository.findOne(id);
    }

    /**
     * 根据查询条件，分页查询匹配的记录。
     *
     * @param queryInfo 查询条件
     * @return 返回数据分页信息
     */
    public Page<Route> findPage(QueryInfo queryInfo) {
        return routeRepository.findPage(queryInfo);
    }

    /**
     * 根据查询条件，查询并返回所有记录列表信息。
     *
     * @param queryInfo 查询条件
     * @return 返回数据集合信息
     */
    public List<Route> findList(QueryInfo queryInfo) {
        return routeRepository.findList(queryInfo);
    }

}

