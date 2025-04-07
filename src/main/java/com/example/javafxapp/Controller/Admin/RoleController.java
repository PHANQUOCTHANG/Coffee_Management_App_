package com.example.javafxapp.Controller.Admin;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Helpper.UploadImage;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Permission;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Model.Role;
import com.example.javafxapp.Service.CategoryService;
import com.example.javafxapp.Service.PermissionService;
import com.example.javafxapp.Service.RolePermissionService;
import com.example.javafxapp.Service.RoleService;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class RoleController {
    @FXML
    private GridPane grid;
    @FXML
    private TextField searchField , roleNameField ;
    @FXML
    private TextArea descriptionField ;

    @FXML
    private Button btnId ;
    @FXML
    private JFXButton btnAdd ;

    @FXML
    private VBox checkboxContainer ;

    private RoleService roleService = new RoleService() ;
    private PermissionService permissionService = new PermissionService() ;
    private RolePermissionService rolePermissionService = new RolePermissionService() ;


    // hàm lấy tất cả cả các role đang hoạt động .
    public void loadData() {



        List<Role> roles = roleService.getAllRole() ;


        if (roles == null || roles.isEmpty()) {
            System.out.println("Không có dữ liệu từ database!");
            return;
        }

        int row = 0;
        for (Role role : roles) {
            // Cột STT
            Label lblStt = new Label(String.valueOf(row + 1));


            // Cột tên
            Label lblName = new Label(role.getRole_name());

            // Cột hành động (Button)
            JFXButton btnDetail = new JFXButton("Chi tiết");
            btnDetail.getStyleClass().add("detail-button");
            btnDetail.setOnAction(e -> handleDetail(role.getRole_id())) ;

            JFXButton btnPermission = new JFXButton("Phân quyền");
            btnPermission.getStyleClass().add("permission-button");
            btnPermission.setOnAction(e -> handleRolePermission(role.getRole_id())) ;

            // Thêm vào GridPane
            grid.add(lblStt, 0, row);
            grid.add(lblName, 1, row);
            grid.add(btnDetail, 2, row);
            grid.add(btnPermission,3,row);

            row++; // Tăng số hàng
        }
    }


    // mặc định khi đi vào file fxml có fx:controller là controller này thì tự động khởi chạy .
    @FXML
    public void initialize() {
        if (grid != null) loadData();
    }

    // chi tiết role .
    private void handleDetail(int roleId) {
        Pages.pageDetailRole(roleId);
    }

    // chuyển qua trang thêm 1 danh mục .
    @FXML
    private void addRole() {
        Pages.pageAddRole();
    }

    // thêm role .
    @FXML
    private void addRolePost() {
        try {
            String role_name = roleNameField.getText().trim() ;
            String description = descriptionField.getText().trim() ;
            roleService.addRole(new Role(role_name,description));
            AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Thêm thành công");
            Stage stage = (Stage) btnAdd.getScene().getWindow() ;
            stage.close();
        }
        catch (RuntimeException e) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Thêm thất bại");
            e.printStackTrace();
        }
    }


    // hàm tìm kiếm bằng tên .
    @FXML
    public void searchRole() {
        String textSearch = searchField.getText().trim();
        if (textSearch.isEmpty()) return;
        grid.getChildren().clear();
        Role role = roleService.findRoleByName(textSearch) ;
        if (role != null) {
            int row = 0;
            // Cột STT
            Label lblStt = new Label(String.valueOf(row + 1));

            // Cột tên
            Label lblName = new Label(role.getRole_name());

            // Cột hành động (Button)
            JFXButton btnDetail = new JFXButton("Chi tiết");
            btnDetail.getStyleClass().add("detail-button");
            btnDetail.setOnAction(e -> handleDetail(role.getRole_id()));

            JFXButton btnPermission = new JFXButton("Phân quyền");
            btnPermission.getStyleClass().add("permission-button");
            btnPermission.setOnAction(e -> handleRolePermission(role.getRole_id())) ;

            // Thêm vào GridPane
            grid.add(lblStt, 0, row);
            grid.add(lblName, 1, row);
            grid.add(btnDetail, 2, row);
            grid.add(btnPermission,3,row);
        }
    }

    // xóa .
    @FXML
    public void deleteRole() {
        try {
            int roleId = Integer.parseInt(btnId.getText()) ;
            System.out.println(roleId);
            if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa không ?")) {
                roleService.deleteRole(roleId);
                AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Xóa thành công");
                Stage stage = (Stage)btnId.getScene().getWindow() ;
                stage.close() ;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    // update .
    @FXML
    public void updateRole() {
        try {
            int role_id = Integer.parseInt(btnId.getText()) ;
            String role_name = roleNameField.getText().trim() ;
            String description = descriptionField.getText().trim() ;
            roleService.updateRole(new Role(role_id,role_name,description));
            AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Cập nhật thành công");
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    // khi chuyển qua trang chi tiết sẽ mặc định gọi .
    @FXML
    public void loadDataDetailRole(int roleId) {
        System.out.println(roleId);
        Role role = roleService.findRoleByID(roleId) ;
        System.out.println(role);
        if (role != null) {
            roleNameField.setText(role.getRole_name());
            descriptionField.setText(role.getDescription());
            btnId.setText(String.valueOf(roleId));
            btnId.setVisible(false);
        } else {
            System.out.println("Không tìm thấy danh mục!");
        }
    }

    private List<CheckBox> checkBoxes = new ArrayList<>() ;
    // chuyển qua trang phân quyền  .
    @FXML
    public void handleRolePermission(int roleId) {Pages.pageRolePermission(roleId);}

    // load dữ liệu khi vào trang phân quyền .
    public void loadDataRolePermission(int roleId) {
        List<Permission> permissions = permissionService.getAllPermission() ;
        List<Integer> role_permission = rolePermissionService.getAllRolePermission(roleId) ;
        btnId.setText(String.valueOf(roleId));
        btnId.setVisible(false);
        for (Permission permission : permissions) {
            CheckBox cb = new CheckBox(permission.getPermission_name());
            cb.setStyle("-fx-font-size: 16; -fx-text-fill: #374151; -fx-padding: 20 20 20 20;");

            for (Integer integer : role_permission) {
                if (integer == permission.getPermission_id()) {
                    cb.setSelected(true);
                    break ;
                }
            }
            checkBoxes.add(cb) ;
            checkboxContainer.getChildren().add(cb) ;
        }
    }

    // lưu quyền được phân .
    @FXML
    public void handleSavePermissions() {
        try {
            int roleId = Integer.parseInt(btnId.getText()) ;
            for (CheckBox cb : checkBoxes) {
                if (cb.isSelected()) {
                    String permission_name = cb.getText() ;
                    Permission permission = permissionService.findPermissionByName(permission_name) ;
                    rolePermissionService.addRolePermission(roleId , permission.getPermission_id());
                    AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Phân quyền thành công");
                }
            }
        }
        catch (RuntimeException e) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Phân quyền thất bại");
            e.printStackTrace();
        }
    }


}
