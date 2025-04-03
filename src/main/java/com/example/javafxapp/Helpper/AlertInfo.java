package com.example.javafxapp.Helpper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertInfo {

    public AlertInfo() {
    }


    // Thông báo .
    public static void showAlert(javafx.scene.control.Alert.AlertType type, String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // xác nhận
    public static boolean confirmAlert(String headerText) {
        // Tạo hộp thoại xác nhận
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận hành động");
        alert.setHeaderText(headerText);
        alert.setContentText("Nhấn OK để xác nhận, Cancel để hủy.");

        // Hiển thị hộp thoại và nhận kết quả
        Optional<ButtonType> result = alert.showAndWait();

        // Kiểm tra xem người dùng chọn gì
        if (result.isPresent() && result.get() == ButtonType.OK) {
            return true ;
        }
        return false ;
    }
}
