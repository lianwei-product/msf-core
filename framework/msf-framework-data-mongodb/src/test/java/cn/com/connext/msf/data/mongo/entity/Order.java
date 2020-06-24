package cn.com.connext.msf.data.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "order_test")
public class Order {
    @Id
    private String id;

    private String orderNo;

    private int quantity;

    private double totalAmount;

    private boolean realMember;

    private Date createTime;

    private LocalDateTime updateTime;

    public static Order from(String id, String orderNo, int quantity, double totalAmount, boolean isMember, Date createTime, LocalDateTime updateTime) {
        Order order = new Order();
        order.id = id;
        order.orderNo = orderNo;
        order.quantity = quantity;
        order.totalAmount = totalAmount;
        order.createTime = createTime;
        order.updateTime = updateTime;
        order.realMember = isMember;
        return order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isRealMember() {
        return realMember;
    }

    public void setRealMember(boolean realMember) {
        this.realMember = realMember;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
