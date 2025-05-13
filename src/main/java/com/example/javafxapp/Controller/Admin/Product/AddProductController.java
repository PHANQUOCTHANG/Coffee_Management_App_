package com.example.javafxapp.Controller.Admin.Product;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.UploadImage;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.CategoryService;
import com.example.javafxapp.Service.ProductService;
import com.example.javafxapp.Validation.ValidationProduct;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddProductController {

    @FXML private TextField productNameField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField priceField;
    @FXML private TextArea descriptionField;
    @FXML private JFXCheckBox activeCheckBox;
    @FXML private JFXCheckBox inactiveCheckBox;
    @FXML private Label statusLabel;
    @FXML private ImageView imgView;
    @FXML private Button btnPathImg;
    @FXML private Button btnCategoryId;
    @FXML private JFXButton btnAdd;
    @FXML private JFXButton btnUpload;
    private String imagePath ;
    private ProductService productService = new ProductService() ;
    private CategoryService categoryService = new CategoryService() ;
    private ProductController productController;
    public void setProductController(ProductController controller) {
        this.productController = controller;
    }

    @FXML
    private void initialize() {
        activeCheckBox.setSelected(true);
        List<String> categories = new ArrayList<>();
        for (Category category : categoryService.getAllCategory()) {
            categories.add(category.getCategory_name());
        }
        categoryComboBox.getItems().addAll(categories);
    }

    @FXML
    private void handleActive(ActionEvent event) {
        if (activeCheckBox.isSelected()) {
            inactiveCheckBox.setSelected(false);
            statusLabel.setText("Hoạt động");
        }
    }

    @FXML
    private void handleInactive(ActionEvent event) {
        if (inactiveCheckBox.isSelected()) {
            activeCheckBox.setSelected(false);
            statusLabel.setText("Không hoạt động");
        }
    }

    // upload ảnh
    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Ảnh (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            // Hiển thị ảnh trên giao diện
            Image image = new Image(file.toURI().toString());
            imgView.setImage(image);
            // Copy file vào thư mục images
            imagePath = UploadImage.saveImageToFolder(file);
            btnPathImg.setText(imagePath);
        }
    }

    @FXML
    public void categoryAction() {
        Category category = categoryService.findCategoryByName((String) categoryComboBox.getValue());
        btnCategoryId.setText(String.valueOf(category.getCategory_id()));
    }

    // thêm 1 sản phẩm .
    @FXML
    public void addProductPost() {
        try {
            String product_name = productNameField.getText().trim();
            if (!ValidationProduct.validationProductName(product_name)) return;
            double price = ValidationProduct.validationPrice(priceField.getText().trim());
            if (price == -1) return;
            String description = descriptionField.getText().trim();
            if (!ValidationProduct.validationCategory(btnCategoryId.getText())) return;
            int category_id = Integer.parseInt(btnCategoryId.getText());
            boolean status = false;
            if (inactiveCheckBox.isSelected()) status = true;
            boolean outstanding = false;
            String imgSrc = btnPathImg.getText();
            Product product = new Product(product_name, description, price, category_id, imgSrc, status, outstanding);
            productService.addProduct(product);
            productController.loadProducts();
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm sản phẩm thành công");
            Stage stage = (Stage) btnAdd.getScene().getWindow();
            stage.close();

        } catch (RuntimeException e) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Thêm sản phẩm thất bại");
            e.printStackTrace();
        }
    }
}
