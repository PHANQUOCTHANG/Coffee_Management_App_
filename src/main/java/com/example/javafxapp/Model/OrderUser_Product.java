package com.example.javafxapp.Model;

public class OrderUser_Product {
    private int orderUser_id;
    private int product_id;
    private int quantity ;

    public OrderUser_Product(int orderUser_id, int product_id, int quantity) {
        this.orderUser_id = orderUser_id;
        this.product_id = product_id;
        this.quantity = quantity;
    }

    public int getOrderUser_id() {
        return orderUser_id;
    }

    public void setOrderUser_id(int orderUser_id) {
        this.orderUser_id = orderUser_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
