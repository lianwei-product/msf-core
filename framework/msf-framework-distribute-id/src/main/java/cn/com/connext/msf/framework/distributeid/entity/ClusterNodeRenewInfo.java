package cn.com.connext.msf.framework.distributeid.entity;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "cluster_node_renewal_info")
public class ClusterNodeRenewInfo {

    @Id
    private int name;

    private String owner;

    @Indexed
    private Date expires;

    public static ClusterNodeRenewInfo from(int name, String owner, int expiresTimeSpan) {
        return from(name, owner, DateUtils.addSeconds(new Date(), expiresTimeSpan));
    }

    public static ClusterNodeRenewInfo from(int name, String owner, Date expires) {
        ClusterNodeRenewInfo renewalInfo = new ClusterNodeRenewInfo();
        renewalInfo.name = name;
        renewalInfo.owner = owner;
        renewalInfo.expires = expires;
        return renewalInfo;
    }

    public int getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public Date getExpires() {
        return expires;
    }
}
