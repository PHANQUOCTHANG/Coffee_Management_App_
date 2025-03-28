package com.example.javafxapp.controller;

import java.io.IOException;
import java.net.URL;

import com.example.javafxapp.alert.AlertInfo;
import com.example.javafxapp.dao.AuthDAO;
import com.example.javafxapp.model.Account;
import com.example.javafxapp.pages.Pages;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class AuthController {
    @FXML
    private TextField loginNameField ;

    @FXML
    private PasswordField passWordField ;

    @FXML
    private TextField signUpLoginNameField ;

    @FXML
    private PasswordField signUpPassWordField ;

    @FXML
    private PasswordField confirmPassWordField ;

    @FXML
    private Button btnLogin , btnSignup ;

    private AuthDAO authDAO = new AuthDAO() ;

    // show sign-up
    public void showSignUp() {
        Stage stage = (Stage) btnSignup.getScene().getWindow() ;
        stage.close();
        Pages.pageSignUp();
    }

    // show login .
    public void showLogin() {
        Stage stage = (Stage) btnLogin.getScene().getWindow() ;
        stage.close();
        Pages.pageLogin();
    }


    // login .
    @FXML
    private void Login(){
        String loginName = loginNameField.getText().trim();
        String password = passWordField.getText().trim();

        if (loginName.isEmpty() || password.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng nhập đầy đủ thông tin.");
            return;
        }
        boolean result = authDAO.isValidAccount(loginName , password) ;

        // check account .
        if (result) {
            Stage loginStage = (Stage) loginNameField.getScene().getWindow();
            loginStage.close();
            Pages.pageDashboard();
            AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Đăng nhập thành công");
        } else {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Tên đăng nhập hoặc mật khẩu không đúng.");
        }
    }

    // sign up .
    public void signUp() {
        String loginName = signUpLoginNameField.getText().trim();
        String password = signUpPassWordField.getText().trim();
        String confirmPassword = confirmPassWordField.getText().trim();

        if (loginName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Mật khẩu xác nhận không khớp.");
        }

        Account newAccount = new Account(loginName, password,2);
        int generatedId = authDAO.signUp(newAccount);

        if (generatedId != -1) {
            signUpLoginNameField.clear();
            signUpPassWordField.clear();
            confirmPassWordField.clear();
            AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Đăng kí thành công");
            Stage stage = (Stage) signUpLoginNameField.getScene().getWindow();
            stage.close(); // đóng trang sign up , chuyển qua login .
            Pages.pageLogin();
        } else {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Đăng kí thất bại");
        }



    }

}

