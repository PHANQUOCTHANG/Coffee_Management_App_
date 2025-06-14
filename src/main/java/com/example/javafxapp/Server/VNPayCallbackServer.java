// File: src/main/java/com/example/javafxapp/Server/VNPayCallbackServer.java
package com.example.javafxapp.Server;

import com.example.javafxapp.Service.VNPayService;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class VNPayCallbackServer {
    private static VNPayCallbackServer instance;
    private HttpServer server;
    private boolean isRunning = false;
    private static final int PORT = 3030;
    private VNPayService vnPayService = new VNPayService(); // Assuming this is the service handling VNPay logic    
    // Singleton pattern
    private VNPayCallbackServer() {}
    
    public static VNPayCallbackServer getInstance() {
        if (instance == null) {
            instance = new VNPayCallbackServer();
        }
        return instance;
    }
    
    public void startServer() throws IOException {
        if (isRunning) {
            System.out.println("🔄 Server đã chạy rồi");
            return;
        }
        
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/vnpay_return", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = vnPayService.parseQueryParams("?" + query);
            
            String responseCode = params.get("vnp_ResponseCode");
            String orderId = params.get("vnp_TxnRef");
            String transactionNo = params.get("vnp_TransactionNo");
            String bankCode = params.get("vnp_BankCode"); // ✅ Lấy bank code

            System.out.println("🔍 VNPay Callback Details:");
            System.out.println("   Response Code: " + responseCode);
            System.out.println("   Order ID: " + orderId);
            System.out.println("   Transaction No: " + transactionNo);
            System.out.println("   Bank Code: " + bankCode);
            
            // thanh cong
            if ("00".equals(responseCode)) {
                vnPayService.processSuccessfulPayment(orderId, transactionNo, bankCode);
                
            } else {
                // PaymentStatusManager.markAsFailed(orderId);
                System.out.println("❌ Thanh toán thất bại cho order: " + orderId + ", mã lỗi: " + responseCode);
            }
            
            // Gửi response về browser
            String response = vnPayService.generateResponseHTML(responseCode, orderId);
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            
            // ✅ QUAN TRỌNG: Set UTF-8 header TRƯỚC khi send
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.getResponseHeaders().set("Cache-Control", "no-cache");
            
            // ✅ Send response với UTF-8 bytes
            exchange.sendResponseHeaders(200, responseBytes.length);
            exchange.getResponseBody().write(responseBytes);
            exchange.getResponseBody().close();
        });

        // ✅ Test endpoint
        server.createContext("/test", exchange -> {
            String clientIP = exchange.getRemoteAddress().getAddress().getHostAddress();
            String response = "✅ Server hoạt động!\n" +
                            "⏰ Time: " + new java.util.Date() + "\n" +
                            "📱 Your IP: " + clientIP + "\n" +
                            "🖥️ Server IP: " + getServerIP();
            
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        });
        
        server.setExecutor(null);
        server.start();
        isRunning = true;
        
        System.out.println("🚀 VNPay Callback Server started on port " + PORT);
    }

    public String getServerIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "localhost";
        }
    }
    
    public void stopServer() {
        if (server != null && isRunning) {
            try {
                System.out.println("🛑 Stopping VNPay server...");
                
                // ✅ Stop với delay để xử lý request cuối
                server.stop(3); // Đợi 3 giây
                
                // ✅ Set null để giải phóng tài nguyên
                server = null;
                isRunning = false;
                
                System.out.println("✅ VNPay server stopped completely");
                
                // ✅ Force garbage collection
                System.gc();
                
            } catch (Exception e) {
                System.err.println("❌ Error stopping server: " + e.getMessage());
                
                // ✅ Force stop nếu có lỗi
                if (server != null) {
                    server.stop(0); // Immediate stop
                    server = null;
                }
            }
        }
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    
    public String getCallbackUrl() {
        try {
            // ✅ Lấy IP thật của máy trong mạng LAN
            String hostIP = InetAddress.getLocalHost().getHostAddress();
            return "http://" + hostIP + ":" + PORT + "/vnpay_return";
        } catch (Exception e) {
            // Fallback về localhost nếu có lỗi
            return "http://localhost:" + PORT + "/vnpay_return";
        }
    }
}