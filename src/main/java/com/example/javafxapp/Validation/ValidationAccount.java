package com.example.javafxapp.Validation;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Service.AccountService;
import javafx.scene.control.Alert;

public class ValidationAccount {

    private static AccountService accountService  =  new AccountService() ;

    // validation check login name , password  , khi đăng nhập .
    public static boolean loginUtils (String loginName , String password) {
        if (loginName.isEmpty() || password.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng nhập đầy đủ thông tin.");
            return false ;
        }
        if (loginName.length() < 8) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Tên đặng nhập ít nhất 8 kí tự");
            return false ;
        }
        if (password.length() < 6) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Mật khẩu ít nhất 6 kí tự");
            return false ;
        }

        return true ;
    }

    // validation check login name , password  , khi đăng kí .
    public static boolean signUpUtils (String loginName , String password , String confirmPassword) {
        if (!loginNameUtils(loginName) || !passwordUtils(password)) return false ;
        if (confirmPassword.length() < 6) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Mật khẩu xác nhận ít nhất 6 kí tự");
            return false ;
        }
        if (!password.equals(confirmPassword)) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Mật khẩu xác nhận không khớp.");
            return false ;
        }
        if (accountService.existsNameAccount(loginName)) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Tên đăng nhập đã tồn tại");
            return false ;
        }
        return true ;
    }

    // validation check login name , password  , khi cập nhật hoặc tạo account bằng admin .
    public static boolean accountUtils (String loginName , String password , String roleName , int account_id) {
        if (roleName.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Vui lòng chọn vai trò cho tài khoản");
            return false ;
        }
        if (!loginNameUtils(loginName) || !passwordUtils(password)) return false ;
        if (accountService.existsNameAccountOther(account_id , loginName)) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Tên đăng nhập đã tồn tại");
            return false ;
        }
        return true ;
    }

    // check login name
    public static boolean loginNameUtils (String loginName) {
        if (loginName.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng nhập đầy đủ thông tin.");
            return false ;
        }
        if (loginName.length() < 8) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Tên đặng nhập ít nhất 8 kí tự");
            return false ;
        }
        return true ;
    }

    // check login name
    public static boolean passwordUtils (String password) {
        if (password.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng nhập đầy đủ thông tin.");
            return false ;
        }
        if (password.length() < 6) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Mật khẩu ít nhất 6 kí tự");
            return false ;
        }
        return true ;
    }

    // login name check exists
//    public static boolean accountUtils (String loginName , int accountId) {
//        if (accountService.existsNameAccountOther(accountId,loginName)) {
//            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Tên đăng nhập đã tồn tại");
//            return false ;
//        }
//        return true ;
//    }
}
