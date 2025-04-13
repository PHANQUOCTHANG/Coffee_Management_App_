package com.example.javafxapp.Controller.User;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

public class OrderDetailController extends BaseController {
    @FXML
    private JFXButton addItemBtn;

    @FXML
    private JFXButton backBtn;

    @FXML
    private JFXButton cancelBtn;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Label totalMoney;

    @FXML
    private void initialize(){
        loadData();

    }

    private void loadData(){

    }

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void goLootPage(ActionEvent event) {

    }

    @FXML
    void saveOrderDetail(ActionEvent event) {

    }

    @FXML
    private void goBackPage(){
        System.out.println("Back button clicked!");
        umsc.handleOrders();
    }

    
}
