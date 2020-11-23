package cn.com.connext.msf.microservice.utils;

import cn.com.connext.msf.webapp.annotation.ApiAuthority;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.reflect.Method;
import java.util.Objects;

@Api(description = "用户管理接口")
class UserApi {
    public static UserApi getInstance() {
        return new UserApi();
    }

    public static Method getMethod() {
        Method[] methods = UserApi.class.getDeclaredMethods();
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
