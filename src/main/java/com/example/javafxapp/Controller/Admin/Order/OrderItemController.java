package com.example.javafxapp.Controller.Admin.Order;

import com.example.javafxapp.Controller.Admin.MainScreenController;
import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Model.Order;
import com.example.javafxapp.Service.OrderService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class OrderItemController {
    @FXML
    private JFXButton delete;

    @FXML
    private JFXButton edit;

    @FXML
    private JFXCheckBox id;

    @FXML
    private Label status;

    @FXML
    private Label staffName;

    @FXML
    private Label time;

    @FXML
    private Label total;

    @FXML
    private Label paymentMethodLabel;

    private Order order;

    private OrderService orderService = new OrderService();
    private OrderController orderController = new OrderController();

    public void setOrder(Order od) {
        this.order = od;
        System.out.println(order);
        id.setText(String.valueOf(order.getId()));
        staffName.setText(String.valueOf(order.getStaffName()));
        String statusText = convertStatus(order.getStatus());
        status.setText(convertStatus(order.getStatus()));
        // xet mau cho tung loai trang thai
        if (statusText.equals("Đang chờ xử lí")) {
            status.setText("⏳ " + statusText);
            status.setStyle("-fx-text-fill: #e67e22; -fx-background-color: #fdf2e9; -fx-background-radius: 12; -fx-padding: 6 12 6 12; -fx-font-weight: bold;");
            
        } else if (statusText.equals("Đang xử lí")) {
            status.setText("🔄 " + statusText);;
            status.setStyle("-fx-text-fill: #3498db; -fx-background-color: #ebf3fd; -fx-background-radius: 12; -fx-padding: 6 12 6 12; -fx-font-weight: bold;");
            
        } else if (statusText.equals("Đã xử lí")) {
            status.setText("✅ " + statusText);
            status.setStyle("-fx-text-fill: #27ae60; -fx-background-color: #eafaf1; -fx-background-radius: 12; -fx-padding: 6 12 6 12; -fx-font-weight: bold;");
            
        } else {
            status.setText("❌ " + statusText);
            status.setStyle("-fx-text-fill: #e74c3c; -fx-background-color: #fdedec; -fx-background-radius: 12; -fx-padding: 6 12 6 12; -fx-font-weight: bold;");
        }
        time.setText("" + order.getOrderTime());
        total.setText(String.format("%,.2f đ", order.getTotalAmount()));

        if (order.getStatus().equals("Completed")){
            if (order.getPaymentMethod().equals("Cash")) {
                paymentMethodLabel.setText("Tiền mặt");
            } else {
                paymentMethodLabel.setText(order.getPaymentMethod());
            }
        }
    }

    public void setOrderController(OrderController oc) {
        orderController = oc;
    }

    private String convertStatus(String s) {
        if (s.equals("Pending"))
            return "Đang chờ xử lí";
        else if (s.equals("Processing"))
            return "Đang xử lí";
        else if (s.equals("Completed"))
            return "Đã xử lí";
        else
            return "Đã huỷ";
    }

    @FXML
    public void initialize() {
        System.out.println("initialize orderitemcontroller");
        id.setText("N/A");
        staffName.setText("N/A");
        status.setText("N/A");
        time.setText("N/A");
        total.setText("0.00 đ");
    }

    @FXML
    void del() {
        if (AlertInfo.confirmAlert("Bạn có thật sự muốn xoá? Hành động này sẽ không thể hoàn tác.")) {
            // xoá dữ liệu trong databse
            orderService.deleteOrder(order.getId());
            // xoá trong dữ liệu mảng của controller cha
            orderController.deleteOrder(order.getId());
        }
    }

    @FXML
    void edit() {
        MainScreenController msc = orderController.getMSC();
        msc.handleEditOrder(order);
    }

    @FXML
    public void handleTick() {
        // Xử lý khi checkbox được click
        if (id.isSelected()) {
            // Xử lý khi checkbox được chọn
            System.out.println("Checkbox selected for order: " + order.getId());
            orderController.putMpTickedOrder(order.getId());
            if (orderController.checkTickAll()) {
                orderController.setIdCheckBox(true);
            }
        } else {
            // Xử lý khi checkbox bị bỏ chọn
            orderController.setIdCheckBox(false);
            System.out.println("Checkbox unselected for order: " + order.getId());
            orderController.removeMpTickedOrder(order.getId());
        }
    }

    public void setSelected(boolean selected) {
        id.setSelected(selected);
    }
}
