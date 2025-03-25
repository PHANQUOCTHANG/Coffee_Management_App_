package com.example.javafxapp.dao;

import com.example.javafxapp.config.DatabaseConnection;
import com.example.javafxapp.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class RoleDAO {
    public int addRole(Role role) {
        String sql = "INSERT INTO Role (role_name, description) VALUES (?, ?)";
        int generatedId = -1;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, role.getName());
            pstmt.setString(2, role.getDescription());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
                System.out.println("Thêm quyền thành công với ID: " + generatedId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi thêm quyền: " + e.getMessage());
        }
        return generatedId;
    }
}

