package cn.com.connext.msf.server.management.service;

import cn.com.connext.msf.framework.query.QueryInfo;
import cn.com.connext.msf.server.management.entity.RouteApiMethod;
import cn.com.connext.msf.server.management.mq.GatewayRouteUpdatePublisher;
import cn.com.connext.msf.server.management.repository.RouteApiMethodRepository;
import cn.com.connext.msf.server.management.validator.RouteApiMethodValidator;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 网关路由接口方法 - 业务服务对象
 * 开发人员: 程瀚
 * 修订日期: 2019-06-03 14:10:36
 */
@Service
public class RouteApiMethodService {

    private final RouteApiMethodValidator routeApiMethodValidator;
    private final RouteApiMethodRepository routeApiMethodRepository;
    private final GatewayRouteUpdatePublisher gatewayRouteUpdatePublisher;

    /**
     * 默认实例化方法。
     *
     * @param routeApiMethodValidator  实体验证器
     * @param routeApiMethodRepository 实体持久层
     */
    public RouteApiMethodService(
            RouteApiMethodValidator routeApiMethodValidator,
            RouteApiMethodRepository routeApiMethodRepository,
            GatewayRouteUpdatePublisher gatewayRouteUpdatePublisher) {
        this.routeApiMethodValidator = routeApiMethodValidator;
        this.routeApiMethodRepository = routeApiMethodRepository;
        this.gatewayRouteUpdatePublisher = gatewayRouteUpdatePublisher;
    }

    /**
     * 创建新的网关路由接口方法。
     *
     * @param routeApiMethod 所要创建的网关路由接口方法对象实例
     * @return 返回所创建的对象实例
     */
    public RouteApiMethod create(RouteApiMethod routeApiMethod) {
        routeApiMethodValidator.createValidate(routeApiMethod);
        routeApiMethodRepository.create(routeApiMethod);
        gatewayRouteUpdatePublisher.send(routeApiMethod.getRouteId());
        return routeApiMethod;
    }

    /**
     * 修改已有网关路由接口方法。
     *
     * @param routeApiMethod 所要修改的网关路由接口方法对象实例
     * @return 返回修改后对象实例
     */
    public RouteApiMethod modify(RouteApiMethod routeApiMethod) {
        routeApiMethodValidator.modifyValidate(routeApiMethod);
        routeApiMethodRepository.modify(routeApiMethod);
        gatewayRouteUpdatePublisher.send(routeApiMethod.getRouteId());
        return routeApiMethod;
    }

    /**
     * 根据主键删除网关路由接口方法。
     *
     * @param id 主键 - 唯一标识
     */
    public void delete(String id) {
        RouteApiMethod routeApiMethod = routeApiMethodValidator.deleteValidate(id);
        routeApiMethodRepository.delete(id);
        gatewayRouteUpdatePublisher.send(routeApiMethod.getRouteId());
    }

    /**
     * 根据主键查找网关路由接口方法。
     *
     * @param id 主键 - 唯一标识
     * @return 返回所匹配的网关路由接口方法对象实例
     */
    public RouteApiMethod findItem(String id) {
        return routeApiMethodRepository.findOne(id);
    }

    /**
     * 根据查询条件，分页查询匹配的记录。
     *
     * @param queryInfo 查询条件
     * @return 返回数据分页信息
     */
    public Page<RouteApiMethod> findPage(QueryInfo queryInfo) {
        return routeApiMethodRepository.findPage(queryInfo);
    }

    /**
     * 根据查询条件，查询并返回所有记录列表信息。
     *
     * @param queryInfo 查询条件
     * @return 返回数据集合信息
     */
    public List<RouteApiMethod> findList(QueryInfo queryInfo) {
        return routeApiMethodRepository.findList(queryInfo);
    }

}

