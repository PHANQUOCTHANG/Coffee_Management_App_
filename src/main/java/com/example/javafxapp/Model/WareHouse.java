package com.example.javafxapp.Model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class WareHouse {
    private int productWareHouse_id;
    private String productWareHouse_name ;
    private String category_name ;
    private String imgSrc;
    private boolean status ;
    private int quantity ;
    private boolean deleted = false ;
    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public WareHouse() {
    }

    public WareHouse(String productWareHouse_name, String category_name, String imgSrc, boolean status, int quantity, boolean deleted) {
        this.productWareHouse_name = productWareHouse_name;
        this.category_name = category_name;
        this.imgSrc = imgSrc;
        this.status = status;
        this.quantity = quantity;
        this.deleted = deleted;
    }

    public WareHouse(int productWareHouse_id, String productWareHouse_name, String category_name, String imgSrc, boolean status, int quantity, boolean deleted) {
        this.productWareHouse_id = productWareHouse_id;
        this.productWareHouse_name = productWareHouse_name;
        this.category_name = category_name;
        this.imgSrc = imgSrc;
        this.status = status;
        this.quantity = quantity;
        this.deleted = deleted;
    }

    public int getProductWareHouse_id() {
        return productWareHouse_id;
    }

    public void setProductWareHouse_id(int productWareHouse_id) {
        this.productWareHouse_id = productWareHouse_id;
    }

    public String getProductWareHouse_name() {
        return productWareHouse_name;
    }

    public void setProductWareHouse_name(String productWareHouse_name) {
        this.productWareHouse_name = productWareHouse_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

}
