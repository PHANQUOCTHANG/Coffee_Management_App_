package com.example.javafxapp.Validation;

import com.example.javafxapp.Helpper.AlertInfo;
import javafx.scene.control.Alert;

import java.util.regex.Pattern;

public class ValidationInformationUser {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(0|\\+84)[3|5|7|8|9][0-9]{8}$");
    public static boolean checkFullName (String fullName) {
        if (fullName.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Họ tên không để trống");
            return false ;
        }
        if (fullName.length() < 8) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Họ tên ít nhất 8 kí tự");
            return false ;
        }
        return true ;
    }

    public static boolean checkEmail (String email) {
        if (email.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Email không để trống");
            return false ;
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Email không đúng định dạng");
            return false ;
        }
        return true ;
    }

    public static boolean checkPhone (String phone) {
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

    public static boolean checkAddress (String address) {
        if (address.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Địa chỉ không để trống");
            return false ;
        }
        if (address.length() < 8) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Địa chỉ ít nhất 8 kí tự");
            return false ;
        }
        return true ;
    }
}
