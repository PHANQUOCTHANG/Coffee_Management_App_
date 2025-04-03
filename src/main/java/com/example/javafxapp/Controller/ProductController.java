package com.example.javafxapp.Controller;

import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Repository.ProductRepository;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.ProductService;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;

public class ProductController {

    @FXML
    private GridPane grid ;

    @FXML
    private TextField searchField ;


    private ProductService productService = new ProductService() ;

    public void loadData() {

            List<Product> products = productService.getAllProduct(); // Lấy danh sách từ database

            if (products == null || products.isEmpty()) {
                System.out.println("Không có dữ liệu từ database!");
                return;
            }

            int row = 0;
            for (Product product : products ) {
                // Cột STT
                Label lblStt = new Label(String.valueOf(row + 1));

                // Cột ảnh (ImageView)
                ImageView imageView = new ImageView(new Image(product.getImgSrc()));
                imageView.setFitHeight(120);
                imageView.setFitWidth(120);

                // Cột tên
                Label lblName = new Label(product.getProduct_name());

                // Cột giá
                Label lblPrice = new Label(String.format("%.2f", product.getPrice()));

                // Cột hành động (Button)
                JFXButton btnDetail = new JFXButton("Chi tiết");
                btnDetail.getStyleClass().add("detail-button");
//            btnDetail.setOnAction(e->handleDetail(product));

                // Thêm vào GridPane
                grid.add(lblStt, 0, row);
                grid.add(imageView, 1, row);
                grid.add(lblName, 2, row);
                grid.add(lblPrice, 3, row);
                grid.add(btnDetail, 4, row);

                row++; // Tăng số hàng
            }
    }

    @FXML
    public void initialize() {
        if (grid != null) loadData();
    }

    // chuyển qua trang thêm 1 product .
    @FXML
    public void addProduct() {
        Pages.pageAddProduct();
    }

//    public void handleDetail() {
//
//    }

    // hàm tìm kiếm sản phẩm .
    @FXML
    public void searchProduct () {
        String textSearch = searchField.getText().trim() ;
        if (textSearch.isEmpty()) return ;
        grid.getChildren().clear();
        Product product = productService.findProductByName(textSearch) ;
        if (product != null) {
            int row = 0 ;
            // Cột STT
            Label lblStt = new Label(String.valueOf(row + 1));

            // Cột ảnh (ImageView)
            ImageView imageView = new ImageView(new Image(product.getImgSrc()));
            imageView.setFitHeight(120);
            imageView.setFitWidth(120);

            // Cột tên
            Label lblName = new Label(product.getProduct_name());

            // Cột giá
            Label lblPrice = new Label(String.format("%.2f", product.getPrice()));

            // Cột hành động (Button)
            JFXButton btnDetail = new JFXButton("Chi tiết");
            btnDetail.getStyleClass().add("detail-button");
//            btnDetail.setOnAction(e->handleDetail(product));

            // Thêm vào GridPane
            grid.add(lblStt, 0, row);
            grid.add(imageView, 1, row);
            grid.add(lblName, 2, row);
            grid.add(lblPrice, 3, row);
            grid.add(btnDetail, 4, row);

        }

    }
}
