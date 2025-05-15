package com.example.javafxapp.Validation;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Service.CategoryService;
import javafx.scene.control.Alert;

public class ValidationCategory {
    private static CategoryService categoryService = new CategoryService() ;

    public static boolean validationCategoryName(String categoryName) {
        if (categoryName.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Tên không rỗng");
            return false ;
        }
        if (categoryName.length() < 6) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Tên ít nhất 6 kí tự");
            return false ;
        }
        if (categoryService.findCategoryByName(categoryName)!= null) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Tên danh mục đã tồn tại");
            return false;
        }
        return true ;
    }


}
