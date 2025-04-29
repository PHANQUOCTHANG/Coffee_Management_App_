package com.example.javafxapp.Controller.User.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.javafxapp.Controller.Admin.MainScreenController;
import com.example.javafxapp.Controller.User.BaseController;
import com.example.javafxapp.Controller.User.UserMainScreenController;
import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Model.Order;
import com.example.javafxapp.Repository.User.OrderRepository;
import com.example.javafxapp.Service.User.OrderService;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class OrderController extends BaseController {
    @FXML
    private ComboBox statusComboBox;

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


    private OrderService orderService = new OrderService();

    private List<Order> orders = new ArrayList<>();

    private int currentPage = 0;
    private final int ordersPerPage = 10;

    // map lưu trữ key value là id -> orderDetailController 
    // dùng để xoá mục orderDetail tương ứng
    Map<Integer, OrderItemController> mp = new HashMap<>();

    public UserMainScreenController getUMSC(){
        return umsc;
    }


    public void loadData(){
        orders = orderService.getAllOrder();
        loadPage(currentPage);

        List<String> statusList = new ArrayList<>();
        statusList.add("Đang chờ xử lí");
        statusList.add("Đang xử lí");
        statusList.add("Đã xử lí");
        statusList.add("Đã huỷ");
        statusList.add("Tất cả");

        statusComboBox.getItems().addAll(statusList);
        statusComboBox.setValue("Tất cả");
    }

    private void loadPage(int pageIdx){
        int row = 0;
        grid.getChildren().clear();
        int start = pageIdx * ordersPerPage;
        firstPage.setText(String.valueOf(currentPage + 1));
        for (int i = start; i < Math.min(start + ordersPerPage, orders.size()); i++){
            Order order = orders.get(i);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/javafxapp/view/orders/orderItem.fxml"));
                HBox hbox = loader.load();

                OrderItemController oic = loader.getController();
                oic.setOrder(order);
                oic.setOrderController(this);
                mp.put(order.getId(), oic);

                grid.add(hbox, 0, row++);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        prevBtn.setDisable(currentPage == 0);
        nextBtn.setDisable((currentPage + 1) * ordersPerPage >= orders.size());
    }
    @FXML
    public void initialize(){
        loadData();
        lastPage.setText(String.valueOf(orders.size() / ordersPerPage + 
                            (orders.size() % ordersPerPage != 0 ? 1 : 0)));
    }

    @FXML
    void nextPage(ActionEvent event) {
        if ((currentPage + 1) * ordersPerPage < orders.size())
            loadPage(++currentPage);
    }

    @FXML
    void prevPage(ActionEvent event) {
        if (currentPage > 0)
            loadPage(--currentPage);
    }

    @FXML
    void addOrder(ActionEvent event) {
        System.out.println("Add order button clicked!");
        umsc.handleAddOrder();
    }

    @FXML
    void filterAction(ActionEvent event) {
        String selectedStatus = (String) statusComboBox.getValue();
        if (selectedStatus.equals("Tất cả"))
            orders = orderService.getAllOrder();
        else {
            String s = "";
            if (selectedStatus.equals("Đang chờ xử lí"))
                s = "Pending";
            else if (selectedStatus.equals("Đang xử lí"))
                s = "Processing";
            else if (selectedStatus.equals("Đã xử lí"))
                s = "Completed";
            else s = "Cancelled";
            orders = orderService.getOrderByStatus(s);
        }
        if (orders.isEmpty() || orders == null){
            System.out.println("Khong lay du lieu duoc!");
        }
        currentPage = 0;
        loadPage(currentPage);
    }

    @FXML
    void searchOrder(ActionEvent event) {
        
    }

    public void deleteOrder(int id){
        orders.removeIf(od -> od.getId() == id);
        loadPage(0);
    }
}
