package com.example.javafxapp.Controller.Admin.Permissison;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Service.PermissionService;
import com.example.javafxapp.Model.Permission;
import com.example.javafxapp.Validation.ValidationPermission;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdatePermissionController {

    @FXML private TextField permissionNameField;
    @FXML private JFXButton btnUpdate;

    private PermissionService permissionService;
    private PermissionController permissionController;
    public static int permission_id = -1;

    /**
     * Thiết lập controller cha để load lại danh sách sau khi cập nhật
     */
    public void setPermissionController(PermissionController controller) {
        this.permissionController = controller;
    }

    @FXML
    private void initialize() {
        permissionService = new PermissionService();
        // Lấy thông tin quyền theo id tĩnh
        if (permission_id >= 0) {
            Permission permission = permissionService.findPermissionByID(permission_id);
            if (permission != null) {
                permissionNameField.setText(permission.getPermission_name()) ;
            }
        }
    }

    @FXML
    private void updatePermission() {
        String permissionName = permissionNameField.getText().trim();
        // Validate dữ liệu
        if (!ValidationPermission.validationPermissionName(permissionName)) {
            return;
        }

        try {
            // Cập nhật thông tin
            Permission updated = new Permission(permission_id, permissionName , false);
            permissionService.updatePermission(updated);
            // Làm mới bảng
            permissionController.loadPermissions();
            AlertInfo.showAlert(javafx.scene.control.Alert.AlertType.INFORMATION,
                    "Thành công", "Cập nhật quyền thành công");
            // Đóng cửa sổ
            Stage stage = (Stage) btnUpdate.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            AlertInfo.showAlert(javafx.scene.control.Alert.AlertType.ERROR,
                    "Lỗi", "Không thể cập nhật quyền");
        }
    }
}
