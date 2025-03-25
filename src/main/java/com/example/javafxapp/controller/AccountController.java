package com.example.javafxapp.controller;

import com.example.javafxapp.alert.AlertInfo;
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

//    @FXML
//    public void initialize() throws SQLException {
//        loadAccounts();
//        accountTable.setItems(accountList);
//    }


    private final AccountDAO accountDAO = new AccountDAO();

    @FXML
    private void addAccount() {
        String accountName = accountNameField.getText();
        String password = passwordField.getText();
        String roleIdText = roleIdField.getText().trim();

        if (accountName.isEmpty() || password.isEmpty() || roleIdText.isEmpty()) {
//            lblStatus.setText("Không được để trống bất kỳ trường nào!");
            System.out.println("11");
            return;
        }

        int roleId;
        try {
            roleId = Integer.parseInt(roleIdText);
        } catch (NumberFormatException e) {
//            lblStatus.setText("Role ID phải là số!");
            System.out.println("11");
            return;
        }

        Account newAccount = new Account(accountName, password, roleId);
        int generatedId = accountDAO.addAccount(newAccount);

        if (generatedId != -1) {
//            lblStatus.setText("Thêm tài khoản thành công với ID: " + generatedId);
            accountNameField.clear();
            passwordField.clear();
            roleIdField.clear();
        } else {
//            lblStatus.setText("Lỗi khi thêm tài khoản!");
        }
    }


//    private void loadAccounts() throws SQLException {
//        accountList.clear();
//        String sql = "SELECT id, account_name, password, role_id FROM account";
//
//        Connection connection = DatabaseConnection.getConnection() ;
//        Statement statement = connection.createStatement() ;
//        ResultSet rs = statement.executeQuery(sql) ;
//        try {
//            while (rs.next()) {
//                accountList.add(new Account(
//                        rs.getInt("id"),
//                        rs.getString("account_name"),
//                        rs.getString("password"),
//                        rs.getInt("role_id")
//                ));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            AlertInfo.showAlert(Alert.AlertType.ERROR ,"Lỗi", "Không thể tải danh sách tài khoản: " + e.getMessage());
//        }
//    }

    private void clearFields() {
        accountNameField.clear();
        passwordField.clear();
        roleIdField.clear();
    }

}
