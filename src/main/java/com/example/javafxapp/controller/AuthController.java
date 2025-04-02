package com.example.javafxapp.controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.dao.AuthDAO;
import com.example.javafxapp.model.Account;
import com.example.javafxapp.pages.Pages;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class AuthController{
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
    private StackPane authContainer;

    @FXML
    private BorderPane loginPane, signUpPane;

    private boolean isLoginVisible = true; // Biến kiểm tra trạng thái

    @FXML
    public void initialize() {
        // Đẩy Sign Up Pane ra ngoài phải khi bắt đầu
        signUpPane.setTranslateX(authContainer.getWidth());

        // Đảm bảo layout ổn định trước khi chạy hiệu ứng
        authContainer.widthProperty().addListener((obs, oldVal, newVal) -> {
            signUpPane.setTranslateX(newVal.doubleValue());
        });
    }

    @FXML
    public void showSignUp() {
        if (isLoginVisible) {
            TranslateTransition slideOut = new TranslateTransition(Duration.seconds(0.5), loginPane);
            slideOut.setToX(-authContainer.getWidth()); // Trượt Login ra ngoài trái

            TranslateTransition slideIn = new TranslateTransition(Duration.seconds(0.5), signUpPane);
            slideIn.setToX(0); // Đưa Sign Up vào màn hình

            slideOut.play();
            slideIn.play();

            isLoginVisible = false;
        }
    }

    @FXML
    public void showLogin() {
        if (!isLoginVisible) {
            TranslateTransition slideOut = new TranslateTransition(Duration.seconds(0.5), signUpPane);
            slideOut.setToX(authContainer.getWidth()); // Trượt Sign Up ra ngoài phải

            TranslateTransition slideIn = new TranslateTransition(Duration.seconds(0.5), loginPane);
            slideIn.setToX(0); // Đưa Login vào màn hình

            slideOut.play();
            slideIn.play();

            isLoginVisible = true;
        }
    }

    private AuthDAO authDAO = new AuthDAO() ;



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
            showLogin();
        } else {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Đăng kí thất bại");
        }



    }

}

