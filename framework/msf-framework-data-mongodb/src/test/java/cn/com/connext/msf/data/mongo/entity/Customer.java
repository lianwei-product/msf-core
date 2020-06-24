package cn.com.connext.msf.data.mongo.entity;

import cn.com.connext.msf.framework.utils.Base58UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "customer")
public class Customer {
    @Id
    private String id;
    private String realName;
    private String mobile;
    private Address address;
    private Date createdTime;

    public Customer() {
        id = Base58UUID.newBase58UUID();
        realName = "Han";
        mobile = "13770655999";
        address = new Address();
        createdTime = new Date();
    }

    public String getId() {
        return id;
    }

    public String getRealName() {
        return realName;
    }

    public String getMobile() {
        return mobile;
    }

    public Address getAddress() {
        return address;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public static class Address {
        private String aliasName;
        private String province;
        private String city;
        private String detail;

        public Address() {
            aliasName = "Home";
            province = "JiangSu";
            city = "NanJing";
            detail = "1865";
        }

        public String getAliasName() {
            return aliasName;
        }

        public String getProvince() {
            return province;
        }

        public String getCity() {
            return city;
        }

        public String getDetail() {
            return detail;
        }
    }
}
