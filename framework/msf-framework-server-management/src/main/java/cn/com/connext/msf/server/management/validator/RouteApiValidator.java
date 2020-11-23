package cn.com.connext.msf.server.management.validator;

import cn.com.connext.msf.framework.exception.BusinessException;
import cn.com.connext.msf.framework.utils.Validator;
import cn.com.connext.msf.server.management.entity.RouteApi;
import cn.com.connext.msf.server.management.repository.RouteApiRepository;
import org.springframework.stereotype.Component;


/**
 * 网关路由接口 - 实体验证定义
 * 开发人员: 程瀚
 * 修订日期: 2019-06-03 14:10:36
 */
@Component
public class RouteApiValidator {

    private final RouteApiRepository routeApiRepository;

    /**
     * 默认实例化方法。
     *
     * @param routeApiRepository 实体持久层
     */
    public RouteApiValidator(RouteApiRepository routeApiRepository) {
        this.routeApiRepository = routeApiRepository;
    }

    /**
     * 验证所要创建的“网关路由接口”对象实例，是否符合数据约定。
     *
     * @param routeApi “网关路由接口”对象实例
     */
    public void createValidate(RouteApi routeApi) {
        if (routeApiRepository.exists(routeApi.getId()))
            throw new BusinessException("所要创建的“网关路由接口”已经存在。");

        entityValidate(routeApi);
    }

    /**
     * 验证所要修改的“网关路由接口”对象实例，是否符合数据约定。
     *
     * @param routeApi “网关路由接口”对象实例
     */
    public void modifyValidate(RouteApi routeApi) {
        if (!routeApiRepository.exists(routeApi.getId()))
            throw new BusinessException("所要修改的“网关路由接口”不存在或已删除。");

        entityValidate(routeApi);
    }

    /**
     * 验证所要删除的“网关路由接口”，是否符合数据约定。
     *
     * @param id “网关路由接口”对象的唯一标识
     */
    public RouteApi deleteValidate(String id) {
        RouteApi routeApi = routeApiRepository.findItem(id);
        if (routeApi == null)
            throw new BusinessException("所要删除的“网关路由接口”不存在或已删除。");
        return routeApi;
    }

    /**
     * 验证所传入的“网关路由接口”对象实例，是否符合数据约定。
     *
     * @param routeApi “网关路由接口”对象实例
     */
    public void entityValidate(RouteApi routeApi) {

        // 检测所属路由是否为空
        if (Validator.isEmpty(routeApi.getRouteId()))
            throw new BusinessException("“所属路由”不能为空。");

        // 检测接口名称是否为空
        if (Validator.isEmpty(routeApi.getName()))
            throw new BusinessException("“接口名称”不能为空。");

        // 检测接口描述是否为空
        if (Validator.isEmpty(routeApi.getDescription()))
            throw new BusinessException("“接口描述”不能为空。");

        // 检测所属路由长度
        if (Validator.isOutLimit(routeApi.getRouteId(), 22))
            throw new BusinessException("“所属路由”最大不能超过22个字符。");

        // 检测接口名称长度
        if (Validator.isOutLimit(routeApi.getName(), 200))
            throw new BusinessException("“接口名称”最大不能超过200个字符。");

        // 检测接口描述长度
        if (Validator.isOutLimit(routeApi.getDescription(), 500))
            throw new BusinessException("“接口描述”最大不能超过500个字符。");

    }

}

