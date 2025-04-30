package com.example.javafxapp.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Order;

public class OrderRepository{
    
    public int add(int userId, BigDecimal totalAmount){
        int ans = -1;
        String sql = "INSERT INTO Orders(user_id, total_amount, status) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pre.setInt(1, userId);
            pre.setBigDecimal(2, totalAmount);
            pre.setString(3, "Pending");

            int affectedRows = pre.executeUpdate(); 

            if (affectedRows > 0) {
                ResultSet rs = pre.getGeneratedKeys();
                if (rs.next()) {
                    ans = rs.getInt(1); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public void update(BigDecimal totalAmount, String status, int id) {
        String sql = "update Orders set total_amount = ?, status = ? where id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            
            pstmt.setBigDecimal(1, totalAmount);
            pstmt.setString(2, status);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "delete from Orders where id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<Order> getAll() {
        String sql = "select o.id, o.user_id, a.account_name, o.total_amount, o.status, o.order_time " + 
                    "from Orders o " +
                    "left join Account a " + 
                    "on o.user_id = a.id " +
                    "order by o.id desc ";
        List<Order> ans = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            while (rs.next()){
                Order order = new Order(
                    rs.getInt("id"), 
                    rs.getInt("user_id"),
                    rs.getString("account_name"),
                    rs.getBigDecimal("total_amount"),
                    rs.getString("status"),
                    rs.getTimestamp("order_time"));
                ans.add(order);
            }
            return ans;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Order> getOrderByStatus(String s) {
        String sql = "select o.id, o.user_id, a.account_name, o.total_amount, o.status, o.order_time " + 
                    "from Orders o " +
                    "left join Account a " + 
                    "on o.user_id = a.id " +
                    "where status = ? " +
                    "order by o.id desc ";
        List<Order> ans = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, s);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Order order = new Order(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("account_name"),
                    rs.getBigDecimal("total_amount"),
                    rs.getString("status"),
                    rs.getTimestamp("order_time")
                );
                ans.add(order);
            }
            return ans;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public void updateOrder(int orderDetailId, BigDecimal val){
        String sql = "update Orders " +
                    "set total_amount = ? " +
                    "where id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ){
            stmt.setBigDecimal(1, val);
            stmt.setInt(2, orderDetailId);
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateStatus(int orderId, String status) {
        String sql = "UPDATE Orders SET status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setString(1, status);
            stmt.setInt(2, orderId);
    
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Order status updated successfully.");
            } else {
                System.out.println("No order found with ID: " + orderId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
