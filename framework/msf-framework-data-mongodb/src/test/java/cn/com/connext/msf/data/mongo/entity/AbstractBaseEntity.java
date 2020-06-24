package cn.com.connext.msf.data.mongo.entity;

import java.util.Date;

public abstract class AbstractBaseEntity {

    private String createBy;
    private Date createDate;

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
