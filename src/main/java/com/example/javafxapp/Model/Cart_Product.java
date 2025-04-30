package com.example.javafxapp.Model;

public class Cart_Product {
    private int cart_id ;
    private int product_id ;
    private int quantity ;

    private boolean deleted = false ;

    public Cart_Product(int cart_id, int product_id) {
        this.cart_id = cart_id;
        this.product_id = product_id;
    }

    public Cart_Product(int cart_id, int product_id, int quantity) {
        this.cart_id = cart_id;
        this.product_id = product_id;
        this.quantity = quantity;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
