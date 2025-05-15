package com.example.javafxapp.Controller.Admin.Account;

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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UpdateAccountController implements Initializable {

    @FXML
    private TextField accountNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private JFXButton btnUpdate;
    @FXML
    private JFXButton btnCancel;

    private AccountService accountService;
    private RoleService roleService;
    public static int account_id = -1;
    private AccountController accountController;

    public void setAccountController(AccountController accountController) {
        this.accountController = accountController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountService = new AccountService();
        roleService = new RoleService();
        loadRoles();

        // load dữ liệu .
        Account account = accountService.findAccountByID(account_id);
        accountNameField.setText(account.getAccountName());
        passwordField.setText(account.getPassword());
        Role role = roleService.findRoleByID(account.getRoleId());
        if (role != null) roleComboBox.setValue(role.getRole_name());
    }

    private void loadRoles() {
        try {
            List<Role> roles = roleService.getAllRole();
            List<String> rolesName = new ArrayList<>();
            if (roles != null) {
                for (Role role : roles) {
                    rolesName.add(role.getRole_name());
                }
            }
            for (Role role : roles) {
                rolesName.add(role.getRole_name());
                roleComboBox.getItems().add(role.getRole_name());
            }

        } catch (Exception e) {
            AlertInfo.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách vai trò.");
            e.printStackTrace();
        }
    }

    @FXML
    private void updateAccount() {

        String accountName = accountNameField.getText().trim();
        String password = passwordField.getText();
        String roleName = (roleComboBox == null) ? "" : roleComboBox.getValue();

        if (ValidationAccount.accountUtils(accountName, password, roleName , account_id))

            try {
                Role role = roleService.findRoleByName(roleName);
                if (role == null) {
                    AlertInfo.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Lỗi", "Vai trò không hợp lệ.");
                    return;
                }
                accountService.updateAccount(new Account(account_id, accountName, password, role.getRole_id()));
                AlertInfo.showAlert(javafx.scene.control.Alert.AlertType.INFORMATION, "Thành công", "Cập nhật tài khoản thành công!");
                if (accountController != null) {
                    accountController.loadAccounts();
                }
                closeDialog();
            } catch (Exception e) {
                AlertInfo.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Lỗi", "Đã xảy ra lỗi khi cập nhật tài khoản.");
                e.printStackTrace();
            }
    }

    @FXML
    private void closeDialog() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
} 