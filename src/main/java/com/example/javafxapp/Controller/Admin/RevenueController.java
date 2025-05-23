package com.example.javafxapp.Controller.Admin;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Model.Order;
import com.example.javafxapp.Model.OrderDetail;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Model.Statistic;
import com.example.javafxapp.Service.OrderDetailService;
import com.example.javafxapp.Service.OrderService;
import com.example.javafxapp.Service.ProductService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import org.apache.poi.ss.usermodel.Cell;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * Controller quản lý các chức năng liên quan đến doanh thu và thống kê cho Admin.
 * Bao gồm các tab: Tổng quan, Chi tiết đơn hàng và Thống kê.
 */
public class RevenueController {

    // Các thành phần FXML được inject từ file FXML tương ứng

    @FXML private Label lblCurrentDate; // Nhãn hiển thị ngày hiện tại

    // Tab Tổng quan (Overview)
    @FXML private ComboBox<String> cboTimeRange; // ComboBox chọn khoảng thời gian (Hôm nay, Tuần này, Tháng này, Tùy chỉnh)
    @FXML private DatePicker dateFrom; // DatePicker chọn ngày bắt đầu cho khoảng thời gian tùy chỉnh
    @FXML private DatePicker dateTo; // DatePicker chọn ngày kết thúc cho khoảng thời gian tùy chỉnh
    @FXML private Button btnApplyFilter; // Nút áp dụng bộ lọc thời gian
    @FXML private Label lblTotalRevenue; // Nhãn hiển thị tổng doanh thu
    @FXML private Label lblTotalOrders; // Nhãn hiển thị tổng số đơn hàng
    @FXML private Label lblAverageOrder; // Nhãn hiển thị giá trị trung bình mỗi đơn hàng
    @FXML private Label lblTopSelling; // Nhãn hiển thị sản phẩm bán chạy nhất
    @FXML private BarChart<String, Number> chartRevenue; // Biểu đồ cột hiển thị doanh thu theo thời gian
    @FXML private PieChart chartProductDistribution; // Biểu đồ tròn hiển thị tỷ lệ phân phối sản phẩm
    @FXML private Button btnExportOverview; // Nút xuất báo cáo tổng quan ra file CSV

    // Tab Chi tiết đơn hàng (Order Details)
    @FXML private DatePicker dateOrderFrom; // DatePicker chọn ngày bắt đầu để lọc đơn hàng
    @FXML private DatePicker dateOrderTo; // DatePicker chọn ngày kết thúc để lọc đơn hàng
    @FXML private Button btnSearchOrders; // Nút tìm kiếm đơn hàng theo khoảng ngày đã chọn
    @FXML private TableView<Order> tblOrders; // Bảng hiển thị danh sách chi tiết các đơn hàng
    @FXML private TableColumn<Order, String> colOrderId; // Cột Mã đơn hàng
    @FXML private TableColumn<Order, String> colOrderDate; // Cột Ngày đặt hàng
    @FXML private TableColumn<Order, String> colOrderTime; // Cột Giờ đặt hàng
    @FXML private TableColumn<Order, String> colItems; // Cột danh sách các sản phẩm trong đơn
    @FXML private TableColumn<Order, String> colQuantity; // Cột tổng số lượng sản phẩm trong đơn
    @FXML private TableColumn<Order, String> colTotal; // Cột Tổng tiền của đơn hàng
    @FXML private TableColumn<Order, String> colPaymentMethod; // Cột Phương thức thanh toán
    @FXML private Label lblOrderCount; // Nhãn hiển thị tổng số đơn hàng được tìm thấy
    @FXML private Button btnExportOrderDetail; // Nút xuất báo cáo chi tiết đơn hàng ra file CSV

