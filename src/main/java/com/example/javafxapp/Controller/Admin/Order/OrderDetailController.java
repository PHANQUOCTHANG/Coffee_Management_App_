package com.example.javafxapp.Controller.Admin.Order;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.List;

import com.example.javafxapp.Controller.Admin.BaseController;
import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.PDFExporter;
import com.example.javafxapp.Helpper.TextNormalizer;
import com.example.javafxapp.Model.Account;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Order;
import com.example.javafxapp.Model.OrderDetail;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.AccountService;
import com.example.javafxapp.Service.CategoryService;
import com.example.javafxapp.Service.OrderDetailService;
import com.example.javafxapp.Service.OrderService;
import com.example.javafxapp.Service.ProductService;
import com.example.javafxapp.Utils.LogUtils;
import com.example.javafxapp.Utils.SaveAccountUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class OrderDetailController extends BaseController {
    @FXML
    private JFXButton cancelBtn;

    @FXML
    private GridPane grid1;

    @FXML
    private GridPane grid2;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private ScrollPane scroll;

    @FXML
    private TextField searchField;

    @FXML
    private Label priceLabel;

    @FXML
    private Label userNameLabel;

    @FXML
    private JFXComboBox<String> statusComboBox;

    @FXML
    private Label timeLabel;

    @FXML
    private JFXButton markBtn;

    @FXML
    private Label idLabel;

    @FXML
    private ScrollPane scroll1;

    private int orderId = -1;

    public OrderDetailController() {
    };

    public void setOrderId(int orderId) {
        this.orderId = orderId;
        loadData();
    }

    public void setOrder(Order order) {
        this.order = order;
        this.orderId = order.getId();
        loadData();
    }

    private Order order = null;

    private double totalPrice = 0;

    // dung cho responsive loadpage
    double containerWidth = -1;

    @FXML
    private JFXComboBox<String> typeComboBox;
    // list chua toan bo product theo filter
    private List<Product> products = new ArrayList<>();
    private ObservableList<OrderDetail> orderDetailList = FXCollections.observableArrayList();

    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();
    private final OrderDetailService orderDetailService = new OrderDetailService();
    private final OrderService orderService = new OrderService();
    private final AccountService accountService = new AccountService();

    // map tu orderDetail item sang productitem
    // => xac dinh duoc controller cua tung product de set trang thai nut add
    private Map<Integer, ProductOrderDetailItemController> mp = new HashMap<>();

    @Override
    protected void initializeComponents() {
        // Initialize status combo box
        statusComboBox.setItems(FXCollections.observableArrayList(
                "Pending", "Processing", "Completed", "Cancelled"));

        // Initialize type combo box
        typeComboBox.setItems(FXCollections.observableArrayList(
                "All", "Food", "Drink", "Other"));
        typeComboBox.setValue("All");
    }

    @Override
    protected void setupEventHandlers() {
        // Setup search field handler
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProducts(newValue);
        });

        // Setup type combo box handler
        typeComboBox.setOnAction(event -> {
            filterProducts(searchField.getText());
        });

        // Setup status combo box handler
        statusComboBox.setOnAction(event -> {
            if (order != null) {
                order.setStatus(statusComboBox.getValue());
            }
        });
    }

    private void filterProducts(String searchText) {
        try {
            String type = typeComboBox.getValue();
            if (type.equals("All")) {
                products = productService.getAllProducts();
            } else {
                products = productService.getProductsByType(type);
            }
            if (searchText != null && !searchText.isEmpty()) {
                products = products.stream()
                        .filter(p -> TextNormalizer.normalize(p.getProduct_name())
                                .contains(TextNormalizer.normalize(searchText)))
                        .toList();
            }
            loadProductList();
        } catch (Exception e) {
            LogUtils.logError("Error filtering products", e);
            showError("Lỗi tìm kiếm", "Không thể tìm kiếm sản phẩm. Vui lòng thử lại sau.");
        }
    }

    private void loadProductList() {
        int row = 0;
        grid1.getChildren().clear();
        for (Product product : products) {
            try {
                // Use ClassLoader to load FXML
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader()
                        .getResource("com/example/javafxapp/View/Orders/OrderDetail/products/product.fxml"));
                VBox vbox = loader.load();
                ProductOrderDetailItemController controller = loader.getController();
                controller.setProduct(product);
                controller.setOrderDetailController(this);
                mp.put(product.getProduct_id(), controller);

                grid1.add(vbox, 0, row++);
                grid1.setMargin(vbox, new Insets(5));
            } catch (IOException e) {
                LogUtils.logError("Error loading product item", e);
            }
        }
    }

    public void loadData() {
        try {
            // Load all products first
            products = productService.getAllProducts();
            loadProductList();

            if (orderId != -1) {
                order = orderService.getOrderById(orderId);
                if (order != null) {
                    idLabel.setText("#" + order.getId());
                    statusComboBox.setValue(order.getStatus());
                    timeLabel.setText(order.getOrderTime().toString());
                    Account staff = accountService.findAccountByName(order.getStaffName());
                    userNameLabel.setText(staff != null ? staff.getAccountName() : "Unknown");

                    orderDetailList.clear();
                    orderDetailList.addAll(orderDetailService.getAll(orderId));
                    loadOrderDetailList();
                    updateTotalPrice();
                }
            }
        } catch (Exception e) {
            LogUtils.logError("Error loading order data", e);
            showError("Lỗi tải dữ liệu", "Không thể tải thông tin đơn hàng. Vui lòng thử lại sau.");
        }
    }

    private void setOrderDetailList() {
        orderDetailList = FXCollections.observableArrayList(orderDetailService.getAll(orderId));
    }

    @FXML
    void filterAction(ActionEvent event) {
        String selected = typeComboBox.getValue();
        if (selected.equals("All")) {
            products = productService.getAllProducts();
        } else {
            products = productService.getProductsByCategory(selected);
        }
        loadPage();
    }

    private void loadPage() {
        updateTotalPrice();

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        List<VBox> vboxes = new ArrayList<>();

        for (int i = 0; i < products.size(); i++) {
            int finalI = i;
            executorService.submit(() -> {
                Product product = products.get(finalI);
                try {
                    // Load product.fxml for the product list
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(
                            "../../../../../resources/com/example/javafxapp/View/Orders/OrderDetail/products/product.fxml"));
                    VBox vbox = loader.load();

                    ProductOrderDetailItemController pic = loader.getController();
                    pic.setProduct(product);
                    pic.setOrderDetailController(this);
                    mp.put(product.getProduct_id(), pic);

                    synchronized (vboxes) {
                        vboxes.add(vbox);
                    }
                } catch (IOException e) {
                    LogUtils.logError("Error loading product item", e);
                }
            });
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        Platform.runLater(() -> {
            grid1.getChildren().clear();
            if (containerWidth == -1) {
                for (int i = 0; i < vboxes.size(); i++) {
                    VBox vbox = vboxes.get(i);
                    grid1.add(vbox, i % 3 + 1, i / 3 + 1);
                    grid1.setMargin(vbox, new Insets(8));
                    grid1.setMaxWidth(214);
                }
            } else {
                int itemWidth = 214;
                int margin = 8;
                int numColumns = Math.max(3, (int) ((containerWidth - margin) / (itemWidth + margin * 2)));

                for (int i = 0; i < vboxes.size(); i++) {
                    VBox vbox = vboxes.get(i);

                    int col = i % numColumns + 1;
                    int row = i / numColumns + 1;

                    grid1.add(vbox, col, row);
                    grid1.setMargin(vbox, new Insets(margin));
                }
            }

            // Load order details in the cart
            loadOrderDetailList();
            loadTitle();
        });

        // Responsive scroll
        PauseTransition pause = new PauseTransition(Duration.seconds(0.75));

        scroll1.widthProperty().addListener((obs, oldVal, newVal) -> {
            pause.stop();
            pause.setOnFinished(event -> {
                Platform.runLater(() -> {
                    grid1.getChildren().clear();

                    containerWidth = newVal.doubleValue();
                    int itemWidth = 214;
                    int margin = 8;
                    int numColumns = Math.max(3, (int) ((containerWidth - margin) / (itemWidth + margin * 2)));

                    for (int i = 0; i < vboxes.size(); i++) {
                        VBox vbox = vboxes.get(i);

                        int col = i % numColumns + 1;
                        int row = i / numColumns + 1;

                        grid1.add(vbox, col, row);
                        grid1.setMargin(vbox, new Insets(margin));
                    }

                    loadOrderDetailList();
                    loadTitle();
                });
            });
            pause.playFromStart();
        });
    }

    // load title
    private void loadTitle() {
        // load username
        setUserNameLabel();

        // load id order
        if (orderId != -1)
            idLabel.setText("#" + orderId);

        // load thoi diem tao
        if (order != null)
            timeLabel.setText("" + order.getOrderTime());
    }

    @FXML
    void cancel() {
        try {
            // kiem tra co thay doi list order detail khong, neu co thi hien bang thong bao
            boolean checkChange = orderDetailService.checkChange(orderId, orderDetailList);
            if ((checkChange && AlertInfo.confirmAlert("Bạn có chắc muốn trở về mà không lưu thay đổi?") ||
                    !checkChange)) {
                msc.handleOrders();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void saveOrderDetail() {
        try {
            // kiem tra co thay doi list order detail khong, neu co thi hien bang thong bao
            boolean checkChange = orderDetailService.checkChange(orderId, orderDetailList);
            if ((orderDetailList.size() > 0 &&
                    AlertInfo.confirmAlert("Bạn có chắc muốn lưu thay đổi?") ||
                    !checkChange)) {
                if (orderDetailList.size() > 0) {
                    // neu hoa don chua co trong orders thi tao mot orders moi, neu da co thi update
                    if (orderId == -1) {
                        orderId = orderService.addOrder(SaveAccountUtils.account_id, new BigDecimal("" + totalPrice));
                    } else
                        orderService.updateOrder(orderId, new BigDecimal("" + totalPrice));
                    // update order trong database
                    orderDetailService.update(orderId, orderDetailList);
                    // update status
                    orderService.updateStatus(orderId, order != null ? order.getStatus() : "Pending");
                    // load ten nguoi tao, cap nhat trang thai order va ngay tao
                    setUserNameLabel();

                    loadData();
                    AlertInfo.showAlert(Alert.AlertType.INFORMATION,
                            "Thành công", "Đã lưu thành công");
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void updateOrderDetailPrice(int productId, int newQuantity) {
        for (OrderDetail od : orderDetailList) {
            if (od.getProductId() == productId) {
                double oldPrice = od.getUnitPrice() / od.getQuantity();
                od.setQuantity(newQuantity);
                od.setUnitPrice(oldPrice * newQuantity);
                break;
            }
        }
        updateTotalPrice();
    }

    public void updateTotalPrice() {
        totalPrice = 0;
        for (OrderDetail od : orderDetailList)
            totalPrice += od.getUnitPrice();
        priceLabel.setText(String.format("%,.0f VNĐ", totalPrice));
    }

    public void addOrderDetail(Product product) {
        // add du lieu vo list
        for (OrderDetail od : orderDetailList)
            if (od.getProductId() == product.getProduct_id())
                return;
        OrderDetail od = new OrderDetail(orderId, product.getProduct_id(), 1, product.getPrice());
        orderDetailList.add(od);

        updateTotalPrice();

        // ui
        loadOrderDetailList();
    }

    public void loadOrderDetailList() {
        int row = 1;
        grid2.getChildren().clear();
        for (int i = orderDetailList.size() - 1; i >= 0; i--) {
            OrderDetail od = orderDetailList.get(i);
            try {
                // Use ClassLoader to load FXML
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader()
                        .getResource("com/example/javafxapp/View/Orders/OrderDetail/orderDetailItem.fxml"));
                HBox hbox = loader.load();
                OrderDetailItemController odic = loader.getController();
                odic.setOrderDetail(od);
                odic.setOrderDetailController(this);

                grid2.add(hbox, 0, row++);
                grid2.setMargin(hbox, new Insets(5));
            } catch (IOException e) {
                LogUtils.logError("Error loading order detail item", e);
            }
        }
    }

    public void removeOrderDetail(int productId) {
        orderDetailList.removeIf(od -> od.getProductId() == productId);
    }

    public ProductOrderDetailItemController getProductItemController(int id) {
        return mp.get(id);
    }

    public void setUserNameLabel() {
        AccountService accountService = new AccountService();
        Account account = accountService.findAccountByID(SaveAccountUtils.account_id);
        if (account != null)
            userNameLabel.setText(accountService.findAccountByID(SaveAccountUtils.account_id).getAccountName());
    }

    @FXML
    void selectStatus() {
        String selectedStatus = (String) statusComboBox.getValue();
        if (selectedStatus.equals("Pending"))
            order.setStatus("Pending");
        else if (selectedStatus.equals("Processing"))
            order.setStatus("Processing");
        else if (selectedStatus.equals("Completed"))
            order.setStatus("Completed");
        else
            order.setStatus("Cancelled");
    }

    @FXML
    void mark(ActionEvent event) {
        statusComboBox.setValue("Completed");
        order.setStatus("Completed");
    }

    @FXML
    void payment(ActionEvent event) {
        if (orderId != -1) {
            boolean checkChange = orderDetailService.checkChange(orderId, orderDetailList);
            if (checkChange
                    && AlertInfo.confirmAlert("Bạn muốn in những thay đổi hay in hoá đơn cũ? Chọn Ok để lưu thay đổi."))
                saveOrderDetail();
            exportInvoicePDF(orderId, (List<OrderDetail>) orderDetailList);
            order.setStatus("Completed");
            AlertInfo.showAlert(Alert.AlertType.INFORMATION,
                    "Thành công", "File invoice_" + orderId + ".pdf đã được lưu trong file invoices.");
        }
    }

    public void exportInvoicePDF(int orderId, List<OrderDetail> details) {
        // Thư mục invoices ở cùng cấp với src/

        File dir = new File("invoices");
        if (!dir.exists())
            dir.mkdirs();

        String fileName = "invoice_" + orderId + ".pdf";
        File file = new File(dir, fileName);

        // Gọi class xuất PDF
        PDFExporter.exportToPDF(file.getAbsolutePath(), details, orderId);

        System.out.println("PDF hóa đơn đã được lưu tại: " + file.getAbsolutePath());
    }

}
