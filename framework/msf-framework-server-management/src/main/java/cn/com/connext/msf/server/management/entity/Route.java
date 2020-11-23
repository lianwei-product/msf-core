package cn.com.connext.msf.server.management.entity;

import cn.com.connext.msf.framework.annotation.QueryVerb;
import cn.com.connext.msf.framework.utils.Base58UUID;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 网关路由 - 实体定义
 * 开发人员: 程瀚
 * 修订日期: 2019-06-02 21:10:58
 */
@Document(collection = "management_route")
public class Route {

    @Id
    @ApiModelProperty(value = "唯一标识", readOnly = true, example = "auto")
    private String id;

    @ApiModelProperty(value = "路由名称", required = true)
    private String name;

    @Indexed(unique = true)
    @ApiModelProperty(value = "路由路径", required = true)
    @QueryVerb(operator = "eq")
    private String path;

    @ApiModelProperty(value = "路由类型", required = true)
    @QueryVerb(operator = "eq")
    private String type;

    @ApiModelProperty(value = "目标服务", required = false)
    @QueryVerb(operator = "eq")
    private String targetService;

    @ApiModelProperty(value = "目标地址", required = false)
    @QueryVerb(operator = "eq")
    private String targetUrl;

    @ApiModelProperty(value = "自动重试", required = true)
    @QueryVerb(operator = "eq", allowSort = true)
    private boolean retryAble;

    @ApiModelProperty(value = "是否启用", required = true)
    @QueryVerb(operator = "eq", allowSort = true)
    private boolean enable;

    @ApiModelProperty(value = "创建时间", required = true)
    @QueryVerb(operator = "gt,lt", allowSort = true)
    private Date createTime;

    @ApiModelProperty(value = "更新时间", required = true)
    @QueryVerb(operator = "gt,lt", allowSort = true)
    private Date updateTime;

    /**
     * 默认实例化方法
     */
    public Route() {
        id = Base58UUID.newBase58UUID();
        retryAble = true;
        enable = true;
        createTime = new Date();
        updateTime = new Date();
    }

    //region getter & setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTargetService() {
        return targetService;
    }

    public void setTargetService(String targetService) {
        this.targetService = targetService;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public boolean isRetryAble() {
        return retryAble;
    }

    public void setRetryAble(boolean retryAble) {
        this.retryAble = retryAble;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    //endregion
}

