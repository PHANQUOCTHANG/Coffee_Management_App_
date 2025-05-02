package com.example.javafxapp.Repository;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.OrderUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderUserRepository {

    // add orderUser .
    public void add(OrderUser orderUser) {
        String sql = "INSERT INTO OrderUser (account_id, fullName, phone, address, note, " +
                "shippingFee, methodPayment, subPrice, discount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1 , orderUser.getAccount_id());
            stmt.setString(2, orderUser.getFullName());
            stmt.setString(3, orderUser.getPhone());
            stmt.setString(4, orderUser.getAddress());
            stmt.setString(5,orderUser.getNote());
            stmt.setDouble(6, orderUser.getShippingFee());
            stmt.setString(7, orderUser.getMethodPayment());
            stmt.setDouble(8, orderUser.getSubPrice());
            stmt.setDouble(9, orderUser.getDiscount());
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "Insert successful" : "Insert failed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // get all orderUser
    public List<OrderUser> getAll() {
        List<OrderUser> list = new ArrayList<>();
        String sql = "SELECT * FROM OrderUser ORDER BY order_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrderUser order = new OrderUser(
                        rs.getInt("account_id"),
                        rs.getString("fullName"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("note"),
                        rs.getDouble("shippingFee"),
                        rs.getString("methodPayment"),
                        rs.getDouble("subPrice"),
                        rs.getDouble("discount"),
                        rs.getString("status")
                );
                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    // get orderUser in currentTime ;
    public OrderUser getOrderUserCurrent () {
        String sql = "SELECT * FROM OrderUser ORDER BY order_time limit 1" ;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery() ;
            if (rs.next()) return new OrderUser(
                    rs.getInt("orderUser_id") ,
                    rs.getInt("account_id"),
                    rs.getString("fullName"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("note"),
                    rs.getDouble("shippingFee"),
                    rs.getString("methodPayment"),
                    rs.getDouble("subPrice"),
                    rs.getDouble("discount"),
                    rs.getString("status")) ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }


}
