package com.example.javafxapp.Controller.Client;

import com.example.javafxapp.Model.VnPayRequest;
import com.example.javafxapp.Service.VNPayService;
import javafx.fxml.FXML;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class VNPayController {
    private VNPayService vnPayService = new VNPayService();

    @FXML
    public void handlePayment() {
        try {
            VnPayRequest vnPayRequest = new VnPayRequest("210" , 50000, "Thanh Toan") ;
            vnPayService.startVNPayResultServer();
            String paymentUrl = vnPayService.createPaymentUrl(vnPayRequest);
            Desktop.getDesktop().browse(new URI(paymentUrl));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
