package com.example.javafxapp.controller;

import com.example.javafxapp.alert.AlertInfo;
import com.example.javafxapp.dao.AccountDAO;
import com.example.javafxapp.model.Account;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;

import java.util.List;

public class AccountController {

    @FXML
    private TextField accountIdField;
    @FXML
    private TextField accountNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField roleIdField;


    private final AccountDAO accountDAO = new AccountDAO();


    // add account
    @FXML
    private void addAccount() {
        String accountName = accountNameField.getText();
        String password = passwordField.getText();
        String roleIdText = roleIdField.getText().trim();

        if (accountName.isEmpty() || password.isEmpty() || roleIdText.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không được để trống");
            return;
        }

        int roleId;
        try {
            roleId = Integer.parseInt(roleIdText);
        } catch (NumberFormatException e) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "roleId phải là số");
            return;
        }

        Account newAccount = new Account(accountName, password, roleId);
        accountDAO.addAccount(newAccount);
        AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Tạo tài khoản thành công");
    }

    // update account .
    @FXML
    public void updateAccount() {
        String accountName = accountNameField.getText();
        String password = passwordField.getText();
        String roleIdText = roleIdField.getText().trim();

        if (accountName.isEmpty() || password.isEmpty() || roleIdText.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không được để trống");
            return;
        }

        int roleId;
        try {
            roleId = Integer.parseInt(roleIdText);
        } catch (NumberFormatException e) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "roleId phải là số");
            return;
        }

        Account newAccount = new Account(accountName, password, roleId);
        accountDAO.updateAccount(newAccount);
        AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Sửa tài khoản thành công");
    }

    // delete account .
    @FXML
    public void deleteAccount() {
        int roleId = Integer.parseInt(accountIdField.getId().trim());
        try {
            accountDAO.deleteAccount(roleId);
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa tài khoản thành công");
        } catch (Exception e) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Không được để trống");AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Xóa tài khoản không thành công");
        }
    }

    // get all account .
    @FXML
    public List<Account> getAllAccount() {
        return accountDAO.getAllAccounts();
    }

    private void clearFields() {
        accountNameField.clear();
        passwordField.clear();
        roleIdField.clear();
    }

}
