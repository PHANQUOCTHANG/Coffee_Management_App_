package com.example.javafxapp.controller;

import java.io.IOException;
import java.net.URL;

import com.example.javafxapp.alert.AlertInfo;
import com.example.javafxapp.config.DatabaseConnection;
import com.example.javafxapp.dao.AuthDAO;
import com.example.javafxapp.pages.Pages;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class Login_SignupController implements Initializable {
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

    private AuthDAO authDAO = new AuthDAO() ;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        if (aplogin == null) {
            System.err.println("aplogin is null");
        } else {
            System.out.println("aplogin is initialized");
        }
        if (apsignup == null) {
            System.err.println("apsignup is null");
        } else {
            System.out.println("apsignup is initialized");
        }
        aplogin.setVisible(true);
        apsignup.setVisible(false);
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
    private void Login() throws SQLException {
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
            Pages.openDashboard(loginStage);
            System.out.println("Success");
        } else {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Tên đăng nhập hoặc mật khẩu không đúng.");
            System.out.println("No Success");
        }
    }


}

