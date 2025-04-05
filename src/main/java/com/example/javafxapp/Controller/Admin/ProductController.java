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

public class ProductController {

    @FXML
    private GridPane grid;

    @FXML
    private TextField searchField;

    @FXML
    private TextField productNameField;

    @FXML
    private TextField priceField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private ComboBox categoryComboBox ;

    @FXML private Button btnUpload  ,  btnId , btnCategoryId , btnPathImg , btnAdd ;
    @FXML private ImageView imgView;

    private String imagePath; // Đường dẫn ảnh


    private ProductService productService = new ProductService();
    private CategoryService categoryService = new CategoryService() ;

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
            JFXButton btnDetail = new JFXButton("Chi tiết");
            btnDetail.getStyleClass().add("detail-button");
            btnDetail.setOnAction(e -> handleDetail(product.getProduct_id()));

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

    // thêm 1 sản phẩm .
    @FXML
    public void addProductPost() {
        try {
            String product_name = productNameField.getText() ;
            double price = Double.parseDouble(priceField.getText()) ;
            String description = descriptionField.getText() ;
            int category_id = Integer.parseInt(btnCategoryId.getText()) ;
            String imgSrc = btnPathImg.getText() ;
            Product product = new Product(product_name , description , price , category_id , imgSrc) ;
            productService.addProduct(product);
            AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Thêm sản phẩm thành công");
            Stage stage = (Stage) btnAdd.getScene().getWindow() ;
            stage.close();
        }
        catch (RuntimeException e) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Thêm sản phẩm thất bại");
            e.printStackTrace();
        }
    }


    // chi tiết 1 sản phẩm .
    @FXML
    public void handleDetail(int productId) {
        Pages.pageDetailProduct(productId);
    }

    // khi chuyển qua trang thêm sản phẩm sẽ mặc định gọi .
    @FXML
    public void loadDataAddProduct() {
            btnCategoryId.setVisible(false);
            btnPathImg.setVisible(false);
            List<String> categories = new ArrayList<>() ;
            for (Category category : categoryService.getAll()) {
                categories.add(category.getCategory_name()) ;
            }
            categoryComboBox.getItems().addAll(categories);
    }

    // khi chuyển qua trang chi tiết sẽ mặc định gọi .
    @FXML
    public void loadDataDetailProduct(int productId) {
        Product product = productService.findProductByID(productId)  ; // Lấy dữ liệu từ database .
        if (product != null) {
            productNameField.setText(product.getProduct_name());
            priceField.setText(String.valueOf(product.getPrice()));
            descriptionField.setText(product.getDescription());
            imgView.setImage(UploadImage.loadImage(product.getImgSrc()));
            btnId.setText(String.valueOf(productId));
            btnId.setVisible(false);
            btnCategoryId.setText(String.valueOf(product.getCategory_id()));
            btnCategoryId.setVisible(false);
            String category_name = categoryService.findCategoryByID(product.getCategory_id()).getCategory_name()  ;
            categoryComboBox.setValue(category_name);
            btnPathImg.setText(product.getImgSrc());
            btnPathImg.setVisible(false);
            List<String> categories = new ArrayList<>() ;
            for (Category category : categoryService.getAll()) {
                categories.add(category.getCategory_name()) ;
            }
            categoryComboBox.getItems().addAll(categories);
        } else {
            System.out.println("Không tìm thấy sản phẩm!");
        }
    }

    // xóa 1 sản phẩm .
    @FXML
    public void deleteProduct() {
        int productId = Integer.parseInt(btnId.getText()) ;
        if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa sản phẩm không ?")) {
            productService.deleteProduct(productId);
            AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Xóa sản phẩm thành công");
            Stage stage = (Stage)btnId.getScene().getWindow() ;
            stage.close() ;
        }
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
            JFXButton btnDetail = new JFXButton("Chi tiết");
            btnDetail.getStyleClass().add("detail-button");
            btnDetail.setOnAction(e -> handleDetail(product.getProduct_id()));

            // Thêm vào GridPane
            grid.add(lblStt, 0, row);
            grid.add(imageView, 1, row);
            grid.add(lblName, 2, row);
            grid.add(lblPrice, 3, row);
            grid.add(btnDetail, 4, row);

        }

    }

    // upload image
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

    // lấy category_id của category mình chọn .
    @FXML
    public void categoryAction() {
        Category category = categoryService.findCategoryByName((String)categoryComboBox.getValue()) ;
        btnCategoryId.setText(String.valueOf(category.getCategory_id()));
    }

    // update sản phẩm .
    @FXML
    public void updateProduct() {
        try {
            int product_id = Integer.parseInt(btnId.getText()) ;
            String product_name = productNameField.getText() ;
            double price = Double.parseDouble(priceField.getText()) ;
            String description = descriptionField.getText() ;
            int category_id = Integer.parseInt(btnCategoryId.getText()) ;
            String imgSrc = btnPathImg.getText() ;
            Product product = new Product(product_id , product_name , description , price , category_id , imgSrc) ;
            productService.updateProduct(product);
            AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Cập nhật sản phẩm thành công");
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
