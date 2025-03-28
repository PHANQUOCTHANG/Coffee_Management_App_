package com.example.javafxapp.model;

public class Product {
    private int product_id;
    private String product_name;
    private String description;
    private double price;
    private int category_id;
    private String imgSrc;

    private boolean deleted = false ;


    public Product() {
    }

    public Product(String product_name, String description, double price, int category_id, String imgSrc) {
        this.product_name = product_name;
        this.description = description;
        this.price = price;
        this.category_id = category_id;
        this.imgSrc = imgSrc;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
