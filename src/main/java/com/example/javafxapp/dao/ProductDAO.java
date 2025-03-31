package com.example.javafxapp.dao;

import com.example.javafxapp.config.DatabaseConnection;
import com.example.javafxapp.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // add product .
    public void addProduct(Product product) {
        String sql = "INSERT INTO Product(product_name , description , price , category_id , imgSrc) Value (?,?,?,?) " ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement pstmt = connection.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setString(1,product.getProduct_name());
            pstmt.setString(2,product.getDescription());
            pstmt.setDouble(3,product.getPrice());
            pstmt.setInt(4,product.getCategory_id());
            pstmt.setString(5, product.getImgSrc());
            pstmt.executeUpdate() ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // update product .
    public void updateProduct(Product product) {
        String sql = "UPDATE Product set product_name = ? , description = ? , price = ? , category_id = ? , imgSrc = ? where product_id = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement pstmt = connection.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setString(1,product.getProduct_name());
            pstmt.setString(2,product.getDescription());
            pstmt.setDouble(3,product.getPrice());
            pstmt.setInt(4,product.getCategory_id());
            pstmt.setString(5, product.getImgSrc());
            pstmt.setInt(6,product.getProduct_id());
            pstmt.executeUpdate() ;
        } catch (SQLException e) {
            e.printStackTrace() ;
        }
    }

    // delete product
    public void deleteProduct(int productId) {
        String sql = "UPDATE Product set deleted = ? where product_id = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement pstmt = connection.prepareStatement(sql) ;
        ) {
            pstmt.setBoolean(1,true);
            pstmt.setInt(2,productId);
            pstmt.executeUpdate() ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // get all product .
    public List<Product> getAllProduct(){
        String sql = "SELECT * FROM Product where deleted = ?" ;
        List<Product> products = new ArrayList<>() ;
        try (Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement pstmt = connection.prepareStatement(sql) ;
        ) {
            pstmt.setBoolean(1,false);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                products.add(new Product(
                        rs.getString("product_name")  ,
                        rs.getString("description") ,
                        rs.getDouble("price") ,
                        rs.getInt("category_id")  ,
                        rs.getString("imgSrc")
                )) ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products ;
    }

}
