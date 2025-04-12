package com.example.javafxapp.Controller.Admin;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Helpper.UploadImage;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.CategoryService;
import com.example.javafxapp.Service.ProductService;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OrderController {

    @FXML
    private GridPane grid;

    @FXML
    private TextField searchField , productNameField , priceField;

    @FXML
    private Spinner<Integer> quantitySpinner  ;
    @FXML
    private ComboBox categoryComboBox;

    private ProductService productService = new ProductService();
    private CategoryService categoryService = new CategoryService();

    public void loadData() {


        List<Product> products = productService.getAllProduct(); // Lấy danh sách từ database

        if (products == null || products.isEmpty()) {
            System.out.println("Không có dữ liệu từ database!");
            return;
        }

        int row = 0;
        for (Product product : products) {
            // Cột STT
            Label lblStt = new Label(String.valueOf(row + 1));

            // Cột ảnh (ImageView)
            ImageView imageView = new ImageView(UploadImage.loadImage(product.getImgSrc()));
            imageView.setFitHeight(120);
            imageView.setFitWidth(120);

            // Cột tên
            Label lblName = new Label(product.getProduct_name());

            // Cột giá
            Label lblPrice = new Label(String.format("%.2f", product.getPrice()));

            // Cột hành động (Button)
            JFXButton btnDetail = new JFXButton("Order");
            btnDetail.getStyleClass().add("detail-button");
            btnDetail.setOnAction(e -> handleOrder(product.getProduct_id()));

            // Thêm vào GridPane
            grid.add(lblStt, 0, row);
            grid.add(imageView, 1, row);
            grid.add(lblName, 2, row);
            grid.add(lblPrice, 3, row);
            grid.add(btnDetail, 4, row);

            row++; // Tăng số hàng
        }

        List<String> categories = new ArrayList<>();
        for (Category category : categoryService.getAllCategory()) {
            categories.add(category.getCategory_name());
        }
        categoryComboBox.getItems().addAll(categories);
    }

    @FXML
    public void initialize() {
        if (grid != null) loadData();
    }

    // chi tiết 1 sản phẩm .
    @FXML
    public void handleOrder(int productId) {
        Pages.pageOrder(productId);
    }

    // hàm tìm kiếm sản phẩm .
    @FXML
    public void searchProduct() {
        String textSearch = searchField.getText().trim();
        if (textSearch.isEmpty()) return;
        grid.getChildren().clear();
        Product product = productService.findProductByName(textSearch);
        if (product != null) {
            int row = 0;
            // Cột STT
            Label lblStt = new Label(String.valueOf(row + 1));

            // Cột ảnh (ImageView)
            ImageView imageView = new ImageView(UploadImage.loadImage(product.getImgSrc()));
            imageView.setFitHeight(120);
            imageView.setFitWidth(120);

            // Cột tên
            Label lblName = new Label(product.getProduct_name());

            // Cột giá
            Label lblPrice = new Label(String.format("%.2f", product.getPrice()));

            // Cột hành động (Button)
            JFXButton btnDetail = new JFXButton("Order");
            btnDetail.getStyleClass().add("detail-button");
            btnDetail.setOnAction(e -> handleOrder(product.getProduct_id()));

            // Thêm vào GridPane
            grid.add(lblStt, 0, row);
            grid.add(imageView, 1, row);
            grid.add(lblName, 2, row);
            grid.add(lblPrice, 3, row);
            grid.add(btnDetail, 4, row);

        }

    }

    // Lọc
    @FXML
    void filterAction() {
        String categoryValue = (String) categoryComboBox.getValue();
        if (categoryValue.isEmpty()) return;
        else {
            grid.getChildren().clear();
            Category category = categoryService.findCategoryByName(categoryValue);
            List<Product> products = productService.getAllByCategoryId(category.getCategory_id());
            if (products == null || products.isEmpty()) {
                System.out.println("Không có dữ liệu từ database!");
                return;
            }
            int row = 0;
            for (Product product : products) {
                // Cột STT
                Label lblStt = new Label(String.valueOf(row + 1));

                // Cột ảnh (ImageView)
                ImageView imageView = new ImageView(UploadImage.loadImage(product.getImgSrc()));
                imageView.setFitHeight(120);
                imageView.setFitWidth(120);

                // Cột tên
                Label lblName = new Label(product.getProduct_name());

                // Cột giá
                Label lblPrice = new Label(String.format("%.2f", product.getPrice()));

                // Cột hành động (Button)
                JFXButton btnDetail = new JFXButton("Order");
                btnDetail.getStyleClass().add("detail-button");
                btnDetail.setOnAction(e -> handleOrder(product.getProduct_id()));

                // Thêm vào GridPane
                grid.add(lblStt, 0, row);
                grid.add(imageView, 1, row);
                grid.add(lblName, 2, row);
                grid.add(lblPrice, 3, row);
                grid.add(btnDetail, 4, row);

                row++; // Tăng số hàng
            }
        }
    }

    // chuyển sang trang order sẽ gọi .
    @FXML
    public void loadDataOrder(int productId) {
        try {
            Product product = productService.findProductByID(productId); // Lấy dữ liệu từ database .
            if (product != null) {
                productNameField.setText(product.getProduct_name());
                priceField.setText(String.valueOf(product.getPrice()));
                quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100,1));
            } else {
                System.out.println("Không tìm thấy sản phẩm!");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

}
