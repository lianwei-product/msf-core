package cn.com.connext.msf.framework.mapping.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class Member02 {

    private List<String> sourceCode;

    @ApiModelProperty("jdMemberId")
    private String jdMemberId;

    @ApiModelProperty("openId")
    private String openId;

    @ApiModelProperty("realName")
    @NotNull
    private String realName;

    @ApiModelProperty("gender")
    @JsonProperty("gender")
    @NotNull
    private String gender;

    @ApiModelProperty("mobile")
    @NotNull
    private String mobile;

    @ApiModelProperty("birthDate")
    private Date birthDate;

    @ApiModelProperty("province")
    private String province;

    @ApiModelProperty("city")
    private String city;

    @ApiModelProperty("updateTime")
    private Date updateTime;

    @ApiModelProperty("addressList")
    private List<AddressInfoItem> addressList;

    public List<String> getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(List<String> sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getJdMemberId() {
        return jdMemberId;
    }

    public void setJdMemberId(String jdMemberId) {
        this.jdMemberId = jdMemberId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<AddressInfoItem> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<AddressInfoItem> addressList) {
        this.addressList = addressList;
    }

    static class AddressInfoItem {

        @ApiModelProperty("type")
        private String type;

        @ApiModelProperty("address")
        private String address;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }


 }
