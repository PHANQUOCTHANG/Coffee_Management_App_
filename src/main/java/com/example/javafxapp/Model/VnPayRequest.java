package com.example.javafxapp.Model;

public class VnPayRequest {
    private String orderId;
    private long amount;
    private String orderInfo;

    public VnPayRequest(String orderId, long amount, String orderInfo) {
        this.orderId = orderId;
        this.amount = amount;
        this.orderInfo = orderInfo;
    }

    public String getOrderId() { return orderId; }
    public long getAmount() { return amount; }
    public String getOrderInfo() { return orderInfo; }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }
}
