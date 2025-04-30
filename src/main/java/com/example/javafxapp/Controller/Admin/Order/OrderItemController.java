package com.example.javafxapp.Controller.Admin.Order;

import com.example.javafxapp.Controller.Admin.MainScreenController;
import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Model.Order;
import com.example.javafxapp.Service.OrderService;
import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class OrderItemController {
    @FXML
    private JFXButton delete;

    @FXML
    private JFXButton edit;

    @FXML
    private Label id;

    @FXML
    private Label name;

    @FXML
    private Label status;

    @FXML
    private Label staffName;

    @FXML
    private Label time;

    @FXML
    private Label total;

    private Order order;

    private OrderService orderService = new OrderService();
    private OrderController orderController = new OrderController();

    public void setOrder(Order od){
        this.order = od;
        System.out.println(order);
        id.setText("" + order.getId());
        staffName.setText(String.valueOf(order.getStaffName()));
        status.setText(convertStatus(order.getStatus()));
        time.setText("" + order.getOrderTime());
        total.setText(String.format("%,.2f đ", order.getTotalAmount()));
    }

    public void setOrderController(OrderController oc){
        orderController = oc;
    }

    private String convertStatus(String s){
        if (s.equals("Pending")) return "Đang chờ xử lí";
        else if (s.equals("Processing")) return "Đang xử lí";
        else if (s.equals("Completed")) return "Đã xử lí";
        else return "Đã huỷ";
    }

    

    @FXML
    public void initialize(){
        System.out.println("initialize orderitemcontroller");
        id.setText("N/A");
        name.setText("Offline"); // name đổi thành loại hoá đơn
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
}
