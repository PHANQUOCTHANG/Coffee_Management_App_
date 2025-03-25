package com.example.javafxapp.controller;

import com.example.javafxapp.model.Product;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ItemsProductController {
    @FXML
    private Label nameItem;

    @FXML
    private Label priceItem;

    @FXML
    private ImageView imgItem;

    private Product product;

    public void setData(Product product){
        this.product = product;
        nameItem.setText(product.getName());
        priceItem.setText(product.getPrice() + "đ");
        Image image = new Image(getClass().getResourceAsStream(product.getImgSrc()));
        imgItem.setImage(image);
    }
}
