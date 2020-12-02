package cn.com.connext.msf.framework.mapping.models;

import cn.com.connext.msf.framework.annotation.Default;
import io.swagger.annotations.ApiModelProperty;

public class Member03 {

    @Default("男")
    @ApiModelProperty("gender")
    private String gender;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
