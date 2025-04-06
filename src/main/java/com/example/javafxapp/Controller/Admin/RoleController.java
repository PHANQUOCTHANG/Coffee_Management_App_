package com.example.javafxapp.Controller.Admin;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Helpper.UploadImage;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Model.Role;
import com.example.javafxapp.Service.CategoryService;
import com.example.javafxapp.Service.RoleService;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
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

    private RoleService roleService = new RoleService() ;


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

            // Thêm vào GridPane
            grid.add(lblStt, 0, row);
            grid.add(lblName, 1, row);
            grid.add(btnDetail, 2, row);

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


    // hàm tìm kiếm danh mục bằng tên .
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

            // Thêm vào GridPane
            grid.add(lblStt, 0, row);
            grid.add(lblName, 1, row);
            grid.add(btnDetail, 2, row);
        }
    }

    // xóa 1 danh mục.
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

    // update danh mục .
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


}
