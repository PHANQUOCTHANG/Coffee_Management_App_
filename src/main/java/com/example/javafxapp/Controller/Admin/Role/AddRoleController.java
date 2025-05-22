package com.example.javafxapp.Controller.Admin.Role;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Model.Role;
import com.example.javafxapp.Service.RoleService;
import com.example.javafxapp.Validation.ValidationRole;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddRoleController {

    @FXML private TextField roleNameField;
    @FXML private TextArea descriptionField;
    @FXML private JFXButton btnAdd;

    private RoleService roleService ;
    private RoleController roleController ;

    /**
     * Truyền controller cha để gọi load lại danh sách sau khi thêm
     */
    public void setRoleController(RoleController controller) {
        this.roleController = controller;
    }

    @FXML
    private void initialize() {
        roleService = new RoleService();
    }

    @FXML
    private void addRole() {
        String name = roleNameField.getText().trim();
        String desc = descriptionField.getText().trim();

        // Validate tên vai trò
        if (!ValidationRole.validationRoleName(name)) {
            return;
        }

        try {
            Role role = new Role(name, desc);
            roleService.addRole(role);
            // làm mới bảng
            roleController.loadRoles();
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm vai trò thành công");

            // đóng cửa sổ
            Stage stage = (Stage) btnAdd.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm vai trò");
        }
    }
}
