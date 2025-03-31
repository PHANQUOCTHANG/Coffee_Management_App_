package com.example.javafxapp.dao;

import com.example.javafxapp.config.DatabaseConnection;
import com.example.javafxapp.model.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    // add account .
    public void addAccount(Account account) {
        String sql = "INSERT INTO Account (account_name, password, role_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, account.getAccountName());
            pstmt.setString(2, account.getPassword());
            pstmt.setInt(3, account.getRoleId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Lỗi khi thêm tài khoản: " + e.getMessage());
        }
    }

    // update account .
    public void updateAccount(Account account) {
        String sql = "UPDATE Account SET account_name = ?, password = ?, role_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, account.getAccountName());
            stmt.setString(2, account.getPassword());
            stmt.setInt(3, account.getRoleId());
            stmt.setInt(4, account.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // delete account .
    public void deleteAccount(int accountId) {
        String sql = "UPDATE Account set deleted = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, true);
            stmt.setInt(2, accountId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // get all account .
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT id, account_name, password, role_id FROM Account where deleted = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, false);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                accounts.add(new Account(
                        rs.getInt("id"),
                        rs.getString("account_name"),
                        rs.getString("password"),
                        rs.getInt("role_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    // get account
    public Account getAccount(int accountId) {
        Account account = new Account();
        String sql = "SELECT id, account_name, password, role_id FROM Account where deleted = ? and id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, false);
            ResultSet rs = stmt.executeQuery();
            account.setId(rs.getInt("id")) ;
            account.setAccountName(rs.getString("account_name")) ;
            account.setPassword(rs.getString("password"));
            account.setRoleId(rs.getInt("role_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }
}
