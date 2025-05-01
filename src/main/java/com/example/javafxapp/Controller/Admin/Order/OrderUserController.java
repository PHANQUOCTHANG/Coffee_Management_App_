package com.example.javafxapp.Controller.Admin.Order;

import com.example.javafxapp.Controller.Admin.BaseController;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class OrderUserController extends BaseController {
    @FXML
    void back(){
        msc.handleOrders();
    }

    @FXML
    private Label firstPage;

    @FXML
    private GridPane grid;

    @FXML
    private Label lastPage;

    @FXML
    private JFXButton nextBtn;

    @FXML
    private JFXButton prevBtn;

    @FXML
    private ScrollPane scroll;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox statusComboBox;

    @FXML
    void filterAction(ActionEvent event) {

    }

    @FXML
    void nextPage(ActionEvent event) {

    }

    @FXML
    void prevPage(ActionEvent event) {

    }
}
