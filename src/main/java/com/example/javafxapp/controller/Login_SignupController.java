package com.example.javafxapp.controller;

import java.net.URL;
import javafx.util.Duration;
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
}
