package cn.com.connext.msf.webapp.webapi;

import cn.com.connext.msf.webapp.annotation.ApiAuthority;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * LoReal 等项目，会在公网对内部服务发起请求，以检测服务是否健康。
 */
@Api(description = "服务健康检查接口")
public class ServerHealthApi {

    @GetMapping("/api/${msf.application.name}/health")
    @ApiOperation("服务状态")
    @ApiAuthority(noAuth = true)
    public String ok() {
        return "OK";
    }
}
