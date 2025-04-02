package com.example.javafxapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Label lblWelcome;

    @FXML
    private Button btnOverview, btnProducts, btnCategories, btnOrders,
            btnRevenue, btnSettings;

    private List<Button> menuButtons;

    @FXML
    public void initialize() {
        // Danh sách các nút trong menu
        menuButtons = new ArrayList<>();
        menuButtons.add(btnOverview);
        menuButtons.add(btnProducts);
        menuButtons.add(btnCategories);
        menuButtons.add(btnOrders);
        menuButtons.add(btnRevenue);
        menuButtons.add(btnSettings);

        // Mặc định chọn "Tổng Quan"
        setActiveButton(btnOverview);
    }

    private void setActiveButton(Button activeButton) {
        for (Button btn : menuButtons) {
            btn.getStyleClass().remove("selected-button");
        }
        activeButton.getStyleClass().add("selected-button");
    }

    @FXML
    private void handleOverview(ActionEvent event) {
        lblWelcome.setText("Tổng Quan");
        setActiveButton(btnOverview);
    }

    @FXML
    private void handleProducts(ActionEvent event) {
        lblWelcome.setText("Quản lý Sản Phẩm");
        setActiveButton(btnProducts);

    }

    @FXML
    private void handleCategories(ActionEvent event) {
        lblWelcome.setText("Quản lý Danh Mục");
        setActiveButton(btnCategories);
    }

    @FXML
    private void handleOrders(ActionEvent event) {
        lblWelcome.setText("Quản lý Đơn Hàng");
        setActiveButton(btnOrders);
    }

    @FXML
    private void handleRevenue(ActionEvent event) {
        lblWelcome.setText("Báo cáo Doanh Thu");
        setActiveButton(btnRevenue);
    }

    @FXML
    private void handleSettings(ActionEvent event) {
        lblWelcome.setText("Cài Đặt Hệ Thống");
        setActiveButton(btnSettings);
    }
}
