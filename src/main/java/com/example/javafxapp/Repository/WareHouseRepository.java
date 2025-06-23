package com.example.javafxapp.Repository;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.WareHouse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WareHouseRepository {

    // Thêm sản phẩm vào kho
    public void add(WareHouse wareHouse) {
        String sql = "INSERT INTO WareHouse (productWareHouse_name, category_name, quantity, imgSrc, status, deleted) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, wareHouse.getProductWareHouse_name());
            pstmt.setString(2, wareHouse.getCategory_name());
            pstmt.setInt(3, wareHouse.getQuantity());
            pstmt.setString(4, wareHouse.getImgSrc());
            pstmt.setBoolean(5, wareHouse.isStatus());
            pstmt.setBoolean(6, wareHouse.isDeleted());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật thông tin sản phẩm
    public void update(WareHouse wareHouse) {
        String sql = "UPDATE WareHouse SET productWareHouse_name = ?, category_name = ?, quantity = ?, imgSrc = ?, status = ?, deleted = ? WHERE productWareHouse_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, wareHouse.getProductWareHouse_name());
            pstmt.setString(2, wareHouse.getCategory_name());
            pstmt.setInt(3, wareHouse.getQuantity());
            pstmt.setString(4, wareHouse.getImgSrc());
            pstmt.setBoolean(5, wareHouse.isStatus());
            pstmt.setBoolean(6, wareHouse.isDeleted());
            pstmt.setInt(7, wareHouse.getProductWareHouse_id());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa mềm sản phẩm khỏi kho
    public void delete(int id) {
        String sql = "UPDATE WareHouse SET deleted = true WHERE productWareHouse_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tìm tất cả sản phẩm còn tồn tại
    public List<WareHouse> getAll() {
        List<WareHouse> list = new ArrayList<>();
        String sql = "SELECT * FROM WareHouse WHERE deleted = false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new WareHouse(
                        rs.getInt("productWareHouse_id"),
                        rs.getString("productWareHouse_name"),
                        rs.getString("category_name"),
                        rs.getString("imgSrc"),
                        rs.getBoolean("status"),
                        rs.getInt("quantity"),
                        rs.getBoolean("deleted")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm theo ID
    public WareHouse findByID(int id) {
        String sql = "SELECT * FROM WareHouse WHERE productWareHouse_id = ? AND deleted = false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new WareHouse(
                        rs.getInt("productWareHouse_id"),
                        rs.getString("productWareHouse_name"),
                        rs.getString("category_name"),
                        rs.getString("imgSrc"),
                        rs.getBoolean("status"),
                        rs.getInt("quantity"),
                        rs.getBoolean("deleted")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public WareHouse findByName(String name) {
        String sql = "SELECT * FROM WareHouse WHERE productWareHouse_name = ? AND deleted = false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new WareHouse(
                        rs.getInt("productWareHouse_id"),
                        rs.getString("productWareHouse_name"),
                        rs.getString("category_name"),
                        rs.getString("imgSrc"),
                        rs.getBoolean("status"),
                        rs.getInt("quantity"),
                        rs.getBoolean("deleted")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Tìm theo tên gần giống
    public List<WareHouse> findAllByKeyword(String keyword) {
        List<WareHouse> list = new ArrayList<>();
        String sql = "SELECT * FROM WareHouse WHERE deleted = false AND productWareHouse_name LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new WareHouse(
                        rs.getInt("productWareHouse_id"),
                        rs.getString("productWareHouse_name"),
                        rs.getString("category_name"),
                        rs.getString("imgSrc"),
                        rs.getBoolean("status"),
                        rs.getInt("quantity"),
                        rs.getBoolean("deleted")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Đổi trạng thái hoạt động
    public void changeStatus(int id, boolean currentStatus) {
        String sql = "UPDATE WareHouse SET status = ? WHERE productWareHouse_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, !currentStatus);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật số lượng
    public void updateQuantity(int id, int quantity) {
        String sql = "UPDATE WareHouse SET quantity = ? WHERE productWareHouse_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
