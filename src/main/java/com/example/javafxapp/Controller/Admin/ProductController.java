package com.example.javafxapp.Controller.Admin;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Helpper.UploadImage;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.CategoryService;
import com.example.javafxapp.Service.ProductService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductController {
    private ProductService productService = new ProductService();
    private CategoryService categoryService = new CategoryService();
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
    private ComboBox categoryComboBox, showBox;
    @FXML
    private Button btnUpload, btnId, btnCategoryId, btnPathImg, btnAdd;
    @FXML
    private ImageView imgView;
    @FXML
    private JFXCheckBox checkBoxAll , activeCheckBox , inactiveCheckBox;
    private List<JFXCheckBox> checkBoxes;
    private String imagePath; // Đường dẫn ảnh
    private String newOperation; // lưu thao tác mới nhất (lọc hoặc tìm kiếm)

    public void loadData() {
        grid.getChildren().clear();
        checkBoxes = new ArrayList<>();
        List<Product> products = productService.getAllProduct(); // Lấy danh sách từ database

        if (products == null || products.isEmpty()) {
            System.out.println("Không có dữ liệu từ database!");
            return;
        }

        int row = 0;
        for (Product product : products) {
            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.setId(String.valueOf(product.getProduct_id()));
            checkBoxes.add(checkBox);

            Label lblStt = new Label(String.valueOf(row + 1) + '.');
            ImageView imageView = new ImageView(UploadImage.loadImage(product.getImgSrc()));
            imageView.setFitHeight(120);
            imageView.setFitWidth(120);

            Label lblName = new Label(product.getProduct_name());
            Label lblPrice = new Label(String.format("%.2f", product.getPrice()));

            JFXButton btnDetail = new JFXButton("Chi tiết");
            btnDetail.getStyleClass().add("detail-button");
            btnDetail.setOnAction(e -> handleDetail(product.getProduct_id()));

            String textStatus = (!product.isStatus()) ? "Hoạt động" : "Dừng hoạt động";
            String styleBtn = (!product.isStatus()) ? "success-button" : "error-button";
            JFXButton btnStatus = new JFXButton(textStatus);
            btnStatus.getStyleClass().add(styleBtn);
            btnStatus.setOnAction(e -> handleStatus(product.getProduct_id(), product.isStatus()));

            // Thêm các cột vào GridPane
            grid.add(checkBox, 0, row);
            grid.add(lblStt, 1, row);
            grid.add(imageView, 2, row);
            grid.add(lblName, 3, row);
            grid.add(lblPrice, 4, row);
            grid.add(btnDetail, 5, row);
            grid.add(btnStatus, 6, row);

            row++; // Tăng số hàng

            // Thêm Line phân cách
            Line separator = new Line();
            separator.setStartX(0);
            // Ràng buộc chiều rộng của separator theo chiều rộng của GridPane
            separator.endXProperty().bind(grid.widthProperty());
            separator.setStroke(Color.LIGHTGRAY);
            separator.setStrokeWidth(1);

            // Gộp Line qua tất cả các cột (0 đến 6) => tổng cộng 7 cột => colspan = 7
            grid.add(separator, 0, row, 7, 1);

            row++; // Tăng số hàng tiếp theo để tránh chồng lặp
        }


        List<String> categories = new ArrayList<>();
        for (Category category : categoryService.getAllCategory()) {
            categories.add(category.getCategory_name());
        }
        categoryComboBox.getItems().addAll(categories);
        showBox.setValue("Hiển thị " + String.valueOf(products.size()));
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
            String product_name = productNameField.getText();
            double price = Double.parseDouble(priceField.getText());
            String description = descriptionField.getText();
            int category_id = Integer.parseInt(btnCategoryId.getText());
            boolean status = false ;
            if (inactiveCheckBox.isSelected()) status = true ;
            String imgSrc = btnPathImg.getText();
            Product product = new Product(product_name, description, price, category_id, imgSrc, status);
            productService.addProduct(product);
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm sản phẩm thành công");
            Stage stage = (Stage) btnAdd.getScene().getWindow();
            stage.close();
        } catch (RuntimeException e) {
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
        List<String> categories = new ArrayList<>();
        for (Category category : categoryService.getAllCategory()) {
            categories.add(category.getCategory_name());
        }
        categoryComboBox.getItems().addAll(categories);
    }

    // khi chuyển qua trang chi tiết sẽ mặc định gọi .
    @FXML
    public void loadDataDetailProduct(int productId) {
        try {
            Product product = productService.findProductByID(productId); // Lấy dữ liệu từ database .
            if (product != null) {
                productNameField.setText(product.getProduct_name());
                priceField.setText(String.valueOf(product.getPrice()));
                descriptionField.setText(product.getDescription());
                imgView.setImage(UploadImage.loadImage(product.getImgSrc()));
                btnId.setText(String.valueOf(productId));
                btnId.setVisible(false);
                btnPathImg.setText(product.getImgSrc());
                btnPathImg.setVisible(false);
                btnCategoryId.setText(String.valueOf(product.getCategory_id()));
                btnCategoryId.setVisible(false);
                System.out.println(product.getCategory_id());
                String category_name = categoryService.findCategoryByID(product.getCategory_id()).getCategory_name();
                categoryComboBox.setValue(category_name);
                List<String> categories = new ArrayList<>();
                for (Category category : categoryService.getAllCategory()) {
                    categories.add(category.getCategory_name());
                }
                categoryComboBox.getItems().addAll(categories);
                if(product.isStatus()) {
                    inactiveCheckBox.setSelected(true);
                    activeCheckBox.setSelected(false);
                }
                else {
                    inactiveCheckBox.setSelected(false);
                    activeCheckBox.setSelected(true);
                }
            } else {
                System.out.println("Không tìm thấy sản phẩm!");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    // xóa 1 sản phẩm .
    @FXML
    public void deleteProduct() {
        try {
            int productId = Integer.parseInt(btnId.getText());
            if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa sản phẩm không ?")) {
                productService.deleteProduct(productId);
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa sản phẩm thành công");
                Stage stage = (Stage) btnId.getScene().getWindow();
                stage.close();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    // xóa nhiều sản phẩm .
    @FXML
    public void deleteAll() {
        try {
            if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa sản phẩm không ?")) {
                boolean check = false ;
                for (JFXCheckBox checkBox : checkBoxes) {
                    if (checkBox.isSelected()) {
                        check = true ;
                        int productId = Integer.parseInt(checkBox.getId());
                        productService.deleteProduct(productId);
                    }
                }
                if (!check) {
                    AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng chọn ít nhất 1 sản phẩm để xóa");
                    return ;
                }
                loadData();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa sản phẩm thành công");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    // hàm tìm kiếm sản phẩm .
    @FXML
    public void searchProduct() {
        String textSearch = searchField.getText().trim();
        if (textSearch.isEmpty()) return;
        newOperation = "search";
        grid.getChildren().clear();
        Product product = productService.findProductByName(textSearch);
        if (product != null) {
            int row = 0;
            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.setId(String.valueOf(product.getProduct_id()));
            checkBoxes.add(checkBox);
            // Cột STT
            Label lblStt = new Label(String.valueOf(row + 1) + '.');

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

            String textStatus = (!product.isStatus()) ? "Hoạt động" : "Dừng hoạt động";
            String styleBtn = (!product.isStatus()) ? "success-button" : "error-button";
            JFXButton btnStatus = new JFXButton(textStatus);
            btnStatus.getStyleClass().add(styleBtn);
            btnStatus.setOnAction(e -> handleStatus(product.getProduct_id(), product.isStatus()));

            // Thêm vào GridPane
            grid.add(checkBox, 0, row);
            grid.add(lblStt, 1, row);
            grid.add(imageView, 2, row);
            grid.add(lblName, 3, row);
            grid.add(lblPrice, 4, row);
            grid.add(btnDetail, 5, row);
            grid.add(btnStatus, 6, row);


            row++; // Tăng số hàng
        }
        showBox.setValue("Hiển thị 1 ");
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
        Category category = categoryService.findCategoryByName((String) categoryComboBox.getValue());
        btnCategoryId.setText(String.valueOf(category.getCategory_id()));
    }

    // update sản phẩm .
    @FXML
    public void updateProduct() {
        try {
            int product_id = Integer.parseInt(btnId.getText());
            String product_name = productNameField.getText();
            double price = Double.parseDouble(priceField.getText());
            String description = descriptionField.getText();
            int category_id = Integer.parseInt(btnCategoryId.getText());
            String imgSrc = btnPathImg.getText();
            boolean status = true;
            Product product = new Product(product_id, product_name, description, price, category_id, imgSrc, status, false);
            productService.updateProduct(product);
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật sản phẩm thành công");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    // Lọc
    @FXML
    void filterAction() {
        String categoryValue = (String) categoryComboBox.getValue();
        if (categoryValue.isEmpty()) return;
        newOperation = "filter" ;
        grid.getChildren().clear();
        Category category = categoryService.findCategoryByName(categoryValue);
        List<Product> products = productService.getAllByCategoryId(category.getCategory_id());
        if (products == null || products.isEmpty()) {
            System.out.println("Không có dữ liệu từ database!");
            return;
        }
        int row = 0;
        for (Product product : products) {
            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.setId(String.valueOf(product.getProduct_id()));
            checkBoxes.add(checkBox);
            // Cột STT
            Label lblStt = new Label(String.valueOf(row + 1) + '.');

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

            String textStatus = (!product.isStatus()) ? "Hoạt động" : "Dừng hoạt động";
            String styleBtn = (!product.isStatus()) ? "success-button" : "error-button";
            JFXButton btnStatus = new JFXButton(textStatus);
            btnStatus.getStyleClass().add(styleBtn);
            btnStatus.setOnAction(e -> handleStatus(product.getProduct_id(), product.isStatus()));

            // Thêm vào GridPane
            grid.add(checkBox, 0, row);
            grid.add(lblStt, 1, row);
            grid.add(imageView, 2, row);
            grid.add(lblName, 3, row);
            grid.add(lblPrice, 4, row);
            grid.add(btnDetail, 5, row);
            grid.add(btnStatus, 6, row);

            row++; // Tăng số hàng
        }
        showBox.setValue("Hiển thị " + String.valueOf(products.size()));

    }

    // checkBox all
    @FXML
    private void checkBoxAll() {
        for (JFXCheckBox jfxCheckBox : checkBoxes) {
            jfxCheckBox.setSelected(checkBoxAll.isSelected());
        }
    }

    // change status
    @FXML
    private void handleStatus(int productId, boolean status) {
        try {
            productService.changeStatus(productId, status);
            if (newOperation == "search") {
                searchProduct();
                return;
            }
            if (newOperation == "filter") {
                filterAction();
                return;
            }
            loadData();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleActive() {
        activeCheckBox.setSelected(true);
        inactiveCheckBox.setSelected(false);
    }

    @FXML
    public void handleInactive() {
        activeCheckBox.setSelected(false);
        inactiveCheckBox.setSelected(true);
    }


}
