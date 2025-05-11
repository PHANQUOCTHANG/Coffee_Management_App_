package com.example.javafxapp.Controller.Admin.Order;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Model.OrderDetail;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.ProductService;
import com.example.javafxapp.Utils.LogUtils;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

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
    private HBox root;

    private OrderDetail orderDetail;
    private double price;
    private OrderDetailController orderDetailController;
    private final ProductService productService = new ProductService();

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
        updateView();
    }

    public void setOrderDetailController(OrderDetailController controller) {
        this.orderDetailController = controller;
    }

    private void updateView() {
        if (orderDetail != null) {
            Product product = productService.findProductByID(orderDetail.getProductId());
            if (product != null) {
                unitName.setText(product.getProduct_name());
                cntItem.setText(String.valueOf(orderDetail.getQuantity()));
                unitPrice.setText(String.format("%,.0f đ", orderDetail.getUnitPrice()));
                price = product.getPrice();
            }
        }
    }

    @FXML
    private void dec() {
        try {
            int cnt = orderDetail.getQuantity();
            if (cnt > 1) {
                cnt--;
                cntItem.setText(String.valueOf(cnt));
                unitPrice.setText(String.format("%,.0f đ", price * cnt));
                orderDetailController.updateOrderDetailPrice(orderDetail.getProductId(), cnt);
                orderDetailController.updateTotalPrice();
                LogUtils.logInfo("Decreased quantity for product: " + orderDetail.getProductId());
            }
        } catch (Exception e) {
            LogUtils.logError("Error decreasing quantity", e);
        }
    }

    @FXML
    private void inc() {
        try {
            int cnt = orderDetail.getQuantity();
            cnt++;
            cntItem.setText(String.valueOf(cnt));
            unitPrice.setText(String.format("%,.0f đ", price * cnt));
            orderDetailController.updateOrderDetailPrice(orderDetail.getProductId(), cnt);
            orderDetailController.updateTotalPrice();
            LogUtils.logInfo("Increased quantity for product: " + orderDetail.getProductId());
        } catch (Exception e) {
            LogUtils.logError("Error increasing quantity", e);
        }
    }

    @FXML
    private void deleteUnit() {
        try {
            if (AlertInfo.confirmAlert("Bạn có thật sự muốn xoá?")) {
                orderDetailController.removeOrderDetail(orderDetail.getProductId());
                LogUtils.logInfo("Deleted order detail: " + orderDetail.getProductId());
            }
        } catch (Exception e) {
            LogUtils.logError("Error deleting order detail", e);
        }
    }
}
