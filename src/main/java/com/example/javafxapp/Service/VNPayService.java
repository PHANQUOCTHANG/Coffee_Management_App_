package com.example.javafxapp.Service;

import com.example.javafxapp.Model.VnPayRequest;
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
    private static final String RETURN_URL = "http://localhost:3030/vnpay_return";
    public static boolean checkPayment = false ;

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

    public void startVNPayResultServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(3030), 0);
        server.createContext("/vnpay_return", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = parseQueryParams("?" + query);

            String responseCode = params.get("vnp_ResponseCode");
            if ("00".equals(responseCode)) {
                checkPayment = true ;
                System.out.println("✅ Thanh toán thành công!");

            } else {
                System.out.println("❌ Thanh toán thất bại, mã lỗi: " + responseCode);
            }

//            // Gửi phản hồi cho trình duyệt
//            String response = "Cảm ơn bạn đã thanh toán!";
//            exchange.sendResponseHeaders(200, response.length());
//            OutputStream os = exchange.getResponseBody();
//            os.write(response.getBytes());
//            os.close();
        });

        server.setExecutor(null); // default executor
        server.start();
    }


    public String createPaymentUrl(VnPayRequest request) {
        try {
            String vnp_TxnRef = request.getOrderId();
            String vnp_IpAddr = "127.0.0.1";
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
            vnp_Params.put("vnp_ReturnUrl", RETURN_URL);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

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
}

