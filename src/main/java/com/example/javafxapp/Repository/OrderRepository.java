package com.example.javafxapp.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Order;

public class OrderRepository {

    public int add(int userId, BigDecimal totalAmount , double discount , String member_phone) {
        int ans = -1;
        String sql = "INSERT INTO Orders(user_id, total_amount, status , discount , member_phone) VALUES (?, ?, ? , ? , ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pre.setInt(1, userId);
            pre.setBigDecimal(2, totalAmount);
            pre.setString(3, "Pending");
            pre.setDouble(4, discount);
            pre.setString(5, member_phone);

            int affectedRows = pre.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pre.getGeneratedKeys()) {
                    if (rs.next()) {
                        ans = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public void update(BigDecimal totalAmount, String status, int id , double discount , String member_phone) {
        String sql = "update Orders set total_amount = ?, status = ? , discount = ? , member_phone = ?  where id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setBigDecimal(1, totalAmount);
            pstmt.setString(2, status);
            pstmt.setDouble(3, discount);
            pstmt.setString(4, member_phone);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "delete from Orders where id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getAll() {
        String sql = "select o.id, o.user_id, a.account_name, o.total_amount, o.status, o.payment_method ,o.order_time, o.discount, o.member_phone " +
                "from Orders o " +
                "left join Account a " +
                "on o.user_id = a.id " +
                "order by o.id desc ";
        List<Order> ans = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("account_name"),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("status"),
                        rs.getString("payment_method"),
                        rs.getTimestamp("order_time"),
                        rs.getDouble("discount"),
                        rs.getString("member_phone"));
                ans.add(order);
            }
            return ans;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Order> getOrderByStatus(String s) {
        String sql = "select o.id, o.user_id, a.account_name, o.total_amount, o.status, o.payment_method, o.order_time, o.discount, o.member_phone " +
                "from Orders o " +
                "left join Account a " +
                "on o.user_id = a.id " +
                "where status = ? " +
                "order by o.id desc ";
        List<Order> ans = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, s);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("account_name"),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("status"),
                        rs.getString("payment_method"),
                        rs.getTimestamp("order_time"),
                        rs.getDouble("discount"),
                        rs.getString("member_phone"));
                ans.add(order);
            }
            return ans;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateOrder(int orderDetailId, BigDecimal val) {
        String sql = "update Orders " +
                "set total_amount = ? " +
                "where id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setBigDecimal(1, val);
            stmt.setInt(2, orderDetailId);
            stmt.executeUpdate();
        } catch (SQLException e) {
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

    public List<Order> getOrdersByDateRange(LocalDate from, LocalDate to) {
        List<Order> orders = new ArrayList<>();
        String sql = """
                SELECT * FROM orders
                WHERE DATE(order_time) BETWEEN ? AND ?
                ORDER BY order_time DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(from));
            stmt.setDate(2, Date.valueOf(to));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("user_id");
                BigDecimal totalAmount = rs.getBigDecimal("total_amount");
                String status = rs.getString("status");
                String paymentMethod = rs.getString("payment_method");
                Timestamp orderTime = rs.getTimestamp("order_time");

                Order order = new Order(id, userId, totalAmount, status, paymentMethod, orderTime);
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Tùy bạn xử lý: throw RuntimeException, log hoặc show alert
        }

        return orders;
    }

    public Order findOrderById(int id){
        String sql = "select o.id, o.user_id, a.account_name, o.total_amount, o.status, o.payment_method, o.order_time, o.discount, o.member_phone " +
                "from Orders o " +
                "left join Account a " +
                "on o.user_id = a.id " +
                "where o.id = ? " +
                "order by o.id desc ";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Order(rs.getInt("id"), rs.getInt("user_id"), rs.getString("account_name"), rs.getBigDecimal("total_amount"), rs.getString("status"), rs.getString("payment_method"), rs.getTimestamp("order_time"), rs.getDouble("discount"), rs.getString("member_phone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Order> getOrder(String roleCheckStr, String roleStr, String fromPrice, String toPrice, String statusCheckStr, List<String> statuses, LocalDate fromDate, LocalDate toDate){
        StringBuilder str = new StringBuilder("select o.*, a.account_name from Orders o " +
                "left join account a " +
                "on o.user_id = a.id " +
                "left join role r " +
                "on a.role_id = r.role_id " +
                " where 1=1");
        boolean checkRoleStr = false;
        if (!roleStr.equals("")) {
            if (roleCheckStr.equals("is"))
                str.append(" and r.role_name like ?");
            else
                str.append(" and r.role_name not like ?");
            checkRoleStr = true;
        }


        str.append(" and o.total_amount >= ? and o.total_amount <= ?");
        boolean checkStatuses = false;
        if (statuses.size() > 0){
            if (statusCheckStr.equals("is not"))
                str.append(" and o.status not in (");
            else
                str.append(" and o.status in (");

            for (int i = 0; i < statuses.size(); i++) {
                str.append("?");
                if (i < statuses.size() - 1) {
                    str.append(", ");
                }
            }
            str.append(")");
            checkStatuses = true;
        }


        // Sửa để bao gồm cả ngày cuối
        str.append(" and o.order_time >= ? and o.order_time <= ? order by o.id desc");
        List<Order> ans = new ArrayList<>();
        String sql = str.toString();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            int val = checkRoleStr ? 0 : 1;
            if (val == 0) stmt.setString(1, roleStr);
            stmt.setBigDecimal(2 - val, new BigDecimal(fromPrice));
            stmt.setBigDecimal(3 - val, new BigDecimal(toPrice));
            for (int i = 0; i < statuses.size(); i++){
                stmt.setString(i + 4 - val, statuses.get(i));
            }
            // kieu du lieu la timestamp, suggest nhieu cach khac nhau
            stmt.setTimestamp(statuses.size() + 4 - val, Timestamp.valueOf(fromDate.atStartOfDay()));
            stmt.setTimestamp(statuses.size() + 5 - val, Timestamp.valueOf(toDate.atStartOfDay()));
            ResultSet rs = stmt.executeQuery();
            System.out.println("getOrder: " + stmt);

            while (rs.next()){
                int id = rs.getInt(1);
                int userId = rs.getInt(2);
                BigDecimal totalAmount = rs.getBigDecimal(3);
                String status = rs.getString(4);
                String paymentMethod = rs.getString(5);
                Timestamp orderTime = rs.getTimestamp(6);
                Double discount = rs.getDouble(7);
                String memberPhone = rs.getString(8);
                String accountName = rs.getString(9);
                System.out.println("getOrder: " + id + " " + userId + " " + accountName + " " + totalAmount + " " + status + " " + orderTime + " " + discount + " " + memberPhone);
                Order order = new Order(id, userId, accountName, totalAmount, status, paymentMethod, orderTime, discount, memberPhone);
                ans.add(order);
            }
            System.out.println("getOrder: " + ans.size());
            if (ans.size() == 0) {
                System.out.println("No orders found with the given criteria.");
            }
            return ans;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }
    public boolean updateOrderPayment(int orderId, String paymentMethod) {
        String sql = "update Orders set payment_method = ?, status = 'Completed' where id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, paymentMethod);
            stmt.setInt(2, orderId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Order payment method updated successfully.");
                return true;
            } else {
                System.out.println("No order found with ID: " + orderId);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
