package com.example.javafxapp.Utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class NotificationUtils {
    
    public static void showError(String title, String content) {
        showAlert(AlertType.ERROR, title, content);
    }
    
    public static void showSuccess(String title, String content) {
        showAlert(AlertType.INFORMATION, title, content);
    }
    
    public static void showWarning(String title, String content) {
        showAlert(AlertType.WARNING, title, content);
    }
    
    public static boolean showConfirmation(String title, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        
        return alert.showAndWait().get() == ButtonType.OK;
    }
    
    private static void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        
        // Lấy Stage hiện tại và set owner cho alert
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        
        alert.showAndWait();
    }
} 