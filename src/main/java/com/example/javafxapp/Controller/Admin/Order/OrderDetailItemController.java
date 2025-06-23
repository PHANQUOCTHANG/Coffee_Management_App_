package com.example.javafxapp.Controller.Admin.Order;

import java.math.BigDecimal;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.UploadImage;
import com.example.javafxapp.Model.OrderDetail;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.ProductService;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class OrderDetailItemController {
    @FXML
    private Label cntItem;

    @FXML
    private JFXButton decBtn;

    @FXML
    private JFXButton deleteBtn;

    @FXML
    private JFXButton incBtn;

    @FXML
    private Label unitName;

    @FXML
    private Label unitPrice;

    @FXML
    private ImageView imgProduct;

    @FXML
    private Label productPriceLabel;

    @FXML
    private Label cntItem2;
    

    private OrderDetail orderDetail;
    private double price;

    private OrderDetailController orderDetailController;
    private ProductService productService = new ProductService();

    public void setOrderDetail(OrderDetail od) {
        orderDetail = od;
        Product product = productService.findProductByID(od.getProductId());
        // Set image for product
        imgProduct.setImage(UploadImage.loadImage(product.getImgSrc()));
        unitName.setText(product.getProduct_name());
        cntItem.setText("" + od.getQuantity());
        cntItem2.setText(" x " + od.getQuantity());
        productPriceLabel.setText(product.getPrice() + " đ");
        unitPrice.setText("" + od.getUnitPrice() + " đ");
        price = product.getPrice();
    }

    @FXML
    void dec() {
        int cnt = orderDetail.getQuantity();
        if (cnt > 1){
            cnt--;
            cntItem.setText("" + cnt);
            cntItem2.setText(" x " + cnt);
            unitPrice.setText(price * cnt + " đ");
            orderDetailController.updateOrderDetailPrice(orderDetail.getProductId(), cnt);
            // cap nhat gia tri cho tong uoc tinh
            orderDetailController.updateTotalPrice();
        }
    }

    @FXML
    void deleteUnit() {
        // update data ben orderDetailController
        orderDetailController.removeOrderDetail(orderDetail.getProductId());

        orderDetailController.updateTotalPrice();
        orderDetailController.loadOrderDetailList();

        ProductOrderDetailItemController pic = orderDetailController.getProductItemController(orderDetail.getProductId());
        pic.setStatus(false);
    }

    @FXML
    void inc() {
        int cnt = orderDetail.getQuantity();
        cnt++;
        cntItem.setText("" + cnt);
        cntItem2.setText(" x " + cnt);
        unitPrice.setText(price * cnt + " đ");
        orderDetailController.updateOrderDetailPrice(orderDetail.getProductId(), cnt);
        // cap nhat gia tri cho tong uoc tinh
        orderDetailController.updateTotalPrice();
    }

    public void setOrderDetailController(OrderDetailController orderDetailController) {
        this.orderDetailController = orderDetailController;
    }

    @FXML
    private void initialize(){
        System.out.println("ODIC initialized!");
        cntItem.setText("1");
        unitName.setText("N/A");
        unitPrice.setText("N/A");
        productPriceLabel.setText("N/A");
        cntItem2.setText(" x 1");
        // chinh co chu mau den
        cntItem2.setStyle("-fx-text-fill: #000000;");
        productPriceLabel.setStyle("-fx-text-fill: #000000;");
    }
}
