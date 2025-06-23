package com.example.javafxapp.Validation;

import com.example.javafxapp.Helpper.AlertInfo;
import javafx.scene.control.Alert;

public class ValidationWareHouse {

    // Kiểm tra tên sản phẩm kho
    public static boolean validationName(String name) {
        if (name == null || name.trim().isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Tên sản phẩm không được để trống.");
            return false;
        }
        return true;
    }

    // Kiểm tra số lượng sản phẩm kho
    public static int validationQuantity(String quantityStr) {
        if (quantityStr == null || quantityStr.trim().isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Số lượng không được để trống.");
            return -1;
        }
        try {
            int quantity = Integer.parseInt(quantityStr.trim());
            if (quantity < 0) {
                throw new ArithmeticException();
            }
            return quantity;
        } catch (NumberFormatException e) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Số lượng phải là một số nguyên.");
        } catch (ArithmeticException e) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Số lượng không được âm.");
        }
        return -1;
    }

    // Kiểm tra danh mục
    public static boolean validationCategory(String categoryIdStr) {
        if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng chọn danh mục.");
            return false;
        }
        return true;
    }
}
