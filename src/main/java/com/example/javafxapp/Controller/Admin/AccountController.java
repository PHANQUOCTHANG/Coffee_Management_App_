package com.example.javafxapp.Controller.Admin;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Model.Account;
import com.example.javafxapp.Model.Role;
import com.example.javafxapp.Service.AccountService;
import com.example.javafxapp.Service.RoleService;
import com.example.javafxapp.Validation.ValidationAccount;
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

public class AccountController{
    @FXML
    private GridPane grid;
    @FXML
    private TextField searchField , accountNameField , passwordField;

    @FXML
    private ComboBox roleComboBox ;
    @FXML
    private Button btnId , btnRoleId ;
    @FXML
    private JFXButton btnAdd ;
    @FXML
    private JFXCheckBox checkBoxAll ;
    private List<JFXCheckBox> checkBoxes;

    private AccountService accountService = new AccountService() ;
    private RoleService roleService = new RoleService() ;


    // hàm lấy tất cả tài khoản đang hoạt động .
    public void loadData() {

        List<String> roles = new ArrayList<>();
        for (Role role : roleService.getAllRole()) {
            roles.add(role.getRole_name()) ;
        }
        roleComboBox.getItems().addAll(roles) ;

        grid.getChildren().clear();

        List<Account> accounts = accountService.getAllAccounts() ;

        if (accounts == null || accounts.isEmpty()) {
            System.out.println("Không có dữ liệu từ database!");
            return;
        }
        checkBoxes = new ArrayList<>() ;
        int row = 0, stt = 1 ;
        for (Account account : accounts) {
            Role role = roleService.findRoleByID(account.getRoleId()) ;
            if (!role.getRole_name().equals("Customer")) {
                JFXCheckBox checkBox = new JFXCheckBox();
                checkBox.setId(String.valueOf(account.getRoleId()));
                checkBoxes.add(checkBox);

                //STT
                Label lblStt = new Label(String.valueOf(stt++) + '.');

                // Cột tên
                Label lblName = new Label(account.getAccountName());

                // Cột vai trò .
                String role_name = roleService.findRoleByID(account.getRoleId()).getRole_name() ;
                Label lblRole = new Label(role_name);

                // Cột hành động (Button)
                JFXButton btnDetail = new JFXButton("Chi tiết");
                btnDetail.getStyleClass().add("detail-button");
                btnDetail.setOnAction(e -> handleDetail(account.getId())) ;

                // Thêm vào GridPane
                grid.add(checkBox, 0, row);
                grid.add(lblStt, 1, row);
                grid.add(lblName, 2, row);
                grid.add(lblRole, 3, row);
                grid.add(btnDetail, 4, row);

                row++; // Tăng số hàng

                // Thêm Line phân cách
                Line separator = new Line();
                separator.setStartX(0);
                // Ràng buộc chiều rộng của separator theo chiều rộng của GridPane
                separator.endXProperty().bind(grid.widthProperty());
                separator.setStroke(Color.LIGHTGRAY);
                separator.setStrokeWidth(1);

                // Gộp Line qua tất cả các cột (0 đến 6) => tổng cộng 7 cột => colspan = 7
                grid.add(separator, 0, row, 5, 1);

                row++; // Tăng số hàng tiếp theo để tránh chồng lặp
            }
        }
    }


    // mặc định khi đi vào file fxml có fx:controller là controller này thì tự động khởi chạy .
    @FXML
    public void initialize() {
        if (grid != null) loadData();
    }

    // Trang chi tiết .
    // chi tiết  .
    private void handleDetail(int accountId) {
        Pages.pageDetailAccount(accountId);
    }

    // khi chuyển qua trang chi tiết sẽ mặc định gọi .
    @FXML
    public void loadDataDetailAccount(int accountId) {
        Account account = accountService.findAccountByID(accountId) ;
        List<String> roles = new ArrayList<>();
        for (Role role : roleService.getAllRole()) {
            roles.add(role.getRole_name()) ;
        }
        roleComboBox.getItems().addAll(roles) ;
        if (account != null) {
            accountNameField.setText(account.getAccountName());
            passwordField.setText(account.getPassword());
            String role_name = roleService.findRoleByID(account.getRoleId()).getRole_name() ;
            roleComboBox.setValue(role_name);
            btnId.setText(String.valueOf(accountId));
            btnId.setVisible(false);
            btnRoleId.setText(String.valueOf(account.getRoleId()));
            btnRoleId.setVisible(false);
        } else {
            System.out.println("Không tìm thấy!");
        }
    }

    // Trang thêm .
    // chuyển qua trang thêm 1 tài khoản  .
    @FXML
    private void addAccount() {
        Pages.pageAddAccount();
    }

