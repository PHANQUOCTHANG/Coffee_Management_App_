package com.example.javafxapp.dao;

import com.example.javafxapp.config.DatabaseConnection;
import com.example.javafxapp.model.Account;

import java.sql.*;

public class AuthDAO {

    // check account login .
    public boolean isValidAccount(String account_name, String password) {
        String sql = "SELECT * FROM Account WHERE account_name = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, account_name);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // sign up
    public int signUp (Account account) {
        String sql = "INSERT INTO Account (account_name, password, role_id) VALUES (?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, account.getAccountName());
            pstmt.setString(2, account.getPassword());
            pstmt.setInt(3, account.getRoleId());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }
}
