package com.example.javafxapp.Repository;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberRepository implements JDBCRepository<Member> {

    // Thêm thành viên mới
    public void add(Member member) {
        String sql = "INSERT INTO Member(member_phone, point) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, member.getMemberPhone());
            pstmt.setInt(2, member.getPoint());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật thông tin thành viên
    public void update(Member member) {
        String sql = "UPDATE Member SET member_phone = ?, point = ? WHERE member_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, member.getMemberPhone());
            pstmt.setInt(2, member.getPoint());
            pstmt.setInt(3, member.getMemberId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xoá mềm (đặt deleted = true)
    public void delete(int memberId) {
        String sql = "UPDATE Member SET deleted = ? WHERE member_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setBoolean(1, true);
            pstmt.setInt(2, memberId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy tất cả thành viên chưa bị xoá
    public List<Member> getAll() {
        String sql = "SELECT * FROM Member WHERE deleted = ?";
        List<Member> members = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setBoolean(1, false);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                members.add(new Member(
                        rs.getInt("member_id"),
                        rs.getString("member_phone"),
                        rs.getInt("point")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }

    // Tìm thành viên theo ID
    public Member findByID(int memberId) {
        String sql = "SELECT * FROM Member WHERE member_id = ? AND deleted = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);
            pstmt.setBoolean(2, false);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Member(
                        rs.getInt("member_id"),
                        rs.getString("member_phone"),
                        rs.getInt("point")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Tìm thành viên theo số điện thoại
    public Member findByName(String phone) {
        String sql = "SELECT * FROM Member WHERE member_phone = ? AND deleted = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, phone);
            pstmt.setBoolean(2, false);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Member(
                        rs.getInt("member_id"),
                        rs.getString("member_phone"),
                        rs.getInt("point")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Tìm thành viên theo từ khoá (số điện thoại)
    public List<Member> findAllByKeyword(String keyword) {
        String sql = "SELECT * FROM Member WHERE member_phone LIKE ? AND deleted = ?";
        List<Member> members = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setBoolean(2, false);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                members.add(new Member(
                        rs.getInt("member_id"),
                        rs.getString("member_phone"),
                        rs.getInt("point")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }
}
