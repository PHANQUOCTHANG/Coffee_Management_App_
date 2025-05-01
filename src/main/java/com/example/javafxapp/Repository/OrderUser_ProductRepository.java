package com.example.javafxapp.Repository;

import com.example.javafxapp.Config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderUser_ProductRepository {
    // add OrderUser_Product .
    // public void add (com.example.javafxapp.Model.OrderUser_Product orderUserProduct) {
    //     String sql = "INSERT INTO OrderUser_Product(orderUser_id , product_id , quantity) VALUES(?,?,?)" ;
    //     try (Connection conn = DatabaseConnection.getConnection();
    //          PreparedStatement stmt = conn.prepareStatement(sql)) {

    //         stmt.setInt(1, orderUserProduct.getOrderUser_id() );
    //         stmt.setInt(2, orderUserProduct.getProduct_id());
    //         stmt.setInt(3, orderUserProduct.getQuantity());

    //         stmt.executeUpdate();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }

    // // get all product by orderUser_id
    // public List<OrderUser_ProductRepository> getAllByOrderUserId (int orderUserId) {

    // }
}
