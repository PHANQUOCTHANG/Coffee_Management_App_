package com.example.javafxapp.Validation;

import com.example.javafxapp.Helpper.AlertInfo;
import javafx.scene.control.Alert;

public class ValidationRole {

    public static boolean validationRoleName(String roleName) {
        if (roleName.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Tên không rỗng");
            return false ;
        }
        if (roleName.length() < 4) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Tên ít nhất 4 kí tự");
            return false ;
        }
        return true ;
    }


}
