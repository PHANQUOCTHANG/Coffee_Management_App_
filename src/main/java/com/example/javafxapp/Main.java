package com.example.javafxapp;

import com.example.javafxapp.config.DatabaseConnection;
import com.example.javafxapp.pages.Pages;
import com.jfoenix.controls.JFXButton;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

import org.controlsfx.control.PopOver;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // quan li san pham 2
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/javafxapp/view/product/product.fxml"));
       Scene scene = new Scene(fxmlLoader.load());
       primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
       primaryStage.setScene(scene);
       primaryStage.setTitle("Coffee Shop Management");
       primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}