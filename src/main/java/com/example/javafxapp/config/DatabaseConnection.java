package com.example.javafxapp.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseConnection {
    private static Connection connection;

    // Hàm kết nối Database
    public static Connection getConnection() {
        if (connection == null) {
            try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("database.properties")) {
                Properties prop = new Properties();
                prop.load(input);

                String url = prop.getProperty("db.url");
                String user = prop.getProperty("db.user");
                String password = prop.getProperty("db.password");

                connection = DriverManager.getConnection(url, user, password);
                System.out.println("✅ Kết nối Database thành công!");
            } catch (Exception e) {
                System.err.println("❌ Lỗi kết nối Database: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }

    // Hàm đóng kết nối
    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("🔌 Đóng kết nối Database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
