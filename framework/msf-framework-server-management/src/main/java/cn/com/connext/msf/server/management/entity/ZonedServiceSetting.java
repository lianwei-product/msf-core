package cn.com.connext.msf.server.management.entity;

import cn.com.connext.msf.framework.utils.Base58UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Optional;

/**
 * 对象说明：服务可用区配置
 * 开发人员：程瀚
 * 摘要说明：
 * 修订日期：2020-06-16 15:24:39
 */
@Document(collection = "management_zoned_service_setting")
public class ZonedServiceSetting {

    @Id
    @ApiModelProperty(value = "唯一标识", readOnly = true, example = "auto")
    private String id;

    @Indexed(unique = true)
    @ApiModelProperty(value = "服务名称", required = true)
    private String name;

    @ApiModelProperty(value = "服务别名", required = true)
    private String aliasName;

    @ApiModelProperty(value = "服务描述", required = true)
    private String description;

    @ApiModelProperty(value = "可用区模板", required = true)
    private List<ZonedServiceTemplate> templates;

    private ZonedServiceSetting() {
        id = Base58UUID.newBase58UUID();
        templates = Lists.newArrayList();
    }

    public static ZonedServiceSetting from(String id, String name, List<ZonedServiceTemplate> templateList) {
        ZonedServiceSetting zonedServiceSetting = new ZonedServiceSetting();
        zonedServiceSetting.id = id;
        zonedServiceSetting.name = name;
        zonedServiceSetting.templates = templateList;
        return zonedServiceSetting;
    }

    @JsonIgnore
    public int getMinInstanceCount() {
        return Optional.ofNullable(templates)
                .map(t -> t.stream().mapToInt(ZonedServiceTemplate::getMinCount).sum())
                .orElse(0);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public String getDescription() {
        return description;
    }

    public List<ZonedServiceTemplate> getTemplates() {
        return templates;
    }
}
