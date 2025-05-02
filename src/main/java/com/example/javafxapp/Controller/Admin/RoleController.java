package com.example.javafxapp.Controller.Admin;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Model.Permission;
import com.example.javafxapp.Model.Role;
import com.example.javafxapp.Service.PermissionService;
import com.example.javafxapp.Service.RolePermissionService;
import com.example.javafxapp.Service.RoleService;
import com.example.javafxapp.Validation.ValidationRole;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class RoleController {
    @FXML
    private GridPane grid;
    @FXML
    private TextField searchField, roleNameField;
    @FXML
    private TextArea descriptionField;

    @FXML
    private Button btnId;
    @FXML
    private JFXButton btnAdd, btnCancel, btnSavePermission;

    @FXML
    private VBox checkboxContainer;

    @FXML
    private ComboBox roleComboBox, showBox;
    @FXML
    private JFXCheckBox checkBoxAll;
    private List<JFXCheckBox> checkBoxes;


    private RoleService roleService = new RoleService();
    private PermissionService permissionService = new PermissionService();
    private RolePermissionService rolePermissionService = new RolePermissionService();


    // hàm lấy tất cả cả các role đang hoạt động .
    public void loadData() {
        grid.getChildren().clear();

        List<Role> roles = roleService.getAllRole();

        if (roles == null || roles.isEmpty()) {
            System.out.println("Không có dữ liệu từ database!");
            return;
        }
        checkBoxes = new ArrayList<>();
        int row = 0, stt = 1;
        for (Role role : roles) {
            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.setId(String.valueOf(role.getRole_id()));
            checkBoxes.add(checkBox);

            // STT
            Label lblStt = new Label(String.valueOf(stt++) + '.');


            // Cột tên
            Label lblName = new Label(role.getRole_name());

            // Cột hành động (Button)
            JFXButton btnDetail = new JFXButton("Chi tiết");
            btnDetail.getStyleClass().add("detail-button");
            btnDetail.setOnAction(e -> handleDetail(role.getRole_id()));

            JFXButton btnPermission = new JFXButton("Phân quyền");
            btnPermission.getStyleClass().add("permission-button");
            btnPermission.setOnAction(e -> handleRolePermission(role.getRole_id()));

            // Thêm vào GridPane
            grid.add(checkBox, 0, row);
            grid.add(lblStt, 1, row);
            grid.add(lblName, 2, row);
            grid.add(btnDetail, 3, row);
            grid.add(btnPermission, 4, row);
            if (role.getRole_name().equals("Customer")) {
                btnPermission.setVisible(false);
                btnPermission.setManaged(false);
            }

            row++; // Tăng số hàng

            // Thêm Line phân cách
            Line separator = new Line();
            separator.setStartX(0);
            // Ràng buộc chiều rộng của separator theo chiều rộng của GridPane
            separator.endXProperty().bind(grid.widthProperty());
            separator.setStroke(Color.LIGHTGRAY);
            separator.setStrokeWidth(1);

            // Gộp Line qua tất cả các cột (0 đến 6) => tổng cộng 7 cột => colspan = 7
            grid.add(separator, 0, row, 5, 1);

            row++;

        }
        showBox.setValue("Hiển thị " + String.valueOf(roles.size()));
    }


    // mặc định khi đi vào file fxml có fx:controller là controller này thì tự động khởi chạy .
    @FXML
    public void initialize() {
        if (grid != null) loadData();
    }


    // Trang chi tiết .
    // chi tiết role .
    private void handleDetail(int roleId) {
        Pages.pageDetailRole(roleId);
    }

    // khi chuyển qua trang chi tiết sẽ mặc định gọi .
    @FXML
    public void loadDataDetailRole(int roleId) {
        Role role = roleService.findRoleByID(roleId);
        if (role != null) {
            roleNameField.setText(role.getRole_name());
            descriptionField.setText(role.getDescription());
            btnId.setText(String.valueOf(roleId));
            btnId.setVisible(false);
        } else {
            System.out.println("Không tìm thấy danh mục!");
        }
    }

    // xóa .
    @FXML
    public void deleteRole() {
        try {
            int roleId = Integer.parseInt(btnId.getText());
            System.out.println(roleId);
            if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa không ?")) {
                roleService.deleteRole(roleId);
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa thành công");
                Stage stage = (Stage) btnId.getScene().getWindow();
                stage.close();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    // update .
    @FXML
    public void updateRole() {
        try {
            int role_id = Integer.parseInt(btnId.getText());
            String role_name = roleNameField.getText().trim();
            if (!ValidationRole.validationRoleName(role_name)) return;
            String description = descriptionField.getText().trim();
            roleService.updateRole(new Role(role_id, role_name, description));
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật thành công");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    // Trang thêm .
    // chuyển qua trang thêm .
    @FXML
    private void addRole() {
        Pages.pageAddRole();
    }

    // thêm role .
    @FXML
    private void addRolePost() {
        try {
            String role_name = roleNameField.getText().trim();
            if (!ValidationRole.validationRoleName(role_name)) return;
            String description = descriptionField.getText().trim();
            roleService.addRole(new Role(role_name, description));
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm thành công");
            Stage stage = (Stage) btnAdd.getScene().getWindow();
            stage.close();
        } catch (RuntimeException e) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Thêm thất bại");
            e.printStackTrace();
        }
    }

    // hàm tìm kiếm bằng tên .
    @FXML
    public void searchRole() {
    }

    private List<CheckBox> checkBoxPermissions = new ArrayList<>();
    @FXML
    private CheckBox checkboxSelectAll;

    // chuyển qua trang phân quyền  .
    @FXML
    public void handleRolePermission(int roleId) {
        Pages.pageRolePermission(roleId);
    }

    // load dữ liệu khi vào trang phân quyền .
    public void loadDataRolePermission(int roleId) {
        List<Permission> permissions = permissionService.getAllPermission();
        List<Integer> role_permission = rolePermissionService.getAllRolePermission(roleId);
        btnId.setText(String.valueOf(roleId));
        btnId.setVisible(false);
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
            int roleId = Integer.parseInt(btnId.getText());
            for (CheckBox cb : checkBoxPermissions) {
                String permission_name = cb.getText();
                Permission permission = permissionService.findPermissionByName(permission_name);
                rolePermissionService.deleteRolePermission(roleId, permission.getPermission_id());
                if (cb.isSelected()) {
                    rolePermissionService.addRolePermission(roleId, permission.getPermission_id());
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
                boolean check = false;
                for (JFXCheckBox checkBox : checkBoxes) {
                    if (checkBox.isSelected()) {
                        check = true;
                        int roleId = Integer.parseInt(checkBox.getId());
                        roleService.deleteRole(roleId);
                    }
                }
                if (!check) {
                    AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng chọn ít nhất 1 vai trò để xóa");
                    return;
                }
                loadData();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa thành công");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    // hủy
    @FXML
    public void handleCancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }


}
