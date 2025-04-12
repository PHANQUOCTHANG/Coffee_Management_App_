package com.example.javafxapp.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Order;

public class OrderRepository implements JDBCRepository<Order>{
    @Override
    public void add(Order order){
        String sql = "insert into table Orders(user_id, table_id, total_amount, status) values (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pre.setInt(1, order.getUserId());
            if (order.getTableId() != null)
                pre.setInt(2, order.getTableId());
            else pre.setNull(2, java.sql.Types.INTEGER);
            pre.setBigDecimal(3, order.getTotalAmount());
            pre.setString(4, order.getStatus());
            pre.executeUpdate();
            
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(Order order) {
        String sql = "update Orders set user_id = ?, table_id = ?, total_amount = ?, status = ? where id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            
            pstmt.setInt(1, order.getUserId());
            if (order.getTableId() != null)
                pstmt.setInt(2, order.getTableId());
            else pstmt.setNull(2, java.sql.Types.INTEGER);
            pstmt.setBigDecimal(3, order.getTotalAmount());
            pstmt.setString(4, order.getStatus());
            pstmt.setInt(5, order.getId());
            pstmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
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

    @Override
    public List<Order> getAll() {
        String sql = "select * from Orders";
        List<Order> ans = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            while (rs.next()){
                Order order = new Order(
                    rs.getInt("id"), 
                    rs.getInt("user_id"),
                    rs.getObject("table_id") != null ? rs.getInt("table_id") : null,
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
}
