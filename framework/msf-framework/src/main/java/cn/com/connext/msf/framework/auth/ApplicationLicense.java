package cn.com.connext.msf.framework.auth;

import java.util.Date;
import java.util.List;

public class ApplicationLicense implements ApplicationLicenseBase {


    /**
     * 唯一标识
     */
    private String id;

    /**
     * 应用编码
     */
    private String applicationCode;

    /**
     * 名称
     */
    private String name;


    /**
     * 允许SaaS版本在线授权
     */
    private boolean allowOnlineAward;

    /**
     * 允许私有版本离线授权
     */
    private boolean allowOfflineGrant;

    /**
     * 功能授权
     */
    private List<AuthorityGroup> authorityGroups;

    /**
     * 授权范围
     */
    private List<AuthorityDetail> authorityDetails;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 描述
     */
    private String description;

    public static ApplicationLicense from(String id,
                                          String applicationCode,
                                          String name,
                                          boolean allowOnlineAward,
                                          boolean allowOfflineGrant,
                                          List<AuthorityGroup> authorityGroups,
                                          List<AuthorityDetail> authorityDetails,
                                          String description,
                                          Date createdTime) {
        ApplicationLicense applicationLicense = new ApplicationLicense();
        applicationLicense.id = id;
        applicationLicense.applicationCode = applicationCode;
        applicationLicense.name = name;
        applicationLicense.allowOnlineAward = allowOnlineAward;
        applicationLicense.allowOfflineGrant = allowOfflineGrant;
        applicationLicense.authorityGroups = authorityGroups;
        applicationLicense.authorityDetails = authorityDetails;
        applicationLicense.description = description;
        applicationLicense.createdTime = createdTime;
        return applicationLicense;
    }

    public String getId() {
        return id;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public String getName() {
        return name;
    }

    public boolean isAllowOnlineAward() {
        return allowOnlineAward;
    }

    public boolean isAllowOfflineGrant() {
        return allowOfflineGrant;
    }

    public List<AuthorityGroup> getAuthorityGroups() {
        return authorityGroups;
    }

    public List<AuthorityDetail> getAuthorityDetails() {
        return authorityDetails;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public Date getCreatedTime() {
        return createdTime;
    }
}
