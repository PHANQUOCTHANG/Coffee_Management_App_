package com.example.javafxapp.Service;

import com.example.javafxapp.Model.VnPayRequest;
import com.example.javafxapp.Server.VNPayCallbackServer;
import com.example.javafxapp.Utils.VnPayUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import com.sun.net.httpserver.HttpServer;

public class VNPayService {
    private static final String TMN_CODE = "VRD15EL3";
    private static final String HASH_SECRET = "S8BB6NJH10I9117ZO68GDDWY5Y2XO35A";
    private static final String VNP_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    // lay ipv4 de dt quet duoc ma qr(chung wifi)
    // private static final String RETURN_URL = "http://localhost:3030/vnpay_return";
    public static boolean checkPayment = false ;
    private static boolean serverStarted = false;
    private OrderService orderService = new OrderService();

    public static boolean checkPayment() {
        return checkPayment ;
    }

    public static Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query == null || query.isEmpty()) return params;
        query = query.startsWith("?") ? query.substring(1) : query;
        for (String pair : query.split("&")) {
            String[] parts = pair.split("=");
            if (parts.length == 2) {
                params.put(parts[0], parts[1]);
            }
        }
        return params;
    }
    // da chuyen qua server VNPayCallbackServer

    // public void startVNPayResultServer() throws IOException {
    //     HttpServer server = HttpServer.create(new InetSocketAddress(3030), 0);
    //     server.createContext("/vnpay_return", exchange -> {
    //         String query = exchange.getRequestURI().getQuery();
    //         Map<String, String> params = parseQueryParams("?" + query);

    //         String responseCode = params.get("vnp_ResponseCode");
    //         if ("00".equals(responseCode)) {
    //             checkPayment = true ;
    //             System.out.println("✅ Thanh toán thành công!");

    //         } else {
    //             System.out.println("❌ Thanh toán thất bại, mã lỗi: " + responseCode);
    //         }

