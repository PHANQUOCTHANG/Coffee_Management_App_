package com.example.javafxapp.Repository;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Permission;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PermissionRepositoty implements JDBCRepository<Permission>{
    // add permission .
    public void add(Permission permission) {
        String sql = "INSERT INTO Permission(permission_name) Value (?) " ;
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setString(1,permission.getPermission_name());
            pstmt.executeUpdate() ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // update permission
    public void update(Permission permission) {
        String sql = "UPDATE Permission set permission_name = ?  where permission_id = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement pstmt = connection.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setString(1,permission.getPermission_name());
            pstmt.setInt(2,permission.getPermission_id());
            pstmt.executeUpdate() ;
        } catch (SQLException e) {
            e.printStackTrace() ;
        }
    }

    // delete permission .
    public void delete(int permissionId) {
        String sql = "UPDATE Permission set deleted = ? where permission_id = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement pstmt = connection.prepareStatement(sql) ;
        ) {
            pstmt.setBoolean(1,true);
            pstmt.setInt(2 , permissionId);
            pstmt.executeUpdate() ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // get all permission .
    public List<Permission> getAll(){
        String sql = "SELECT * FROM Permission where deleted = ?" ;
        List<Permission> permissions = new ArrayList<>() ;
        try (Connection connection = DatabaseConnection.getConnection() ;
             PreparedStatement pstmt = connection.prepareStatement(sql) ;
        ) {
            pstmt.setBoolean(1,false);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                permissions.add(new Permission(
                        rs.getInt("permission_id") ,
                        rs.getString("permission_name") ,
                        rs.getBoolean("deleted")
                )) ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return permissions ;
    }

    // find permission by permission_id .
    public Permission findByID(int permissionId) {
        String sql = "SELECT * from Permission where permission_id = ? AND deleted = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setInt(1,permissionId);
            preparedStatement.setBoolean(2,false);
            ResultSet rs = preparedStatement.executeQuery() ;
            if (rs.next()) {
               return (new Permission(
                        rs.getInt("permission_id") ,
                        rs.getString("permission_name") ,
                        rs.getBoolean("deleted")
                )) ;
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }

    // find permission by permission_name .
    public Permission findByName(String permissionName) {
        String sql = "SELECT * from Permission where permission_name = ? AND deleted = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setString(1,permissionName);
            preparedStatement.setBoolean(2,false);
            ResultSet rs = preparedStatement.executeQuery() ;
            if (rs.next()) {
                return (new Permission(
                        rs.getInt("permission_id") ,
                        rs.getString("permission_name") ,
                        rs.getBoolean("deleted")
                )) ;
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }

    // find all permisson by keyword .
    public List<Permission> findAllByKeyword(String keyword) {
        String sql = "SELECT * from Permission where permission_name like ? AND deleted = ?" ;
        List<Permission> permissions = new ArrayList<>() ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setString(1,"%" + keyword + "%");
            preparedStatement.setBoolean(2,false);
            ResultSet rs = preparedStatement.executeQuery() ;
            if (rs.next()) {
                permissions.add (new Permission(
                        rs.getInt("permission_id") ,
                        rs.getString("permission_name") ,
                        rs.getBoolean("deleted")
                )) ;
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }

}
