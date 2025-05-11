package com.example.javafxapp.Controller.Admin.Order;

import com.example.javafxapp.Controller.Admin.BaseController;
import com.example.javafxapp.Controller.Admin.MainScreenController;
import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.TextNormalizer;
import com.example.javafxapp.Model.Order;
import com.example.javafxapp.Model.OrderDetail;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.OrderService;
import com.example.javafxapp.Utils.LogUtils;
import com.jfoenix.controls.JFXButton;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.util.List;

public class OrderController extends BaseController implements OrderItemView.OrderItemViewListener {
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private Label firstPage;
    @FXML
    private Label lastPage;
    @FXML
    private JFXButton nextBtn;
    @FXML
    private JFXButton prevBtn;
    @FXML
    private GridPane grid;
    @FXML
    private ScrollPane scroll;
    @FXML
    private TextField searchField;
    @FXML
    private Label totalLabel;

    private final OrderService orderService = new OrderService();
    private ObservableList<Order> orders = FXCollections.observableArrayList();
    private ObservableList<OrderDetail> orderDetailList = FXCollections.observableArrayList();
    private Order currentOrder;
    private int currentPage = 0;
    private final int ordersPerPage = 10;

    @Override
    protected void initializeComponents() {
        setupStatusComboBox();
        setupSearchField();
    }

    @Override
    protected void setupEventHandlers() {
        nextBtn.setOnAction(e -> nextPage());
        prevBtn.setOnAction(e -> prevPage());
        statusComboBox.setOnAction(e -> filterByStatus());
    }

    @Override
    protected void loadData() {
        try {
            orders.setAll(orderService.getAllOrder());
            loadPage(currentPage);
            updatePagination();
            LogUtils.logInfo("Loaded " + orders.size() + " orders");
        } catch (Exception e) {
            LogUtils.logError("Error loading orders", e);
            showError("Lỗi tải dữ liệu", "Không thể tải danh sách đơn hàng. Vui lòng thử lại sau.");
        }
    }

    private void setupStatusComboBox() {
        List<String> statusList = List.of(
                "Đang chờ xử lí",
                "Đang xử lí",
                "Đã xử lí",
                "Đã huỷ",
                "Tất cả");
        statusComboBox.getItems().addAll(statusList);
        statusComboBox.setValue("Tất cả");
    }

