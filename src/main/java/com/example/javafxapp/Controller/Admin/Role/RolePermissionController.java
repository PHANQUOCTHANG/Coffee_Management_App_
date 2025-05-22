package com.example.javafxapp.Controller.Admin.Role;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Model.Permission;
import com.example.javafxapp.Service.PermissionService;
import com.example.javafxapp.Service.RolePermissionService;
import com.example.javafxapp.Service.RoleService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RolePermissionController implements Initializable {
    @FXML
    private VBox checkboxContainer;
    @FXML
    private CheckBox checkboxSelectAll;
    @FXML
    private JFXButton btnSavePermission, btnCancel;
    private RoleService roleService = new RoleService();
    private PermissionService permissionService = new PermissionService();
    private RolePermissionService rolePermissionService = new RolePermissionService();
    List<CheckBox> checkBoxPermissions = new ArrayList<>();
    List<JFXCheckBox> checkBoxes = new ArrayList<>();
    private RoleController roleController;

    /**
     * Truyền controller cha để gọi load lại danh sách sau khi thêm
     */
    public void setRoleController(RoleController controller) {
        this.roleController = controller;
    }
    public static int role_id = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       loadDataRolePermission(role_id);
    }
    // load dữ liệu khi vào trang phân quyền .
    public void loadDataRolePermission(int roleId) {
        List<Permission> permissions = permissionService.getAllPermission();
        List<Integer> role_permission = rolePermissionService.getAllRolePermission(roleId);
        int cnt = 0;
        for (Permission permission : permissions) {
            CheckBox cb = new CheckBox(permission.getPermission_name());
            cb.setStyle("-fx-font-size: 16; -fx-text-fill: #374151; -fx-padding: 20 20 20 20;");
            if (role_permission.contains(permission.getPermission_id())) {
                cb.setSelected(true);
                cnt++;
            }
            checkBoxPermissions.add(cb);
            checkboxContainer.getChildren().add(cb);
        }
        if (cnt == permissions.size()) checkboxSelectAll.setSelected(true);
    }

    // lưu quyền được phân .
    @FXML
    public void handleSavePermissions() {
        try {
            for (CheckBox cb : checkBoxPermissions) {
                String permission_name = cb.getText();
                Permission permission = permissionService.findPermissionByName(permission_name);
                rolePermissionService.deleteRolePermission(role_id, permission.getPermission_id());
                if (cb.isSelected()) {
                    rolePermissionService.addRolePermission(role_id, permission.getPermission_id());
                }
            }
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Phân quyền thành công");
            Stage stage = (Stage) btnSavePermission.getScene().getWindow();
            stage.close();
        } catch (RuntimeException e) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Phân quyền thất bại");
            e.printStackTrace();
        }
    }

    // select all trong role_permission .
    @FXML
    private void selectAll() {
        for (CheckBox checkBox : checkBoxPermissions) {
            checkBox.setSelected(checkboxSelectAll.isSelected());
        }
    }

    // hủy
    @FXML
    public void handleCancel() {
        var stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }


}
