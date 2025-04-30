package com.example.javafxapp.Model;

public class Cart {
    private int cart_id ;
    private int account_id ;
    private boolean deleted = false ;

    public Cart(int account_id) {
        this.account_id = account_id;
    }

    public Cart(int cart_id, int account_id, boolean deleted) {
        this.cart_id = cart_id;
        this.account_id = account_id;
        this.deleted = deleted;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
