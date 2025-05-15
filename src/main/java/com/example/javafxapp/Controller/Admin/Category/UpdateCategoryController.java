package com.example.javafxapp.Controller.Admin.Category;

import com.example.javafxapp.Controller.Admin.Category.CategoryController;
import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Service.CategoryService;
import com.example.javafxapp.Validation.ValidationCategory;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateCategoryController {

    @FXML private TextField categoryNameField;
    @FXML private JFXButton btnUpdate;
    
    private CategoryService categoryService;
    private CategoryController categoryController;
    public static int category_id = -1  ;

    public void setCategoryController(CategoryController controller) {
        this.categoryController = controller;
    }

    @FXML
    private void initialize() {
        categoryService = new CategoryService();
        Category category = categoryService.findCategoryByID(category_id) ;
        categoryNameField.setText(category.getCategory_name());
    }

    @FXML
    private void updateCategory() {
        String categoryName = categoryNameField.getText().trim();
        
        if (!ValidationCategory.validationCategoryName(categoryName)) return ;

        try {
            categoryService.updateCategory(new Category(category_id , categoryName));
            categoryController.loadCategories();
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật danh mục thành công");
            
            // Close the window
            Stage stage = (Stage) btnUpdate.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật danh mục");
        }
    }
} 