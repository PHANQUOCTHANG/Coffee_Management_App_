package com.example.javafxapp.Controller.Admin;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Helpper.UploadImage;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.CategoryService;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class CategoryController {
    @FXML
    private GridPane grid;
    @FXML
    private TextField searchField , categoryNameField ;

    @FXML
    private Button btnId ;
    @FXML
    private JFXButton btnAdd ;

    private CategoryService categoryService = new CategoryService() ;


    // hàm lấy tất cả cá danh mục đang hoạt động .
    public void loadData() {



        List<Category> categories = categoryService.getAllCategory() ;

        if (categories == null || categories.isEmpty()) {
            System.out.println("Không có dữ liệu từ database!");
            return;
        }

        int row = 0;
        for (Category category : categories) {
            // Cột STT
            Label lblStt = new Label(String.valueOf(row + 1));


            // Cột tên
            Label lblName = new Label(category.getCategory_name());

            // Cột hành động (Button)
            JFXButton btnDetail = new JFXButton("Chi tiết");
            btnDetail.getStyleClass().add("detail-button");
            btnDetail.setOnAction(e -> handleDetail(category.getCategory_id())) ;

            // Thêm vào GridPane
            grid.add(lblStt, 0, row);
            grid.add(lblName, 1, row);
            grid.add(btnDetail, 2, row);

            row++; // Tăng số hàng
        }
    }


    // mặc định khi đi vào file fxml có fx:controller là controller này thì tự động khởi chạy .
    @FXML
    public void initialize() {
        if (grid != null) loadData();
    }

    // chi tiết category .
    private void handleDetail(int categoryId) {
        Pages.pageDetailCategory(categoryId) ;
    }

    // chuyển qua trang thêm 1 danh mục .
    @FXML
    private void addCategory() {
        Pages.pageAddCategory();
    }

    @FXML
    private void addCategoryPost() {
        try {
            String category_name = categoryNameField.getText() ;
            categoryService.addCategory(new Category(category_name));
            AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Thêm danh mục thành công");
            Stage stage = (Stage) btnAdd.getScene().getWindow() ;
            stage.close();
        }
        catch (RuntimeException e) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Thêm damh mục thất bại");
            e.printStackTrace();
        }
    }


    // hàm tìm kiếm danh mục bằng tên .
    @FXML
    public void searchCategory() {
        String textSearch = searchField.getText().trim();
        if (textSearch.isEmpty()) return;
        grid.getChildren().clear();
        Category category = categoryService.findCategoryByName(textSearch) ;
        if (category != null) {
            int row = 0;
            // Cột STT
            Label lblStt = new Label(String.valueOf(row + 1));

            // Cột tên
            Label lblName = new Label(category.getCategory_name());

            // Cột hành động (Button)
            JFXButton btnDetail = new JFXButton("Chi tiết");
            btnDetail.getStyleClass().add("detail-button");
            btnDetail.setOnAction(e -> handleDetail(category.getCategory_id()));

            // Thêm vào GridPane
            grid.add(lblStt, 0, row);
            grid.add(lblName, 1, row);
            grid.add(btnDetail, 2, row);
        }
    }

    // xóa 1 danh mục.
    @FXML
    public void deleteCategory() {
        try {
            int productId = Integer.parseInt(btnId.getText()) ;
            if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa danh mục không ?")) {
                categoryService.deleteCategory(productId);
                AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Xóa danh mục thành công");
                Stage stage = (Stage)btnId.getScene().getWindow() ;
                stage.close() ;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    // update danh mục .
    @FXML
    public void updateCategory() {
        try {
            int category_id = Integer.parseInt(btnId.getText()) ;
            String category_name = categoryNameField.getText().trim() ;
            categoryService.updateCategory(new Category(category_id , category_name));
            AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Cập nhật sản phẩm thành công");
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    // khi chuyển qua trang chi tiết sẽ mặc định gọi .
    @FXML
    public void loadDataDetailCategory(int categoryId) {
        Category category = categoryService.findCategoryByID(categoryId) ;
        if (category != null) {
            categoryNameField.setText(category.getCategory_name());
            btnId.setText(String.valueOf(categoryId));
            btnId.setVisible(false);
        } else {
            System.out.println("Không tìm thấy danh mục!");
        }
    }


}
