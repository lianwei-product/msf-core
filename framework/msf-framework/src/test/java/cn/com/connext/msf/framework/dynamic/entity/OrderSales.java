package cn.com.connext.msf.framework.dynamic.entity;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderSales {

    @ApiModelProperty(value = "订单标识")
    private String id;

    @ApiModelProperty(value = "渠道标识")
    private String channelId;

    @ApiModelProperty(value = "会员标识")
    private String memberId;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "订单实付金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "扩展信息")
    private List<Extend> extend;

    @ApiModelProperty(value = "订单创建时间")
    private Date createdTime;

    public OrderSales() {
    }

    public String getId() {
        return id;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public List<Extend> getExtend() {
        return extend;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public static class Extend {

        @ApiModelProperty(value = "键")
        private String name;

        @ApiModelProperty(value = "值")
        private String value;

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}
