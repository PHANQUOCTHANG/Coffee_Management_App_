package com.example.javafxapp.Controller.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.javafxapp.Model.*;
import com.example.javafxapp.Service.*;

public class RevenueController implements Initializable {

    // Labels
    @FXML private Label lblCurrentDate;
    @FXML private Label lblTotalRevenue;
    @FXML private Label lblTotalOrders;
    @FXML private Label lblAverageOrder;
    @FXML private Label lblTopSelling;
    @FXML private Label lblOrderCount;
    @FXML private Label lblStatisticTitle;

    // ComboBoxes
    @FXML private ComboBox<String> cboTimeRange;
    @FXML private ComboBox<String> cboStatisticType;
    @FXML private ComboBox<String> cboStatisticPeriod;

    // DatePickers
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    @FXML private DatePicker dateOrderFrom;
    @FXML private DatePicker dateOrderTo;
    @FXML private DatePicker dateStatisticFrom;
    @FXML private DatePicker dateStatisticTo;

    // Buttons
    @FXML private Button btnApplyFilter;
    @FXML private Button btnSearchOrders;
    @FXML private Button btnGenerateStatistic;
    @FXML private Button btnExportOverview;
    @FXML private Button btnExportOrderDetail;
    @FXML private Button btnExportStatistic;

    // Charts
    @FXML private BarChart<String, Number> chartRevenue;
    @FXML private PieChart chartProductDistribution;
    @FXML private BarChart<String, Number> chartStatistic;

    // TableViews
    @FXML private TableView<Order> tblOrders;
    @FXML private TableView<StatisticData> tblStatistic;

    // TableColumns for Orders
    @FXML private TableColumn<Order, String> colOrderId;
    @FXML private TableColumn<Order, String> colOrderDate;
    @FXML private TableColumn<Order, String> colOrderTime;
    @FXML private TableColumn<Order, String> colItems;
    @FXML private TableColumn<Order, Integer> colQuantity;
    @FXML private TableColumn<Order, String> colTotal;
    @FXML private TableColumn<Order, String> colPaymentMethod;

    // TableColumns for Statistics
    @FXML private TableColumn<StatisticData, String> colStatPeriod;
    @FXML private TableColumn<StatisticData, Integer> colStatOrderCount;
    @FXML private TableColumn<StatisticData, String> colStatRevenue;
    @FXML private TableColumn<StatisticData, String> colStatAverage;
    @FXML private TableColumn<StatisticData, String> colStatTopProduct;

    // Services
    private OrderService orderService;
    private ProductService productService;
//    private StatisticService statisticService;

    // Data lists
    private ObservableList<Order> allOrders = FXCollections.observableArrayList(); ;
    private ObservableList<Order> filteredOrders = FXCollections.observableArrayList();;
    private ObservableList<StatisticData> statisticDataList;

    // Formatters
    private NumberFormat currencyFormatter;
    private DateTimeFormatter dateFormatter;
    private DateTimeFormatter timeFormatter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize services
        orderService = new OrderService();
        productService = new ProductService();
//        statisticService = new StatisticService();

        // Initialize formatters
        currencyFormatter = new DecimalFormat("#,###.## VND");
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Set current date
        lblCurrentDate.setText(LocalDate.now().format(dateFormatter));

        // Initialize comboboxes
        initializeComboBoxes();

        // Initialize table columns
        initializeTableColumns();

