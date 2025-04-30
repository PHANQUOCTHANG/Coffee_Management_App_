package com.example.javafxapp.Controller.Admin.Order;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.List;

import com.example.javafxapp.Controller.Admin.MainScreenController;
import com.example.javafxapp.Controller.Admin.BaseController;
import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.PDFExporter;
import com.example.javafxapp.Helpper.Pages;
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
import javafx.stage.Stage;
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
    private JFXComboBox statusComboBox;

    @FXML
    private Label timeLabel;

    @FXML
    private JFXButton markBtn;

    @FXML
    private Label idLabel;

    private int orderId = -1;

    public OrderDetailController(){};

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    private Order order = null;

    private double totalPrice = 0;

    @FXML
    private JFXComboBox typeComboBox;
    // list chua toan bo product theo filter
    private List<Product> products = new ArrayList<>();
    private ObservableList<OrderDetail> orderDetailList = FXCollections.observableArrayList();

    ProductService productService = new ProductService(); 
    CategoryService categoryService = new CategoryService();
    OrderDetailService orderDetailService = new OrderDetailService();
    OrderService orderService = new OrderService();

    // map tu orderDetail item sang productitem 
    // => xac dinh duoc controller cua tung product de set trang thai nut add
    private Map<Integer, ProductOrderDetailItemController> mp = new HashMap<>();

    @FXML 
    private void initialize(){
        userNameLabel.setText("N/A");
        timeLabel.setText("N/A");
        idLabel.setText("#...");

        PauseTransition pause = new PauseTransition(Duration.seconds(0.75));

        // ham search
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            pause.stop(); // Dừng transition nếu đang chạy
            pause.setOnFinished(event -> {
                if (newValue != null) {
                    String cleaned = newValue.trim().replaceAll("\\s+", " ");
                    if (cleaned.isEmpty()) {
                        updateProductDisplay(products);
                        return;
                    }

                    String keyword = TextNormalizer.normalize(cleaned);

                    List<Product> filtered = new ArrayList<>();
                    for (Product product : products) {
                        if (TextNormalizer.normalize(product.getProduct_name()).contains(keyword)) {
                            filtered.add(product);
                        }
                    }

                    updateProductDisplay(filtered);
                }
            });
            pause.playFromStart(); // Bắt đầu đếm lại 1s sau mỗi lần nhập
        });

        

        // chon trang thai
        List<String> statusList = new ArrayList<>();
        statusList.add("Đang chờ xử lí");
        statusList.add("Đang xử lí");
        statusList.add("Đã xử lí");
        statusList.add("Đã huỷ");

        statusComboBox.getItems().addAll(statusList);
        statusComboBox.setValue("Đang chờ xử lí");

        // load list filter (loai sp trong combobox)
        
        List<Category> typeList = categoryService.getAllCategory();
        typeList.add(new Category(0, "Tất cả"));
        typeComboBox.getItems().addAll(typeList);
        typeComboBox.setValue(typeList.get(typeList.size() - 1));

        loadData();
    }

    private void updateProductDisplay(List<Product> l){
        List<Product> p = products;
        products = l;
        loadPage();
        products = p;
    }

    public void setOrder(Order order){
        this.order = order;
        orderId = order.getId();
    }

    public void loadData(){
        // load danh sach sp
        products = productService.getAllProduct();
        setOrderDetailList();
        System.out.println("odl size = " + orderDetailList.size());

        

        loadPage();
    }

    private void setOrderDetailList(){
        orderDetailList = FXCollections.observableArrayList(orderDetailService.getAll(orderId));
    }

    @FXML
    void filterAction(ActionEvent event) {
        Category selected = (Category) typeComboBox.getValue();
        if (selected.getCategory_id() == 0)
            products = productService.getAllProduct();            
        else 
            products = productService.getAllByCategoryId(selected.getCategory_id());
        if (products.isEmpty() || products == null){
            System.out.println("Khong lay du lieu duoc!");
        }
        loadPage();
    }

    private void loadPage(){
        updateTotalPrice();

        ExecutorService executorService = Executors.newFixedThreadPool(10);  // Sử dụng 4 thread để tải dữ liệu

        List<VBox> vboxes = new ArrayList<>();  // Dùng để lưu các VBox trước khi thêm vào grid

        for (int i = 0; i < products.size(); i++) {
            int finalI = i;
            executorService.submit(() -> {
                Product product = products.get(finalI);
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/com/example/javafxapp/view/orders/orderDetail/products/product.fxml"));
                    VBox vbox = loader.load();

                    ProductOrderDetailItemController pic = loader.getController();
                    pic.setProduct(product);
                    pic.setOrderDetailController(this);
                    mp.put(product.getProduct_id(), pic);

                    // Lưu các VBox vào list, tránh cập nhật giao diện liên tục
                    synchronized (vboxes) {
                        vboxes.add(vbox);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        try {
            // Đợi các thread hoàn thành
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        // Sau khi hoàn thành tất cả các thread, cập nhật giao diện trên thread chính
        Platform.runLater(() -> {
            grid1.getChildren().clear();
            for (int i = 0; i < vboxes.size(); i++) {
                VBox vbox = vboxes.get(i);
                grid1.add(vbox, i % 3 + 1, i / 3 + 1);
                grid1.setMargin(vbox, new Insets(2));
                grid1.setMaxWidth(214);
            }

            loadOrderDetailList();
            loadTitle();
        });
    }

    // load title
    private void loadTitle(){
        //load username
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
                if (orderDetailList.size() > 0){
                    // neu hoa don chua co trong orders thi tao mot orders moi, neu da co thi update
                    if (orderId == -1){
                        orderId = orderService.addOrder(SaveAccountUtils.account_id, new BigDecimal("" + totalPrice));
                    }
                    else orderService.updateOrder(orderId, new BigDecimal("" + totalPrice));
                    // update order trong database
                    orderDetailService.update(orderId, orderDetailList);
                    // update status
                    orderService.updateStatus(orderId, order.getStatus());
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

    public void updateOrderDetailPrice(int id, int cnt){
        for (OrderDetail od : orderDetailList)
            if (od.getProductId() == id){
                od.setUnitPrice(od.getUnitPrice() / od.getQuantity() * cnt);
                od.setQuantity(cnt);
            }
    }

    public void updateTotalPrice(){
        totalPrice = 0;
        for (OrderDetail od : orderDetailList)
            totalPrice += od.getUnitPrice();
        priceLabel.setText(totalPrice + " đ");
    }


    public void addOrderDetail(Product product){
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

    public void loadOrderDetailList(){
        int row = 1;
        grid2.getChildren().clear();
        for (int i = orderDetailList.size() - 1; i >= 0; i--){
            OrderDetail od = orderDetailList.get(i);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/javafxapp/view/orders/orderDetail/orderDetailItem.fxml"
                ));
                HBox hbox = loader.load();
                OrderDetailItemController odic = loader.getController();
                odic.setOrderDetail(od);
                odic.setOrderDetailController(this);

                grid2.add(hbox, 0, row++);
                grid2.setMargin(hbox, new Insets(5));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }            
        }
    }

    public void removeOrderDetail(int productId){
        orderDetailList.removeIf(od -> od.getProductId() == productId);
    }

    public ProductOrderDetailItemController getProductItemController(int id){
        return mp.get(id);
    }

    public void setUserNameLabel(){
        AccountService accountService = new AccountService();
        Account account = accountService.findAccountByID(SaveAccountUtils.account_id);
        if (account != null) 
            userNameLabel.setText(accountService.findAccountByID(SaveAccountUtils.account_id).getAccountName());
    }

    @FXML
    void selectStatus(){
        String selectedStatus = (String) statusComboBox.getValue();
        if (selectedStatus.equals("Đang chờ xử lí"))
            order.setStatus("Pending");
        else if (selectedStatus.equals("Đang xử lí"))
            order.setStatus("Processing");
        else if (selectedStatus.equals("Đã xử lí"))
            order.setStatus("Completed");
        else order.setStatus("Cancelled");
    }

    @FXML
    void mark(ActionEvent event) {
        statusComboBox.setValue("Đã xử lí");
        order.setStatus("Completed");
    }

    @FXML
    void payment(ActionEvent event) {
        if (orderId != -1){
            boolean checkChange = orderDetailService.checkChange(orderId, orderDetailList);
            if (checkChange && AlertInfo.confirmAlert("Bạn muốn in những thay đổi hay in hoá đơn cũ? Chọn Ok để lưu thay đổi."))
                saveOrderDetail();
            exportInvoicePDF(orderId, (List<OrderDetail>) orderDetailList);
            if (!order.getStatus().equals("Completed"))
                order.setStatus("Processing");
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, 
                        "Thành công", "File invoice_" + orderId + ".pdf đã được lưu trong file invoices.");
        }
    }

    public void exportInvoicePDF(int orderId, List<OrderDetail> details) {
        // Thư mục invoices ở cùng cấp với src/

        File dir = new File("invoices");
        if (!dir.exists()) dir.mkdirs();

        String fileName = "invoice_" + orderId + ".pdf";
        File file = new File(dir, fileName);

        // Gọi class xuất PDF
        PDFExporter.exportToPDF(file.getAbsolutePath(), details, orderId);

        System.out.println("PDF hóa đơn đã được lưu tại: " + file.getAbsolutePath());
    }

    
}
