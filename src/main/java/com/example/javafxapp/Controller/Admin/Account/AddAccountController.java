package com.example.javafxapp.Controller.Admin.Account;


import com.example.javafxapp.Controller.Admin.Account.AccountController;
import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Model.Account;
import com.example.javafxapp.Model.Role;
import com.example.javafxapp.Service.AccountService;
import com.example.javafxapp.Service.RoleService;
import com.example.javafxapp.Validation.ValidationAccount;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddAccountController implements Initializable {

    @FXML private TextField accountNameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private JFXButton btnAddAccount;

    private AccountService accountService;
    private RoleService roleService;
    private AccountController accountController;

    public void setAccountController(AccountController accountController) {
        this.accountController = accountController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountService = new AccountService();
        roleService = new RoleService();
        loadRoles();
    }

    private void loadRoles() {
        try {
            List<Role> roles = roleService.getAllRole() ;
            roleComboBox.setValue("Vai trò");
            if (!roles.isEmpty()) {
                roleComboBox.getItems().addAll(
                    roles.stream().map(Role::getRole_name).toList()
                );
            }
        } catch (Exception e) {
            AlertInfo.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách vai trò.");
            e.printStackTrace();
        }
    }

    @FXML
    private void addAccountPost() {
        String accountName = accountNameField.getText();
        String password = passwordField.getText();
        String roleName =  roleComboBox.getValue();

        if (!ValidationAccount.accountUtils(accountName,password , roleName , 0)) return ;

        try {
            Role role = roleService.findRoleByName(roleName);
            if (role == null) {
                 AlertInfo.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Lỗi", "Vai trò không hợp lệ.");
                 return;
            }

            Account newAccount = new Account(accountName, password , role.getRole_id());

            accountService.addAccount(newAccount);
                AlertInfo.showAlert(javafx.scene.control.Alert.AlertType.INFORMATION, "Thành công", "Tạo tài khoản thành công!");
                if (accountController != null) {
                    accountController.loadAccounts();
                }
                closeDialog();
        } catch (Exception e) {
            AlertInfo.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Lỗi", "Đã xảy ra lỗi khi tạo tài khoản.");
            e.printStackTrace();
        }
    }

    @FXML
    private void closeDialog() {
        Stage stage = (Stage) btnAddAccount.getScene().getWindow();
        stage.close();
    }
} 