    // Tab Thống kê (Statistics)
    @FXML private ComboBox<String> cboStatisticType; // ComboBox chọn loại thống kê (Doanh thu, Số lượng đơn)
    @FXML private ComboBox<String> cboStatisticPeriod; // ComboBox chọn chu kỳ thống kê (Hôm nay, Tuần này, Tháng này, Tùy chỉnh)
    @FXML private DatePicker dateStatisticFrom; // DatePicker chọn ngày bắt đầu cho thống kê tùy chỉnh
    @FXML private DatePicker dateStatisticTo; // DatePicker chọn ngày kết thúc cho thống kê tùy chỉnh
    @FXML private Button btnGenerateStatistic; // Nút tạo báo cáo thống kê
    @FXML private Label lblStatisticTitle; // Nhãn hiển thị tiêu đề của bảng và biểu đồ thống kê
    @FXML private TableView<Statistic> tblStatistic; // Bảng hiển thị dữ liệu thống kê
    @FXML private TableColumn<Statistic, String> colStatPeriod; // Cột Chu kỳ/Thời gian thống kê
    @FXML private TableColumn<Statistic, String> colStatOrderCount; // Cột Số lượng đơn hàng trong thống kê
    @FXML private TableColumn<Statistic, String> colStatRevenue; // Cột Doanh thu trong thống kê
    @FXML private TableColumn<Statistic, String> colStatAverage; // Cột Giá trị trung bình/đơn trong thống kê
    @FXML private TableColumn<Statistic, String> colStatTopProduct; // Cột Sản phẩm bán chạy nhất trong thống kê
    @FXML private BarChart<String, Number> chartStatistic; // Biểu đồ cột hiển thị dữ liệu thống kê
    @FXML private Button btnExportStatistic; // Nút xuất báo cáo thống kê ra file CSV

    // Khởi tạo các đối tượng Service để tương tác với tầng dữ liệu
    private OrderDetailService orderDetailService = new OrderDetailService();
    private ProductService productService = new ProductService();
    private OrderService orderService = new OrderService();

