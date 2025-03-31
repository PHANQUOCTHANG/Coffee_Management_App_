package com.example.javafxapp.dao;

import com.example.javafxapp.config.DatabaseConnection;
import com.example.javafxapp.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {

    // add role .
    public void addRole(Role role) {
        String sql = "INSERT INTO Role (role_name, description) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, role.getRole_name());
            pstmt.setString(2, role.getDescription());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // update role .
    public void updateRole(Role role) {
        String sql = "UPDATE Role set role_name = ? , description = ? where role_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, role.getRole_name());
            pstmt.setString(2, role.getDescription());
            pstmt.setInt(3, role.getRole_id());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // delete role .
    public void deleteRole(int roleId) {
        String sql = "UPDATE Account set deleted = ? where role_id = id ";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, true);
            stmt.setInt(2, roleId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // get all role .
    public List<Role> getAllRole() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM Role where deleted = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, false);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                roles.add(new Role(
                        rs.getString("role_name"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }
}

