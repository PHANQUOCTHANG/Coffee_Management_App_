package com.example.javafxapp.dao;

import com.example.javafxapp.config.DatabaseConnection;
import com.example.javafxapp.model.Account;

import java.sql.*;

public class AuthDAO {

    // check account
    public boolean isValidAccount(String accountName, String password) throws SQLException {
        String query = "SELECT * FROM account WHERE account_name= ? AND password = ?";
        Connection connection = DatabaseConnection.getConnection() ;
        PreparedStatement preparedStatement = connection.prepareStatement(query) ;
        preparedStatement.setString(1,accountName);
        preparedStatement.setString(2,password);
        ResultSet rs = preparedStatement.executeQuery() ;
        return rs.next() ;
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
                System.out.println("✅ Thêm tài khoản thành công với ID: " + generatedId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Lỗi khi thêm tài khoản: " + e.getMessage());
        }
        return generatedId;
    }
}
