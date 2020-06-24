package cn.com.connext.msf.framework.auth;

import java.util.Date;
import java.util.List;

public interface ApplicationLicenseBase {

    String getId();

    String getApplicationCode();

    boolean isAllowOnlineAward();

    boolean isAllowOfflineGrant();

    String getName();

    List<AuthorityGroup> getAuthorityGroups();

    List<AuthorityDetail> getAuthorityDetails();

    String getDescription();

    Date getCreatedTime();

}
