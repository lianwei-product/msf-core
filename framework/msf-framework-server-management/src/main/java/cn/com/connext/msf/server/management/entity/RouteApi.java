package cn.com.connext.msf.server.management.entity;

import cn.com.connext.msf.framework.utils.Base58UUID;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 网关路由接口 - 实体定义
 * 开发人员: 程瀚
 * 修订日期: 2019-06-03 14:10:36
 */
@Document(collection = "management_route_api")
public class RouteApi {

    @Id
    @ApiModelProperty(value = "唯一标识", readOnly = true, example = "auto")
    private String id;

    @Indexed
    @ApiModelProperty(value = "所属路由", required = true)
    private String routeId;

    @ApiModelProperty(value = "接口名称", required = true)
    private String name;

    @ApiModelProperty(value = "接口描述", required = true)
    private String description;

    /**
     * 默认实例化方法
     */
    public RouteApi() {
        id = Base58UUID.newBase58UUID();
    }

    //region getter & setter

    /**
     * 获取唯一标识。
     */
    public String getId() {
        return id;
    }

    /**
     * 设置唯一标识。
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取所属路由。
     */
    public String getRouteId() {
        return routeId;
    }

    /**
     * 设置所属路由。
     */
    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    /**
     * 获取接口名称。
     */
    public String getName() {
        return name;
    }

    /**
     * 设置接口名称。
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取接口描述。
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置接口描述。
     */
    public void setDescription(String description) {
        this.description = description;
    }

    //endregion
}

