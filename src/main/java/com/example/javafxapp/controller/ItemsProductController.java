package com.example.javafxapp.controller;

import java.util.function.Consumer;

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
    private Consumer<Product> onClick; // bien luu ham xu li khi click

    // nhan du lieu product va ham xu li khi click
    public void setData(Product product, Consumer<Product> onClick){
        this.product = product;
        this.onClick = onClick;
        nameItem.setText(product.getName());
        priceItem.setText(String.valueOf(product.getPrice()));
        Image image = new Image(getClass().getResourceAsStream(product.getImgSrc()));
        imgItem.setImage(image);
    }

    @FXML
    private void click(MouseEvent event){
        if (onClick != null){
            onClick.accept(product); // goi callback khi click
        }
    }
}
