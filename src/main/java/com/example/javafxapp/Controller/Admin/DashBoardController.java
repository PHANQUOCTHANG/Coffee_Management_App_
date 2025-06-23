package com.example.javafxapp.Controller.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {

    @FXML private Label dateTimeLabel;
    @FXML private Label userNameLabel;
    @FXML private ComboBox<String> roleComboBox;

    // Dynamic sections
    @FXML private GridPane statsGrid;
    @FXML private HBox chartsSection;
    @FXML private VBox tablesSection;
    @FXML private VBox actionsSection;
    @FXML private HBox quickActionsBox;
    @FXML private Label actionsTitle;

    // Admin only section
    @FXML private VBox adminOnlySection;
    @FXML private Button userManagementButton;
    @FXML private Button systemSettingsButton;
    @FXML private Button reportsButton;
    @FXML private Button backupButton;

    // Performance section
    @FXML private VBox performanceSection;
    @FXML private Label performanceTitle;
    @FXML private Button refreshPerformanceButton;
    @FXML private TableView<Performance> performanceTable;
    @FXML private TableColumn<Performance, String> employeeColumn;
    @FXML private TableColumn<Performance, Integer> tasksCompletedColumn;
    @FXML private TableColumn<Performance, String> salesColumn;
    @FXML private TableColumn<Performance, String> ratingColumn;
    @FXML private TableColumn<Performance, String> statusEmployeeColumn;

    // Current role
    private String currentRole = "Admin";

    // Charts (will be created dynamically)
    private LineChart<String, Number> revenueChart;
    private PieChart categoryChart;

    // Tables (will be created dynamically)
    private TableView<Activity> activitiesTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeDateTime();
        initializeRoleComboBox();
        initializePerformanceTable();
        setupUIForRole(currentRole);
    }

    private void initializeDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy - HH:mm");
        String currentDateTime = LocalDateTime.now().format(formatter);
        dateTimeLabel.setText(currentDateTime);
    }

    private void initializeRoleComboBox() {
        roleComboBox.setItems(FXCollections.observableArrayList("Admin", "Nhân Viên"));
        roleComboBox.setValue(currentRole);
    }

    private void initializePerformanceTable() {
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        tasksCompletedColumn.setCellValueFactory(new PropertyValueFactory<>("tasksCompleted"));
        salesColumn.setCellValueFactory(new PropertyValueFactory<>("sales"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        statusEmployeeColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    @FXML
    private void handleRoleChange(ActionEvent event) {
        currentRole = roleComboBox.getValue();
        setupUIForRole(currentRole);
    }

    private void setupUIForRole(String role) {
        // Clear existing content
        statsGrid.getChildren().clear();
        chartsSection.getChildren().clear();
        tablesSection.getChildren().clear();
        quickActionsBox.getChildren().clear();

        // Update user name
        userNameLabel.setText("Xin chào, " + role);

        if ("Admin".equals(role)) {
            setupAdminUI();
        } else {
            setupEmployeeUI();
        }

        loadPerformanceData(role);
    }

    private void setupAdminUI() {
        adminOnlySection.setVisible(true);
        performanceTitle.setText("Hiệu Suất Tất Cả Nhân Viên");

        createAdminStatsCards();
        createAdminCharts();
        createAdminTables();
        createAdminActions();
    }

    private void setupEmployeeUI() {
        adminOnlySection.setVisible(false);
        performanceTitle.setText("Hiệu Suất Của Tôi");

        createEmployeeStatsCards();
        createEmployeeCharts();
        createEmployeeTables();
        createEmployeeActions();
    }

    private void createAdminStatsCards() {
        // Admin sees all business metrics
        VBox revenueCard = createStatsCard("Tổng Doanh Thu", "2,450,000 VND", "↑ 12% so với tháng trước", "#3498DB");
        VBox ordersCard = createStatsCard("Tổng Đơn Hàng", "1,256", "↑ 8% so với tuần trước", "#E74C3C");
        VBox customersCard = createStatsCard("Khách Hàng Mới", "124", "↑ 15% so với tháng trước", "#27AE60");
        VBox employeesCard = createStatsCard("Nhân Viên Hoạt Động", "15", "2 nhân viên mới tháng này", "#F39C12");

        statsGrid.add(revenueCard, 0, 0);
        statsGrid.add(ordersCard, 1, 0);
        statsGrid.add(customersCard, 2, 0);
        statsGrid.add(employeesCard, 3, 0);
    }

    private void createEmployeeStatsCards() {
        // Employee sees personal metrics
        VBox tasksCard = createStatsCard("Nhiệm Vụ Hoàn Thành", "23", "↑ 5 so với tuần trước", "#3498DB");
        VBox salesCard = createStatsCard("Doanh Số Cá Nhân", "145,000 VND", "↑ 20% so với tuần trước", "#E74C3C");
        VBox customersCard = createStatsCard("Khách Hàng Phụ Trách", "42", "↑ 3 khách hàng mới", "#27AE60");
        VBox ratingCard = createStatsCard("Đánh Giá", "4.7/5", "Xuất sắc", "#F39C12");

        statsGrid.add(tasksCard, 0, 0);
        statsGrid.add(salesCard, 1, 0);
        statsGrid.add(customersCard, 2, 0);
        statsGrid.add(ratingCard, 3, 0);
    }

    private VBox createStatsCard(String title, String value, String description, String color) {
        VBox card = new VBox();
        card.setAlignment(javafx.geometry.Pos.CENTER);
        card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        card.setPadding(new Insets(20));

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 24px;");

        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-text-fill: #D5DBDB; -fx-font-size: 12px;");

        card.getChildren().addAll(titleLabel, valueLabel, descLabel);
        return card;
    }

    private void createAdminCharts() {
        // Admin comprehensive charts
        VBox chartContainer1 = new VBox();
        chartContainer1.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        HBox.setHgrow(chartContainer1, javafx.scene.layout.Priority.ALWAYS);

        Label chartTitle1 = new Label("Doanh Thu Theo Tháng");
        chartTitle1.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        chartTitle1.setPadding(new Insets(20, 20, 10, 20));

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        revenueChart = new LineChart<>(xAxis, yAxis);
        revenueChart.setPrefHeight(300);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh Thu (Triệu VND)");
        series.getData().addAll(
                new XYChart.Data<>("T1", 1.8),
                new XYChart.Data<>("T2", 2.1),
                new XYChart.Data<>("T3", 1.9),
                new XYChart.Data<>("T4", 2.3),
                new XYChart.Data<>("T5", 2.0),
                new XYChart.Data<>("T6", 2.4)
        );
        revenueChart.getData().add(series);

        chartContainer1.getChildren().addAll(chartTitle1, revenueChart);

        // Pie chart for admin
        VBox chartContainer2 = new VBox();
        chartContainer2.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label chartTitle2 = new Label("Phân Loại Sản Phẩm");
        chartTitle2.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        chartTitle2.setPadding(new Insets(20, 20, 10, 20));

        categoryChart = new PieChart();
        categoryChart.setPrefHeight(300);
        categoryChart.setPrefWidth(400);
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Điện thoại", 35),
                new PieChart.Data("Laptop", 25),
                new PieChart.Data("Phụ kiện", 20),
                new PieChart.Data("Máy tính bảng", 15),
                new PieChart.Data("Khác", 5)
        );
        categoryChart.setData(pieData);

        chartContainer2.getChildren().addAll(chartTitle2, categoryChart);

        chartsSection.getChildren().addAll(chartContainer1, chartContainer2);
    }

    private void createEmployeeCharts() {
        // Employee personal performance chart
        VBox chartContainer = new VBox();
        chartContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label chartTitle = new Label("Hiệu Suất Cá Nhân Theo Tuần");
        chartTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        chartTitle.setPadding(new Insets(20, 20, 10, 20));

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> personalChart = new LineChart<>(xAxis, yAxis);
        personalChart.setPrefHeight(300);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Số nhiệm vụ hoàn thành");
        series.getData().addAll(
                new XYChart.Data<>("Tuần 1", 18),
                new XYChart.Data<>("Tuần 2", 22),
                new XYChart.Data<>("Tuần 3", 19),
                new XYChart.Data<>("Tuần 4", 25),
                new XYChart.Data<>("Tuần 5", 23)
        );
        personalChart.getData().add(series);

        chartContainer.getChildren().addAll(chartTitle, personalChart);
        chartsSection.getChildren().add(chartContainer);
    }

    private void createAdminTables() {
        // Admin sees all activities
        createActivitiesTable("Hoạt Động Gần Đây - Tất Cả Nhân Viên", getAdminActivities());
    }

    private void createEmployeeTables() {
        // Employee sees only their activities
        createActivitiesTable("Hoạt Động Gần Đây - Của Tôi", getEmployeeActivities());
    }

    private void createActivitiesTable(String title, ObservableList<Activity> data) {
        VBox tableContainer = new VBox();
        tableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        HBox headerBox = new HBox();
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        headerBox.setSpacing(10);
        headerBox.setPadding(new Insets(20, 20, 10, 20));

        Label tableTitle = new Label(title);
        tableTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Button refreshButton = new Button("Làm mới");
        refreshButton.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-background-radius: 5;");
        refreshButton.setOnAction(e -> refreshActivities());

        headerBox.getChildren().addAll(tableTitle, refreshButton);

        activitiesTable = new TableView<>();
        activitiesTable.setPrefHeight(200);

        TableColumn<Activity, String> timeCol = new TableColumn<>("Thời Gian");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        timeCol.setPrefWidth(120);

        TableColumn<Activity, String> actionCol = new TableColumn<>("Hành Động");
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionCol.setPrefWidth(250);

        TableColumn<Activity, String> userCol = new TableColumn<>("Người Thực Hiện");
        userCol.setCellValueFactory(new PropertyValueFactory<>("user"));
        userCol.setPrefWidth(150);

        TableColumn<Activity, String> statusCol = new TableColumn<>("Trạng Thái");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(120);

        activitiesTable.getColumns().addAll(timeCol, actionCol, userCol, statusCol);
        activitiesTable.setItems(data);

        tableContainer.getChildren().addAll(headerBox, activitiesTable);
        tablesSection.getChildren().add(tableContainer);
    }

    private void createAdminActions() {
        Button addOrderBtn = createActionButton("Thêm Đơn Hàng", "#27AE60", e -> handleAddOrder());
        Button viewReportsBtn = createActionButton("Xem Báo Cáo", "#E74C3C", e -> handleViewReports());
        Button manageCustomersBtn = createActionButton("Quản Lý Khách Hàng", "#9B59B6", e -> handleManageCustomers());
        Button manageInventoryBtn = createActionButton("Quản Lý Kho", "#F39C12", e -> handleManageInventory());
        Button settingsBtn = createActionButton("Cài Đặt", "#34495E", e -> handleSettings());

        quickActionsBox.getChildren().addAll(addOrderBtn, viewReportsBtn, manageCustomersBtn, manageInventoryBtn, settingsBtn);
    }

    private void createEmployeeActions() {
        Button addOrderBtn = createActionButton("Thêm Đơn Hàng", "#27AE60", e -> handleAddOrder());
        Button viewMyTasksBtn = createActionButton("Nhiệm Vụ Của Tôi", "#E74C3C", e -> handleViewMyTasks());
        Button customerServiceBtn = createActionButton("Chăm Sóc Khách Hàng", "#9B59B6", e -> handleCustomerService());
        Button updateProfileBtn = createActionButton("Cập Nhật Hồ Sơ", "#34495E", e -> handleUpdateProfile());

        quickActionsBox.getChildren().addAll(addOrderBtn, viewMyTasksBtn, customerServiceBtn, updateProfileBtn);
    }

    private Button createActionButton(String text, String color, javafx.event.EventHandler<ActionEvent> handler) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 10;");
        button.setOnAction(handler);
        return button;
    }

    private ObservableList<Activity> getAdminActivities() {
        return FXCollections.observableArrayList(
                new Activity("10:30", "Đơn hàng mới #1234", "Nguyễn Văn A (NV)", "Hoàn tất"),
                new Activity("10:15", "Thanh toán đơn #1233", "Trần Thị B (NV)", "Thành công"),
                new Activity("09:45", "Khách hàng mới đăng ký", "Lê Văn C (NV)", "Đã xác nhận"),
                new Activity("09:30", "Cập nhật kho hàng", "Phạm Thị D (NV)", "Hoàn tất"),
                new Activity("09:15", "Backup dữ liệu", "System Admin", "Thành công")
        );
    }

    private ObservableList<Activity> getEmployeeActivities() {
        return FXCollections.observableArrayList(
                new Activity("10:30", "Xử lý đơn hàng #1234", "Tôi", "Hoàn tất"),
                new Activity("10:15", "Gọi điện tư vấn khách hàng", "Tôi", "Thành công"),
                new Activity("09:45", "Cập nhật thông tin sản phẩm", "Tôi", "Hoàn tất"),
                new Activity("09:30", "Phản hồi email khách hàng", "Tôi", "Đã gửi"),
                new Activity("09:15", "Kiểm tra hàng tồn kho", "Tôi", "Hoàn tất")
        );
    }

    private void loadPerformanceData(String role) {
        ObservableList<Performance> data;
        if ("Admin".equals(role)) {
            data = FXCollections.observableArrayList(
                    new Performance("Nguyễn Văn A", 23, "145,000 VND", "4.7/5", "Hoạt động"),
                    new Performance("Trần Thị B", 19, "132,000 VND", "4.5/5", "Hoạt động"),
                    new Performance("Lê Văn C", 21, "158,000 VND", "4.8/5", "Hoạt động"),
                    new Performance("Phạm Thị D", 17, "98,000 VND", "4.2/5", "Nghỉ phép"),
                    new Performance("Hoàng Văn E", 25, "175,000 VND", "4.9/5", "Hoạt động")
            );
        } else {
            // Employee only sees their own performance
            data = FXCollections.observableArrayList(
                    new Performance("Tôi", 23, "145,000 VND", "4.7/5", "Hoạt động")
            );
        }
        performanceTable.setItems(data);
    }

    // Event Handlers
    private void refreshActivities() {
        loadPerformanceData(currentRole);
        initializeDateTime();
        System.out.println("Dữ liệu đã được làm mới!");
    }

    @FXML
    private void handleRefreshPerformance(ActionEvent event) {
        loadPerformanceData(currentRole);
        System.out.println("Dữ liệu hiệu suất đã được làm mới!");
    }

    // Admin Event Handlers
    @FXML
    private void handleUserManagement(ActionEvent event) {
        System.out.println("Chức năng quản lý người dùng được gọi!");
        // Add logic to open user management window
    }

    @FXML
    private void handleSystemSettings(ActionEvent event) {
        System.out.println("Chức năng cài đặt hệ thống được gọi!");
        // Add logic to open system settings
    }

    @FXML
    private void handleSystemReports(ActionEvent event) {
        System.out.println("Chức năng báo cáo tổng quan được gọi!");
        // Add logic to generate system reports
    }

    @FXML
    private void handleBackup(ActionEvent event) {
        System.out.println("Chức năng sao lưu dữ liệu được gọi!");
        // Add logic to backup data
    }

    // Common Event Handlers
    private void handleAddOrder() {
        System.out.println("Chức năng thêm đơn hàng được gọi!");
        // Add logic to create new order
    }

    private void handleViewReports() {
        System.out.println("Chức năng xem báo cáo được gọi!");
        // Add logic to view reports
    }

    private void handleManageCustomers() {
        System.out.println("Chức năng quản lý khách hàng được gọi!");
        // Add logic to manage customers
    }

    private void handleManageInventory() {
        System.out.println("Chức năng quản lý kho được gọi!");
        // Add logic to manage inventory
    }

    private void handleSettings() {
        System.out.println("Chức năng cài đặt được gọi!");
        // Add logic to open settings
    }

    // Employee Event Handlers
    private void handleViewMyTasks() {
        System.out.println("Chức năng xem nhiệm vụ cá nhân được gọi!");
        // Add logic to view personal tasks
    }

    private void handleCustomerService() {
        System.out.println("Chức năng chăm sóc khách hàng được gọi!");
        // Add logic for customer service
    }

    private void handleUpdateProfile() {
        System.out.println("Chức năng cập nhật hồ sơ được gọi!");
        // Add logic to update employee profile
    }

    // Data Classes
    public static class Activity {
        private String time;
        private String action;
        private String user;
        private String status;

        public Activity(String time, String action, String user, String status) {
            this.time = time;
            this.action = action;
            this.user = user;
            this.status = status;
        }

        // Getters
        public String getTime() { return time; }
        public String getAction() { return action; }
        public String getUser() { return user; }
        public String getStatus() { return status; }

        // Setters
        public void setTime(String time) { this.time = time; }
        public void setAction(String action) { this.action = action; }
        public void setUser(String user) { this.user = user; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class Performance {
        private String employeeName;
        private Integer tasksCompleted;
        private String sales;
        private String rating;
        private String status;

        public Performance(String employeeName, Integer tasksCompleted, String sales, String rating, String status) {
            this.employeeName = employeeName;
            this.tasksCompleted = tasksCompleted;
            this.sales = sales;
            this.rating = rating;
            this.status = status;
        }

        // Getters
        public String getEmployeeName() { return employeeName; }
        public Integer getTasksCompleted() { return tasksCompleted; }
        public String getSales() { return sales; }
        public String getRating() { return rating; }
        public String getStatus() { return status; }

        // Setters
        public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
        public void setTasksCompleted(Integer tasksCompleted) { this.tasksCompleted = tasksCompleted; }
        public void setSales(String sales) { this.sales = sales; }
        public void setRating(String rating) { this.rating = rating; }
        public void setStatus(String status) { this.status = status; }
    }
}