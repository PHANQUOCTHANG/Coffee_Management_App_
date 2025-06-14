package com.example.javafxapp.Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order {
    private int id;
    private int userId;
    private BigDecimal totalAmount;
    private String status;
    private String paymentMethod;
    private Timestamp orderTime;

    // dung cho fe
    private String staffName;
    
    public Order(int id, int userId, String staffName, BigDecimal totalAmount, String status, String paymentMethod, Timestamp orderTime) {
        this.id = id;
        this.userId = userId;
        this.staffName = staffName;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.orderTime = orderTime;
    }

    public Order(int id, int userId, BigDecimal totalAmount, String status, String paymentMethod, Timestamp orderTime) {
        this.id = id;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentMethod = paymentMethod;
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
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    @Override
    public String toString() {
        return "Order [id=" + id + ", userId=" + userId + ", staffName=" + staffName + ", totalAmount=" + totalAmount
                + ", status=" + status + ", orderTime=" + orderTime + "]";
    }
    
    

    
}
