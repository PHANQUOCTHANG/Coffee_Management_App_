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

public class AddCategoryController {

    @FXML private TextField categoryNameField;
    @FXML private JFXButton btnAdd;
    
    private CategoryService categoryService;
    private CategoryController categoryController;

    public void setCategoryController(CategoryController controller) {
        this.categoryController = controller;
    }

    @FXML
    private void initialize() {
        categoryService = new CategoryService();
    }

    @FXML
    private void addCategory() {
        String categoryName = categoryNameField.getText().trim();
        
        if (!ValidationCategory.validationCategoryName(categoryName)) return ;

        try {
            Category category = new Category(categoryName);
            categoryService.addCategory(category);
            categoryController.loadCategories();
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm danh mục thành công");
            
            // Close the window
            Stage stage = (Stage) btnAdd.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm danh mục");
        }
    }
} 