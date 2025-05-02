package com.example.javafxapp.Controller.Admin;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Model.Permission;
import com.example.javafxapp.Service.PermissionService;
import com.example.javafxapp.Validation.ValidationPermission;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
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
    @FXML
    private JFXCheckBox checkBoxAll ;
    private List<JFXCheckBox> checkBoxes;
    @FXML
    private ComboBox showBox;
    private PermissionService permissionService = new PermissionService() ;


    // hàm lấy tất cả các quyền đang hoạt động .
    public void loadData() {
        grid.getChildren().clear();

        List<Permission> permissions = permissionService.getAllPermission() ;
        if (permissions == null || permissions.isEmpty()) {
            System.out.println("Không có dữ liệu từ database!");
            return;
        }
        checkBoxes = new ArrayList<>();
        int row = 0 , stt = 1 ;
        for (Permission permission : permissions) {

            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.setId(String.valueOf(permission.getPermission_id()));
            checkBoxes.add(checkBox);

            // STT
            Label lblStt = new Label(String.valueOf(stt++) + '.');


            // Cột tên
            Label lblName = new Label(permission.getPermission_name());

            // Cột hành động (Button)
            JFXButton btnDetail = new JFXButton("Chi tiết");
            btnDetail.getStyleClass().add("detail-button");
            btnDetail.setOnAction(e -> handleDetail(permission.getPermission_id())) ;

            // Thêm vào GridPane
            grid.add(checkBox, 0, row);
            grid.add(lblStt, 1, row);
            grid.add(lblName, 2, row);
            grid.add(btnDetail, 3, row);

            row++; // Tăng số hàng

            // Thêm Line phân cách
            Line separator = new Line();
            separator.setStartX(0);
            // Ràng buộc chiều rộng của separator theo chiều rộng của GridPane
            separator.endXProperty().bind(grid.widthProperty());
            separator.setStroke(Color.LIGHTGRAY);
            separator.setStrokeWidth(1);

            // Gộp Line qua tất cả các cột (0 đến 6) => tổng cộng 7 cột => colspan = 7
            grid.add(separator, 0, row, 4, 1);

            row++ ;
        }
        showBox.setValue("Hiển thị " + String.valueOf(permissions.size()));
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

    // Trang chi tiết .
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

    // Trang thêm .
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
            if (!ValidationPermission.validationPermissionName(permission_name)) return ;
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
            if (!ValidationPermission.validationPermissionName(permission_name)) return ;
            permissionService.updatePermission(new Permission(permission_id,permission_name,false));
            AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Cập nhật thành công");
        }
        catch (RuntimeException e) {
            e.printStackTrace();
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
                boolean check = false ;
                for (JFXCheckBox checkBox : checkBoxes) {
                    if (checkBox.isSelected()) {
                        check = true ;
                        int permissionId = Integer.parseInt(checkBox.getId());
                        permissionService.deletePermission(permissionId);
                    }
                }
                if (!check) {
                    AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng chọn ít nhất 1 danh mục để xóa");
                    return ;
                }
                loadData();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa thành công");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }


}
