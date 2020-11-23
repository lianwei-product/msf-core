package cn.com.connext.msf.server.management.validator;

import cn.com.connext.msf.framework.exception.BusinessException;
import cn.com.connext.msf.framework.utils.Validator;
import cn.com.connext.msf.server.management.entity.Route;
import cn.com.connext.msf.server.management.repository.RouteRepository;
import org.springframework.stereotype.Component;


/**
 * 网关路由 - 实体验证定义
 * 开发人员: 程瀚
 * 修订日期: 2019-06-02 21:10:58
 */
@Component
public class RouteValidator {

    private final RouteRepository routeRepository;

    /**
     * 默认实例化方法。
     *
     * @param routeRepository 实体持久层
     */
    public RouteValidator(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    /**
     * 验证所要创建的“网关路由”对象实例，是否符合数据约定。
     *
     * @param route “网关路由”对象实例
     */
    public void createValidate(Route route) {
        if (routeRepository.exists(route.getId()))
            throw new BusinessException("所要创建的“网关路由”已经存在。");

        entityValidate(route);
    }

    /**
     * 验证所要修改的“网关路由”对象实例，是否符合数据约定。
     *
     * @param route “网关路由”对象实例
     */
    public void modifyValidate(Route route) {
        if (!routeRepository.exists(route.getId()))
            throw new BusinessException("所要修改的“网关路由”不存在或已删除。");

        entityValidate(route);
    }

    /**
     * 验证所要删除的“网关路由”，是否符合数据约定。
     *
     * @param id “网关路由”对象的唯一标识
     */
    public void deleteValidate(String id) {
        if (!routeRepository.exists(id))
            throw new BusinessException("所要删除的“网关路由”不存在或已删除。");
    }

    /**
     * 验证所传入的“网关路由”对象实例，是否符合数据约定。
     *
     * @param route “网关路由”对象实例
     */
    public void entityValidate(Route route) {

        // 检测路由名称是否为空
        if (Validator.isEmpty(route.getName()))
            throw new BusinessException("“路由名称”不能为空。");

        // 检测路由路径是否为空
        if (Validator.isEmpty(route.getPath()))
            throw new BusinessException("“路由路径”不能为空。");

        // 检测路由类型是否为空
        if (Validator.isEmpty(route.getType()))
            throw new BusinessException("“路由类型”不能为空。");

        // 检测更新时间是否为空
        if (Validator.isEmpty(route.getCreateTime()))
            throw new BusinessException("“更新时间”不能为空。");

        // 检测更新时间是否为空
        if (Validator.isEmpty(route.getUpdateTime()))
            throw new BusinessException("“更新时间”不能为空。");

        // 检测路由名称长度
        if (Validator.isOutLimit(route.getName(), 200))
            throw new BusinessException("“路由名称”最大不能超过200个字符。");

        // 检测路由路径长度
        if (Validator.isOutLimit(route.getPath(), 500))
            throw new BusinessException("“路由路径”最大不能超过500个字符。");

        // 检测路由类型长度
        if (Validator.isOutLimit(route.getType(), 50))
            throw new BusinessException("“路由类型”最大不能超过50个字符。");

        // 检测目标服务长度
        if (Validator.isOutLimit(route.getTargetService(), 200))
            throw new BusinessException("“目标服务”最大不能超过200个字符。");

        // 检测目标地址长度
        if (Validator.isOutLimit(route.getTargetUrl(), 2000))
            throw new BusinessException("“目标地址”最大不能超过2000个字符。");

        // 检测路由路径是否已经存在
        if (routeRepository.countByPathAndIdNot(route.getPath(), route.getId()) > 0)
            throw new BusinessException("“路由路径”不可以重复，系统中已存在相同的记录。");

    }

}