    // thêm tài khoản .
    @FXML
    private void addAccountPost() {
        try {
            String account_name = accountNameField.getText().trim() ;
            String password = passwordField.getText().trim() ;
            int roleId = roleService.findRoleByName((String)roleComboBox.getValue()).getRole_id() ;
            if (!ValidationAccount.accountUtils(account_name,password,0)) return     ;
            accountService.addAccount(new Account(account_name,password,roleId));
            AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Thêm thành công");
            Stage stage = (Stage) btnAdd.getScene().getWindow() ;
            stage.close();
        }
        catch (RuntimeException e) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Thêm damh mục thất bại");
            e.printStackTrace();
        }
    }

    // khi chuyển qua trang thêm sẽ mặc định gọi .
    @FXML
    public void loadDataAddAccount() {
        btnRoleId.setVisible(false);
        List<String> roles = new ArrayList<>() ;
        for (Role role : roleService.getAllRole()) {
            roles.add(role.getRole_name()) ;
        }
        roleComboBox.getItems().addAll(roles) ;
    }



    // hàm tìm kiếm bằng tên .
    @FXML
    public void searchAccount() {
        String textSearch = searchField.getText().trim();
        if (textSearch.isEmpty()) return;
        grid.getChildren().clear();
        Account account = accountService.findAccountByName(textSearch) ;
        if (account != null) {
            int row = 0;
            // Cột STT
            Label lblStt = new Label(String.valueOf(row + 1));


            // Cột tên
            Label lblName = new Label(account.getAccountName());

            // Cột vai trò .
            String role_name = roleService.findRoleByID(account.getRoleId()).getRole_name() ;
            Label lblRole = new Label(role_name);

            // Cột hành động (Button)
            JFXButton btnDetail = new JFXButton("Chi tiết");
            btnDetail.getStyleClass().add("detail-button");
            btnDetail.setOnAction(e -> handleDetail(account.getId())) ;

            // Thêm vào GridPane
            grid.add(lblStt, 0, row);
            grid.add(lblName, 1, row);
            grid.add(lblRole, 2, row);
            grid.add(btnDetail, 3, row);

            row++; // Tăng số hàng
        }
    }

    // xóa.
    @FXML
    public void deleteAccount() {
        try {
            int accountId = Integer.parseInt(btnId.getText()) ;
            if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa không ?")) {
                accountService.deleteAccount(accountId);
                AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Xóa thành công");
                Stage stage = (Stage)btnId.getScene().getWindow() ;
                stage.close() ;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
    // xóa nhiều đối tượng cùng 1 lúc.
    @FXML
    public void deleteAll() {
        try {
            if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa không ?")) {
                boolean check = false;
                for (JFXCheckBox checkBox : checkBoxes) {
                    if (checkBox.isSelected()) {
                        check = true;
                        int accountId = Integer.parseInt(checkBox.getId());
                        accountService.deleteAccount(accountId);
                    }
                }
                if (!check) {
                    AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng chọn ít nhất 1 tài khoản để xóa");
                    return;
                }
                loadData();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa tài khoản thành công");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    // update  .
    @FXML
    public void updateAccount() {
        try {
            int account_id = Integer.parseInt(btnId.getText()) ;
            String account_name = accountNameField.getText().trim() ;
            String password = passwordField.getText().trim() ;
            int roleId = roleService.findRoleByName((String)roleComboBox.getValue()).getRole_id() ;
            if (!ValidationAccount.accountUtils(account_name,password,account_id)) return     ;
            accountService.updateAccount(new Account(account_id , account_name,password,roleId));
            AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Cập nhật thành công");
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    // lấy role_id của role mình chọn .
    @FXML
    public void roleAction() {
        Role role = roleService.findRoleByName((String)roleComboBox.getValue()) ;
        btnRoleId.setText(String.valueOf(role.getRole_id()));
    }

    // Lọc
    @FXML
    void filterAction() {
        String roleValue = (String) roleComboBox.getValue();
        if (roleValue.isEmpty()) return;
        else {
            grid.getChildren().clear();
            Role role = roleService.findRoleByName(roleValue) ;
            List<Account> accounts = accountService.getAllByRoleId(role.getRole_id()) ;
            if (accounts == null || accounts.isEmpty()) {
                System.out.println("Không có dữ liệu từ database!");
                return;
            }
            int row = 0 , stt = 1 ;
            for (Account account : accounts) {
                JFXCheckBox checkBox = new JFXCheckBox();
                checkBox.setId(String.valueOf(account.getRoleId()));
                checkBoxes.add(checkBox);

                //STT
                Label lblStt = new Label(String.valueOf(stt++) + '.');

                // Cột tên
                Label lblName = new Label(account.getAccountName());

                // Cột vai trò .
                String role_name = roleService.findRoleByID(account.getRoleId()).getRole_name() ;
                Label lblRole = new Label(role_name);

                // Cột hành động (Button)
                JFXButton btnDetail = new JFXButton("Chi tiết");
                btnDetail.getStyleClass().add("detail-button");
                btnDetail.setOnAction(e -> handleDetail(account.getId())) ;

                // Thêm vào GridPane
                grid.add(checkBox, 0, row);
                grid.add(lblStt, 1, row);
                grid.add(lblName, 2, row);
                grid.add(lblRole, 3, row);
                grid.add(btnDetail, 4, row);

                row++; // Tăng số hàng

                // Thêm Line phân cách
                Line separator = new Line();
                separator.setStartX(0);
                // Ràng buộc chiều rộng của separator theo chiều rộng của GridPane
                separator.endXProperty().bind(grid.widthProperty());
                separator.setStroke(Color.LIGHTGRAY);
                separator.setStrokeWidth(1);

                grid.add(separator, 0, row, 5, 1);

                row++; // Tăng số hàng tiếp theo để tránh chồng lặp
            }
        }
    }

    // checkBox all
    @FXML
    private void checkBoxAll() {
        for (JFXCheckBox jfxCheckBox : checkBoxes) {
            jfxCheckBox.setSelected(checkBoxAll.isSelected());
        }
    }


}