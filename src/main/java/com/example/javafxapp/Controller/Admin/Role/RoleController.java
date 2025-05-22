package com.example.javafxapp.Controller.Admin.Role;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Model.Role;
import com.example.javafxapp.Service.RoleService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RoleController implements Initializable {
    @FXML private TextField searchField;
    @FXML private JFXCheckBox checkBoxAll;
    @FXML private JFXButton btnAdd;
    @FXML private TableView<Role> roleTable;
    @FXML private TableColumn<Role, Boolean> checkBoxColumn;
    @FXML private TableColumn<Role, Integer> indexColumn;
    @FXML private TableColumn<Role, String> nameColumn;
    @FXML private TableColumn<Role, HBox> actionColumn;
    @FXML private Label roleCountLabel;

    private final RoleService roleService = new RoleService();
    private final ObservableList<Role> roleList = FXCollections.observableArrayList();
    private final ObservableList<Role> filteredList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Listener tìm kiếm
        searchField.textProperty().addListener((obs, oldV, newV) -> applyFilters());

        setupTableColumns();
        loadRoles();
    }

    private void setupTableColumns() {
        // Cột checkbox
        checkBoxColumn.setCellValueFactory(c -> c.getValue().selectedProperty());
        checkBoxColumn.setCellFactory(col -> new TableCell<Role, Boolean>() {
            @Override protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }
                Role r = getTableRow().getItem();

                // Nếu là Admin, không hiển thị checkbox
                if ("Admin".equalsIgnoreCase(r.getRole_name())) {
                    setGraphic(null); // không đặt graphic => không hiển thị checkbox
                } else {
                    JFXCheckBox cb = new JFXCheckBox();
                    cb.setAlignment(Pos.CENTER);
                    cb.selectedProperty().unbindBidirectional(r.selectedProperty());
                    cb.selectedProperty().bindBidirectional(r.selectedProperty());

                    setAlignment(Pos.CENTER);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    setGraphic(cb);
                }
            }
        });

        // Cột STT
        indexColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(
                roleTable.getItems().indexOf(c.getValue()) + 1
        ));
        indexColumn.setStyle("-fx-alignment: CENTER;");

        // Cột tên
        nameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRole_name()));
        nameColumn.setStyle("-fx-alignment: CENTER;");

        // Cột hành động
        actionColumn.setCellValueFactory(c -> {
            Role r = c.getValue();
            HBox box = new HBox(10);
            box.setAlignment(Pos.CENTER);

            JFXButton updateBtn = new JFXButton("Sửa");
            updateBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding : 10px ");
            updateBtn.setOnAction(e -> updateRole(r));

            JFXButton deleteBtn = new JFXButton("Xóa");
            deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding : 10px");
            deleteBtn.setOnAction(e -> deleteRole(r));
            if ("Admin".equalsIgnoreCase(r.getRole_name())) {
                deleteBtn.setVisible(false);
                deleteBtn.setManaged(false);
            }

            JFXButton rolePermissionBtn = new JFXButton("Phân quyền") ;
            rolePermissionBtn.setStyle("-fx-background-color: #4299E1; -fx-text-fill: white; -fx-padding : 10px");
            rolePermissionBtn.setOnAction(e -> rolePermission(r));

            box.getChildren().addAll(updateBtn ,  deleteBtn , rolePermissionBtn) ;
            return new SimpleObjectProperty<>(box);
        });
    }

    public void loadRoles() {
        try {
            roleList.clear();
            roleList.addAll(roleService.getAllRole());
            filteredList.clear();
            filteredList.addAll(roleList);
            roleTable.setItems(filteredList);
            updateDisplayStatus();
        } catch (Exception e) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tải được danh sách vai trò");
        }
    }

    private void updateDisplayStatus() {
        roleCountLabel.setText("Tổng số vai trò: " + filteredList.size());
    }

    @FXML private void checkBoxAll(ActionEvent e) {
        boolean sel = checkBoxAll.isSelected();
        for (Role role : roleList) {
            if ("Admin".equalsIgnoreCase(role.getRole_name())) role.setSelected(false);
            else role.setSelected(sel);
        }
        roleTable.refresh();
    }

    @FXML private void addRole() {
        Pages.pageAddRole(this);
    }

    @FXML private void updateRole(Role role) {
        Pages.pageUpdateRole(role.getRole_id() , this);
    }

    private void deleteRole(Role r) {
        if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa vai trò này?")) {
            try {
                roleService.deleteRole(r.getRole_id());
                loadRoles();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa vai trò thành công");
            } catch (Exception ex) {
                AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Xóa vai trò thất bại");
            }
        }
    }

    @FXML private void deleteAll(ActionEvent e) {
        List<Role> sel = new ArrayList<>();
        filteredList.forEach(r -> { if (r.isSelected()) sel.add(r); });
        if (sel.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn ít nhất 1 vai trò");
            return;
        }
        if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa các vai trò đã chọn?")) {
            sel.forEach(r -> roleService.deleteRole(r.getRole_id()));
            loadRoles();
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa thành công");
        }
    }

    @FXML private void searchRole() {
        applyFilters();
    }

    private void applyFilters() {
        String kw = searchField.getText().trim();
        filteredList.clear();
        if (kw.isEmpty()) {
            filteredList.addAll(roleList);
        } else {
            // dùng service find
            filteredList.addAll(roleService.findRolesByKeyword(kw));
        }
        updateDisplayStatus();
    }

    @FXML private void rolePermission (Role role) {
        Pages.pageRolePermission(role.getRole_id() , this);
    }
}
