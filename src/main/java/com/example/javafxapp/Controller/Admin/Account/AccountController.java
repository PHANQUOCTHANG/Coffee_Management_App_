package com.example.javafxapp.Controller.Admin.Account;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Model.Account;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Model.Role;
import com.example.javafxapp.Service.AccountService;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class AccountController implements Initializable {
    @FXML private TableView<Account> accountTable;
    @FXML private TableColumn<Account, Boolean> checkBoxColumn;
    @FXML private TableColumn<Account, Integer> sttColumn;
    @FXML private TableColumn<Account, String> accountNameColumn;
    @FXML private TableColumn<Account, String> roleColumn;
    @FXML private TableColumn<Account, HBox> actionColumn;
    @FXML private JFXCheckBox checkBoxAll;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private JFXButton resetButton;
    @FXML private Label accountCountLabel;
    @FXML private Label statusLabel;

    private final AccountService accountService = new AccountService();
    private final RoleService roleService = new RoleService();
    private ObservableList<Account> accountList;
    private ObservableList<Account> filteredList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountList = FXCollections.observableArrayList();
        filteredList = FXCollections.observableArrayList();

        // Thêm listener cho các bộ lọc
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        roleComboBox.setOnAction(event -> applyFilters());

        setupTableColumns();
        setupComboBoxes();
        loadAccounts();

        // Hiển thị dữ liệu ban đầu
        accountTable.setItems(filteredList);
    }

    private void setupTableColumns() {
        checkBoxColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        checkBoxColumn.setCellFactory(column -> new TableCell<Account, Boolean>() {
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

                Account account = getTableRow().getItem();
                if (account.selectedProperty() != null) {
                    checkBox.selectedProperty().unbindBidirectional(account.selectedProperty());
                    checkBox.selectedProperty().bindBidirectional(account.selectedProperty());
                }

                setGraphic(checkBox);
            }
        });

        sttColumn.setCellValueFactory(cellData -> {
            int index = accountTable.getItems().indexOf(cellData.getValue()) + 1;
            return new SimpleObjectProperty<>(index);
        });
        sttColumn.setStyle("-fx-alignment: CENTER;");

        // Sửa lại cách lấy tên tài khoản
        accountNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAccountName()));
        accountNameColumn.setStyle("-fx-alignment: CENTER;");

        roleColumn.setCellValueFactory(cellData -> {
            Role role = roleService.findRoleByID(cellData.getValue().getRoleId());
            return new SimpleStringProperty(role != null ? role.getRole_name() : "");
        });
        roleColumn.setStyle("-fx-alignment: CENTER;");

        actionColumn.setCellValueFactory(cellData -> {
            Account account = cellData.getValue();
            HBox actionBox = new HBox(10);
            actionBox.setAlignment(Pos.CENTER);

            JFXButton editButton = new JFXButton("Sửa");
            editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            editButton.setOnAction(event -> updateAccount(account));

            JFXButton deleteButton = new JFXButton("Xóa");
            deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
            deleteButton.setOnAction(event -> deleteAccount(account));

            actionBox.getChildren().addAll(editButton, deleteButton);
            return new SimpleObjectProperty<>(actionBox);
        });
    }

    private void setupComboBoxes() {
        List<Role> roles = roleService.getAllRole();
        List<String> roleNames = new ArrayList<>();
        roleNames.add("Tất cả");
        for (Role role : roles) {
            roleNames.add(role.getRole_name());
        }
        roleComboBox.getItems().setAll(roleNames);
        roleComboBox.setValue("Tất cả");
    }

    public void loadAccounts() {
        try {
            accountList.clear();
            accountList.addAll(accountService.getAllAccounts());
            filteredList.clear();
            filteredList.addAll(accountList) ;
            updateDisplayStatus();
        } catch (Exception e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách tài khoản");
        }
    }

    private void updateDisplayStatus() {
        int totalAccounts = filteredList.size();
        accountCountLabel.setText("Tổng số tài khoản: " + totalAccounts);

        // Cập nhật thêm trạng thái nếu cần
        if (statusLabel != null) {
            statusLabel.setText(totalAccounts > 0 ? "Đã tải " + totalAccounts + " tài khoản" : "Không có tài khoản nào");
        }
    }

    @FXML
    private void checkBoxAll() {
        boolean selected = checkBoxAll.isSelected();
        for (Account account : filteredList) {
            account.setSelected(selected);
        }
        accountTable.refresh();
    }

    @FXML
    private void searchAccount() {
        applyFilters();
    }

    @FXML
    private void filterAction() {
        applyFilters();
    }

    private void applyFilters() {
        String searchText = searchField.getText().trim();
        String selectedRole = roleComboBox.getValue();

        // Xóa hết dữ liệu cũ
        filteredList.clear();
        accountTable.getItems().clear();
        accountTable.refresh();

        // Khởi tạo Collator cho tiếng Việt
        Collator collator = Collator.getInstance(new Locale("vi", "VN"));
        collator.setStrength(Collator.PRIMARY); // Bỏ qua phân biệt hoa thường và dấu .

        // nếu có dữ liệu tìm kiếm thì lấy danh sách các sản phẩm theo keyworld đó để duyệt các filter khác ,
        // còn nếu không có dữ liệu tìm kiếm thì lấy theo danh sách tất cả sản phẩm từ cơ sở dữ liệu .
        List<Account> list = (searchText.isEmpty())?accountList:accountService.findAllByKeyword(searchText) ;

        // Lọc theo tất cả các điều kiện
        for (Account account : list) {
            Role role = roleService.findRoleByID(account.getRoleId()) ;
            boolean matchCategory = selectedRole.equals("Tất cả") ||
                    (role != null && role.getRole_name().equals(selectedRole));

            if (matchCategory) {
                filteredList.add(account);
            }
        }

        updateDisplayStatus();
    }

    @FXML
    private void addAccount() {
        Pages.pageAddAccount(this);
    }

    @FXML
    private void deleteAll() {
        List<Account> accountsToDelete = new ArrayList<>();
        for (Account account : filteredList) {
            if (account.isSelected()) {
                accountsToDelete.add(account);
            }
        }

        if (accountsToDelete.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn tài khoản cần xóa");
            return;
        }

        if (AlertInfo.confirmAlert("Bạn có chắc chắn muốn xóa ")) {
            try {
                for (Account account : accountsToDelete) {
                    accountService.deleteAccount(account.getId());
                }
                loadAccounts();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã xóa " + accountsToDelete.size() + " tài khoản");
            } catch (Exception e) {
                AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa tài khoản: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void updateAccount(Account account) {
        Pages.pageUpdateAccount(account.getId(), this);
    }

    private void deleteAccount(Account account) {
        if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa tài khoản này không?")) {
            try {
                accountService.deleteAccount(account.getId());
                loadAccounts();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa tài khoản thành công");
            } catch (Exception e) {
                e.printStackTrace();
                AlertInfo.showAlert(Alert.AlertType.ERROR, "Thất bại", "Xóa tài khoản thất bại: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleReset(ActionEvent event) {
        searchField.clear();
        roleComboBox.setValue("Tất cả");
        applyFilters();
    }
}