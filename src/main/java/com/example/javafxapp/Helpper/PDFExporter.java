package com.example.javafxapp.Helpper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import com.example.javafxapp.Model.OrderDetail;
import com.example.javafxapp.Service.ProductService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.List;

public class PDFExporter {

    public static void exportToPDF(String filePath, List<OrderDetail> orderDetails, int oi) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Load Vietnamese font
            InputStream is = PDFExporter.class.getResourceAsStream("/com/example/javafxapp/view/fonts/Lora-Italic-VariableFont_wght.ttf");
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

            double tax = subtotal * 0.1;
            double total = subtotal + tax;
            addTotalRow(totalTable, "Tổng Cộng:", String.format("%.2f đ", total), totalFont);

            document.add(totalTable);

            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            // Footer
            Paragraph thankYou = new Paragraph("Thank you for choosing My Coffee Shop!", regularFont);
            thankYou.setAlignment(Element.ALIGN_CENTER);
            document.add(thankYou);

            document.close();
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
