package com.example.javafxapp.Controller.Admin.Order.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.javafxapp.Controller.Admin.BaseController;
import com.example.javafxapp.Controller.Admin.MainScreenController;
import com.example.javafxapp.Model.Order;
import com.example.javafxapp.Model.OrderUser;
import com.example.javafxapp.Service.OrderService;
import com.example.javafxapp.Service.OrderUserService;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class OrderUserController extends BaseController {
    

    @FXML
    private Label firstPage;

    @FXML
    private GridPane grid;

    @FXML
    private Label lastPage;

    @FXML
    private JFXButton nextBtn;

    @FXML
    private JFXButton prevBtn;

    @FXML
    private ScrollPane scroll;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox statusComboBox;

    private OrderUserService orderUserService = new OrderUserService();

    private List<OrderUser> orderUsers = new ArrayList<>();

    Map<Integer, OrderUserItemController> mp = new HashMap<>();

    private int currentPage = 0;
    private final int ordersPerPage = 10;

    @FXML
    void filterAction(ActionEvent event) {

    }

    @FXML
    void nextPage(ActionEvent event) {

    }

    @FXML
    void prevPage(ActionEvent event) {

    }

    public MainScreenController getMSC(){
        return msc;
    }

    @FXML
    void back(){
        msc.handleOrders();
    }


    public void loadData(){
        orderUsers = orderUserService.getAll();
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
        for (int i = start; i < Math.min(start + ordersPerPage, orderUsers.size()); i++){
            OrderUser orderUser = orderUsers.get(i);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/com/example/javafxapp/View/Orders/orderItem.fxml"));
                HBox hbox = loader.load();

                OrderUserItemController ouic = loader.getController();
                ouic.setOrderUser(orderUser);
                ouic.setOrderUserController(this);
                mp.put(orderUser.getOrderUser_id(), ouic);

                grid.add(hbox, 0, row++);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        prevBtn.setDisable(currentPage == 0);
        nextBtn.setDisable((currentPage + 1) * ordersPerPage >= orderUsers.size());
    }

    public void delete(int id){
        orderUsers.removeIf(od -> od.getOrderUser_id() == id);
        loadPage(currentPage);
    }
}
