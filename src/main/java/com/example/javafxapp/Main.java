package com.example.javafxapp;

import com.example.javafxapp.Model.Account;
import com.example.javafxapp.Repository.AccountRepository;
import com.example.javafxapp.Server.VNPayCallbackServer;
import com.example.javafxapp.Service.VNPayService;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static VNPayService vnPayService;
    @Override
    public void start(Stage primaryStage) throws IOException {
        // quan li san pham 2

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/javafxapp/View/Auth/auth.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
        primaryStage.setScene(scene);
        primaryStage.setTitle("Coffee Shop Management");

        primaryStage.setOnCloseRequest(event -> {
            System.out.println("🔄 User đang đóng ứng dụng...");
            handleApplicationExit();
        });

        primaryStage.show();

        // Optional: Thông báo server status
        if (VNPayCallbackServer.getInstance().isRunning()) {
            System.out.println("💳 VNPay payment ready!");
        }

//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/javafxapp/View/Client/Checkout/vnPay.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        primaryStage.setScene(scene);
//        primaryStage.show();

//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/javafxapp/View/Admin/MainScreen/mainScreen.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Coffee Shop Management");
//        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("🛑 JavaFX Application stop() method called");
        handleApplicationExit();
        super.stop();
    }

    // ✅ Method xử lý thoát ứng dụng
    private void handleApplicationExit() {
        System.out.println("🔄 Đang thoát ứng dụng...");

        try {
            // Đóng VNPay server
            VNPayCallbackServer server = VNPayCallbackServer.getInstance();
            if (server.isRunning()) {
                System.out.println("🛑 Đang đóng VNPay server...");
                server.stopServer();

                // Đợi server đóng hoàn toàn
                Thread.sleep(1000);

                if (!server.isRunning()) {
                    System.out.println("✅ VNPay server đã đóng thành công");
                } else {
                    System.out.println("⚠️ VNPay server có thể chưa đóng hoàn toàn");
                }
            }

            // Cleanup resources khác nếu có
            // DatabaseConnection.closeAll();
            // FileLogger.close();

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đóng VNPay server: " + e.getMessage());
        } finally {
            // ✅ Đảm bảo JavaFX Platform thoát
            Platform.exit();

            // ✅ Force thoát JVM để đảm bảo tất cả threads dừng
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        //    AccountRepository accountRepository = new AccountRepository() ;
        //    accountRepository.add(new Account("Admin1234" , "123456" , 1));
        try {
            // ✅ Khởi động VNPay server trước khi launch JavaFX
            System.out.println("🚀 Đang khởi động VNPay Callback Server...");
            VNPayCallbackServer.getInstance().startServer();
            System.out.println("✅ VNPay Callback Server đã sẵn sàng!");

            // ✅ Shutdown hook (backup cho trường hợp force kill)
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("🔄 Shutdown hook triggered");
                try {
                    VNPayCallbackServer server = VNPayCallbackServer.getInstance();
                    if (server.isRunning()) {
                        System.out.println("🛑 Force stopping VNPay server...");
                        server.stopServer();
                        Thread.sleep(500);
                        System.out.println("✅ VNPay server force stopped");
                    }
                } catch (Exception e) {
                    System.err.println("❌ Error in shutdown hook: " + e.getMessage());
                }
            }));

            // Launch JavaFX application
            launch(args);

        } catch (Exception e) {
            System.err.println("❌ Không thể khởi động VNPay server: " + e.getMessage());
            e.printStackTrace();

            // Vẫn có thể chạy app nhưng không có VNPay
            System.out.println("⚠️ App sẽ chạy không có tính năng VNPay");
            launch(args);
        } finally {
            // ✅ Cleanup khi main method kết thúc
            System.out.println("👋 Main method finished");
        }
    }
}