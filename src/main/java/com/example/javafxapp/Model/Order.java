package com.example.javafxapp.Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order {
    private int id;
    private int userId;
    private String staffName;
    private BigDecimal totalAmount;
    private String status;
    private Timestamp orderTime;
    public Order(int id, int userId, String staffName, BigDecimal totalAmount, String status, Timestamp orderTime) {
        this.id = id;
        this.userId = userId;
        this.staffName = staffName;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderTime = orderTime;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getStaffName() {
        return staffName;
    }
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Timestamp getOrderTime() {
        return orderTime;
    }
    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }
    @Override
    public String toString() {
        return "Order [id=" + id + ", userId=" + userId + ", staffName=" + staffName + ", totalAmount=" + totalAmount
                + ", status=" + status + ", orderTime=" + orderTime + "]";
    }
    
    

    
}
