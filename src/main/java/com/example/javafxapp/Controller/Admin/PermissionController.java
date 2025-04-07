package com.example.javafxapp.Controller.Admin;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Helpper.UploadImage;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Permission;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.CategoryService;
import com.example.javafxapp.Service.PermissionService;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class PermissionController {
    @FXML
    private GridPane grid;
    @FXML
    private TextField searchField , permissionNameField ;

    @FXML
    private Button btnId ;
    @FXML
    private JFXButton btnAdd ;

    private PermissionService permissionService = new PermissionService() ;


    // hàm lấy tất cả các quyền đang hoạt động .
    public void loadData() {



        List<Permission> permissions = permissionService.getAllPermission() ;

        if (permissions == null || permissions.isEmpty()) {
            System.out.println("Không có dữ liệu từ database!");
            return;
        }

        int row = 0;
        for (Permission permission : permissions) {
            // Cột STT
            Label lblStt = new Label(String.valueOf(row + 1));


            // Cột tên
            Label lblName = new Label(permission.getPermission_name());

            // Cột hành động (Button)
            JFXButton btnDetail = new JFXButton("Chi tiết");
            btnDetail.getStyleClass().add("detail-button");
            btnDetail.setOnAction(e -> handleDetail(permission.getPermission_id())) ;

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

    // chi tiết.
    private void handleDetail(int permissionId) {
        Pages.pageDetailPermission(permissionId) ;
    }

    // chuyển qua trang thêm.
    @FXML
    private void addPermission() {
        Pages.pageAddPermission();
    }

    // thêm permission .
    @FXML
    private void addPermissionPost() {
        try {
            String permission_name = permissionNameField.getText() ;
            permissionService.addPermission(new Permission(permission_name,false));
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
    public void searchPermission() {
        String textSearch = searchField.getText().trim();
        if (textSearch.isEmpty()) return;
        grid.getChildren().clear();
        Permission permission = permissionService.findPermissionByName(textSearch) ;
        if (permission != null) {
            int row = 0;
            // Cột STT
            Label lblStt = new Label(String.valueOf(row + 1));

            // Cột tên
            Label lblName = new Label(permission.getPermission_name());

            // Cột hành động (Button)
            JFXButton btnDetail = new JFXButton("Chi tiết");
            btnDetail.getStyleClass().add("detail-button");
            btnDetail.setOnAction(e -> handleDetail(permission.getPermission_id()));

            // Thêm vào GridPane
            grid.add(lblStt, 0, row);
            grid.add(lblName, 1, row);
            grid.add(btnDetail, 2, row);
        }
    }

    // xóa.
    @FXML
    public void deletePermission() {
        try {
            int permissionId = Integer.parseInt(btnId.getText()) ;
            if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa không ?")) {
                permissionService.deletePermission(permissionId);
                AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Xóa thành công");
                Stage stage = (Stage)btnId.getScene().getWindow() ;
                stage.close() ;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    // update.
    @FXML
    public void updatePermission() {
        try {
            int permission_id = Integer.parseInt(btnId.getText()) ;
            String permission_name = permissionNameField.getText().trim() ;
            permissionService.updatePermission(new Permission(permission_id,permission_name,false));
            AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Cập nhật thành công");
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    // khi chuyển qua trang chi tiết sẽ mặc định gọi .
    @FXML
    public void loadDataDetailPermission(int permissionId) {
        Permission permission = permissionService.findPermissionByID(permissionId) ;
        if (permission != null) {
            permissionNameField.setText(permission.getPermission_name());
            btnId.setText(String.valueOf(permissionId));
            btnId.setVisible(false);
        } else {
            System.out.println("Không tìm thấy !");
        }
    }


}
