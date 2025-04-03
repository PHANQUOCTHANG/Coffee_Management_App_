package com.example.javafxapp.Utils;

import com.example.javafxapp.Controller.AccountController;
import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Repository.AccountRepository;
import javafx.scene.control.Alert;

public class ValidationUtils {

    private static AccountController accountController = new AccountController() ;

    public static boolean loginUtils (String loginName , String password) {
        if (loginName.isEmpty() || password.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng nhập đầy đủ thông tin.");
            return false ;
        }
        if (loginName.length() < 8) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Tên đặng nhập ít nhất 8 kí tự");
            return false ;
        }
        if (loginName.length() < 6) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Mật khẩu ít nhất 6 kí tự");
            return false ;
        }

        return true ;
    }

    public static boolean signUpUtils (String loginName , String password , String confirmPassword) {
        if (loginName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng nhập đầy đủ thông tin.");
            return false ;
        }
        if (loginName.length() < 8) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Tên đặng nhập ít nhất 8 kí tự");
            return false ;
        }
        if (loginName.length() < 6) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Mật khẩu ít nhất 6 kí tự");
            return false ;
        }
        if (confirmPassword.length() < 6) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Mật khẩu xác nhận ít nhất 6 kí tự");
            return false ;
        }
        if (!password.equals(confirmPassword)) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Mật khẩu xác nhận không khớp.");
            return false ;
        }
        if (accountController.existsNameAccount(loginName)) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Tên đăng nhập đã tồn tại");
            return false ;
        }
        return true ;
    }
}
