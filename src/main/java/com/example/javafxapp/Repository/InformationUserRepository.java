package com.example.javafxapp.Repository;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Account;
import com.example.javafxapp.Model.InformationUser;
import com.example.javafxapp.Utils.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InformationUserRepository {

    // add informationUser .
    public void add(InformationUser informationUser) {
        String sql = "INSERT INTO InformationUser (fullName, email,phone , address , account_id) VALUES (?, ?, ?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1,informationUser.getFullName());
            pstmt.setString(2,informationUser.getEmail());
            pstmt.setString(3,informationUser.getPhone());
            pstmt.setString(4,informationUser.getAddress());
            pstmt.setInt(5,informationUser.getAccount_id());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Lỗi khi thêm tài khoản: " + e.getMessage());
        }
    }

    // update informationUser .
    public void update(InformationUser informationUser) {
        String sql = "UPDATE InformationUser SET fullName = ?, email = ?, phone = ?, address = ? WHERE informationUser_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, informationUser.getFullName());
            stmt.setString(2, informationUser.getEmail());
            stmt.setString(3,informationUser.getPhone());
            stmt.setString(4,informationUser.getAddress());
            stmt.setInt(5,informationUser.getInformation_id());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // get information user by accountId .
    public InformationUser getInformationUserByAccountId(int accountId) {
        String sql = "Select * from InformationUser where account_id = ? and deleted = ?" ;
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, accountId);
            preparedStatement.setBoolean(2,false);
            ResultSet rs = preparedStatement.executeQuery() ;
            if (rs.next()) {
                return new InformationUser(
                        rs.getInt("informationUser_id") ,
                        rs.getString("fullName") ,
                        rs.getString("email") ,
                        rs.getString("phone") ,
                        rs.getString("address") ,
                        rs.getInt("account_id") ,
                        rs.getBoolean("deleted")
                ) ;
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }
}
