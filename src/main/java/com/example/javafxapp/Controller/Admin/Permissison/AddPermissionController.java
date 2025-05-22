package com.example.javafxapp.Controller.Admin.Permissison;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Model.Permission;
import com.example.javafxapp.Service.PermissionService;
import com.example.javafxapp.Validation.ValidationPermission;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPermissionController {

    @FXML private TextField permissionNameField;
    @FXML private JFXButton btnAdd;

    private PermissionService permissionService;
    private PermissionController permissionController;

    /**
     * Thiết lập controller cha để gọi lại loadPermissions sau khi thêm
     */
    public void setPermissionController(PermissionController controller) {
        this.permissionController = controller;
    }

    @FXML
    private void initialize() {
        permissionService = new PermissionService();
    }

    @FXML
    private void addPermission() {
        String permissionName = permissionNameField.getText().trim();

        // Validate tên quyền
        if (!ValidationPermission.validationPermissionName(permissionName)) {
            return;
        }

        try {
            Permission permission = new Permission(permissionName , false);
            permissionService.addPermission(permission);
            // Reload bảng
            permissionController.loadPermissions();
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm quyền thành công");

            // Đóng cửa sổ
            Stage stage = (Stage) btnAdd.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm quyền");
        }
    }
}
