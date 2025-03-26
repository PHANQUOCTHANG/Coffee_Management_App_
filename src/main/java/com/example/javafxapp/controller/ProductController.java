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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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

    // phan xu li product 
    @FXML
    private JFXButton btnManageUsers1;

    @FXML
    private ScrollPane scroll;

    @FXML
    private GridPane gridItem;

    @FXML
    private TextField nameText;

    @FXML
    private TextField priceText;

    @FXML
    private TextField desText;

    @FXML
    private TextField typeText;

     @FXML
    private HBox ImgBox;

    @FXML
    private ImageView imgSelected;

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

    // ham select item o ben duoi ham nay
    public void setChosenProduct(Product product){
        nameText.setText(product.getName());
        nameText.setEditable(false);
        priceText.setText(String.valueOf(product.getPrice()));
        priceText.setEditable(false);
        desText.setText(product.getDescription());
        desText.setEditable(false);
        typeText.setText(product.getCategory());
        typeText.setEditable(false);
        Image imageSelected = new Image(getClass().getResourceAsStream(product.getImgSrc()));
        imgSelected.setImage(imageSelected);
        
        // tinh trung binh mau de lam nen cho hinh duoc chon
        PixelReader pixelReader = imageSelected.getPixelReader();
        int width = (int) imageSelected.getWidth();
        int height = (int) imageSelected.getHeight();

        int sumRed = 0;
        int sumGreen = 0;
        int sumBlue = 0;
        int pixelCount = 0;

        for (int y = 0; y < height; y += 3){
            for (int x = 0; x < width; x += 3){
                int argb = pixelReader.getArgb(x, y);
                sumRed += (argb >> 16) & 0xFF;
                sumGreen += (argb >> 8) & 0xFF;
                sumBlue += argb & 0xFF;
                pixelCount++;
            }
        }

        // // Tinh trung binh mau
        int avgRed = sumRed / pixelCount;
        int avgGreen = sumGreen / pixelCount;
        int avgBlue = sumBlue / pixelCount;

        String avgColor = String.format("-fx-background-color: rgb(%d, %d, %d);", avgRed, avgGreen, avgBlue);
        ImgBox.setStyle(avgColor + ";\n"+
                        "-fx-background-radius: 30;");
        
        
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        products.addAll(getData());

        // so hang va cot hien thi san pham
        int column = 0;
        int row = 1;

        try{
            for (int i = 0; i < products.size(); i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/example/javafxapp/view/product/itemsProduct.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
    
                ItemsProductController itemsProductController = fxmlLoader.getController();
                itemsProductController.setData(products.get(i), this::setChosenProduct);

                if (column == 4){
                    column = 0;
                    row++;
                }

                gridItem.add(anchorPane, column++, row);
                // set item grid width
                gridItem.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridItem.setPrefWidth(Region.USE_COMPUTED_SIZE);
                gridItem.setMaxWidth(Region.USE_PREF_SIZE);


                // set item grid height
                gridItem.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridItem.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridItem.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
    
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
