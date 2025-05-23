package com.example.javafxapp.Helpper;

import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.CategoryService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelExporter {
    private CategoryService categoryService = new CategoryService() ;
    public String exportProductsToExcel(List<Product> products) throws IOException {
        // Create workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] columns = {"STT", "Tên sản phẩm", "Giá", "Danh mục", "Trạng thái", "Nổi bật"};

        // Create header style
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // Create header cells
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 20 * 256); // Set column width
        }

        // Create data rows
        int rowNum = 1 , STT = 1 ;
        for (Product product : products) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(String.valueOf(STT));
            STT++ ;
            row.createCell(1).setCellValue(product.getProduct_name());
            row.createCell(2).setCellValue(String.valueOf(product.getPrice()));
            Category category = categoryService.findCategoryByID(product.getCategory_id()) ;
            row.createCell(3).setCellValue(category.getCategory_name());
            row.createCell(4).setCellValue(product.isStatus() ? "Ngừng hoạt động" : "Hoạt động");
            row.createCell(5).setCellValue(product.isOutstanding() ? "Có" : "Không");
        }

        // Create directory if it doesn't exist
        String exportDir = "exports";
        File directory = new File(exportDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Generate filename with timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = exportDir + "/products_" + timestamp + ".xlsx";
        File file = new File(fileName);

        // Write to file
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }
        workbook.close();

        // Open the file automatically
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(file);
            }
        }

        return fileName;
    }


}