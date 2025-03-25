package com.example.javafxapp.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.example.javafxapp.model.Product;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ProductController implements Initializable {
    // dashboard
    @FXML
    private Label lblWelcome;

    @FXML
    private StackPane contentArea;

    @FXML
    private Button btnManageUsers, btnManageRoles, btnReports, btnSettings, btnLogout;

    // main 
    @FXML
    private JFXButton btnManageUsers1;

    @FXML
    private ScrollPane scroll;

    @FXML
    private GridPane gridItem;

    @FXML
    private void handleManageUsers() {
        if (lblWelcome != null)
            lblWelcome.setText("Đây là trang Quản lý người dùng");
    }

    @FXML
    private void handleManageRoles() {
        if (lblWelcome != null)
            lblWelcome.setText("Đây là trang Quản lý vai trò");
    }

    @FXML
    private void handleReports() {
        if (lblWelcome != null)
            lblWelcome.setText("Đây là trang Báo cáo");
    }

    @FXML
    private void handleSettings() {
        if (lblWelcome != null)
            lblWelcome.setText("Đây là trang Cài đặt");
    }
    @FXML
    private void handleLogout() {
        // Đóng cửa sổ Dashboard
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        stage.close();

        // Quay lại màn hình đăng nhập
        System.out.println("Đã đăng xuất!");
    }

    private List<Product> products = new ArrayList<>();


    //test data
    private List<Product> getData(){
        List<Product> products = new ArrayList<>();
        Product product;

        for (int i = 0; i < 10; i++) {
            product = new Product();
            product.setId(i);
            product.setName("Product " + i);
            product.setDescription("Description " + i);
            product.setPrice(25000 + i);
            product.setCategory("Category " + i);
            product.setImgSrc("/com/example/javafxapp/view/images/milkcoffee.png");
            product.setStock(10 + i);
            products.add(product);
        }

        return products;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        products.addAll(getData());

        // so hang va cot hien thi san pham
        int column = 0;
        int row = 0;

        try{
            for (int i = 0; i < products.size(); i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/example/javafxapp/view/product/itemsProduct.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
    
                ItemsProductController itemsProductController = fxmlLoader.getController();
                itemsProductController.setData(products.get(i));

                if (column == 6){
                    column = 0;
                    row++;
                }

                gridItem.add(anchorPane, column++, row);
                GridPane.setMargin(anchorPane, new Insets(10, 20, 10, 20));
    
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
