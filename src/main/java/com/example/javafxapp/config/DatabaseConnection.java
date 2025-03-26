package com.example.javafxapp.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseConnection {


    // Hàm tạo một kết nối mới mỗi lần gọi
    public static Connection getConnection() throws SQLException {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new SQLException("Không tìm thấy file cấu hình database.properties");
            }

            Properties prop = new Properties();
            prop.load(input);

            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.user");
            String password = prop.getProperty("db.password");

            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            throw new SQLException("❌ Lỗi khi kết nối Database: " + e.getMessage(), e);
        }
    }


//    // Hàm đóng kết nối
//    public static void closeConnection() {
//        try {
//            if (connection != null) {
//                connection.close();
//                System.out.println("🔌 Đóng kết nối Database.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

}
