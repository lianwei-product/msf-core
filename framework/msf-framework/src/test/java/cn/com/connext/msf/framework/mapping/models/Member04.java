package cn.com.connext.msf.framework.mapping.models;

import cn.com.connext.msf.framework.annotation.Default;
import io.swagger.annotations.ApiModelProperty;

public class Member04 {

    @Default("")
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("name")
    private String name;
    @ApiModelProperty("province")
    private String province;
    @ApiModelProperty("city")
    private String city;
    @ApiModelProperty("district")
    private String district;
    @ApiModelProperty("address")
    private String address;

}
