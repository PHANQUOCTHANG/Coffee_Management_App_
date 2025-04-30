package com.example.javafxapp.Validation;

import com.example.javafxapp.Helpper.AlertInfo;
import javafx.scene.control.Alert;

public class ValidationProduct {

    public static boolean validationProductName(String productName) {
        if (productName.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Tên không rỗng");
            return false ;
        }
        if (productName.length() < 6) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Tên ít nhất 6 kí tự");
            return false ;
        }
        return true ;
    }

    public static double validationPrice(String price) {
        if (price == null || price.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Giá không rỗng");
            return -1 ;
        }
        try {
            double newPrice = Double.parseDouble(price) ;
            if (newPrice < 0) {
                throw new ArithmeticException() ;
            }
            return newPrice ;
        }
        catch (NumberFormatException e) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Giá phải là số");
        }
        catch (ArithmeticException e) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Giá không được âm");
        }
        return -1 ;
    }

    public static boolean validationCategory (String valueCategory) {
        if (valueCategory.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Vui lòng chọn danh mục");
            return false ;
        }
        return true ;
    }


}
