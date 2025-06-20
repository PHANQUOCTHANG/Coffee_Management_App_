package com.example.javafxapp.Validation;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Utils.IntegerUtils;
import javafx.scene.control.Alert;

import java.util.regex.Pattern;

public class ValidationMember {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(0|\\+84)[3|5|7|8|9][0-9]{8}$");

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

    public static boolean validationPoint (String point) {
        if (point.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Số điện thoại không để trống");
            return false ;
        }
        if (!IntegerUtils.isInteger(point)) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Điểm phải là số");
            return false ;
        }
        return true ;
    }
}
