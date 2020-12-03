package cn.com.connext.msf.framework.mapping.models;

import cn.com.connext.msf.framework.annotation.Default;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Member01 {

    @NotNull
    private String id;

    @ApiModelProperty("realName")
    private String realName;

    @Default("男")
    @ApiModelProperty("gender")
    private String gender;

    @ApiModelProperty("mobile")
    @NotNull
    private String mobile;

    @ApiModelProperty("pro")
    @JsonProperty("pro")
    @NotNull
    private String province;

    @ApiModelProperty("telephone")
    @NotNull
    private String telephone;

    @ApiModelProperty("telephones")
    private List<String> telephones;

    @ApiModelProperty("addressTypes")
    private List<String> addressTypes;

    @ApiModelProperty("addressList")
    private List<AddressInfoItem> addressList;

    @ApiModelProperty("lastAddress")
    private AddressInfoItem lastAddress;

    @ApiModelProperty("behavior")
    private Behavior behavior;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<String> getTelephones() {
        return telephones;
    }

    public void setTelephones(List<String> telephones) {
        this.telephones = telephones;
    }

    public List<String> getAddressTypes() {
        return addressTypes;
    }

    public void setAddressTypes(List<String> addressTypes) {
        this.addressTypes = addressTypes;
    }

    public List<AddressInfoItem> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<AddressInfoItem> addressList) {
        this.addressList = addressList;
    }

    public AddressInfoItem getLastAddress() {
        return lastAddress;
    }

    public void setLastAddress(AddressInfoItem lastAddress) {
        this.lastAddress = lastAddress;
    }

    public Behavior getBehavior() {
        return behavior;
    }

    public void setBehavior(Behavior behavior) {
        this.behavior = behavior;
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

    static class Behavior {
        @ApiModelProperty("type")
        private String type;
        @ApiModelProperty("detail")
        private BehaviorDetail detail;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public BehaviorDetail getDetail() {
            return detail;
        }

        public void setDetail(BehaviorDetail detail) {
            this.detail = detail;
        }
    }

    static class BehaviorDetail {
        @ApiModelProperty("source")
        private String source;

        @ApiModelProperty("action")
        private String action;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }
}
