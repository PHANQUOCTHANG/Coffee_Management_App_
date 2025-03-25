package com.example.javafxapp.controller;

import com.example.javafxapp.dao.RoleDAO;
import com.example.javafxapp.model.Role;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RoleController {

    @FXML
    private TextField txtRoleName;
    @FXML
    private TextField txtDescription;
    @FXML
    private Label lblStatus;

    private final RoleDAO roleDAO = new RoleDAO();

    @FXML
    private void addRole() {
        String roleName = txtRoleName.getText().trim();
        String description = txtDescription.getText().trim();

        if (roleName.isEmpty()) {
            lblStatus.setText("Tên quyền không được để trống!");
            return;
        }

        Role newRole = new Role(roleName, description);
        int generatedId = roleDAO.insertRole(newRole);

        if (generatedId != -1) {
            lblStatus.setText("Thêm quyền thành công với ID: " + generatedId);
            txtRoleName.clear();
            txtDescription.clear();
        } else {
            lblStatus.setText("Lỗi khi thêm quyền!");
        }
    }
}

