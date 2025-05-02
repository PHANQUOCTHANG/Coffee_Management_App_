package com.example.javafxapp.Repository;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.OrderUser_Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderUser_ProductRepository {
    // add OrderUser_Product .
    public void add (OrderUser_Product orderUserProduct) {
        String sql = "INSERT INTO OrderUser_Product(orderUser_id , product_id , quantity) VALUES(?,?,?)" ;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderUserProduct.getOrderUser_id() );
            stmt.setInt(2, orderUserProduct.getProduct_id());
            stmt.setInt(3, orderUserProduct.getQuantity());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // get all product by orderUser_id
    public List<OrderUser_Product> getAllByOrderUserId(int orderUserId) {
        List<OrderUser_Product> list = new ArrayList<>();
        String sql = "SELECT * FROM OrderUser_Product WHERE orderUser_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderUserId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new OrderUser_Product(
                        rs.getInt("orderUser_id") ,
                        rs.getInt("product_id") ,
                        rs.getInt("quantity")
                )) ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}
