package com.example.javafxapp.Controller.Admin.Order;

import com.example.javafxapp.Controller.Admin.BaseController;
import com.example.javafxapp.Helpper.TextNormalizer;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.ProductService;
import com.example.javafxapp.Utils.LogUtils;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductOrderDetailController extends BaseController {
    @FXML
    private GridPane grid1;
    @FXML
    private TextField searchField;
    @FXML
    private JFXComboBox<String> typeComboBox;

    private List<Product> products = new ArrayList<>();
    private final ProductService productService = new ProductService();

    @Override
    protected void initializeComponents() {
        // Initialize type combo box
        typeComboBox.setItems(FXCollections.observableArrayList(
                "All", "Food", "Drink", "Other"));
        typeComboBox.setValue("All");

        // Load initial products
        loadProducts();
    }

    @Override
    protected void setupEventHandlers() {
        // Setup search field handler
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProducts(newValue);
        });

        // Setup type combo box handler
        typeComboBox.setOnAction(event -> {
            filterProducts(searchField.getText());
        });
    }

    @Override
    protected void loadData() {
        loadProducts();
    }

    private void loadProducts() {
        try {
            String selectedType = typeComboBox.getValue();
            if (selectedType != null) {
                if (selectedType.equals("All")) {
                    products = productService.getAllProducts();
                } else {
                    products = productService.getProductsByType(selectedType);
                }
                loadProductList();
                LogUtils.logInfo("Loaded " + products.size() + " products");
            }
        } catch (Exception e) {
            LogUtils.logError("Error loading products", e);
            showError("Lỗi tải dữ liệu", "Không thể tải danh sách sản phẩm. Vui lòng thử lại sau.");
        }
    }

    private void filterProducts(String searchText) {
        try {
            if (searchText == null || searchText.trim().isEmpty()) {
                loadProducts();
                return;
            }

            String keyword = TextNormalizer.normalize(searchText.trim());
            List<Product> filtered = products.stream()
                    .filter(product -> TextNormalizer.normalize(product.getProduct_name()).contains(keyword))
                    .toList();

            products = filtered;
            loadProductList();
            LogUtils.logInfo("Filtered products with keyword: " + keyword);
        } catch (Exception e) {
            LogUtils.logError("Error filtering products", e);
            showError("Lỗi tìm kiếm", "Không thể tìm kiếm sản phẩm. Vui lòng thử lại sau.");
        }
    }

    private void loadProductList() {
        int row = 0;
        grid1.getChildren().clear();
        for (Product product : products) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/com/example/javafxapp/view/orders/orderDetail/products/product.fxml"));
                HBox hbox = loader.load();
                ProductOrderDetailItemController controller = loader.getController();
                controller.setProduct(product);
                controller.setOrderDetailController((OrderDetailController) getParentController().getCenterContent());
                grid1.add(hbox, 0, row++);
                grid1.setMargin(hbox, new Insets(5));
            } catch (IOException e) {
                LogUtils.logError("Error loading product item", e);
            }
        }
    }
}
