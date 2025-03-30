package com.example.javafxapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

import java.util.List;

import com.example.javafxapp.dao.ProductDAO;
import com.example.javafxapp.model.Product;

public class CustomerProductController {

    @FXML
    private ListView<String> categoryListView;

    @FXML
    private FlowPane productFlowPane;

    private final ProductDAO productDAO = new ProductDAO();

    @FXML
    public void initialize() {
        List<String> categories = List.of(
                "MÓN NỔI BẬT", "TRÀ SỮA", "CHÈ", "FRESH FRUIT TEA", "MACCHIATO", "SPECIAL DRINK"
        );

        categoryListView.getItems().addAll(categories);

        categoryListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadProductsByCategory(categoryListView.getSelectionModel().getSelectedIndex() + 1);
            }
        });

        // Hiển thị danh mục đầu tiên mặc định
        loadProductsByCategory(1);
    }

    private void loadProductsByCategory(int categoryId) {
        productFlowPane.getChildren().clear();
        List<Product> products = productDAO.getProductsByCategory(categoryId);

        for (Product product : products) {
            VBox productBox = createProductBox(product);
            productFlowPane.getChildren().add(productBox);
        }
    }

    private VBox createProductBox(Product product) {
        ImageView imageView = new ImageView(new Image(product.getImgSrc()));
        imageView.setFitWidth(150);
        imageView.setFitHeight(200);

        Label nameLabel = new Label(product.getProduct_name());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label priceLabel = new Label("Giá: " + product.getPrice() + "đ");

        VBox productBox = new VBox(10, imageView, nameLabel, priceLabel);
        productBox.setAlignment(Pos.CENTER);
        productBox.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1;");
        
        return productBox;
    }
}
