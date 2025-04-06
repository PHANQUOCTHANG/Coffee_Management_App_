package com.example.javafxapp.Repository;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoleRepository implements JDBCRepository<Role> {

    // add role .
    public void add(Role role) {
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
    public void update(Role role) {
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
    public void delete(int roleId) {
        String sql = "UPDATE Role set deleted = ? where role_id = ? ";
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
    public List<Role> getAll() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM Role where deleted = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, false);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                roles.add(new Role(
                        rs.getInt("role_id") ,
                        rs.getString("role_name") ,
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    // find role by role_id .
    public Role findRoleByID(int role_id) {
        String sql = "SELECT * from role where role_id = ? AND deleted = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setInt(1 , role_id);
            preparedStatement.setBoolean(2,false);
            ResultSet rs = preparedStatement.executeQuery() ;
            if (rs.next()) {
                return new Role(
                        rs.getInt("role_id") ,
                        rs.getString("role_name") ,
                        rs.getString("description")
                ) ;
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }

    // find role by role_name.
    public Role findRoleByName(String role_name) {
        String sql = "SELECT * from role where role_name = ? AND deleted = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setString(1,role_name);
            preparedStatement.setBoolean(2,false);
            ResultSet rs = preparedStatement.executeQuery() ;
            if (rs.next()) {
                return new Role(
                        rs.getInt("role_id") ,
                        rs.getString("role_name") ,
                        rs.getString("description")
                ) ;
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }
}

