package com.example.javafxapp.Repository;

import com.example.javafxapp.Config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolePermissionRepository {
    public void add (int roleId , int permissionId) {
        String sql = "INSERT INTO Role_Permission(role_id,permission_id) Value(?,?)" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql) ;
        ){
            preparedStatement.setInt(1,roleId);
            preparedStatement.setInt(2,permissionId) ;
            preparedStatement.executeUpdate() ;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void delete (int roleId) {
        String sql = "DELETE FROM Role_Permission where role_id = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql) ;
        ){
            preparedStatement.setInt(1,roleId);
            preparedStatement.executeUpdate() ;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getAll(int roleId) {
        String sql = "SELECT * from Role_Permission where role_id = ?" ;
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql) ;
        ){
            preparedStatement.setInt(1,roleId);
            ResultSet rs = preparedStatement.executeQuery();
            List<Integer> role_permission = new ArrayList<>() ;
            if (rs.next()) {
                role_permission.add(rs.getInt("permission_id")) ;
            }
            return role_permission ;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }
}
