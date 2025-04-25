package com.example.javafxapp.Repository.User;

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
    
    public List<OrderDetail> getAll(int id){
        List<OrderDetail> ans = new ArrayList<>();
        String sql = "select * " +
                    "from OrderDetail " +
                    "where order_detail_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                ans.add(new OrderDetail(
                    rs.getInt("order_id"), 
                    rs.getInt("product_id"), 
                    rs.getInt("quantity"), 
                    rs.getDouble("unit_price")));
                return ans;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return ans;
    }

    public boolean checkChange(int id, ObservableList<OrderDetail> obs){
        String sql = "select count(product_id) as cnt " +
                    "from OrderDetail" +
                    "where product_id = ?" +  
                    "and quantity = ?";
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

    public void update(int id, ObservableList<OrderDetail> obs){
        String sql = "delete * " + 
                    "from OrderDetail " +
                    "where order_detail_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            sql = "insert into OrderDetail(order_id, product_id, quantity, unit_price) values (?, ?, ?, ?)";
            try (PreparedStatement stmt2 = conn.prepareStatement(sql)){
                for (OrderDetail od : obs){
                    stmt2.setInt(1, od.getOrderId());
                    stmt2.setInt(2, od.getProductId());
                    stmt2.setInt(3, od.getQuantity());
                    stmt2.setDouble(4, od.getUnitPrice());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
