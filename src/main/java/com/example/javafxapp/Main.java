package com.example.javafxapp;

import com.example.javafxapp.config.DatabaseConnection;
import com.example.javafxapp.pages.Pages;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // quan li san pham
       FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/javafxapp/view/product/product.fxml"));
       Scene scene = new Scene(fxmlLoader.load());
       primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
       primaryStage.setScene(scene);
       primaryStage.setTitle("Coffee Shop Management");
       primaryStage.show();

        //  login
        //  FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/javafxapp/view/login_signup/login_signup.fxml"));
        //  Scene scene = new Scene(fxmlLoader.load());
        //  primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
        //  primaryStage.setScene(scene);
        //  primaryStage.setTitle("Coffee Shop Management");
        //  primaryStage.setResizable(false);
        //  primaryStage.show();

        // add quyen
        // FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/javafxapp/view/Role/RoleForm.fxml"));
        // Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        // primaryStage.setTitle("Thêm Quyền");
        // primaryStage.setScene(scene);
        // primaryStage.show();

        // add account
        // FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/javafxapp/view/account/addAccount.fxml"));
        // Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        // primaryStage.setTitle("Thêm Quyền");
        // primaryStage.setScene(scene);
        // primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
