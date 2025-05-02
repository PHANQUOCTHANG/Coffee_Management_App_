package com.example.javafxapp.Controller.Admin;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Service.CategoryService;
import com.example.javafxapp.Validation.ValidationCategory;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
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
    @FXML
    private JFXCheckBox checkBoxAll ;
    @FXML
    private ComboBox showBox;
    private List<JFXCheckBox> checkBoxes;

    private CategoryService categoryService = new CategoryService() ;


    // hàm lấy tất cả cá danh mục đang hoạt động .
    public void loadData() {
        grid.getChildren().clear();


        List<Category> categories = categoryService.getAllCategory() ;

        if (categories == null || categories.isEmpty()) {
            System.out.println("Không có dữ liệu từ database!");
            return;
        }
        checkBoxes = new ArrayList<>();
        int row = 0 , stt = 1 ;
        for (Category category : categories) {
            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.setId(String.valueOf(category.getCategory_id()));
            checkBoxes.add(checkBox);

            // STT
            Label lblStt = new Label(String.valueOf(stt++) + '.');


            // Cột tên
            Label lblName = new Label(category.getCategory_name());

            // Cột hành động (Button)
            JFXButton btnDetail = new JFXButton("Chi tiết");
            btnDetail.getStyleClass().add("detail-button");
            btnDetail.setOnAction(e -> handleDetail(category.getCategory_id())) ;

            // Thêm vào GridPane
            grid.add(checkBox, 0, row);
            grid.add(lblStt, 1, row);
            grid.add(lblName, 2, row);
            grid.add(btnDetail, 3, row);

            row++; // Tăng số hàng

            // Thêm Line phân cách
            Line separator = new Line();
            separator.setStartX(0);
            // Ràng buộc chiều rộng của separator theo chiều rộng của GridPane
            separator.endXProperty().bind(grid.widthProperty());
            separator.setStroke(Color.LIGHTGRAY);
            separator.setStrokeWidth(1);

            // Gộp Line qua tất cả các cột (0 đến 3) => tổng cộng 4 cột => colspan = 4
            grid.add(separator, 0, row, 4, 1);
            row++ ;
        }
        showBox.setValue("Hiển thị " + String.valueOf(categories.size()));
    }


    // mặc định khi đi vào file fxml có fx:controller là controller này thì tự động khởi chạy .
    @FXML
    public void initialize() {
        if (grid != null) loadData();
    }

    // Trang chi tiết .
    // chi tiết category .
    private void handleDetail(int categoryId) {
        Pages.pageDetailCategory(categoryId) ;
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

    // Trang thêm .
    // chuyển qua trang thêm 1 danh mục .
    @FXML
    private void addCategory() {
        Pages.pageAddCategory();
    }

    // thêm danh mục .
    @FXML
    private void addCategoryPost() {
        try {
            String category_name = categoryNameField.getText() ;
            if(!ValidationCategory.validationCategoryName(category_name)) return ;
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
        // Lắng nghe nhập liệu trong searchField
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String textSearch = newValue.trim();
            grid.getChildren().clear();
            if (textSearch.isEmpty()) {
                loadData() ;
                return ;
            }

            // Giả sử đây là method bạn thêm vào CategoryService để trả về nhiều kết quả
            List<Category> categories = categoryService.findCategoriesByKeyword(textSearch);
            if (categories == null || categories.isEmpty()) return ;
            int row = 0 , stt = 1 ;
            for (Category category : categories) {
                JFXCheckBox checkBox = new JFXCheckBox();
                checkBox.setId(String.valueOf(category.getCategory_id()));

                // STT
                Label lblStt = new Label(String.valueOf(stt++) + '.');


                // Cột tên
                Label lblName = new Label(category.getCategory_name());

                // Cột hành động (Button)
                JFXButton btnDetail = new JFXButton("Chi tiết");
                btnDetail.getStyleClass().add("detail-button");
                btnDetail.setOnAction(e -> handleDetail(category.getCategory_id())) ;

                // Thêm vào GridPane
                grid.add(checkBox, 0, row);
                grid.add(lblStt, 1, row);
                grid.add(lblName, 2, row);
                grid.add(btnDetail, 3, row);

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
                row++ ;
            }
            showBox.setValue("Hiển thị " + String.valueOf(categories.size()));
        });
    }

    // xóa 1 danh mục.
    @FXML
    public void deleteCategory() {
        try {
            int categoryId = Integer.parseInt(btnId.getText()) ;
            if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa danh mục không ?")) {
                categoryService.deleteCategory(categoryId);
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
            if(!ValidationCategory.validationCategoryName(category_name)) return ;
            categoryService.updateCategory(new Category(category_id , category_name));
            AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Cập nhật sản phẩm thành công");
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    // checkBox all
    @FXML
    private void checkBoxAll() {
        for (JFXCheckBox jfxCheckBox : checkBoxes) {
            jfxCheckBox.setSelected(checkBoxAll.isSelected());
        }
    }

    // xóa nhiều đối tượng cùng 1 lúc.
    @FXML
    public void deleteAll() {
        try {
            if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa không ?")) {
                boolean check = false ;
                for (JFXCheckBox checkBox : checkBoxes) {
                    if (checkBox.isSelected()) {
                        check = true ;
                        int categoryId = Integer.parseInt(checkBox.getId());
                        categoryService.deleteCategory(categoryId);
                    }
                }
                if (!check) {
                    AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng chọn ít nhất 1 danh mục để xóa");
                    return ;
                }
                loadData();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa thành công");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

}
