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
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        //  login2
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/javafxapp/view/login_signup/signin.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Coffee Shop Management");
//        primaryStage.show();


          // dashboard .
          FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/javafxapp/view/dashboard/dashboard.fxml"));
          Scene scene = new Scene(fxmlLoader.load());
          primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
          primaryStage.setScene(scene);
//          primaryStage.setFullScreen(true); // Hiển thị toàn màn hình
          primaryStage.setTitle("Coffee Shop Management");

          primaryStage.show();

        // quan li san pham
        //    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/javafxapp/view/product/product.fxml"));
        //    Scene scene = new Scene(fxmlLoader.load());
        //    primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
        //    primaryStage.setScene(scene);
        //    primaryStage.setTitle("Coffee Shop Management");
        //    primaryStage.show();

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

        // sign up .
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/javafxapp/view/login_signup/signup.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Coffee Shop Management");
//        primaryStage.show();

        System.out.println(getClass().getResource("/com/example/javafxapp/view/styles/style.css"));


        // Thêm file CSS vào Scene
        scene.getStylesheets().add(getClass().getResource("/com/example/javafxapp/view/styles/style.css").toExternalForm());

    }

    public static void main(String[] args) {
        launch();
    }
}