    private void setupSearchField() {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.75));
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            pause.stop();
            pause.setOnFinished(e -> performSearch(newVal));
            pause.playFromStart();
        });
    }

    private void performSearch(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            loadData();
            return;
        }

        try {
            String keyword = TextNormalizer.normalize(searchText.trim());
            List<Order> filtered = orders.stream()
                    .filter(order -> TextNormalizer.normalize(order.getStaffName()).contains(keyword))
                    .toList();

            orders.setAll(filtered);
            currentPage = 0;
            loadPage(currentPage);
            LogUtils.logInfo("Searched orders with keyword: " + keyword);
        } catch (Exception e) {
            LogUtils.logError("Error searching orders", e);
            showError("Lỗi tìm kiếm", "Không thể tìm kiếm đơn hàng. Vui lòng thử lại sau.");
        }
    }

    private void loadPage(int pageIdx) {
        try {
            grid.getChildren().clear();
            int start = pageIdx * ordersPerPage;
            firstPage.setText(String.valueOf(currentPage + 1));

            for (int i = start; i < Math.min(start + ordersPerPage, orders.size()); i++) {
                Order order = orders.get(i);
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(
                            "/com/example/javafxapp/View/Orders/orderItem.fxml"));
                    HBox hbox = loader.load();

                    OrderItemView itemView = loader.getController();
                    itemView.setOrder(order);
                    itemView.setListener(this);

                    grid.add(hbox, 0, i - start);
                } catch (Exception e) {
                    LogUtils.logError("Error loading order item: " + order.getId(), e);
                }
            }

            updatePagination();
        } catch (Exception e) {
            LogUtils.logError("Error loading page " + pageIdx, e);
            showError("Lỗi tải trang", "Không thể tải trang " + (pageIdx + 1) + ". Vui lòng thử lại sau.");
        }
    }

    private void updatePagination() {
        prevBtn.setDisable(currentPage == 0);
        nextBtn.setDisable((currentPage + 1) * ordersPerPage >= orders.size());
        lastPage.setText(String.valueOf(orders.size() / ordersPerPage +
                (orders.size() % ordersPerPage != 0 ? 1 : 0)));
    }

    @FXML
    private void nextPage() {
        if ((currentPage + 1) * ordersPerPage < orders.size()) {
            loadPage(++currentPage);
        }
    }

    @FXML
    private void prevPage() {
        if (currentPage > 0) {
            loadPage(--currentPage);
        }
    }

    @FXML
    private void filterByStatus() {
        try {
            String selectedStatus = statusComboBox.getValue();
            if (selectedStatus != null) {
                if (selectedStatus.equals("Tất cả")) {
                    orders.setAll(orderService.getAllOrder());
                } else {
                    orders.setAll(orderService.getOrderByStatus(selectedStatus));
                }
                currentPage = 0;
                loadPage(currentPage);
                updatePagination();
                LogUtils.logInfo("Filtered orders by status: " + selectedStatus);
            }
        } catch (Exception e) {
            LogUtils.logError("Error filtering orders by status", e);
            showError("Lỗi lọc đơn hàng", "Không thể lọc đơn hàng theo trạng thái. Vui lòng thử lại sau.");
        }
    }

    @FXML
    private void goToOnline() {
        try {
            msc.handleOnlineOrder();
            LogUtils.logInfo("Navigated to online orders");
        } catch (Exception e) {
            LogUtils.logError("Error navigating to online orders", e);
            showError("Lỗi chuyển trang", "Không thể chuyển đến trang đơn hàng trực tuyến. Vui lòng thử lại sau.");
        }
    }

    @FXML
    private void addOrder() {
        try {
            msc.handleAddOrder();
            LogUtils.logInfo("Navigated to add order");
        } catch (Exception e) {
            LogUtils.logError("Error navigating to add order", e);
            showError("Lỗi chuyển trang", "Không thể chuyển đến trang thêm đơn hàng. Vui lòng thử lại sau.");
        }
    }

    // OrderItemViewListener implementation
    @Override
    public void onItemClick(OrderItemView view) {
        try {
            Order order = view.getOrder();
            if (order != null) {
                msc.handleEditOrder(order);
                LogUtils.logInfo("Viewing order details: " + order.getId());
            }
        } catch (Exception e) {
            LogUtils.logError("Error viewing order details", e);
            showError("Lỗi xem chi tiết", "Không thể xem chi tiết đơn hàng. Vui lòng thử lại sau.");
        }
    }

    @Override
    public void onStatusChange(OrderItemView view, String newStatus) {
        try {
            Order order = view.getOrder();
            if (order != null) {
                order.setStatus(newStatus);
                orderService.updateStatus(order.getId(), newStatus);
                LogUtils.logInfo("Changed order status: " + order.getId() + " to " + newStatus);
            }
        } catch (Exception e) {
            LogUtils.logError("Error changing order status", e);
            showError("Lỗi cập nhật trạng thái", "Không thể cập nhật trạng thái đơn hàng. Vui lòng thử lại sau.");
        }
    }

    @Override
    public void onQuantityChange(OrderItemView view, int newQuantity) {
        try {
            OrderDetail orderDetail = view.getOrderDetail();
            if (orderDetail != null) {
                orderDetail.setQuantity(newQuantity);
                orderDetail.setUnitPrice(orderDetail.getUnitPrice() / orderDetail.getQuantity() * newQuantity);
                orderService.updateOrder(orderDetail.getOrderId(), BigDecimal.valueOf(orderDetail.getUnitPrice()));
                LogUtils.logInfo("Changed order detail quantity: " + orderDetail.getProductId() + " to " + newQuantity);
            }
        } catch (Exception e) {
            LogUtils.logError("Error changing quantity", e);
            showError("Lỗi cập nhật số lượng", "Không thể cập nhật số lượng sản phẩm. Vui lòng thử lại sau.");
        }
    }

    @Override
    public void onRemove(OrderItemView view) {
        try {
            OrderDetail orderDetail = view.getOrderDetail();
            if (orderDetail != null) {
                orderService.deleteOrder(orderDetail.getOrderId());
                LogUtils.logInfo("Removed order detail: " + orderDetail.getProductId());
            }
        } catch (Exception e) {
            LogUtils.logError("Error removing order detail", e);
            showError("Lỗi xóa sản phẩm", "Không thể xóa sản phẩm khỏi đơn hàng. Vui lòng thử lại sau.");
        }
    }

    @Override
    public void onAddProduct(OrderItemView view, Product product) {
        try {
            Order currentOrder = view.getOrder();
            if (product != null && currentOrder != null) {
                OrderDetail orderDetail = new OrderDetail(currentOrder.getId(), product.getProduct_id(), 1,
                        product.getPrice());
                orderService.addOrder(currentOrder.getUserId(), BigDecimal.valueOf(orderDetail.getUnitPrice()));
                LogUtils.logInfo("Added product to order: " + product.getProduct_id());
            }
        } catch (Exception e) {
            LogUtils.logError("Error adding product", e);
            showError("Lỗi thêm sản phẩm", "Không thể thêm sản phẩm vào đơn hàng. Vui lòng thử lại sau.");
        }
    }

    @Override
    public void updateTotalPrice() {
        try {
            double total = 0;
            for (OrderDetail od : orderDetailList) {
                total += od.getUnitPrice();
            }
            totalLabel.setText(String.format("%,.0f VNĐ", total));
        } catch (Exception e) {
            LogUtils.logError("Error updating total price", e);
            showError("Lỗi cập nhật tổng tiền", "Không thể cập nhật tổng tiền đơn hàng. Vui lòng thử lại sau.");
        }
    }
}
