package cn.com.connext.msf.server.management.validator;

import cn.com.connext.msf.framework.exception.BusinessException;
import cn.com.connext.msf.framework.utils.Validator;
import cn.com.connext.msf.server.management.entity.RouteApiMethod;
import cn.com.connext.msf.server.management.repository.RouteApiMethodRepository;
import org.springframework.stereotype.Component;


/**
 * 网关路由接口方法 - 实体验证定义
 * 开发人员: 程瀚
 * 修订日期: 2019-06-03 14:10:36
 */
@Component
public class RouteApiMethodValidator {

    private final RouteApiMethodRepository routeApiMethodRepository;

    /**
     * 默认实例化方法。
     *
     * @param routeApiMethodRepository 实体持久层
     */
    public RouteApiMethodValidator(RouteApiMethodRepository routeApiMethodRepository) {
        this.routeApiMethodRepository = routeApiMethodRepository;
    }

    /**
     * 验证所要创建的“网关路由接口方法”对象实例，是否符合数据约定。
     *
     * @param routeApiMethod “网关路由接口方法”对象实例
     */
    public void createValidate(RouteApiMethod routeApiMethod) {
        if (routeApiMethodRepository.exists(routeApiMethod.getId()))
            throw new BusinessException("所要创建的“网关路由接口方法”已经存在。");

        entityValidate(routeApiMethod);
    }

    /**
     * 验证所要修改的“网关路由接口方法”对象实例，是否符合数据约定。
     *
     * @param routeApiMethod “网关路由接口方法”对象实例
     */
    public void modifyValidate(RouteApiMethod routeApiMethod) {
        if (!routeApiMethodRepository.exists(routeApiMethod.getId()))
            throw new BusinessException("所要修改的“网关路由接口方法”不存在或已删除。");

        entityValidate(routeApiMethod);
    }

    /**
     * 验证所要删除的“网关路由接口方法”，是否符合数据约定。
     *
     * @param id “网关路由接口方法”对象的唯一标识
     */
    public RouteApiMethod deleteValidate(String id) {
        RouteApiMethod routeApiMethod = routeApiMethodRepository.findItem(id);
        if (routeApiMethod == null)
            throw new BusinessException("所要删除的“网关路由接口方法”不存在或已删除。");
        return routeApiMethod;
    }

    /**
     * 验证所传入的“网关路由接口方法”对象实例，是否符合数据约定。
     *
     * @param routeApiMethod “网关路由接口方法”对象实例
     */
    public void entityValidate(RouteApiMethod routeApiMethod) {

        // 检测所属路由是否为空
        if (Validator.isEmpty(routeApiMethod.getRouteId()))
            throw new BusinessException("“所属路由”不能为空。");

        // 检测所属接口是否为空
        if (Validator.isEmpty(routeApiMethod.getRouteApiId()))
            throw new BusinessException("“所属接口”不能为空。");

        // 检测方法名称是否为空
        if (Validator.isEmpty(routeApiMethod.getName()))
            throw new BusinessException("“方法名称”不能为空。");

        // 检测方法描述是否为空
        if (Validator.isEmpty(routeApiMethod.getDescription()))
            throw new BusinessException("“方法描述”不能为空。");

        // 检测请求方法是否为空
        if (Validator.isEmpty(routeApiMethod.getHttpMethod()))
            throw new BusinessException("“请求方法”不能为空。");

        // 检测请求地址是否为空
        if (Validator.isEmpty(routeApiMethod.getUri()))
            throw new BusinessException("“请求地址”不能为空。");

        // 检测所属路由长度
        if (Validator.isOutLimit(routeApiMethod.getRouteId(), 22))
            throw new BusinessException("“所属路由”最大不能超过22个字符。");

        // 检测所属接口长度
        if (Validator.isOutLimit(routeApiMethod.getRouteApiId(), 22))
            throw new BusinessException("“所属接口”最大不能超过22个字符。");

        // 检测方法名称长度
        if (Validator.isOutLimit(routeApiMethod.getName(), 200))
            throw new BusinessException("“方法名称”最大不能超过200个字符。");

        // 检测方法描述长度
        if (Validator.isOutLimit(routeApiMethod.getDescription(), 500))
            throw new BusinessException("“方法描述”最大不能超过500个字符。");

        // 检测请求方法长度
        if (Validator.isOutLimit(routeApiMethod.getHttpMethod(), 200))
            throw new BusinessException("“请求方法”最大不能超过10个字符。");

        // 检测请求地址长度
        if (Validator.isOutLimit(routeApiMethod.getUri(), 200))
            throw new BusinessException("“请求地址”最大不能超过10个字符。");

        // 检测所需权限长度
        if (Validator.isOutLimit(routeApiMethod.getAuthority(), 200))
            throw new BusinessException("“所需权限”最大不能超过200个字符。");

    }

}

