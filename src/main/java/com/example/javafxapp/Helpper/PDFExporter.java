package com.example.javafxapp.Helpper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.awt.Desktop;

import com.example.javafxapp.Model.OrderDetail;
import com.example.javafxapp.Model.VnPayRequest;
import com.example.javafxapp.Service.ProductService;
import com.example.javafxapp.Service.VNPayService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.util.Date;

public class PDFExporter {

    public static void exportToPDF(String filePath, List<OrderDetail> orderDetails, int oi) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Load Vietnamese font
            InputStream is = PDFExporter.class.getResourceAsStream("/com/example/javafxapp/Font/Lora-Italic-VariableFont_wght.ttf");
            if (is == null) {
                throw new IOException("Font not found");
            }
            byte[] fontBytes = is.readAllBytes();
            BaseFont baseFont = BaseFont.createFont("Lora-Italic-VariableFont_wght.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, fontBytes, null);
            // BaseFont baseFont = BaseFont.createFont("Arial", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font titleFont = new Font(baseFont, 20, Font.BOLD);
            Font regularFont = new Font(baseFont, 12);
            Font boldFont = new Font(baseFont, 12, Font.BOLD);
            Font totalFont = new Font(baseFont, 12, Font.BOLD, BaseColor.BLACK);

            // Title
            Paragraph title = new Paragraph("MY COFFEE SHOP", titleFont);
            title.setAlignment(Element.ALIGN_LEFT);
            document.add(title);

            Paragraph subTitle = new Paragraph("SPECIALTY COFFEE", regularFont);
            subTitle.setSpacingAfter(10);
            document.add(subTitle);

            // Order ID + Date
            Paragraph orderId = new Paragraph("Hoá đơn #" + oi, new Font(baseFont, 12, Font.BOLD, BaseColor.BLUE));
            orderId.setSpacingAfter(5);
            document.add(orderId);

            String date = new SimpleDateFormat("MMMM dd, yyyy • HH:mm").format(new Date());
            Paragraph dateTime = new Paragraph(date, regularFont);
            dateTime.setAlignment(Element.ALIGN_RIGHT);
            document.add(dateTime);

            document.add(Chunk.NEWLINE);

            // Table header
            PdfPTable table = new PdfPTable(new float[]{4, 1, 2, 2});
            table.setWidthPercentage(100);
            addCell(table, "Sản phẩm", boldFont);
            addCell(table, "S.Lượng", boldFont);
            addCell(table, "Đơn giá", boldFont);
            addCell(table, "Tổng giá", boldFont);

            double subtotal = 0;
            ProductService productService = new ProductService();
            for (OrderDetail od : orderDetails) {
                addCell(table, productService.findProductByID(od.getProductId()).getProduct_name(), regularFont);
                addCell(table, String.valueOf(od.getQuantity()), regularFont);
                addCell(table, String.format("%.2f đ", productService.findProductByID(od.getProductId()).getPrice()), regularFont);
                addCell(table, String.format("%.2f đ", od.getUnitPrice()), regularFont);
                subtotal += od.getUnitPrice();
            }

            document.add(table);

            document.add(Chunk.NEWLINE);

            // Subtotal and Tax
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(40);
            totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            double total = subtotal;
            addTotalRow(totalTable, "Tổng Cộng:", String.format("%.2f đ", total), totalFont);

            document.add(totalTable);

            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            // Tạo VNPay QR Code với thông tin người nhận Agribank
            try {
                // Tạo VNPay request đơn giản
                // Tạo unique order ID
                String uniqueOrderId = "ORDER_" + oi + "_" + System.currentTimeMillis();
                VnPayRequest vnPayRequest = new VnPayRequest();
                vnPayRequest.setOrderId(uniqueOrderId); // ✅ Unique mỗi lần
                vnPayRequest.setAmount(Math.round(total));
                vnPayRequest.setOrderInfo("Thanh toan don hang " + oi);

                // Tạo VNPay payment URL
                VNPayService vnPayService = new VNPayService();
                String vnPayUrl = vnPayService.createPaymentUrl(vnPayRequest);
                System.out.println("VNPay URL: " + vnPayUrl);

                // Tạo mã QR từ VNPay URL
                BarcodeQRCode qrCode = new BarcodeQRCode(vnPayUrl, 300, 300, null);
                Image qrImage = qrCode.getImage();
                qrImage.setAlignment(Element.ALIGN_CENTER);
                qrImage.scaleAbsolute(200, 200);

                // Thêm ghi chú dưới QR
                Paragraph qrNote = new Paragraph("Quét mã để thanh toán qua VNPay", regularFont);
                qrNote.setAlignment(Element.ALIGN_CENTER);
                qrNote.setSpacingAfter(5);

                // Thông tin thanh toán đơn giản
                Paragraph paymentInfo = new Paragraph(
                    "Số tiền: " + String.format("%.0f", total) + " đ\n" +
                    "Mã đơn hàng: ORDER_" + oi, 
                    new Font(baseFont, 10, Font.NORMAL, BaseColor.GRAY)
                );
                paymentInfo.setAlignment(Element.ALIGN_CENTER);

                document.add(qrImage);
                document.add(qrNote);
                document.add(paymentInfo);

            } catch (Exception e) {
                // Fallback nếu không tạo được VNPay QR
                System.err.println("Error creating VNPay QR code: " + e.getMessage());
                
                // Tạo QR đơn giản với thông tin cơ bản
                String fallbackQR = "Thanh toan don hang #" + oi + "\n" +
                                "So tien: " + String.format("%.0f", total) + " VND\n" +
                                "My Coffee Shop";
                BarcodeQRCode qrCode = new BarcodeQRCode(fallbackQR, 300, 300, null);
                Image qrImage = qrCode.getImage();
                qrImage.setAlignment(Element.ALIGN_CENTER);
                qrImage.scaleAbsolute(200, 200);

                Paragraph qrNote = new Paragraph("Thông tin thanh toán", regularFont);
                qrNote.setAlignment(Element.ALIGN_CENTER);

                document.add(qrImage);
                document.add(qrNote);
            }

            document.close();
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(new File(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("PDF exported successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    private static void addCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }

    private static void addTotalRow(PdfPTable table, String label, String amount, Font font) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell amountCell = new PdfPCell(new Phrase(amount, font));
        amountCell.setBorder(Rectangle.NO_BORDER);
        amountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.addCell(labelCell);
        table.addCell(amountCell);
    }
}
