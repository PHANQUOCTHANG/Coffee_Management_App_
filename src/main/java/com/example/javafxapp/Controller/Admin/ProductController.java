package com.example.javafxapp.Controller.Admin;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Helpper.UploadImage;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.CategoryService;
import com.example.javafxapp.Service.ProductService;
import com.example.javafxapp.Validation.ValidationProduct;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
    private Button btnUpload, btnId, btnCategoryId, btnPathImg, btnAdd, prevButton, nextButton;
    @FXML
    private ImageView imgView;
    @FXML
    private JFXCheckBox checkBoxAll, activeCheckBox, inactiveCheckBox;
    private List<JFXCheckBox> checkBoxes;
    private String imagePath; // Đường dẫn ảnh
    private String newOperation; // lưu thao tác mới nhất (lọc hoặc tìm kiếm)

    @FXML
    private HBox boxPage;
    private List<Button> buttons; // chứa các button phân trang .
    private int currentPage = 1, limitPage = 4, numberPage = 3, pages; // limit page : số phần tử tối đa , number page số trang muốn hiển thị  .
    private boolean checkPage = false;


    // load data mặc định khi vào trang .
    public void loadData() {
        grid.getChildren().clear();
        List<Product> products = productService.getAllProduct(); // Lấy danh sách từ database

        if (products == null || products.isEmpty()) {
            System.out.println("Không có dữ liệu từ database!");
            return;
        }
        checkBoxes = new ArrayList<>();
        int row = 0, stt = 1, startIndex = (currentPage - 1) * limitPage;
        for (int i = startIndex; i < Math.min(startIndex + limitPage, products.size()); i++) {
            Product product = products.get(i);
            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.setId(String.valueOf(product.getProduct_id()));
            checkBoxes.add(checkBox);

            Label lblStt = new Label(String.valueOf(stt++) + '.');
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

            String textOutStanding = (product.isOutstanding()) ? "Mở" : "Đóng";
            styleBtn = (product.isOutstanding()) ? "success-button" : "error-button";
            JFXButton btnOutStanding = new JFXButton(textOutStanding);
            btnOutStanding.getStyleClass().add(styleBtn);
            btnOutStanding.setOnAction(e -> handleOutStanding(product.getProduct_id(), product.isOutstanding()));


            // Thêm các cột vào GridPane
            grid.add(checkBox, 0, row);
            grid.add(lblStt, 1, row);
            grid.add(imageView, 2, row);
            grid.add(lblName, 3, row);
            grid.add(lblPrice, 4, row);
            grid.add(btnDetail, 5, row);
            grid.add(btnStatus, 6, row);
            grid.add(btnOutStanding, 7, row);

            row++; // Tăng số hàng

            // Thêm Line phân cách
            Line separator = new Line();
            separator.setStartX(0);
            // Ràng buộc chiều rộng của separator theo chiều rộng của GridPane
            separator.endXProperty().bind(grid.widthProperty());
            separator.setStroke(Color.LIGHTGRAY);
            separator.setStrokeWidth(1);

            // Gộp Line qua tất cả các cột (0 đến 6) => tổng cộng 7 cột => colspan = 7
            grid.add(separator, 0, row, 8, 1);

            row++; // Tăng số hàng tiếp theo để tránh chồng lặp
        }

        List<String> categories = new ArrayList<>();
        for (Category category : categoryService.getAllCategory()) {
            categories.add(category.getCategory_name());
        }
        categoryComboBox.getItems().addAll(categories);
        searchField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) searchProduct();
        });
    }

    // Tạo ra các nút chuyển trang .
    public void createPaginationButtons() {
        // Xóa hết nút cũ trừ Prev và Next
        boxPage.getChildren().removeIf(node -> node != prevButton && node != nextButton);
        buttons = new ArrayList<>();
        List<Product> products = productService.getAllProduct();
        pages = (products.size() % limitPage == 0) ? products.size() / limitPage : products.size() / limitPage + 1;
        int totalPages = Math.min(numberPage, pages);
        // Thêm nút phân trang từ 1 -> totalPages
        for (int i = 1; i <= totalPages; i++) {
            Button pageButton = new Button(String.valueOf(i));
            pageButton.setPrefWidth(60);
            pageButton.setPrefHeight(50);
            buttons.add(pageButton);
            int page = i;
            pageButton.setOnAction(event -> {
                loadDataPage(page);

            });

            boxPage.getChildren().add(boxPage.getChildren().size() - 1, pageButton); // thêm trước nút nextButton
        }
        currentPage = 1;
        buttons.get(0).getStyleClass().add("active");
    }

    // load khi chuyển trang .
    @FXML
    public void loadDataPage(int page) {
        if (page == currentPage) return;
        currentPage = page;
        for (Button button : buttons) {
            button.getStyleClass().remove("active");
        }
        buttons.get(page - Integer.parseInt(buttons.get(0).getText())).getStyleClass().add("active");
        loadData();
    }

    // lui về trang trước .
    @FXML
    public void handlePrev() {
        if (currentPage == 1) return;
        int index = currentPage - Integer.parseInt(buttons.get(0).getText());
        buttons.get(index).getStyleClass().remove("active");
        currentPage--;
        loadData();
        if (Integer.parseInt(buttons.get(0).getText()) == 1) {
            buttons.get(index - 1).getStyleClass().add("active");
            return;
        }
        for (Button button : buttons) {
            int newPage = Integer.parseInt(button.getText()) - 1;
            button.setText(String.valueOf(newPage));
            button.setOnAction(event -> loadDataPage(newPage));
        }
        buttons.get(index).getStyleClass().add("active");
    }

    // đi về trang sau .
    @FXML
    public void handleNext() {
        if (currentPage == pages) return;
        int index = currentPage - Integer.parseInt(buttons.get(0).getText());
        buttons.get(index).getStyleClass().remove("active");
        currentPage++;
        loadData();
        if (Integer.parseInt(buttons.get(buttons.size() - 1).getText()) == pages) {
            buttons.get(index + 1).getStyleClass().add("active");
            return;
        }
        for (Button button : buttons) {
            int newPage = Integer.parseInt(button.getText()) + 1;
            button.setText(String.valueOf(newPage));
            button.setOnAction(event -> loadDataPage(newPage));
        }
        buttons.get(index).getStyleClass().add("active");
    }


    // tự động chạy khi vào .
    @FXML
    public void initialize() {
        if (grid != null) {
            loadData();
            createPaginationButtons();
        }
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
        activeCheckBox.setSelected(true);
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
                btnPathImg.setText(product.getImgSrc());
                btnCategoryId.setText(String.valueOf(product.getCategory_id()));
                System.out.println(product.getCategory_id());
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

    // xóa nhiều đối tượng cùng 1 lúc.
    @FXML
    public void deleteAll() {
        try {
            if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa sản phẩm không ?")) {
                boolean check = false;
                for (JFXCheckBox checkBox : checkBoxes) {
                    if (checkBox.isSelected()) {
                        check = true;
                        int productId = Integer.parseInt(checkBox.getId());
                        productService.deleteProduct(productId);
                    }
                }
                if (!check) {
                    AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng chọn ít nhất 1 sản phẩm để xóa");
                    return;
                }
                createPaginationButtons();
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
        // Lắng nghe nhập liệu trong searchField
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            newOperation = "search";
            String textSearch = newValue.trim();
            grid.getChildren().clear();
            if (textSearch.isEmpty() || textSearch == null) {
                loadData();
                return;
            }

            List<Product> products = productService.findProductsByKeyword(textSearch);
            if (products == null || products.isEmpty()) return  ;
            int row = 0, stt = 1;
            for (Product product : products) {
                JFXCheckBox checkBox = new JFXCheckBox();
                checkBox.setId(String.valueOf(product.getProduct_id()));
                checkBoxes.add(checkBox);

                Label lblStt = new Label(String.valueOf(stt++) + '.');
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
        });
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
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    // Lọc
    @FXML
    void filterAction() {
        String categoryValue = (String) categoryComboBox.getValue();
        if (categoryValue.isEmpty()) return;
        newOperation = "filter";
        grid.getChildren().clear();
        Category category = categoryService.findCategoryByName(categoryValue);
        List<Product> products = productService.getAllByCategoryId(category.getCategory_id());
        if (products == null || products.isEmpty()) {
            System.out.println("Không có dữ liệu từ database!");
            return;
        }
        int row = 0, stt = 1;
        for (Product product : products) {
            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.setId(String.valueOf(product.getProduct_id()));
            checkBoxes.add(checkBox);

            Label lblStt = new Label(String.valueOf(stt++) + '.');
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
    }

    // checkBox all
    @FXML
    private void checkBoxAll() {
        for (JFXCheckBox jfxCheckBox : checkBoxes) {
            jfxCheckBox.setSelected(checkBoxAll.isSelected());
        }
    }

    // thay đổi trạng thái.
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

    // thay đổi nổi bật .
    @FXML
    private void handleOutStanding(int productId, boolean outstanding) {
        try {
            productService.changeOutStanding(productId, outstanding);
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

    // Khi click chọn hoạt động .
    @FXML
    public void handleActive() {
        activeCheckBox.setSelected(true);
        inactiveCheckBox.setSelected(false);
    }

    // Khi click chọn không hoạt động .
    @FXML
    public void handleInactive() {
        activeCheckBox.setSelected(false);
        inactiveCheckBox.setSelected(true);
    }


}
