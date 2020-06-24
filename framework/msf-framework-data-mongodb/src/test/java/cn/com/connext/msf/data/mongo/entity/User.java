package cn.com.connext.msf.data.mongo.entity;

import cn.com.connext.msf.framework.annotation.Nesting;
import cn.com.connext.msf.framework.entity.AbstractAuditingEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "sys_user")
public class User extends AbstractAuditingEntity {
    @Id
    private String id;

    private String name;

    @Nesting
    private Org org;

    @Nesting
    private List<Address> addressList;

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

    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
}
