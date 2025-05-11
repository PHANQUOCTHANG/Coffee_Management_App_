package com.example.javafxapp.Controller.Admin.Order;

import com.example.javafxapp.Helpper.UploadImage;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.OrderDetailService;
import com.example.javafxapp.Utils.LogUtils;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ProductOrderDetailItemController {
    @FXML
    private JFXButton addBtn;
    @FXML
    private ImageView img;
    @FXML
    private Label nameLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private VBox root;

    private Product product;
    private OrderDetailController orderDetailController;
    private final OrderDetailService orderDetailService = new OrderDetailService();

    public void setProduct(Product product) {
        this.product = product;
        updateView();
    }

    public void setOrderDetailController(OrderDetailController controller) {
        this.orderDetailController = controller;
    }

    public Product getProduct() {
        return product;
    }

    private void updateView() {
        if (product != null) {
            img.setImage(UploadImage.loadImage(product.getImgSrc()));
            img.setFitHeight(214);
            img.setFitWidth(214);
            nameLabel.setText(product.getProduct_name());
            priceLabel.setText(String.format("%,.0f đ", product.getPrice()));
        }
    }

    @FXML
    private void addProduct() {
        try {
            if (product != null && orderDetailController != null) {
                addBtn.setDisable(true);
                orderDetailController.addOrderDetail(product);
                LogUtils.logInfo("Added product to order: " + product.getProduct_name());
            }
        } catch (Exception e) {
            LogUtils.logError("Error adding product to order", e);
            addBtn.setDisable(false);
        }
    }

    public void setStatus(boolean status) {
        if (addBtn != null) {
            addBtn.setDisable(status);
        }
    }
}