    /**
     * Phương thức này được tự động gọi sau khi các thành phần FXML đã được inject.
     * Dùng để khởi tạo các giá trị ban đầu, thiết lập listener và tải dữ liệu mặc định.
     */
    @FXML
    public void initialize() {
        // Hiển thị ngày hiện tại
        lblCurrentDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Thiết lập giá trị mặc định cho các DatePicker là ngày hiện tại
        dateFrom.setValue(LocalDate.now());
        dateTo.setValue(LocalDate.now());
        dateOrderFrom.setValue(LocalDate.now());
        dateOrderTo.setValue(LocalDate.now());
        dateStatisticFrom.setValue(LocalDate.now());
        dateStatisticTo.setValue(LocalDate.now());

        // Khởi tạo các mục lựa chọn cho ComboBoxes
        cboTimeRange.setItems(FXCollections.observableArrayList("Hôm nay", "Tuần này", "Tháng này", "Tùy chỉnh"));
        cboStatisticType.setItems(FXCollections.observableArrayList("Doanh thu", "Số lượng đơn"));
        cboStatisticPeriod.setItems(FXCollections.observableArrayList("Hôm nay", "Tuần này", "Tháng này", "Tùy chỉnh"));

        // Khởi tạo các cột cho TableView tblOrders (Chi tiết đơn hàng)
        colOrderId.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        colOrderDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderTime().toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        colOrderTime.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getOrderTime().toLocalDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))));

        // Cột "Sản phẩm": Lấy danh sách tên sản phẩm từ chi tiết đơn hàng
        colItems.setCellValueFactory(cellData -> {
            List<OrderDetail> orderDetails = orderDetailService.getAll(cellData.getValue().getId());
            List<String> products = new ArrayList<>();
            for (OrderDetail orderDetail : orderDetails) {
                Product product = productService.findProductByID(orderDetail.getProductId());
                products.add(product.getProduct_name());
            }
            String productString = String.join(", ", products); // Nối tên các sản phẩm bằng dấu phẩy
            return new SimpleStringProperty(productString);
        });

        // Cột "Số lượng": Tính tổng số lượng các mặt hàng trong đơn
        colQuantity.setCellValueFactory(cellData -> {
            List<OrderDetail> orderDetails = orderDetailService.getAll(cellData.getValue().getId());
            int quantity = 0;
            for (OrderDetail orderDetail : orderDetails) {
                quantity += orderDetail.getQuantity();
            }
            return new SimpleStringProperty(String.valueOf(quantity));
        });

        colTotal.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getTotalAmount())));
        colPaymentMethod.setCellValueFactory(cellData -> new SimpleStringProperty("Tiền Mặt")); // Giả định phương thức thanh toán là Tiền Mặt (TM)

        // Khởi tạo các cột cho TableView tblStatistic (Thống kê)
        colStatPeriod.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));
        colStatOrderCount.setCellValueFactory(cellData ->new SimpleStringProperty(cellData.getValue().getOrderCount()));
        colStatRevenue.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRevenue()));
        colStatAverage.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAverage()));
        colStatTopProduct.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTopProduct()));

        // Thêm listener cho ComboBox cboTimeRange (Tab Tổng quan)
        // Khi người dùng thay đổi lựa chọn, cập nhật DatePicker tương ứng
        cboTimeRange.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateDatePickers(newValue, dateFrom, dateTo);
        });

        // Thêm listener cho ComboBox cboStatisticPeriod (Tab Thống kê)
        // Khi người dùng thay đổi lựa chọn, cập nhật DatePicker tương ứng
        cboStatisticPeriod.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateDatePickers(newValue, dateStatisticFrom, dateStatisticTo);
        });

        // Tải dữ liệu ban đầu cho các tab
        loadOverviewData(); // Tải dữ liệu cho tab Tổng quan
        loadOrderData();    // Tải dữ liệu cho tab Chi tiết đơn hàng
        loadStatisticData();// Tải dữ liệu cho tab Thống kê
    }

    /**
     * Cập nhật giá trị và trạng thái (enable/disable) của các DatePicker
     * dựa trên khoảng thời gian được chọn từ ComboBox.
     * @param timeRange Khoảng thời gian được chọn (ví dụ: "Hôm nay", "Tuần này").
     * @param dateFrom DatePicker cho ngày bắt đầu.
     * @param dateTo DatePicker cho ngày kết thúc.
     */
    private void updateDatePickers(String timeRange, DatePicker dateFrom, DatePicker dateTo) {
        LocalDate today = LocalDate.now(); // Lấy ngày hiện tại
        switch (timeRange) {
            case "Hôm nay":
                dateFrom.setValue(today);
                dateTo.setValue(today);
                dateFrom.setDisable(true); // Vô hiệu hóa lựa chọn ngày
                dateTo.setDisable(true);
                break;
            case "Tuần này":
                LocalDate startOfWeek = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)); // Ngày đầu tuần (Thứ 2)
                dateFrom.setValue(startOfWeek);
                dateTo.setValue(today);
                dateFrom.setDisable(true);
                dateTo.setDisable(true);
                break;
            case "Tháng này":
                LocalDate startOfMonth = today.withDayOfMonth(1); // Ngày đầu tháng
                dateFrom.setValue(startOfMonth);
                dateTo.setValue(today);
                dateFrom.setDisable(true);
                dateTo.setDisable(true);
                break;
            case "Tùy chỉnh":
                dateFrom.setValue(null); // Cho phép người dùng chọn ngày
                dateTo.setValue(null);
                dateFrom.setDisable(false); // Kích hoạt lựa chọn ngày
                dateTo.setDisable(false);
                break;
            default: // Mặc định (hoặc nếu timeRange là null)
                dateFrom.setValue(null);
                dateTo.setValue(null);
                dateFrom.setDisable(false);
                dateTo.setDisable(false);
                break;
        }
    }

    /**
     * Xử lý sự kiện khi nhấn nút "Áp dụng" (btnApplyFilter) trên tab Tổng quan.
     * Tải lại dữ liệu tổng quan dựa trên khoảng thời gian đã chọn.
     * @param event Sự kiện ActionEvent.
     */
    @FXML
    private void handleApplyFilter(ActionEvent event) {
        // String timeRange = cboTimeRange.getValue(); // Giá trị này không được sử dụng trực tiếp ở đây, vì dateFrom/To đã được cập nhật bởi listener
        LocalDate fromDate = dateFrom.getValue();
        LocalDate toDate = dateTo.getValue();

        // Kiểm tra tính hợp lệ của khoảng ngày: ngày kết thúc không được trước ngày bắt đầu
        if (fromDate != null && toDate != null && toDate.isBefore(fromDate)) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu!");
            return;
        }
        loadOverviewData(); // Tải lại dữ liệu tổng quan với bộ lọc mới
    }

    /**
     * Xử lý sự kiện khi nhấn nút "Tìm kiếm" (btnSearchOrders) trên tab Chi tiết đơn hàng.
     * Hiển thị danh sách đơn hàng trong khoảng ngày đã chọn.
     * @param event Sự kiện ActionEvent.
     */
    @FXML
    private void handleSearchOrders(ActionEvent event) {
        LocalDate fromDate = dateOrderFrom.getValue();
        LocalDate toDate = dateOrderTo.getValue();

        // Kiểm tra tính hợp lệ của khoảng ngày
        if (fromDate != null && toDate != null && toDate.isBefore(fromDate)) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu!");
            return;
        }
        // Lấy danh sách đơn hàng từ service dựa trên khoảng ngày
        ObservableList<Order> orders = FXCollections.observableArrayList(
                orderService.getOrdersByDateRange(fromDate, toDate)
        );
        tblOrders.setItems(orders); // Cập nhật bảng hiển thị đơn hàng
        lblOrderCount.setText("Tổng số đơn: " + orders.size()); // Cập nhật số lượng đơn hàng
    }

    /**
     * Xử lý sự kiện khi nhấn nút "Tạo báo cáo" (btnGenerateStatistic) trên tab Thống kê.
     * Tạo và hiển thị dữ liệu thống kê (bảng và biểu đồ).
     * @param event Sự kiện ActionEvent.
     */
    @FXML
    private void handleGenerateStatistic(ActionEvent event) {
        String statType = cboStatisticType.getValue(); // Loại thống kê (Doanh thu/Số lượng đơn)
        String period = cboStatisticPeriod.getValue(); // Chu kỳ thống kê (Hôm nay, Tuần này,...)
        LocalDate fromDate = dateStatisticFrom.getValue();
        LocalDate toDate = dateStatisticTo.getValue();

        // Kiểm tra người dùng đã chọn đủ thông tin chưa
        if (statType == null || period == null) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng chọn đầy đủ thông tin để tạo báo cáo");
            return;
        }

        // Kiểm tra tính hợp lệ của khoảng ngày (nếu là "Tùy chỉnh")
        if (fromDate != null && toDate != null && toDate.isBefore(fromDate)) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỏi", "Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu!");
            return;
        }

        // Cập nhật tiêu đề cho phần thống kê
        lblStatisticTitle.setText("Thống kê " + statType.toLowerCase() + " " + period.toLowerCase());

        // Lấy dữ liệu thống kê
        ObservableList<Statistic> stats = FXCollections.observableArrayList(
                statisticList() // Gọi phương thức để tạo danh sách thống kê
        );
        tblStatistic.setItems(stats); // Cập nhật bảng thống kê

        // Cập nhật biểu đồ thống kê
        updateStatisticChart(statType, period, fromDate, toDate);
    }

    /**
     * Xử lý sự kiện xuất báo cáo tổng quan ra file CSV.
     * @param event Sự kiện ActionEvent.
     */
    @FXML
    private void handleExportOverview(ActionEvent event) {
        String fileName = "overview_report_" + UUID.randomUUID().toString() + ".xlsx";
        exportToExcel(fileName, getOverviewData());
    }

    /**
     * Xử lý sự kiện xuất báo cáo chi tiết đơn hàng ra file CSV.
     * @param event Sự kiện ActionEvent.
     */
    @FXML
    private void handleExportOrderDetail(ActionEvent event) {
        String fileName = "order_detail_report_" + UUID.randomUUID().toString() + ".xlsx";
        exportToExcel(fileName, getOrderDetailData());
    }

    /**
     * Xử lý sự kiện xuất báo cáo thống kê ra file CSV.
     * @param event Sự kiện ActionEvent.
     */
    @FXML
    private void handleExportStatistic(ActionEvent event) {
        String fileName = "statistic_report_" + UUID.randomUUID().toString() + ".xlsx";
        exportToExcel(fileName, getStatisticData());
    }

    /**
     * Tải và hiển thị dữ liệu cho tab Tổng quan.
     * Bao gồm tổng doanh thu, tổng đơn hàng, trung bình đơn, sản phẩm bán chạy nhất
     * và cập nhật các biểu đồ liên quan.
     */
    private void loadOverviewData() {
        // Lấy danh sách đơn hàng dựa trên khoảng ngày đã chọn trên tab Tổng quan (dateFrom, dateTo)
        List<Order> orders = orderService.getOrdersByDateRange(dateFrom.getValue(), dateTo.getValue());
        double totalMount = 0; // Tổng doanh thu
        String productBestSeller = "Không có"; // Tên sản phẩm bán chạy nhất
        int quantityProductBestSeller = 0; // Số lượng bán của sản phẩm bán chạy nhất
        int orderCount = 0; // Tổng số đơn hàng
        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>(); // Dùng để đếm số lượng bán của mỗi sản phẩm

        for (Order order : orders) {
            List<OrderDetail> orderDetails = orderDetailService.getAll(order.getId());
            for (OrderDetail orderDetail : orderDetails) {
                Product product = productService.findProductByID(orderDetail.getProductId());
                totalMount += product.getPrice() * orderDetail.getQuantity(); // Cộng dồn doanh thu

                // Cập nhật số lượng bán của sản phẩm
                if (hashMap.containsKey(orderDetail.getProductId())) {
                    int quantity = hashMap.get(orderDetail.getProductId());
                    hashMap.put(orderDetail.getProductId(), quantity + orderDetail.getQuantity());
                } else {
                    hashMap.put(orderDetail.getProductId(), orderDetail.getQuantity());
                }

                // Kiểm tra và cập nhật sản phẩm bán chạy nhất
                if (quantityProductBestSeller < hashMap.get(orderDetail.getProductId())) {
                    quantityProductBestSeller = hashMap.get(orderDetail.getProductId());
                    productBestSeller = product.getProduct_name();
                }
            }
            orderCount++; // Tăng số lượng đơn hàng
        }

        // Hiển thị các thông tin đã tính toán
        lblTotalRevenue.setText(String.valueOf(totalMount) + " VND");
        lblTotalOrders.setText(String.valueOf(orderCount));
        if (orderCount > 0) {
            lblAverageOrder.setText(String.valueOf(totalMount / orderCount) + " VND");
        } else {
            lblAverageOrder.setText("0 VND"); // Tránh chia cho 0
        }
        lblTopSelling.setText(productBestSeller);

        // Cập nhật các biểu đồ trên tab Tổng quan
        updateRevenueChart(dateFrom.getValue(), dateTo.getValue());
        updateProductDistributionChart(dateFrom.getValue(), dateTo.getValue());
    }

    /**
     * Tải và hiển thị dữ liệu cho tab Chi tiết đơn hàng.
     * Dữ liệu được lấy dựa trên khoảng ngày dateOrderFrom và dateOrderTo.
     */
    private void loadOrderData() {
        ObservableList<Order> orders = FXCollections.observableArrayList(
                orderService.getOrdersByDateRange(dateOrderFrom.getValue(), dateOrderTo.getValue())
        );
        tblOrders.setItems(orders);
        lblOrderCount.setText("Tổng số đơn: " + orders.size());
    }

    /**
     * Tạo danh sách các đối tượng Statistic dựa trên khoảng ngày đã chọn (dateStatisticFrom, dateStatisticTo).
     * Lưu ý: Logic hiện tại tạo một mục Statistic cho MỖI ĐƠN HÀNG trong ngày, thay vì một mục tổng hợp cho cả ngày.
     * Điều này có thể không phải là hành vi mong muốn cho thống kê theo ngày.
     * @return Danh sách các đối tượng Statistic.
     */
    private List<Statistic> statisticList() {
        List<Statistic> statisticList = new ArrayList<>();
        // Lặp qua từng ngày trong khoảng thời gian thống kê
        for (LocalDate date = dateStatisticFrom.getValue(); !date.isAfter(dateStatisticTo.getValue()); date = date.plusDays(1)) {
            List<Order> orders = orderService.getOrdersByDateRange(date, date); // Lấy đơn hàng cho ngày 'date'
            // HashMap này nên được khai báo ngoài vòng lặp orders nếu muốn tìm sản phẩm bán chạy nhất CỦA NGÀY
            // HashMap<Integer, Integer> dailyProductSales = new HashMap<>();

            for (Order order : orders) { // Lặp qua từng đơn hàng trong ngày
                // Các biến này đang được tính cho TỪNG ĐƠN HÀNG, không phải tổng hợp cho NGÀY
                HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>(); // Đếm sản phẩm cho đơn hàng hiện tại
                List<OrderDetail> orderDetails = orderDetailService.getAll(order.getId());
                double totalMountForThisOrder = 0; // Doanh thu của đơn hàng này
                String productBestSellerForThisOrder = "Không có"; // SP bán chạy trong đơn hàng này
                int quantityProductBestSellerForThisOrder = 0; // Số lượng SP bán chạy trong đơn hàng này
                // int orderCountForThisStatisticEntry = 0; // Biến này sẽ luôn là 1 nếu thống kê theo từng đơn

                for (OrderDetail orderDetail : orderDetails) {
                    Product product = productService.findProductByID(orderDetail.getProductId());
                    totalMountForThisOrder += product.getPrice() * orderDetail.getQuantity();

                    // Tính sản phẩm bán chạy nhất CHO ĐƠN HÀNG NÀY
                    if (hashMap.containsKey(orderDetail.getProductId())) {
                        int quantity = hashMap.get(orderDetail.getProductId());
                        hashMap.put(orderDetail.getProductId(), quantity + orderDetail.getQuantity());
                    } else {
                        hashMap.put(orderDetail.getProductId(), orderDetail.getQuantity());
                    }
                    if (quantityProductBestSellerForThisOrder < hashMap.get(orderDetail.getProductId())) {
                        quantityProductBestSellerForThisOrder = hashMap.get(orderDetail.getProductId());
                        productBestSellerForThisOrder = product.getProduct_name();
                    }
                }
                // orderCountForThisStatisticEntry++; // Sẽ là 1

                // Thêm một mục Statistic cho đơn hàng hiện tại.
                // Nếu muốn thống kê theo ngày, cần tổng hợp thông tin từ tất cả các 'order' trong ngày 'date'
                // và chỉ add một lần sau vòng lặp 'for (Order order : orders)'.
                // Hiện tại, cột "Số lượng đơn" sẽ là 1 cho mỗi dòng này.
                statisticList.add(new Statistic(
                        date.format(DateTimeFormatter.ofPattern("dd/MM")), // Ngày
                        "1", // Số lượng đơn (đang là 1 vì tính cho từng đơn)
                        String.valueOf(totalMountForThisOrder) + " VND", // Doanh thu của đơn này
                        String.valueOf(totalMountForThisOrder) + " VND", // Trung bình (doanh thu/1)
                        productBestSellerForThisOrder // Sản phẩm bán chạy của đơn này
                ));
            }
        }
        return statisticList;
    }


    /**
     * Tải và hiển thị dữ liệu ban đầu cho tab Thống kê.
     * Mặc định hiển thị thống kê doanh thu theo ngày.
     */
    private void loadStatisticData() {
        // Sử dụng dateStatisticFrom và dateStatisticTo mặc định (ngày hiện tại) khi gọi statisticList() lần đầu
        ObservableList<Statistic> stats = FXCollections.observableArrayList(statisticList());
        tblStatistic.setItems(stats);
        // Cập nhật biểu đồ thống kê với giá trị mặc định (Doanh thu, Theo ngày, và khoảng ngày hiện tại từ datepicker)
        updateStatisticChart("Doanh thu", cboStatisticPeriod.getValue() != null ? cboStatisticPeriod.getValue() : "Hôm nay", dateStatisticFrom.getValue(), dateStatisticTo.getValue());
    }

    /**
     * Cập nhật biểu đồ doanh thu (chartRevenue) trên tab Tổng quan.
     * Biểu đồ hiển thị doanh thu theo từng ngày trong khoảng thời gian đã chọn.
     * @param fromDate Ngày bắt đầu.
     * @param toDate Ngày kết thúc.
     */
    private void updateRevenueChart(LocalDate fromDate, LocalDate toDate) {
        chartRevenue.getData().clear(); // Xóa dữ liệu cũ
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu"); // Tên của dãy dữ liệu

        if (fromDate != null && toDate != null) {
            // Lặp qua từng ngày trong khoảng thời gian
            for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
                double totalMountPerDay = 0; // Tổng doanh thu của ngày hiện tại
                List<Order> orders = orderService.getOrdersByDateRange(date, date); // Lấy đơn hàng trong ngày
                for (Order order : orders) {
                    List<OrderDetail> orderDetails = orderDetailService.getAll(order.getId());
                    for (OrderDetail orderDetail : orderDetails) {
                        Product product = productService.findProductByID(orderDetail.getProductId());
                        totalMountPerDay += product.getPrice() * orderDetail.getQuantity();
                    }
                }
                // Thêm dữ liệu (ngày, doanh thu ngày đó) vào series
                series.getData().add(new XYChart.Data<>(date.format(DateTimeFormatter.ofPattern("dd/MM")), totalMountPerDay));
            }
        }
        chartRevenue.getData().add(series); // Thêm series vào biểu đồ
    }

    /**
     * Cập nhật biểu đồ phân phối sản phẩm (chartProductDistribution) trên tab Tổng quan.
     * Biểu đồ tròn hiển thị tỷ lệ số lượng bán của các sản phẩm.
     * @param fromDate Ngày bắt đầu.
     * @param toDate Ngày kết thúc.
     */
    private void updateProductDistributionChart(LocalDate fromDate, LocalDate toDate) {
        chartProductDistribution.getData().clear(); // Xóa dữ liệu cũ
        HashMap<Integer, Integer> productSalesCount = new HashMap<>(); // Đếm số lượng bán của mỗi sản phẩm
        List<Order> orders = orderService.getOrdersByDateRange(fromDate, toDate);

        for (Order order : orders) {
            for (OrderDetail orderDetail : orderDetailService.getAll(order.getId())) {
                // Tăng số lượng bán cho sản phẩm tương ứng
                productSalesCount.put(orderDetail.getProductId(),
                        productSalesCount.getOrDefault(orderDetail.getProductId(), 0) + orderDetail.getQuantity());
            }
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<Integer, Integer> entry : productSalesCount.entrySet()) {
            // Thêm dữ liệu (tên sản phẩm, số lượng bán) vào danh sách cho biểu đồ tròn
            pieChartData.add(new PieChart.Data(productService.findProductByID(entry.getKey()).getProduct_name(), entry.getValue()));
        }
        chartProductDistribution.setData(pieChartData); // Cập nhật dữ liệu cho biểu đồ
    }

    /**
     * Cập nhật biểu đồ thống kê (chartStatistic) trên tab Thống kê.
     * Biểu đồ hiển thị doanh thu hoặc số lượng đơn hàng theo từng ngày.
     * @param statType Loại thống kê ("Doanh thu" hoặc "Số lượng đơn").
     * @param period Chu kỳ (không dùng trực tiếp trong logic vẽ biểu đồ này, nhưng có thể dùng để định dạng trục X nếu phức tạp hơn).
     * @param fromDate Ngày bắt đầu.
     * @param toDate Ngày kết thúc.
     */
    private void updateStatisticChart(String statType, String period, LocalDate fromDate, LocalDate toDate) {
        chartStatistic.getData().clear(); // Xóa dữ liệu cũ
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(statType); // Tên series là loại thống kê

        int maxY = 0; // Dùng để xác định giới hạn trên của trục Y cho "Số lượng đơn"

        if (fromDate != null && toDate != null) {
            // Lặp qua từng ngày trong khoảng thời gian
            for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
                double totalAmountPerDay = 0; // Tổng doanh thu của ngày
                int orderCountPerDay = 0; // Tổng số đơn của ngày

                List<Order> orders = orderService.getOrdersByDateRange(date, date);
                for (Order order : orders) {
                    orderCountPerDay++; // Tăng số lượng đơn
                    List<OrderDetail> orderDetails = orderDetailService.getAll(order.getId());
                    for (OrderDetail orderDetail : orderDetails) {
                        Product product = productService.findProductByID(orderDetail.getProductId());
                        totalAmountPerDay += product.getPrice() * orderDetail.getQuantity(); // Tính tổng doanh thu
                    }
                }

                String xValue = date.format(DateTimeFormatter.ofPattern("dd/MM")); // Giá trị cho trục X (ngày)

                if ("Doanh thu".equalsIgnoreCase(statType)) {
                    series.getData().add(new XYChart.Data<>(xValue, totalAmountPerDay));
                } else { // "Số lượng đơn"
                    series.getData().add(new XYChart.Data<>(xValue, orderCountPerDay));
                    maxY = Math.max(maxY, orderCountPerDay); // Tìm số lượng đơn lớn nhất để điều chỉnh trục Y
                }
            }
        }

        chartStatistic.getData().add(series); // Thêm series vào biểu đồ

        // Điều chỉnh trục Y (NumberAxis) tùy theo loại thống kê
        if (!"Doanh thu".equalsIgnoreCase(statType)) { // Nếu là "Số lượng đơn"
            if (chartStatistic.getYAxis() instanceof NumberAxis yAxis) {
                yAxis.setAutoRanging(false); // Tắt tự động điều chỉnh khoảng
                yAxis.setLowerBound(0); // Giới hạn dưới là 0
                yAxis.setTickUnit(1); // Đơn vị mỗi vạch chia là 1
                yAxis.setUpperBound(maxY + 1); // Giới hạn trên (lớn hơn maxY một chút)

                // Định dạng nhãn trên trục Y để hiển thị số nguyên
                yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
                    @Override
                    public String toString(Number object) {
                        return String.format("%d", object.intValue());
                    }
                });
                yAxis.setLabel("Số lượng đơn"); // Đặt tên cho trục Y
            }
        } else { // Nếu là "Doanh thu"
            if (chartStatistic.getYAxis() instanceof NumberAxis yAxis) {
                yAxis.setAutoRanging(true); // Bật lại tự động điều chỉnh khoảng
                yAxis.setTickLabelFormatter(null); // Xóa formatter cũ (nếu có)
                yAxis.setLabel("VNĐ"); // Đặt tên cho trục Y
            }
        }
    }


    /**
     * Chuẩn bị dữ liệu tổng quan dưới dạng danh sách các chuỗi để xuất ra file CSV.
     * @return Danh sách các chuỗi, mỗi chuỗi là một dòng trong file CSV.
     */
    private List<String> getOverviewData() {
        List<String> data = new ArrayList<>();
        data.add("Tổng doanh thu,Số lượng đơn,Trung bình mỗi đơn,Top bán chạy"); // Dòng tiêu đề
        data.add(String.format("%s,%s,%s,%s",
                lblTotalRevenue.getText().replace(" VND", ""), // Loại bỏ " VND" để dễ xử lý hơn nếu đọc lại
                lblTotalOrders.getText(),
                lblAverageOrder.getText().replace(" VND", ""),
                lblTopSelling.getText()));
        return data;
    }

    /**
     * Chuẩn bị dữ liệu chi tiết đơn hàng từ bảng tblOrders dưới dạng danh sách các chuỗi để xuất ra file CSV.
     * @return Danh sách các chuỗi, mỗi chuỗi là một dòng trong file CSV.
     */
    private List<String> getOrderDetailData() {
        List<String> data = new ArrayList<>();
        data.add("Mã đơn,Ngày đặt,Giờ đặt,Sản phẩm,Số lượng,Tổng tiền,Phương thức TT"); // Dòng tiêu đề
        System.out.println("Số lượng đơn hàng trong bảng để xuất: " + tblOrders.getItems().size()); // Ghi log kiểm tra
        for (Order order : tblOrders.getItems()) {
            List<OrderDetail> orderDetails = orderDetailService.getAll(order.getId());
            List<String> products = new ArrayList<>();
            int quantity = 0;
            for (OrderDetail orderDetail : orderDetails) {
                Product product = productService.findProductByID(orderDetail.getProductId());
                products.add(product.getProduct_name().replace(",", ";")); // Thay dấu phẩy trong tên SP để tránh lỗi CSV
                quantity += orderDetail.getQuantity();
            }
            String productString = String.join("; ", products); // Dùng dấu chấm phẩy để ngăn cách sản phẩm

            data.add(String.format("%s,%s,%s,\"%s\",%s,%s,%s", // Đặt tên sản phẩm trong dấu nháy kép nếu có thể chứa dấu phẩy
                    order.getId(),
                    order.getOrderTime().toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    order.getOrderTime().toLocalDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    productString,
                    String.valueOf(quantity),
                    String.valueOf(order.getTotalAmount()), // Đảm bảo là chuỗi
                    "Tiền mặt")); // Phương thức thanh toán
        }
        return data;
    }

    /**
     * Chuẩn bị dữ liệu thống kê từ bảng tblStatistic dưới dạng danh sách các chuỗi để xuất ra file CSV.
     * @return Danh sách các chuỗi, mỗi chuỗi là một dòng trong file CSV.
     */
    private List<String> getStatisticData() {
        List<String> data = new ArrayList<>();
        data.add("Thời gian,Số đơn hàng,Doanh thu,Trung bình/đơn,Sản phẩm bán chạy"); // Dòng tiêu đề
        for (Statistic stat : tblStatistic.getItems()) {
            data.add(String.format("%s,%s,%s,%s,\"%s\"", // Đặt sản phẩm bán chạy trong dấu nháy kép
                    stat.getPeriod(),
                    stat.getOrderCount().replace(" VND", ""),
                    stat.getRevenue().replace(" VND", ""),
                    stat.getAverage().replace(" VND", ""),
                    stat.getTopProduct().replace(",",";"))); // Thay dấu phẩy trong tên SP
        }
        return data;
    }

    private void exportToExcel(String fileName, List<String> data) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Báo cáo");

        // Tạo style căn giữa
        CellStyle centerStyle = workbook.createCellStyle();
        centerStyle.setAlignment(HorizontalAlignment.CENTER);

       // Tạo style cho dòng tiêu đề (căn giữa + in đậm)
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i);
            String[] cells = data.get(i).split(","); // Tách dữ liệu theo dấu phẩy
            for (int j = 0; j < cells.length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(cells[j]);
                // Áp dụng style: nếu là dòng đầu thì dùng style tiêu đề
                if (i == 0) {
                    cell.setCellStyle(headerStyle);
                } else {
                    cell.setCellStyle(centerStyle);
                }
            }
        }

        // Auto resize cột
        if (!data.isEmpty()) {
            int columnCount = data.get(0).split(",").length;
            for (int i = 0; i < columnCount; i++) {
                sheet.autoSizeColumn(i);
            }
        }

        // Tạo thư mục nếu chưa tồn tại
        String path = "C:\\Users\\Quoc Thang\\OneDrive\\Documents\\Export_CoffeeManagement";
        File dir = new File(path);
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, fileName);

        try (FileOutputStream out = new FileOutputStream(file)) {
            workbook.write(out);
            workbook.close();

            // Thông báo thành công
            AlertInfo.showAlert(Alert.AlertType.INFORMATION,"Thành công" , "Xuất Excel thành công");

            // Mở file
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (IOException e) {
            AlertInfo.showAlert(Alert.AlertType.ERROR,"Lỗi" , "Không xuất Excel được");
        }
    }


}