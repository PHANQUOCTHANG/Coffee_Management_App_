package com.example.javafxapp.Validation;

import com.example.javafxapp.Helpper.AlertInfo;
import javafx.scene.control.Alert;

public class ValidationCategory {

    public static boolean validationCategoryName(String categoryName) {
        if (categoryName.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Tên không rỗng");
            return false ;
        }
        if (categoryName.length() < 6) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Tên ít nhất 6 kí tự");
            return false ;
        }
        return true ;
    }


}
