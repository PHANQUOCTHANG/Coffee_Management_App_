package com.example.javafxapp.Model;

public class OrderUser {
    private int orderUser_id ;
    private int account_id ;
    private String fullName ;
    private String phone ;
    private String address ;
    private String note ;
    private double shippingFee ;
    private String methodPayment ;
    private double subPrice ;
    private double discount ;
    private String status ;

    public OrderUser(int account_id, String fullName, String phone, String address, String note, double shippingFee, String methodPayment, double subPrice, double discount , String status) {
        this.account_id = account_id;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.note = note;
        this.shippingFee = shippingFee;
        this.methodPayment = methodPayment;
        this.subPrice = subPrice;
        this.discount = discount;
        this.status = status ;
    }

    public OrderUser(int orderUser_id, int account_id, String fullName, String phone, String address, String note, double shippingFee, String methodPayment, double subPrice, double discount, String status) {
        this.orderUser_id = orderUser_id;
        this.account_id = account_id;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.note = note;
        this.shippingFee = shippingFee;
        this.methodPayment = methodPayment;
        this.subPrice = subPrice;
        this.discount = discount;
        this.status = status;
    }

    public int getOrderUser_id() {
        return orderUser_id;
    }

    public void setOrderUser_id(int orderUser_id) {
        this.orderUser_id = orderUser_id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getMethodPayment() {
        return methodPayment;
    }

    public void setMethodPayment(String methodPayment) {
        this.methodPayment = methodPayment;
    }

    public double getSubPrice() {
        return subPrice;
    }

    public void setSubPrice(double subPrice) {
        this.subPrice = subPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
