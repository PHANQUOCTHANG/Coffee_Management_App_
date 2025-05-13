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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UpdateProductController {

    @FXML private Button btnId;
    @FXML private Button btnCategoryId;
    @FXML private Button btnPathImg;

    @FXML private TextField productNameField;
    @FXML private TextField priceField;
    @FXML private TextArea descriptionField;
    @FXML private ComboBox<String> categoryComboBox;

    @FXML private JFXCheckBox activeCheckBox;
    @FXML private JFXCheckBox inactiveCheckBox;
    @FXML private Label statusLabel;

    @FXML private ImageView imgView;
    @FXML private JFXButton btnUpload;
    @FXML private JFXButton btnUpdate;
    private ProductService productService = new ProductService() ;
    private CategoryService categoryService = new CategoryService() ;

    private String imagePath ;
    public static int product_id = -1  ;
    private ProductController productController;
    public void setProductController(ProductController controller) {
        this.productController = controller;
    }

    @FXML
    public void initialize() {
        try {
            Product product = productService.findProductByID(product_id); // Lấy dữ liệu từ database .
            if (product != null) {
                productNameField.setText(product.getProduct_name());
                priceField.setText(String.valueOf(product.getPrice()));
                descriptionField.setText(product.getDescription());
                imgView.setImage(UploadImage.loadImage(product.getImgSrc()));
                btnPathImg.setText(product.getImgSrc());
                btnCategoryId.setText(String.valueOf(product.getCategory_id()));
                String category_name = categoryService.findCategoryByID(product.getCategory_id()).getCategory_name();
                categoryComboBox.setValue(category_name);
                List<String> categories = new ArrayList<>();
                for (Category category : categoryService.getAllCategory()) {
                    categories.add(category.getCategory_name());
                }
                categoryComboBox.getItems().addAll(categories);
                if (product.isStatus()) {
                    inactiveCheckBox.setSelected(true);
                    activeCheckBox.setSelected(false);
                } else {
                    inactiveCheckBox.setSelected(false);
                    activeCheckBox.setSelected(true);
                }
            } else {
                System.out.println("Không tìm thấy sản phẩm!");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        activeCheckBox.setSelected(true);
        statusLabel.setText("Đang hoạt động");
    }

    @FXML
    private void handleActive(ActionEvent event) {
        if (activeCheckBox.isSelected()) {
            inactiveCheckBox.setSelected(false);
            statusLabel.setText("Đang hoạt động");
        } else {
            statusLabel.setText("Chọn trạng thái");
        }
    }

    @FXML
    private void handleInactive(ActionEvent event) {
        if (inactiveCheckBox.isSelected()) {
            activeCheckBox.setSelected(false);
            statusLabel.setText("Không hoạt động");
        } else {
            statusLabel.setText("Chọn trạng thái");
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

    // update sản phẩm .
    @FXML
    public void updateProduct() {
        try {
            Product productFind = productService.findProductByID(product_id);
            String product_name = productNameField.getText().trim();
            if (!ValidationProduct.validationProductName(product_name)) return;
            double price = ValidationProduct.validationPrice(priceField.getText().trim());
            if (price == -1) return;
            String description = descriptionField.getText().trim();
            if (!ValidationProduct.validationCategory(btnCategoryId.getText())) return;
            int category_id = Integer.parseInt(btnCategoryId.getText());
            String imgSrc = btnPathImg.getText();
            boolean status = false;
            if (inactiveCheckBox.isSelected()) status = true;
            Product product = new Product(product_id, product_name, description, price, category_id, imgSrc, status, productFind.isOutstanding(), false);
            productService.updateProduct(product);
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật sản phẩm thành công");
            productController.loadProducts();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
