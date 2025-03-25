package com.example.javafxapp.pages;

import com.example.javafxapp.alert.AlertInfo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Pages {
    public Pages() {
    }

    // Chuyển qua page dashboard .
    public static void openDashboard(Stage loginStage) {
        try {
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource("/com/example/javafxapp/view/dashboard/dashboard.fxml"));
            Pane root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();

            // Đóng cửa sổ login sau khi mở dashboard
            loginStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang Dashboard.");
        }
    }
}
