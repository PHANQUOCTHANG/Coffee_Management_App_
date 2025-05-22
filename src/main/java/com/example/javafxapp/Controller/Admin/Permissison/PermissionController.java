package com.example.javafxapp.Controller.Admin.Permissison;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Model.Permission;
import com.example.javafxapp.Service.PermissionService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.property.SimpleBooleanProperty;
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
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class PermissionController implements Initializable {

    @FXML private TextField searchField;
    @FXML private JFXCheckBox checkBoxAll;
    @FXML private JFXButton btnAdd;
    @FXML private TableView<Permission> permissionTable;
    @FXML private TableColumn<Permission, Boolean> checkBoxColumn;
    @FXML private TableColumn<Permission, Integer> indexColumn;
    @FXML private TableColumn<Permission, String> nameColumn;
    @FXML private TableColumn<Permission, HBox> actionColumn;
    @FXML private Label permissionCountLabel, statusLabel;

    private final PermissionService permissionService = new PermissionService();
    private final ObservableList<Permission> permissionList = FXCollections.observableArrayList();
    private final ObservableList<Permission> filteredList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());

        setupTableColumns();
        loadPermissions();
    }

    private void setupTableColumns() {
        checkBoxColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        checkBoxColumn.setCellFactory(column -> new TableCell<Permission, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }

                CheckBox checkBox = new CheckBox();
                checkBox.setAlignment(Pos.CENTER);
                setAlignment(Pos.CENTER);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                Permission permission = getTableRow().getItem();
                checkBox.selectedProperty().unbindBidirectional(permission.selectedProperty());
                checkBox.selectedProperty().bindBidirectional(permission.selectedProperty());

                setGraphic(checkBox);
            }
        });

        indexColumn.setCellValueFactory(cellData -> {
            int index = permissionTable.getItems().indexOf(cellData.getValue()) + 1;
            return new SimpleObjectProperty<>(index);
        });
        indexColumn.setStyle("-fx-alignment: CENTER;");

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPermission_name()));
        nameColumn.setStyle("-fx-alignment: CENTER;");

        actionColumn.setCellValueFactory(cellData -> {
            Permission permission = cellData.getValue();
            HBox actionBox = new HBox(10);
            actionBox.setAlignment(Pos.CENTER);

            JFXButton updateButton = new JFXButton("Sửa");
            updateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px");
            updateButton.setOnAction(event -> updatePermission(permission));

            JFXButton deleteButton = new JFXButton("Xóa");
            deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 10px");
            deleteButton.setOnAction(event -> deletePermission(permission));

            actionBox.getChildren().addAll(updateButton, deleteButton);
            return new SimpleObjectProperty<>(actionBox);
        });
    }

    void loadPermissions() {
        try {
            permissionList.clear();
            permissionList.addAll(permissionService.getAllPermission());
            filteredList.clear();
            filteredList.addAll(permissionList);
            permissionTable.setItems(filteredList);
            updateDisplayStatus();
        } catch (Exception e) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách quyền");
        }
    }

    private void updateDisplayStatus() {
        int total = filteredList.size();
        permissionCountLabel.setText("Tổng số quyền: " + total);
        if (statusLabel != null) {
            statusLabel.setText(total > 0 ? "Đã tải " + total + " quyền" : "Không có quyền nào");
        }
    }

    @FXML
    private void checkBoxAll(ActionEvent event) {
        boolean selected = checkBoxAll.isSelected();
        for (Permission permission : permissionList) {
            permission.setSelected(selected);
        }
        permissionTable.refresh();
    }

    @FXML
    private void addPermission(ActionEvent event) {
        Pages.pageAddPermission(this);
    }

    private void updatePermission(Permission permission) {
        Pages.pageUpdatePermission(permission.getPermission_id() , this);
    }

    private void deletePermission(Permission permission) {
        if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa không?")) {
            try {
                permissionService.deletePermission(permission.getPermission_id());
                loadPermissions();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa quyền thành công");
            } catch (Exception e) {
                e.printStackTrace();
                AlertInfo.showAlert(Alert.AlertType.ERROR, "Thất bại", "Xóa quyền thất bại");
            }
        }
    }

    @FXML
    private void deleteAll(ActionEvent event) {
        List<Permission> selectedPermissions = new ArrayList<>();
        for (Permission permission : filteredList) {
            if (permission.isSelected()) {
                selectedPermissions.add(permission);
            }
        }

        if (selectedPermissions.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Cảnh báo", "Vui lòng chọn ít nhất một quyền để xóa");
            return;
        }

        if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa không?")) {
            try {
                for (Permission permission : selectedPermissions) {
                    permissionService.deletePermission(permission.getPermission_id());
                }
                loadPermissions();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa thành công");
            } catch (Exception e) {
                e.printStackTrace();
                AlertInfo.showAlert(Alert.AlertType.ERROR, "Thất bại", "Xóa thất bại");
            }
        }
    }

    @FXML
    private void searchPermission() {
        applyFilters();
    }

    private void applyFilters() {
        String searchText = searchField.getText().trim();
        filteredList.clear();
        permissionTable.getItems().clear();
        permissionTable.refresh();

        Collator collator = Collator.getInstance(new Locale("vi", "VN"));
        collator.setStrength(Collator.PRIMARY);

        List<Permission> list = (searchText.isEmpty())
                ? permissionList
                : permissionService.findAllByKeyword(searchText);

        filteredList.addAll(list);
        updateDisplayStatus();
    }
}
