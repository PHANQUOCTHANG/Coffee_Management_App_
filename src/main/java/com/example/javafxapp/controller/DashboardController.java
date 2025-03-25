package com.example.javafxapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private Label lblWelcome;

    @FXML
    private StackPane contentArea;

    @FXML
    private Button btnManageUsers, btnManageRoles, btnReports, btnSettings, btnLogout;

    // Phương thức gọi khi trang Dashboard được tải
    public void initialize() {
        lblWelcome.setText("Chào mừng, Người dùng!");
    }

    @FXML
    private void handleManageUsers() {
        lblWelcome.setText("Đây là trang Quản lý người dùng");
    }

    @FXML
    private void handleManageRoles() {
        lblWelcome.setText("Đây là trang Quản lý vai trò");
    }

    @FXML
    private void handleReports() {
        lblWelcome.setText("Đây là trang Báo cáo");
    }

    @FXML
    private void handleSettings() {
        lblWelcome.setText("Đây là trang Cài đặt");
    }

    @FXML
    private void handleLogout() {
        // Đóng cửa sổ Dashboard
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        stage.close();

        // Quay lại màn hình đăng nhập
        System.out.println("Đã đăng xuất!");
    }
}
