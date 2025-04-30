package com.example.javafxapp.Controller.Admin.Order;

import java.math.BigDecimal;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Model.OrderDetail;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.ProductService;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

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

    private OrderDetail orderDetail;
    private double price;

    private OrderDetailController orderDetailController;
    private ProductService productService = new ProductService();

    public void setOrderDetail(OrderDetail od) {
        orderDetail = od;
        System.out.println(orderDetail);
        Product product = productService.findProductByID(od.getProductId());
        unitName.setText(product.getProduct_name());
        cntItem.setText("" + od.getQuantity());
        unitPrice.setText("" + od.getUnitPrice() + " đ");
        price = product.getPrice();
    }

    @FXML
    void dec() {
        int cnt = orderDetail.getQuantity();
        if (cnt > 1){
            cnt--;
            cntItem.setText("" + cnt);
            unitPrice.setText(price * cnt + " đ");
            orderDetailController.updateOrderDetailPrice(orderDetail.getProductId(), cnt);
            // cap nhat gia tri cho tong uoc tinh
            orderDetailController.updateTotalPrice();
        }
    }

    @FXML
    void deleteUnit() {
        if (AlertInfo.confirmAlert("Bạn có thật sự muốn xoá?")) {
            // update data ben orderDetailController
            orderDetailController.removeOrderDetail(orderDetail.getProductId());

            orderDetailController.updateTotalPrice();
            orderDetailController.loadOrderDetailList();

            ProductOrderDetailItemController pic = orderDetailController.getProductItemController(orderDetail.getProductId());
            pic.setStatus(false);
        }
    }

    @FXML
    void inc() {
        int cnt = orderDetail.getQuantity();
        cnt++;
        cntItem.setText("" + cnt);
        unitPrice.setText(price * cnt + " đ");
        orderDetailController.updateOrderDetailPrice(orderDetail.getProductId(), cnt);
        // cap nhat gia tri cho tong uoc tinh
        orderDetailController.updateTotalPrice();
    }

    public void setOrderDetailController(OrderDetailController orderDetailController) {
        this.orderDetailController = orderDetailController;
    }

    @FXML
    private void initialized(){
        System.out.println("ODIC initialized!");
        cntItem.setText("1");
        unitName.setText("N/A");
        unitPrice.setText("N/A");
    }
}
