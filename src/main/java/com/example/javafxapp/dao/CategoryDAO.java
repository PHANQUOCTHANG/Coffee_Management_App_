package com.example.javafxapp.dao;

import com.example.javafxapp.config.DatabaseConnection;
import com.example.javafxapp.model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    // add category .
    public int addProduct(Category category) {
        int generatedId = -1 ;
        String sql = "INSERT INTO Category(category_name) Value (?) " ;
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setString(1,category.getCategory_name());
            pstmt.executeUpdate() ;
            ResultSet rs = pstmt.getGeneratedKeys() ;
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId ;
    }

    // update product .
    public int updateProduct(Category category) {
        int generatedId = -1 ;
        String sql = "UPDATE Category set category_name = ?  where category_id = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement pstmt = connection.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setString(1,category.getCategory_name());
            pstmt.setInt(2,category.getCategory_id());
            pstmt.executeUpdate() ;
            ResultSet rs = pstmt.getGeneratedKeys() ;
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace() ;
        }
        return generatedId ;
    }

    // delete product
    public void deleteProduct(int categoryId) {
        String sql = "UPDATE Category set deleted = ? where category_id = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement pstmt = connection.prepareStatement(sql) ;
        ) {
            pstmt.setBoolean(1,true);
            pstmt.setInt(2 , categoryId);
            pstmt.executeUpdate() ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // get all product .
    public List<Category> getAllProduct(){
        String sql = "SELECT * FROM Category where deleted = ?" ;
        List<Category> categories = new ArrayList<>() ;
        try (Connection connection = DatabaseConnection.getConnection() ;
             PreparedStatement pstmt = connection.prepareStatement(sql) ;
        ) {
            pstmt.setBoolean(1,false);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                categories.add(new Category(
                        rs.getString("category_name")
                )) ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories ;
    }
}
