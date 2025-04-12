package com.example.javafxapp.Controller.User;

import java.io.IOException;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class UserMainScreenController {
    @FXML
    private JFXButton btnCart;

    @FXML
    private JFXButton btnLogOut;

    @FXML
    private JFXButton btnOrders;

    @FXML
    private JFXButton btnOverview;

    @FXML
    private JFXButton btnProducts;

    @FXML
    private StackPane centerPane;

    JFXButton activeBtn;

    @FXML
    public void initialize(){
        activeBtn = btnOverview;
        handleOverview();
    }

    public void setActiveButton(JFXButton btn){
        activeBtn.getStyleClass().remove("selected-button");
        activeBtn = btn;
        activeBtn.getStyleClass().add("selected-button");
    }

    @FXML
    public void handleOverview(){
        System.out.println("Overview button clicked!");
        loadCenterContent("/com/example/javafxapp/view/dashboard/homePage.fxml");
        setActiveButton(btnOverview);
    }

    public void loadCenterContent(String fxmlPath){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent p;
        try {
            p = loader.load();
            centerPane.getChildren().setAll(p);
            centerPane.setMargin(p, new Insets(0));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    @FXML
    private void handleLogOut() {
        System.out.println("Log out button clicked!");
        if (AlertInfo.confirmAlert("Bạn có thật sự muốn thoát?")){
            Stage st = (Stage) btnLogOut.getScene().getWindow();
            Pages.pageLogin();
            st.close();
        }
        setActiveButton(btnLogOut);
    }

    @FXML
    private void handleOrders() {
        System.out.println("Orders button clicked!");
        loadCenterContent("/com/example/javafxapp/view/orders/orders.fxml");
        setActiveButton(btnOrders);
    }

    @FXML
    private void handleProducts() {
        System.out.println("Products button clicked!");
        loadCenterContent("/com/example/javafxapp/view/userProduct/userProducts.fxml");
        setActiveButton(btnProducts);
    }

    @FXML
    private void handleCart(){
        System.out.println("Cart button clicked!");
        loadCenterContent("/com/example/javafxapp/view/userDashboard/cartPage.fxml");
        setActiveButton(btnCart);
    }
}
