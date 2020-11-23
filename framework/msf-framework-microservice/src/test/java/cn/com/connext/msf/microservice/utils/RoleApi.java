package cn.com.connext.msf.microservice.utils;

import cn.com.connext.msf.webapp.annotation.ApiAuthority;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.reflect.Method;
import java.util.Objects;

@Api(description = "角色管理接口")
class RoleApi {

    public static RoleApi getInstance() {
        return new RoleApi();
    }

    public static Method getMethod() {
        Method[] methods = RoleApi.class.getDeclaredMethods();
        for (Method method : methods) {
            if (Objects.equals("create", method.getName())) {
                return method;
            }
        }
        return null;
    }

    @PostMapping
    @ApiOperation("创建信息")
    @ApiAuthority
    public void create() {

    }
}
