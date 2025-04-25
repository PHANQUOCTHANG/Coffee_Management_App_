package com.example.javafxapp.Controller.User.Order;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
        for (int i = 0; i < products.size(); i++){
            Product product = products.get(i);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/javafxapp/view/orderDetail/products/product.fxml"));
                VBox vbox = loader.load();

                ProductItemController pic = loader.getController();
                pic.setProduct(product);

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
            if (checkChange && AlertInfo.confirmAlert("Bạn có chắc muốn huỷ thay đổi?")) {
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
            if (orderDetailList.size() > 0 && checkChange && 
                AlertInfo.confirmAlert("Bạn có chắc muốn lưu thay đổi?")) {
                umsc.handleOrders();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, 
                    "Thành công", "Đã lưu thành công");
                orderDetailService.update(orderDetailId, orderDetailList);
                orderService.addOrder(userId, new BigDecimal("" + totalPrice));
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }


}
