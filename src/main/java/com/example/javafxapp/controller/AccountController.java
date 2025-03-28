package com.example.javafxapp.controller;

import com.example.javafxapp.dao.AccountDAO;
import com.example.javafxapp.model.Account;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;

public class AccountController {

    @FXML
    private TextField accountIdField ;
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


    // add account
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
            lblStatus.setText("Thêm tài khoản thành công với ID: " + generatedId);
            clearFields();
        } else {
            lblStatus.setText("Lỗi khi thêm tài khoản!");
        }
    }

    // update account .
    @FXML public void updateAccount() {
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
        accountDAO.updateAccount(newAccount);
    }

    // delete account .
    @FXML
    public void deleteAccount() {
        int roleId = Integer.parseInt(accountIdField.getId().trim());
        try {
            accountDAO.deleteAccount(roleId);
            // alert
        }
        catch (Exception e) {

        }
    }

    // get all account .
    @FXML
    public List<Account> getAllAccount() {
        return accountDAO.getAllAccounts() ;
    }

    private void clearFields() {
        accountNameField.clear();
        passwordField.clear();
        roleIdField.clear();
    }

}
