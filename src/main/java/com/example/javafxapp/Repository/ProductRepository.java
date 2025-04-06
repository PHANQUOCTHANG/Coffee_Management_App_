package com.example.javafxapp.Repository;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository implements JDBCRepository<Product> {

    // add product .
    public void add(Product product) {
        String sql = "INSERT INTO Product(product_name , description , price , category_id , imgSrc) Value (?,?,?,?,?) " ;
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
    public void update(Product product) {
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
    public void delete(int productId) {
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
    public List<Product> getAll(){
        String sql = "SELECT * FROM Product where deleted = ?" ;
        List<Product> products = new ArrayList<>() ;
        try (Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement pstmt = connection.prepareStatement(sql) ;
        ) {
            pstmt.setBoolean(1,false);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                products.add(new Product(
                        rs.getInt("product_id") ,
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

    // find product by product_name .
    public Product findProductByID(int productId) {
        String sql = "SELECT * from product where product_id = ? AND deleted = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setInt(1,productId);
            preparedStatement.setBoolean(2,false);
            ResultSet rs = preparedStatement.executeQuery() ;
            if (rs.next()) {
                return new Product(
                        rs.getInt("product_id") ,
                        rs.getString("product_name")  ,
                        rs.getString("description") ,
                        rs.getDouble("price") ,
                        rs.getInt("category_id")  ,
                        rs.getString("imgSrc") );
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }

    // find product by product_name .
    public Product findProductByName(String product_name) {
        String sql = "SELECT * from product where product_name = ? AND deleted = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setString(1,product_name);
            preparedStatement.setBoolean(2,false);
            ResultSet rs = preparedStatement.executeQuery() ;
            if (rs.next()) {
                return new Product(
                        rs.getInt("product_id") ,
                        rs.getString("product_name")  ,
                        rs.getString("description") ,
                        rs.getDouble("price") ,
                        rs.getInt("category_id")  ,
                        rs.getString("imgSrc") );
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }

}
