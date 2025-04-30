package com.example.javafxapp.Repository;

import com.example.javafxapp.Utils.PasswordUtils;
import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Account;

import java.sql.*;

public class AuthRepository {

    // check account login .
    public boolean login(String account_name, String password) {
        String sql = "SELECT * FROM Account WHERE account_name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, account_name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String passwordCheck = rs.getString("password") ;
                return PasswordUtils.checkPassword(password,passwordCheck) ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getId(String userName){
        String sql = "SELECT id FROM Account WHERE account_name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }else {
                throw new RuntimeException() ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getRole(int id){
        String sql = "SELECT r.role_name " +
                    "from Account a " +
                    "left join Role r " +
                    "on r.role_id = a.role_id " +
                    "where a.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role_name");
            }else {
                throw new RuntimeException() ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }


    // sign up
    public int signUp (Account account) {
        String sql = "INSERT INTO Account (account_name, password, role_id) VALUES (?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, account.getAccountName());
            pstmt.setString(2, PasswordUtils.hashPassword( account.getPassword()));
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
