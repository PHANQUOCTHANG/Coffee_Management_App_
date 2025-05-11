package com.example.javafxapp.Controller.Admin.Order.User;

import com.example.javafxapp.Controller.Admin.MainScreenController;
import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.TimeConverter;
import com.example.javafxapp.Model.Order;
import com.example.javafxapp.Model.OrderUser;
import com.example.javafxapp.Service.OrderService;
import com.example.javafxapp.Service.OrderUserService;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class OrderUserItemController {
     @FXML
    private Label id;

    @FXML
    private Label name;

    @FXML
    private Label status;

    @FXML
    private Label time;

    @FXML
    private Label total;

    

    private OrderUser orderUser;

    private OrderUserService orderUserService = new OrderUserService();
    private OrderUserController orderUserController = new OrderUserController();

    public void setOrderUser(OrderUser od){
        this.orderUser = od;
        System.out.println(orderUser);
        id.setText("" + orderUser.getOrderUser_id());
        name.setText(String.valueOf(orderUser.getFullName()));
        status.setText(convertStatus(orderUser.getStatus()));
        time.setText("" + TimeConverter.toGmtPlus7(orderUser.getOrder_time()));
        total.setText(String.format("%,.2f đ", orderUser.getSubPrice() - orderUser.getDiscount() + orderUser.getShippingFee()));
    }

    public void setOrderUserController(OrderUserController oc){
        orderUserController = oc;
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
        name.setText("N/A");
        status.setText("N/A");
        time.setText("N/A");
        total.setText("0.00 đ");
    }

    @FXML
    void del() {
        if (AlertInfo.confirmAlert("Bạn có thật sự muốn xoá? Hành động này sẽ không thể hoàn tác.")) {
            // xoá dữ liệu trong databse
            orderUserService.delete(orderUser.getOrderUser_id());
            // xoá trong dữ liệu mảng của controller cha
            orderUserController.delete(orderUser.getOrderUser_id());
        }
    }

    @FXML
    void edit() {
        MainScreenController msc = orderUserController.getMSC();
        msc.handleEditOrderUser(orderUser);
    }

    @FXML
    void tickAll(ActionEvent event) {

    }
}
