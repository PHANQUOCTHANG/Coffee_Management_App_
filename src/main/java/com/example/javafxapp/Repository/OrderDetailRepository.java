package com.example.javafxapp.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.OrderDetail;
import com.example.javafxapp.Model.Product;

import javafx.collections.ObservableList;

public class OrderDetailRepository {
    
    public List<OrderDetail> getAll(int orderId){
        List<OrderDetail> ans = new ArrayList<>();
        String sql = "SELECT * FROM OrderDetail WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ans.add(new OrderDetail(
                    rs.getInt("order_id"), 
                    rs.getInt("product_id"), 
                    rs.getInt("quantity"), 
                    rs.getDouble("unit_price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public boolean checkChange(int id, ObservableList<OrderDetail> obs){
        String sql = "select count(product_id) as cnt " +
                    "from OrderDetail " +
                    "where product_id = ? " +  
                    "and quantity = ? ";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);){
            for (OrderDetail od : obs){
                stmt.setInt(1, od.getProductId());
                stmt.setInt(2, od.getQuantity());
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    int cnt = rs.getInt(1);
                    if (cnt == 0) return true;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public void update(int orderId, ObservableList<OrderDetail> obs){
        String deleteSQL = "DELETE FROM OrderDetail WHERE order_id = ?";
        String insertSQL = "INSERT INTO OrderDetail(order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
            
            // Xóa chi tiết đơn hàng cũ
            deleteStmt.setInt(1, orderId);
            deleteStmt.executeUpdate();

            // Chèn lại chi tiết đơn hàng mới
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                for (OrderDetail od : obs) {
                    insertStmt.setInt(1, orderId);
                    insertStmt.setInt(2, od.getProductId());
                    insertStmt.setInt(3, od.getQuantity());
                    insertStmt.setDouble(4, od.getUnitPrice());
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
