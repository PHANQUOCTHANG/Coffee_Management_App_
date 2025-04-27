package com.example.javafxapp.Controller.User.Order;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import com.example.javafxapp.Controller.User.BaseController;
import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.OrderDetail;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.CategoryService;
import com.example.javafxapp.Service.ProductService;
import com.example.javafxapp.Service.User.OrderDetailService;
import com.example.javafxapp.Service.User.OrderService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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

    private int orderDetailId;

    public OrderDetailController(){};

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

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
    private Map<Integer, ProductItemController> mp = new HashMap<>();

    @FXML 
    private void initialize(){
        loadData();
    }

    private void loadData(){
        // load danh sach sp
        products = productService.getAllProduct();
        orderDetailList = FXCollections.observableArrayList(orderDetailService.getAll(orderDetailId));

        // load list filter (loai sp trong combobox)
        
        List<Category> typeList = categoryService.getAllCategory();
        typeList.add(new Category(0, "Tất cả"));
        typeComboBox.getItems().addAll(typeList);
        typeComboBox.setValue(typeList.get(typeList.size() - 1));


        loadPage();
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
        grid1.getChildren().clear();
        updateTotalPrice();
        for (int i = 0; i < products.size(); i++){
            Product product = products.get(i);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/javafxapp/view/orderDetail/products/product.fxml"));
                VBox vbox = loader.load();

                ProductItemController pic = loader.getController();
                pic.setProduct(product);
                pic.setOrderDetailController(this);
                mp.put(product.getProduct_id(), pic);

                grid1.add(vbox, i % 3 + 1, i / 3);
                grid1.setMargin(vbox, new Insets(3));
                grid1.setMaxWidth(214);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    }

    @FXML
    void cancel() {
        try {
            // kiem tra co thay doi list order detail khong, neu co thi hien bang thong bao
            boolean checkChange = orderDetailService.checkChange(orderDetailId, orderDetailList);
            if ((checkChange && AlertInfo.confirmAlert("Bạn có chắc muốn huỷ thay đổi?") ||
                !checkChange)) {
                umsc.handleOrders();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void saveOrderDetail() {
        try {
            // kiem tra co thay doi list order detail khong, neu co thi hien bang thong bao
            boolean checkChange = orderDetailService.checkChange(orderDetailId, orderDetailList);
            if ((orderDetailList.size() > 0 &&
                AlertInfo.confirmAlert("Bạn có chắc muốn lưu thay đổi?") || 
                !checkChange)) {
                if (orderDetailList.size() > 0 &&
                AlertInfo.confirmAlert("Bạn có chắc muốn lưu thay đổi?")){
                    AlertInfo.showAlert(Alert.AlertType.INFORMATION, 
                        "Thành công", "Đã lưu thành công");
                    orderDetailService.update(orderDetailId, orderDetailList);
                    orderService.addOrder(userId, new BigDecimal("" + totalPrice));
                    loadData();
                }
                umsc.handleOrders();
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
        OrderDetail od = new OrderDetail(orderDetailId, product.getProduct_id(), 1, product.getPrice());
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
                    "/com/example/javafxapp/view/orderDetail/orderDetailItem.fxml"
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

    public ProductItemController getProductItemController(int id){
        return mp.get(id);
    }

    
}
