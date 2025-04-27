package com.example.javafxapp.Controller.User.Order;

import com.example.javafxapp.Helpper.UploadImage;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.User.OrderDetailService;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProductItemController {
    @FXML
    private JFXButton addBtn;

    @FXML
    private ImageView img;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;

    OrderDetailService orderDetailService = new OrderDetailService();

    private Product product;

    private OrderDetailController orderDetailController;

    public void setProduct(Product p){
        product = p;
        System.out.println(product);
        img.setImage(UploadImage.loadImage(product.getImgSrc())); 
        img.setFitHeight(214);
        img.setFitWidth(214);
        nameLabel.setText(p.getProduct_name());
        priceLabel.setText("" + p.getPrice() + "VND");
    }

    public Product getProduct(){
        return product;
    }

    @FXML
    void addProduct() {
        addBtn.setDisable(true);
        orderDetailController.addOrderDetail(product);
    }

    public void setOrderDetailController(OrderDetailController orderDetailController) {
        this.orderDetailController = orderDetailController;
    }

    @FXML
    private void initialize(){
        System.out.println("pic initialized!"); 
        nameLabel.setText("N/A");
        priceLabel.setText("N/A");
    }

    public void setStatus(boolean status){
        addBtn.setDisable(status);
    }
}
