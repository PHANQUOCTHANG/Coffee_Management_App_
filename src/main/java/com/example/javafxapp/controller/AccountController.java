package com.example.javafxapp.controller;

import com.example.javafxapp.config.DatabaseConnection;
import com.example.javafxapp.dao.AccountDAO;
import com.example.javafxapp.model.Account;
import com.example.javafxapp.model.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.*;

public class AccountController {
    @FXML
    private TextField accountNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField roleIdField;
    @FXML
    private TableView<Account> accountTable;
    @FXML
    private TableColumn<Account, Integer> idColumn;
    @FXML
    private TableColumn<Account, String> nameColumn;
    @FXML
    private TableColumn<Account, Integer> roleColumn;

    @FXML
    private Label lblStatus;


    private ObservableList<Account> accountList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadAccounts();
        accountTable.setItems(accountList);
    }

//    @FXML
//    public void handleAddAccount() {
//        String name = accountNameField.getText();
//        String password = passwordField.getText();
//        int roleId;
//
//        // Kiểm tra Role ID có phải số hay không
//        try {
//            roleId = Integer.parseInt(roleIdField.getText());
//        } catch (NumberFormatException e) {
//            showAlert("Lỗi", "Role ID phải là số!", Alert.AlertType.ERROR);
//            return;
//        }
//
//        // Kiểm tra kết nối database
//        try (Connection conn = DatabaseConnection.getConnection()) {
//            if (conn == null || conn.isClosed()) {
//                showAlert("Lỗi", "Không thể kết nối đến cơ sở dữ liệu!", Alert.AlertType.ERROR);
//                return;
//            }
//        } catch (SQLException e) {
//            showAlert("Lỗi", "Lỗi khi kiểm tra kết nối database: " + e.getMessage(), Alert.AlertType.ERROR);
//            return;
//        }
//
//        // Thêm tài khoản vào database
//        if (insertAccount(name, password, roleId)) {
//            showAlert("Thành công", "Tài khoản đã được thêm!", Alert.AlertType.INFORMATION);
//            loadAccounts();
//            clearFields();
//        } else {
//            showAlert("Lỗi", "Không thể thêm tài khoản!", Alert.AlertType.ERROR);
//        }
//    }

//    private boolean insertAccount(String accountName, String password, int roleId) {
//        String sql = "INSERT INTO Account (account_name, password, role_id) VALUES (?, ?, ?)";
//
//        try (Connection conn = DatabaseConnection.getConnection()) {
//            if (conn == null || conn.isClosed()) {
//                showAlert("Lỗi", "Kết nối database không hợp lệ!", Alert.AlertType.ERROR);
//                return false;
//            }
//
//            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setString(1, accountName);
//                stmt.setString(2, password); // Bạn có thể thêm mã hóa mật khẩu ở đây nếu cần
//                stmt.setInt(3, roleId);
//
//                int rowsAffected = stmt.executeUpdate();
//                return rowsAffected > 0;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            showAlert("Lỗi SQL", "Không thể thêm tài khoản: " + e.getMessage(), Alert.AlertType.ERROR);
//            return false;
//        }
//    }

    private final AccountDAO accountDAO = new AccountDAO();

    @FXML
    private void addAccount() {
        String accountName = accountNameField.getText();
        String password = passwordField.getText();
        String roleIdText = roleIdField.getText().trim();

        if (accountName.isEmpty() || password.isEmpty() || roleIdText.isEmpty()) {
            lblStatus.setText("Không được để trống bất kỳ trường nào!");
            return;
        }

        int roleId;
        try {
            roleId = Integer.parseInt(roleIdText);
        } catch (NumberFormatException e) {
            lblStatus.setText("Role ID phải là số!");
            return;
        }

        Account newAccount = new Account(accountName, password, roleId);
        int generatedId = accountDAO.insertAccount(newAccount);

        if (generatedId != -1) {
            lblStatus.setText("Thêm tài khoản thành công với ID: " + generatedId);
            accountNameField.clear();
            passwordField.clear();
            roleIdField.clear();
        } else {
            lblStatus.setText("Lỗi khi thêm tài khoản!");
        }
    }


    private void loadAccounts() {
        accountList.clear();
        String sql = "SELECT id, account_name, password, role_id FROM account";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                accountList.add(new Account(
                        rs.getInt("id"),
                        rs.getString("account_name"),
                        rs.getString("password"),
                        rs.getInt("role_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể tải danh sách tài khoản: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void clearFields() {
        accountNameField.clear();
        passwordField.clear();
        roleIdField.clear();
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