        // Load initial data
        loadInitialData();
    }

    private void initializeComboBoxes() {
        // Time range options
        ObservableList<String> timeRangeOptions = FXCollections.observableArrayList(
                "Hôm nay",
                "Hôm qua",
                "7 ngày qua",
                "30 ngày qua",
                "Tháng này",
                "Tháng trước",
                "Tùy chỉnh"
        );
        cboTimeRange.setItems(timeRangeOptions);
        cboTimeRange.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateDateRangeBasedOnSelection(newVal);
        });

        // Statistic type options
        ObservableList<String> statisticTypeOptions = FXCollections.observableArrayList(
                "Doanh thu",
                "Số lượng đơn hàng",
                "Sản phẩm bán chạy"
        );
        cboStatisticType.setItems(statisticTypeOptions);

        // Statistic period options
        ObservableList<String> statisticPeriodOptions = FXCollections.observableArrayList(
                "Theo ngày",
                "Theo tuần",
                "Theo tháng",
                "Theo quý",
                "Theo năm"
        );
        cboStatisticPeriod.setItems(statisticPeriodOptions);

        // Set default selections
        cboTimeRange.getSelectionModel().select("7 ngày qua");
        cboStatisticType.getSelectionModel().select("Doanh thu");
        cboStatisticPeriod.getSelectionModel().select("Theo ngày");
    }

    private void initializeTableColumns() {
        // Initialize order table columns
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colOrderTime.setCellValueFactory(new PropertyValueFactory<>("orderTime"));
        colItems.setCellValueFactory(new PropertyValueFactory<>("items"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("formattedTotal"));
        colPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));

        // Initialize statistic table columns
        colStatPeriod.setCellValueFactory(new PropertyValueFactory<>("period"));
        colStatOrderCount.setCellValueFactory(new PropertyValueFactory<>("orderCount"));
        colStatRevenue.setCellValueFactory(new PropertyValueFactory<>("formattedRevenue"));
        colStatAverage.setCellValueFactory(new PropertyValueFactory<>("formattedAverage"));
        colStatTopProduct.setCellValueFactory(new PropertyValueFactory<>("topProduct"));
    }

    private void loadInitialData() {
        // Apply the default filter (7 days)
        updateDateRangeBasedOnSelection("7 ngày qua");

        // Load orders data
        List<Order> orders = orderService.getAllOrder() ;
        allOrders.addAll(orders) ;

        // Apply filter
        handleApplyFilter(null);

        // Load initial statistics
        handleGenerateStatistic(null);
    }

    private void updateDateRangeBasedOnSelection(String selection) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate;

        switch (selection) {
            case "Hôm nay":
                startDate = endDate;
                break;
            case "Hôm qua":
                startDate = endDate.minusDays(1);
                endDate = startDate;
                break;
            case "7 ngày qua":
                startDate = endDate.minusDays(6);
                break;
            case "30 ngày qua":
                startDate = endDate.minusDays(29);
                break;
            case "Tháng này":
                startDate = endDate.withDayOfMonth(1);
                break;
            case "Tháng trước":
                startDate = endDate.minusMonths(1).withDayOfMonth(1);
                endDate = endDate.withDayOfMonth(1).minusDays(1);
                break;
            case "Tùy chỉnh":
                // Don't change dates, let user select
                return;
            default:
                startDate = endDate.minusDays(6);
                break;
        }

        dateFrom.setValue(startDate);
        dateTo.setValue(endDate);
    }

    @FXML
    private void handleApplyFilter(ActionEvent event) {
        LocalDate startDate = dateFrom.getValue();
        LocalDate endDate = dateTo.getValue();

        if (startDate == null || endDate == null) {
            showAlert("Lỗi", "Vui lòng chọn khoảng thời gian hợp lệ");
            return;
        }

        // Filter orders within the date range
        List<Order> orders = orderService.getOrdersByDateRange(startDate, endDate);
        filteredOrders.addAll(orders) ;

        // Update dashboard metrics
        updateDashboardMetrics();

        // Update charts
        updateRevenueChart();
        updateProductDistributionChart();
    }

    @FXML
    private void handleSearchOrders(ActionEvent event) {
        LocalDate startDate = dateOrderFrom.getValue();
        LocalDate endDate = dateOrderTo.getValue();

        if (startDate == null || endDate == null) {
            showAlert("Lỗi", "Vui lòng chọn khoảng thời gian hợp lệ");
            return;
        }

        // Filter orders for the table
//        ObservableList<Order> tableOrders = orderService.filterOrdersByDateRange(allOrders, startDate, endDate);
//        tblOrders.setItems(tableOrders);

        // Update count label
//        lblOrderCount.setText("Tổng số đơn: " + tableOrders.size());
    }

    @FXML
    private void handleGenerateStatistic(ActionEvent event) {
        String statisticType = cboStatisticType.getValue();
        String periodType = cboStatisticPeriod.getValue();
        LocalDate startDate = dateStatisticFrom.getValue();
        LocalDate endDate = dateStatisticTo.getValue();

        if (statisticType == null || periodType == null || startDate == null || endDate == null) {
            showAlert("Lỗi", "Vui lòng chọn đầy đủ thông tin cho báo cáo");
            return;
        }

        // Set statistic title
        lblStatisticTitle.setText("Thống kê " + statisticType.toLowerCase() + " " + periodType.toLowerCase());

        // Generate statistic data
//        statisticDataList = statisticService.generateStatistics(
//                allOrders,
//                statisticType,
//                periodType,
//                startDate,
//                endDate
//        );

        // Update statistic table
        tblStatistic.setItems(statisticDataList);

        // Update statistic chart
        updateStatisticChart(statisticType);
    }

    @FXML
    private void handleExportOverview(ActionEvent event) {
        try {
            // Create workbook and sheet
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Tổng quan doanh thu");

            // Create header row
            createHeaderRow(sheet, new String[]{
                    "Thời gian", "Tổng doanh thu", "Số đơn hàng", "Trung bình mỗi đơn", "Sản phẩm bán chạy"
            });

            // Create data row
            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(dateFrom.getValue().format(dateFormatter) + " - " +
                    dateTo.getValue().format(dateFormatter));
            dataRow.createCell(1).setCellValue(lblTotalRevenue.getText());
            dataRow.createCell(2).setCellValue(lblTotalOrders.getText());
            dataRow.createCell(3).setCellValue(lblAverageOrder.getText());
            dataRow.createCell(4).setCellValue(lblTopSelling.getText());

            // Export workbook
            exportWorkbook(workbook, "Báo_cáo_tổng_quan");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể xuất báo cáo: " + e.getMessage());
        }
    }

    @FXML
    private void handleExportOrderDetail(ActionEvent event) {
//        try {
//            // Get orders from table
//            ObservableList<Order> orders = tblOrders.getItems();
//
//            if (orders.isEmpty()) {
//                showAlert("Thông báo", "Không có dữ liệu để xuất báo cáo");
//                return;
//            }
//
//            // Create workbook and sheet
//            Workbook workbook = new XSSFWorkbook();
//            Sheet sheet = workbook.createSheet("Chi tiết đơn hàng");
//
//            // Create header row
//            createHeaderRow(sheet, new String[]{
//                    "Mã đơn", "Ngày đặt", "Giờ đặt", "Sản phẩm", "Số lượng", "Tổng tiền", "Phương thức thanh toán"
//            });
//
//            // Create data rows
//            int rowNum = 1;
//            for (Order order : orders) {
//                Row row = sheet.createRow(rowNum++);
//                row.createCell(0).setCellValue(order.getOrderId());
//                row.createCell(1).setCellValue(order.getOrderDate());
//                row.createCell(2).setCellValue(order.getOrderTime());
//                row.createCell(3).setCellValue(order.getItems());
//                row.createCell(4).setCellValue(order.getTotalQuantity());
//                row.createCell(5).setCellValue(order.getFormattedTotal());
//                row.createCell(6).setCellValue(order.getPaymentMethod());
//            }
//
//            // Export workbook
//            exportWorkbook(workbook, "Báo_cáo_chi_tiết_đơn_hàng");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            showAlert("Lỗi", "Không thể xuất báo cáo: " + e.getMessage());
//        }
    }

    @FXML
    private void handleExportStatistic(ActionEvent event) {
//        try {
//            // Get statistic data from table
//            ObservableList<StatisticData> statistics = tblStatistic.getItems();
//
//            if (statistics.isEmpty()) {
//                showAlert("Thông báo", "Không có dữ liệu để xuất báo cáo");
//                return;
//            }
//
//            // Create workbook and sheet
//            Workbook workbook = new XSSFWorkbook();
//            Sheet sheet = workbook.createSheet(lblStatisticTitle.getText());
//
//            // Create header row
//            createHeaderRow(sheet, new String[]{
//                    "Thời gian", "Số đơn hàng", "Doanh thu", "Trung bình/đơn", "Sản phẩm bán chạy"
//            });
//
//            // Create data rows
//            int rowNum = 1;
//            for (StatisticData stat : statistics) {
//                Row row = sheet.createRow(rowNum++);
//                row.createCell(0).setCellValue(stat.getPeriod());
//                row.createCell(1).setCellValue(stat.getOrderCount());
//                row.createCell(2).setCellValue(stat.getFormattedRevenue());
//                row.createCell(3).setCellValue(stat.getFormattedAverage());
//                row.createCell(4).setCellValue(stat.getTopProduct());
//            }
//
//            // Export workbook
//            exportWorkbook(workbook, "Báo_cáo_thống_kê");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            showAlert("Lỗi", "Không thể xuất báo cáo: " + e.getMessage());
//        }
    }

    private void updateDashboardMetrics() {
        // Calculate total revenue
//        double totalRevenue = filteredOrders.stream()
//                .mapToDouble(Order::getTotal)
//                .sum();
//
//        // Calculate average order value
//        double averageOrder = filteredOrders.isEmpty() ? 0 : totalRevenue / filteredOrders.size();
//
//        // Get top selling product
//        String topProduct = productService.getTopSellingProduct(filteredOrders);
//
//        // Update labels
//        lblTotalRevenue.setText(currencyFormatter.format(totalRevenue));
//        lblTotalOrders.setText(String.valueOf(filteredOrders.size()));
//        lblAverageOrder.setText(currencyFormatter.format(averageOrder));
//        lblTopSelling.setText(topProduct != null ? topProduct : "Chưa có");
    }

    private void updateRevenueChart() {
//        chartRevenue.getData().clear();
//
//        XYChart.Series<String, Number> series = new XYChart.Series<>();
//        series.setName("Doanh thu");
//
//        // Group orders by date and calculate revenue
//        Map<String, Double> revenueByDate = new LinkedHashMap<>();
//
//        // Sort orders by date
//        filteredOrders.sort(Comparator.comparing(Order::getOrderDateTime));
//
//        // Group by date
//        for (Order order : filteredOrders) {
//            String date = order.getOrderDate();
//            revenueByDate.put(date, revenueByDate.getOrDefault(date, 0.0) + order.getTotal());
//        }
//
//        // Add data points
//        for (Map.Entry<String, Double> entry : revenueByDate.entrySet()) {
//            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
//        }
//
//        chartRevenue.getData().add(series);
    }

    private void updateProductDistributionChart() {
//        chartProductDistribution.getData().clear();
//
//        // Get product distribution data
//        Map<String, Integer> productDistribution = productService.getProductCategoryDistribution(filteredOrders);
//
//        // Add data points
//        for (Map.Entry<String, Integer> entry : productDistribution.entrySet()) {
//            PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
//            chartProductDistribution.getData().add(slice);
//        }
    }

    private void updateStatisticChart(String statisticType) {
//        chartStatistic.getData().clear();
//
//        XYChart.Series<String, Number> series = new XYChart.Series<>();
//        series.setName(statisticType);
//
//        // Add data points based on statistic type
//        for (StatisticData stat : statisticDataList) {
//            Number value;
//
//            switch (statisticType) {
//                case "Doanh thu":
//                    value = stat.getRevenue();
//                    break;
//                case "Số lượng đơn hàng":
//                    value = stat.getOrderCount();
//                    break;
//                default:
//                    value = stat.getRevenue(); // Default to revenue
//                    break;
//            }
//
//            series.getData().add(new XYChart.Data<>(stat.getPeriod(), value));
//        }
//
//        chartStatistic.getData().add(series);
    }

    private void createHeaderRow(Sheet sheet, String[] headers) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
    }

    private void exportWorkbook(Workbook workbook, String fileName) throws Exception {
        // Show file chooser dialog
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu báo cáo Excel");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
        );
        fileChooser.setInitialFileName(fileName + ".xlsx");

        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            // Write to file
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                workbook.write(outputStream);
            }

            showAlert("Thành công", "Xuất báo cáo thành công!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}