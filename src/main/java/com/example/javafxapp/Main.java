package com.example.javafxapp;

import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Model.Account;
import com.example.javafxapp.Repository.AccountRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // quan li san pham 2
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/javafxapp/view/login_signup/auth.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
        primaryStage.setScene(scene);
        primaryStage.setTitle("Coffee Shop Management");
        primaryStage.show();

//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/javafxapp/view/admin/mainScreen/mainScreen.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
//        primaryStage.setScene(scene);
//        // Full màn hình
//        primaryStage.setMaximized(true);
//        primaryStage.setTitle("Coffee Shop Management");
//        primaryStage.show() ;

//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/javafxapp/view/home.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
//        primaryStage.setScene(scene);
//        scene.getStylesheets().add(Pages.class.getResource("/com/example/javafxapp/view/styles/style.css").toExternalForm());
//        // Full màn hình
//        primaryStage.setMaximized(true);
//        primaryStage.setTitle("Coffee Shop Management");
//        primaryStage.show();

        // quan li san pham 2
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/javafxapp/view/client/cart/cart.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
//        primaryStage.setScene(scene);
//        primaryStage.setMaximized(true);
//        primaryStage.setTitle("Coffee Shop Management");
//        primaryStage.show();

//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/javafxapp/view/client/checkout/checkout.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
//        primaryStage.setScene(scene);
//        primaryStage.setMaximized(true);
//        primaryStage.setTitle("Coffee Shop Management");
//        primaryStage.show();


    }

    public static void main(String[] args) {
//        AccountRepository accountRepository = new AccountRepository() ;
//        accountRepository.add(new Account("Admin1234" , "123456" , 1));
        launch();
    }
}