//            // Gửi phản hồi cho trình duyệt
//            String response = "Cảm ơn bạn đã thanh toán!";
//            exchange.sendResponseHeaders(200, response.length());
//            OutputStream os = exchange.getResponseBody();
//            os.write(response.getBytes());
//            os.close();
        // });

    //     server.setExecutor(null); // default executor
    //     server.start();
    // }

    // public boolean waitForPaymentResult() {
    //     System.out.println("⏳ Đang chờ khách hàng thanh toán...");

    //     // ✅ Reset checkPayment trước khi chờ
    //     checkPayment = false;
    
    //     // Chờ tối đa 5 phút
    //     int timeout = 300; // 300 giây = 5 phút
    //     int elapsed = 0;
        
    //     while (!checkPayment && elapsed < timeout) {
    //         try {
    //             Thread.sleep(1000); // Chờ 1 giây
    //             elapsed++;
                
    //             if (elapsed % 10 == 0) { // In log mỗi 10 giây
    //                 System.out.println("⏰ Đã chờ " + elapsed + " giây...");
    //             }
    //         } catch (InterruptedException e) {
    //             break;
    //         }
    //     }
        
    //     // Kiểm tra kết quả
    //     boolean ans;
    //     if (checkPayment) {
    //         System.out.println("🎉 Thanh toán thành công!");
    //         // Cập nhật database, in hóa đơn, gửi email...
    //         // handleSuccessfulPayment();
    //         checkPayment = false; // Reset lại sau khi xử lý
    //         ans = true;
    //     } else {
    //         System.out.println("⏰ Hết thời gian chờ hoặc thanh toán thất bại");
    //         // handleFailedPayment();
    //         ans = false;
    //     }
    //     return ans;
    // }


    public String createPaymentUrl(VnPayRequest request) {
        try {
            String vnp_TxnRef = request.getOrderId();
            String vnp_IpAddr = getServerIP();
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", TMN_CODE);
            vnp_Params.put("vnp_Amount", String.valueOf(request.getAmount() * 100));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", request.getOrderInfo());
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", getReturnUrl());
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            // ✅ Debug log
            System.out.println("🔍 VNPay URL sử dụng:");
            System.out.println("   📍 Return URL: " + getReturnUrl());
            System.out.println("   🆔 Transaction Ref: " + vnp_TxnRef);
            System.out.println("   🖥️ Client IP: " + vnp_IpAddr);

            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            for (int i = 0; i < fieldNames.size(); i++) {
                String fieldName = fieldNames.get(i);
                String fieldValue = vnp_Params.get(fieldName);
                hashData.append(fieldName).append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                if (i < fieldNames.size() - 1) {
                    hashData.append('&');
                    query.append('&');
                }
            }

            String secureHash = VnPayUtils.hmacSHA512(HASH_SECRET, hashData.toString());
            query.append("&vnp_SecureHash=").append(secureHash);

            return VNP_URL + "?" + query.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean checkUpdated(int orderId, String paymentMethod) {
        return orderService.updateOrderPayment(orderId, paymentMethod);
    }

    
    
    public String generateResponseHTML(String responseCode, String orderId) {
        if ("00".equals(responseCode)) {
            return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" + // ✅ Thêm UTF-8 encoding
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>Thanh toán thành công</title>" +
                "</head>" +
                "<body style='text-align:center; font-family:Arial, sans-serif; padding:20px; background:#f5f5f5;'>" +
                "    <div style='background:white; padding:30px; border-radius:10px; box-shadow:0 2px 10px rgba(0,0,0,0.1); max-width:500px; margin:50px auto;'>" +
                "        <h1 style='color:#28a745; margin-bottom:20px;'>✅ Thanh toán thành công!</h1>" +
                "        <p style='font-size:16px; margin:15px 0;'>Mã đơn hàng: <strong style='color:#007bff;'>" + orderId + "</strong></p>" +
                "        <p style='font-size:14px; color:#666; margin:15px 0;'>Cảm ơn bạn đã sử dụng dịch vụ My Coffee Shop!</p>" +
                "        <p style='font-size:12px; color:#999; margin-top:30px;'><em>Bạn có thể đóng cửa sổ này.</em></p>" +
                "        <button onclick='window.close()' style='background:#28a745; color:white; border:none; padding:10px 20px; border-radius:5px; cursor:pointer; margin-top:20px;'>Đóng cửa sổ</button>" +
                "    </div>" +
                "    <script>" +
                "        setTimeout(function(){ window.close(); }, 5000);" + // ✅ Auto close sau 5 giây
                "    </script>" +
                "</body>" +
                "</html>";
        } else {
            return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" + // ✅ Thêm UTF-8 encoding
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>Thanh toán thất bại</title>" +
                "</head>" +
                "<body style='text-align:center; font-family:Arial, sans-serif; padding:20px; background:#f5f5f5;'>" +
                "    <div style='background:white; padding:30px; border-radius:10px; box-shadow:0 2px 10px rgba(0,0,0,0.1); max-width:500px; margin:50px auto;'>" +
                "        <h1 style='color:#dc3545; margin-bottom:20px;'>❌ Thanh toán thất bại!</h1>" +
                "        <p style='font-size:16px; margin:15px 0;'>Mã đơn hàng: <strong style='color:#007bff;'>" + orderId + "</strong></p>" +
                "        <p style='font-size:16px; margin:15px 0;'>Mã lỗi: <strong style='color:#dc3545;'>" + responseCode + "</strong></p>" +
                "        <p style='font-size:14px; color:#666; margin:15px 0;'>Vui lòng thử lại hoặc liên hệ hỗ trợ!</p>" +
                "        <button onclick='history.back()' style='background:#007bff; color:white; border:none; padding:10px 20px; border-radius:5px; cursor:pointer; margin-top:20px;'>Thử lại</button>" +
                "    </div>" +
                "</body>" +
                "</html>";
        }
    }
    private String extractOrderId(String vnpTxnRef) {
        // vnpTxnRef format: "ORDER_123_1234567890"
        // Cần lấy "123"
        if (vnpTxnRef != null && vnpTxnRef.startsWith("ORDER_")) {
            String[] parts = vnpTxnRef.split("_");
            if (parts.length >= 2) {
                return parts[1]; // Trả về "123"
            }
        }
        return vnpTxnRef; // Fallback
    }

    public void processSuccessfulPayment(String orderId, String transactionNo, String bankCode) {
        try {
            String actualOrderId = extractOrderId(orderId);
            // ✅ Xác định payment method dựa trên bankCode
            String paymentMethod = getPaymentMethodFromBankCode(bankCode);

            boolean updated = checkUpdated(Integer.parseInt(actualOrderId), paymentMethod);
            if (updated) {
                checkPayment = true;
                System.out.println("✅ Thanh toán thành công cho order: " + actualOrderId);
                System.out.println("💳 Phương thức: " + paymentMethod);
                System.out.println("🏦 Giao dịch: " + transactionNo);
            } else {
                System.out.println("❌ Không thể cập nhật trạng thái thanh toán cho order: " + actualOrderId);
            }
            System.out.println("✅ Thanh toán thành công cho order: " + actualOrderId + ", giao dịch: " + transactionNo);
            checkPayment = true; // Đặt cờ thanh toán thành công
        } catch (NumberFormatException e) {
            System.out.println("❌ Mã đơn hàng không hợp lệ: " + orderId);
        }
    }

    private String getReturnUrl() {
        try {
            VNPayCallbackServer server = VNPayCallbackServer.getInstance();
            return server.getCallbackUrl();
        } catch (Exception e) {
            return "http://localhost:3030/vnpay_return"; // Fallback URL
        }
    }

    private String getServerIP() {
        return VNPayCallbackServer.getInstance().getServerIP();
    }

    private String getPaymentMethodFromBankCode(String bankCode) {
        if (bankCode == null || bankCode.isEmpty()) {
            return "VNPay";
        }
    
        Map<String, String> bankMapping = new HashMap<>();
        bankMapping.put("NCB", "VNPay - NCB");
        bankMapping.put("AGRIBANK", "VNPay - Agribank");
        bankMapping.put("SCB", "VNPay - Sacombank");
        bankMapping.put("SAIGONBANK", "VNPay - SaigonBank");
        bankMapping.put("ACB", "VNPay - ACB");
        bankMapping.put("TCB", "VNPay - Techcombank");
        bankMapping.put("VPBANK", "VNPay - VPBank");
        bankMapping.put("TPBANK", "VNPay - TPBank");
        bankMapping.put("STB", "VNPay - Saigon Thuong Tin Bank");
        bankMapping.put("BIDV", "VNPay - BIDV");
        bankMapping.put("VIETINBANK", "VNPay - VietinBank");
        bankMapping.put("VIETCOMBANK", "VNPay - Vietcombank");
        bankMapping.put("HDBANK", "VNPay - HDBank");
        bankMapping.put("MBBANK", "VNPay - MBBank");
        bankMapping.put("MSB", "VNPay - MSB");
        bankMapping.put("NAMABANK", "VNPay - Nam A Bank");
        bankMapping.put("VNMART", "VNPay - VnMart");
        bankMapping.put("VIETCAPITALBANK", "VNPay - Viet Capital Bank");
        bankMapping.put("BRC", "VNPay - BRC");
        bankMapping.put("OCEANBANK", "VNPay - OceanBank");
        bankMapping.put("IVB", "VNPay - IVB");
        bankMapping.put("VISA", "VNPay - Visa");
        bankMapping.put("MASTERCARD", "VNPay - Mastercard");
        bankMapping.put("JCB", "VNPay - JCB");
        
        return bankMapping.getOrDefault(bankCode.toUpperCase(), "VNPay - " + bankCode);
    }
}
