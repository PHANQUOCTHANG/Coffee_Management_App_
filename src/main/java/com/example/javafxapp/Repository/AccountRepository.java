package com.example.javafxapp.Repository;

import com.example.javafxapp.Utils.PasswordUtils;
import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository implements JDBCRepository<Account> {

    // add account .
    public void add(Account account) {
        String sql = "INSERT INTO Account (account_name, password, role_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, account.getAccountName());
            pstmt.setString(2, PasswordUtils.hashPassword(account.getPassword()));
            pstmt.setInt(3, account.getRoleId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Lỗi khi thêm tài khoản: " + e.getMessage());
        }
    }

    // update account .
    public void update(Account account) {
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
    public void delete(int accountId) {
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
    public List<Account> getAll() {
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
            account.setId(rs.getInt("id"));
            account.setAccountName(rs.getString("account_name"));
            account.setPassword(rs.getString("password"));
            account.setRoleId(rs.getInt("role_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    // get nameAccount()
    public boolean existsNameAccount(String accountName){
        String sql = "SELECT account_name from account where account_name = ? " ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1,accountName);
            ResultSet rs = preparedStatement.executeQuery() ;
            if (rs.next()) {
                return true ;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false ;
    }
}
