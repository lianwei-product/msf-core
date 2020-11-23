package cn.com.connext.msf.server.management.entity;

import cn.com.connext.msf.framework.utils.Base58UUID;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 网关路由接口方法 - 实体定义
 * 开发人员: 程瀚
 * 修订日期: 2019-06-03 14:10:36
 */
@Document(collection = "management_route_api_method")
public class RouteApiMethod {

    @Id
    @ApiModelProperty(value = "唯一标识", readOnly = true, example = "auto")
    private String id;

    @Indexed
    @ApiModelProperty(value = "所属路由", required = true)
    private String routeId;

    @Indexed
    @ApiModelProperty(value = "所属接口", required = true)
    private String routeApiId;

    @ApiModelProperty(value = "方法名称", required = true)
    private String name;

    @ApiModelProperty(value = "方法描述", required = true)
    private String description;

    @ApiModelProperty(value = "请求方法", required = true)
    private String httpMethod;

    @ApiModelProperty(value = "请求地址", required = true)
    private String uri;

    @ApiModelProperty(value = "无需鉴权", required = true)
    private boolean noAuth;

    @ApiModelProperty(value = "所需权限", required = true)
    private String authority;

    @ApiModelProperty(value = "配置信息", required = true)
    private RouteConfig config;

    /**
     * 默认实例化方法
     */
    public RouteApiMethod() {
        id = Base58UUID.newBase58UUID();
        config = new RouteConfig();
    }

    //region getter & setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteApiId() {
        return routeApiId;
    }

    public void setRouteApiId(String routeApiId) {
        this.routeApiId = routeApiId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isNoAuth() {
        return noAuth;
    }

    public void setNoAuth(boolean noAuth) {
        this.noAuth = noAuth;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public RouteConfig getConfig() {
        return config;
    }

    public void setConfig(RouteConfig config) {
        this.config = config;
    }

    public String getUrlHttpMethod()
    {
        return uri+httpMethod;
    }
    //endregion
}

