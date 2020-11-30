package cn.com.connext.msf.framework.dynamic;

import org.springframework.data.annotation.Id;

public class CommonModelInfo {

    @Id
    public String id;
    public String name;
    public String aliasName;

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

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    @Override
    public String toString() {
        return "CommonModelInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", aliasName='" + aliasName + '\'' +
                '}';
    }
}
