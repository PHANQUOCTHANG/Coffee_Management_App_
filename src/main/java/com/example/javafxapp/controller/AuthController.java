package com.example.javafxapp.controller;

import java.io.IOException;
import java.net.URL;

import com.example.javafxapp.alert.AlertInfo;
import com.example.javafxapp.dao.AuthDAO;
import com.example.javafxapp.model.Account;
import com.example.javafxapp.pages.Pages;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.*;
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

public class AuthController implements Initializable {
    @FXML
    private AnchorPane aplogin;
    @FXML
    private AnchorPane apsignup;
    @FXML
    private ImageView img;

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
    private Label lblStatus;

    @FXML
    private Button btnLogin , btnSignup ;

    private AuthDAO authDAO = new AuthDAO() ;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

//        if (aplogin == null) {
//            System.err.println("aplogin is null");
//        } else {
//            System.out.println("aplogin is initialized");
//        }
//        if (apsignup == null) {
//            System.err.println("apsignup is null");
//        } else {
//            System.out.println("apsignup is initialized");
//        }
//        aplogin.setVisible(true);
//        apsignup.setVisible(false);
    }

    // show sign-up
    @FXML
    public void showSignup(){
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), aplogin);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(event -> {
            aplogin.setVisible(false);
            apsignup.setVisible(true);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), apsignup);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        });

        fadeOut.play();

        TranslateTransition moveLeft = new TranslateTransition(Duration.seconds(1.5), img); 
        moveLeft.setInterpolator(Interpolator.EASE_BOTH);
        moveLeft.setToX(-757); 
        moveLeft.play();
    }


    // show login .
    @FXML
    public void showLogin() {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), apsignup);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(event -> {
            apsignup.setVisible(false);
            aplogin.setVisible(true);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), aplogin);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        });

        fadeOut.play();

        TranslateTransition moveRight = new TranslateTransition(Duration.seconds(1.5), img); 
        moveRight.setInterpolator(Interpolator.EASE_BOTH);
        moveRight.setToX(0);
        moveRight.play();
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
            Stage loginStage = (Stage) btnLogin.getScene().getWindow();
            Pages.pageDashboard(loginStage);
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
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Mật khẩu xác nhận chưa đúng.");
        }

        Account newAccount = new Account(loginName, password,2);
        int generatedId = authDAO.signUp(newAccount);

        if (generatedId != -1) {
//            lblStatus.setText("Thêm tài khoản thành công với ID: " + generatedId);
            signUpLoginNameField.clear();
            signUpPassWordField.clear();
            confirmPassWordField.clear();
            Stage stage = (Stage) btnSignup.getScene().getWindow();
            Pages.pageLogin(stage);
        } else {
//            lblStatus.setText("Lỗi khi thêm tài khoản!");
        }



    }

}

