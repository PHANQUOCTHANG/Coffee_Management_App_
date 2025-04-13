package com.example.javafxapp.Controller.User;

import com.example.javafxapp.Model.Order;
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

    public void setOrder(Order od){
        this.order = od;
        System.out.println(order);
        id.setText("" + order.getId());
        staffName.setText(String.valueOf(order.getStaffName()));
        status.setText(convertStatus(order.getStatus()));
        time.setText("" + order.getOrderTime());
        total.setText(String.format("%,.2f đ", order.getTotalAmount()));
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

        if (id == null)
            System.out.println("id label is null");
        else 
            System.out.println("id label is not null");

        if (name == null)
            System.out.println("name label is null");
        else 
            System.out.println("name label is not null");

        if (status == null)
            System.out.println("status label is null");
        else 
            System.out.println("status label is not null");

        if (staffName == null)
            System.out.println("staffName label is null");
        else 
            System.out.println("staffName label is not null");

        if (time == null)
            System.out.println("time label is null");
        else 
            System.out.println("time label is not null");

        if (total == null)
            System.out.println("total label is null");
        else 
            System.out.println("total label is not null");
        id.setText("N/A");
        name.setText("N/A");
        staffName.setText("N/A");
        status.setText("N/A");
        time.setText("N/A");
        total.setText("0.00 đ");
    }
}
