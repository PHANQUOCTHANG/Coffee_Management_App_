package com.example.javafxapp.Model;

public class VnPayRequest {
    private String orderId;
    private long amount;
    private String orderInfo;
    private String bankCode;
    private String receiverAccount;
    private String receiverName;

    public VnPayRequest(String orderId, long amount, String orderInfo) {
        this.orderId = orderId;
        this.amount = amount;
        this.orderInfo = orderInfo;
    }

    public VnPayRequest() {
        // Default constructor
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

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(String receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    
}
