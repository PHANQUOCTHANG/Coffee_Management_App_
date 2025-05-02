package com.example.javafxapp.Validation;

import com.example.javafxapp.Helpper.AlertInfo;
import javafx.scene.control.Alert;

import java.util.regex.Pattern;

public class ValidationEmployee {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(0|\\+84)[3|5|7|8|9][0-9]{8}$");
    public static boolean validationEmployeeName(String employeeName) {
        if (employeeName.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Tên không rỗng");
            return false ;
        }
        if (employeeName.length() < 6) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Tên ít nhất 6 kí tự");
            return false ;
        }
        return true ;
    }

    public static boolean validationPhone (String phone) {
        if (phone.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Số điện thoại không để trống");
            return false ;
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Số điện thoại không đúng định dạng");
            return false ;
        }
        return true ;
    }
}

