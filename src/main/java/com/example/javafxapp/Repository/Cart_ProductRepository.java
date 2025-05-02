package com.example.javafxapp.Repository;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Cart;
import com.example.javafxapp.Model.Cart_Product;
import com.example.javafxapp.Model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Cart_ProductRepository {
    public void add (Cart_Product cartProduct) {
        String sql = "INSERT INTO Cart_Product(cart_id,product_id,quantity) VALUE(?,?,?)" ;
        try (Connection connection = DatabaseConnection.getConnection() ;
             PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setInt(1,cartProduct.getCart_id());
            preparedStatement.setInt(2,cartProduct.getProduct_id());
            preparedStatement.setInt(3,cartProduct.getQuantity());
            preparedStatement.executeUpdate() ;
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void update (Cart_Product cartProduct) {
        String sql = "UPDATE Cart_Product SET quantity = ? where cart_id = ? and product_id = ? " ;
        try (Connection connection = DatabaseConnection.getConnection() ;
             PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setInt(1,cartProduct.getQuantity());
            preparedStatement.setInt(2,cartProduct.getCart_id());
            preparedStatement.setInt(3,cartProduct.getProduct_id());
            preparedStatement.executeUpdate() ;
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

//    public void delete (Cart_Product cartProduct) {
//        String sql = "UPDATE Cart_Product SET deleted = ? where cart_id = ? and product_id = ? " ;
//        try (Connection connection = DatabaseConnection.getConnection() ;
//             PreparedStatement preparedStatement = connection.prepareStatement(sql))
//        {
//            preparedStatement.setBoolean(1,true);
//            preparedStatement.setInt(2,cartProduct.getCart_id());
//            preparedStatement.setInt(3,cartProduct.getProduct_id());
//            preparedStatement.executeUpdate() ;
//        }catch(SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public void delete (Cart_Product cartProduct) {
        String sql = "Delete from Cart_Product where cart_id = ? and product_id = ? " ;
        try (Connection connection = DatabaseConnection.getConnection() ;
             PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setInt(1,cartProduct.getCart_id());
            preparedStatement.setInt(2,cartProduct.getProduct_id());
            preparedStatement.executeUpdate() ;
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }



    // get all cart .
    public List<Cart_Product> getAll(int cartId){
        String sql = "SELECT * FROM Cart_Product where cart_id = ? and deleted = ?" ;
        List<Cart_Product> carts = new ArrayList<>() ;
        try (Connection connection = DatabaseConnection.getConnection() ;
             PreparedStatement pstmt = connection.prepareStatement(sql) ;
        ) {
            pstmt.setInt(1,cartId);
            pstmt.setBoolean(2,false);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                carts.add(new Cart_Product(
                        rs.getInt("cart_id") ,
                        rs.getInt("product_id")  ,
                        rs.getInt("quantity")
                )) ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carts ;
    }

    // get product in card .
    public Cart_Product getProductInCart(int cartId , int productId){
        String sql = "SELECT * FROM Cart_Product where cart_id = ? and product_id = ? and deleted = ?" ;
        List<Cart_Product> carts = new ArrayList<>() ;
        try (Connection connection = DatabaseConnection.getConnection() ;
             PreparedStatement pstmt = connection.prepareStatement(sql) ;
        ) {
            pstmt.setInt(1,cartId);
            pstmt.setInt(2,productId);
            pstmt.setBoolean(3,false);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Cart_Product(
                        rs.getInt("cart_id") ,
                        rs.getInt("product_id")  ,
                        rs.getInt("quantity")
                ) ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }
}
