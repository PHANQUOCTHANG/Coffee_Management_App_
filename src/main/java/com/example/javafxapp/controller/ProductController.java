package com.example.javafxapp.controller;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProductController {
    @FXML
    private ImageView productImg;

    @FXML
    private Label productName;

    @FXML
    private Label productStock;

    @FXML
    private Label productCategory;

    @FXML
    private Label productPrice;

    public void setProductData(String imgSrc, String name, int stock, int category, double price){
        File file = new File(imgSrc);
        if (file.exists()){
            productImg.setImage(new Image(file.toURI().toString()));
        }
        productName.setText(name);
        productStock.setText(stock + "");
        productCategory.setText(category + "");
        productPrice.setText(price + "");
    }
}
