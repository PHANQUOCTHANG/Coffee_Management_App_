package com.example.javafxapp.Controller.Admin.Role;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Model.Role;
import com.example.javafxapp.Service.RoleService;
import com.example.javafxapp.Validation.ValidationRole;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateRoleController {

    @FXML private TextField roleNameField;
    @FXML private TextArea descriptionField;
    @FXML private JFXButton btnUpdate;

    private RoleService roleService;
    private RoleController roleController;
    public static int role_id = -1;

    /**
     * Gán RoleController để reload bảng
     */
    public void setRoleController(RoleController controller) {
        this.roleController = controller;
    }

    @FXML
    private void initialize() {
        roleService = new RoleService();
        if (role_id >= 0) {
            Role role = roleService.findRoleByID(role_id);
            if (role != null) {
                roleNameField.setText(role.getRole_name());
                descriptionField.setText(role.getDescription());
            }
        }
    }

    @FXML
    private void updateRole() {
        String name = roleNameField.getText().trim();
        String desc = descriptionField.getText().trim();

        if (!ValidationRole.validationRoleName(name)) return;

        try {
            roleService.updateRole(new Role(role_id, name, desc));
            roleController.loadRoles();
            AlertInfo.showAlert(javafx.scene.control.Alert.AlertType.INFORMATION,
                    "Thành công", "Cập nhật vai trò thành công");
            // Đóng cửa sổ
            Stage stage = (Stage) btnUpdate.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            AlertInfo.showAlert(javafx.scene.control.Alert.AlertType.ERROR,
                    "Lỗi", "Không thể cập nhật vai trò");
        }
    }
}
