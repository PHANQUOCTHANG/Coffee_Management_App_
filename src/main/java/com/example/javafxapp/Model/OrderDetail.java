package com.example.javafxapp.Model;

public class OrderDetail {
    // database:
    // order_detail_id int auto_increment primary key,
    // order_id int not null,
    // product_id int not null,
    // quantity int not null,
    // unit_price decimal(10, 2) not null,

    private int orderId;
    private int productId;
    private int quantity;
    private double unitPrice;

    OrderDetail(){}

    public OrderDetail(int orderId, int productId, int quantity, double unitPrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    
}
