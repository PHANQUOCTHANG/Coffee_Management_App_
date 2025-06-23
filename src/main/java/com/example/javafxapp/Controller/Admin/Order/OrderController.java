package com.example.javafxapp.Controller.Admin.Order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.javafxapp.Controller.Admin.BaseController;
import com.example.javafxapp.Controller.Admin.MainScreenController;
import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.TextNormalizer;
import com.example.javafxapp.Model.Order;
import com.example.javafxapp.Model.Role;
import com.example.javafxapp.Service.OrderService;
import com.example.javafxapp.Service.RoleService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableMap;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class OrderController extends BaseController {

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
    private JFXCheckBox idCheckBox;

    @FXML
    private HBox hboxHandleTicked;

    @FXML
    private Label cntTickedLabel;

    @FXML
    private VBox vboxFilter;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXComboBox roleCheck, roleComboBox, statusCheck;

    @FXML
    private JFXTextArea fromPrice, toPrice;

    @FXML
    private DatePicker fromDate, toDate;

    @FXML
    private HBox statusComboBox;

    @FXML
    private HBox selectedStatusesHBox;

    VBox statusVBox;

    private OrderService orderService = new OrderService();
    private RoleService roleService = new RoleService();

    private List<Order> orders = new ArrayList<>();

    private int currentPage = 0;
    private final int ordersPerPage = 10;

    // map lưu trữ key value là id -> orderDetailController
    // dùng để xoá mục orderDetail tương ứng
    Map<Integer, OrderItemController> mp = new HashMap<>();
    // map dung de luu cac order da tick
    ObservableMap<Integer, Boolean> mpTickedOrder = FXCollections.observableHashMap();

    // phan bo loc
    ObservableList<Label> selectedStatuses = FXCollections.observableArrayList();

    @FXML
    void goToOnline() {
        msc.handleOnlineOrder();
    }

    public MainScreenController getMSC() {
        return msc;
    }

    public void loadData() {
        orders = orderService.getAllOrder();
        loadPage(currentPage);

        filterLoadData();
    }

    private void filterLoadData() {
        List<String> checkList = new ArrayList<>();
        checkList.add("is");
        checkList.add("is not");
        roleCheck.setItems(FXCollections.observableArrayList(checkList));
        roleCheck.setValue("is");
        statusCheck.setItems(FXCollections.observableArrayList(checkList));
        statusCheck.setValue("is");

        List<Role> roleList = new ArrayList<>();
        roleList = roleService.getAllRole();
        for (Role role : roleList) {
            roleComboBox.getItems().add(role.getRole_name());
        }

    }

    private void loadPage(int pageIdx) {
        currentPage = pageIdx;
        int row = 0;
        mpTickedOrder.clear();
        grid.getChildren().clear();
        int start = pageIdx * ordersPerPage;
        firstPage.setText(String.valueOf(currentPage + 1));
        for (int i = start; i < Math.min(start + ordersPerPage, orders.size()); i++) {
            Order order = orders.get(i);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/com/example/javafxapp/View/Orders/orderItem.fxml"));
                HBox hbox = loader.load();

                OrderItemController oic = loader.getController();
                oic.setOrder(order);
                oic.setOrderController(this);
                mp.put(order.getId(), oic);

                grid.add(hbox, 0, row++);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        prevBtn.setDisable(currentPage == 0);
        nextBtn.setDisable((currentPage + 1) * ordersPerPage >= orders.size());
    }

    @FXML
    public void initialize() {
        hboxHandleTicked.setVisible(false);
        hboxHandleTicked.setOpacity(0);

        // Create fade transitions
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), hboxHandleTicked);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), hboxHandleTicked);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(event -> hboxHandleTicked.setVisible(false));

        // dat ham theo doi thay doi mpTickedOrder.size()
        mpTickedOrder.addListener((MapChangeListener.Change<? extends Integer, ? extends Boolean> change) -> {
            if (mpTickedOrder.size() > 0) {
                hboxHandleTicked.setVisible(true);
                if (hboxHandleTicked.getOpacity() == 0)
                    fadeIn.play();
                cntTickedLabel.setText(String.valueOf(mpTickedOrder.size()));
            } else {
                fadeOut.play();
            }
        });

        vboxFilter.setVisible(false);
        // Khi click ra ngoài vboxFilter thì ẩn nó
        rootPane.setOnMouseClicked(event -> {
            vboxFilter.setVisible(false);
            statusVBox.setVisible(false);
        });

        // Khi click vào chính vboxFilter thì không làm gì cả (ngăn sự kiện lan ra
        // ngoài)
        vboxFilter.setOnMouseClicked(event -> {
            event.consume();
            statusVBox.setVisible(false);
        });
        // tao hbox an trong statusComboBox
        statusVBox = new VBox();
        createStatusOptions();
        statusVBox.setVisible(false);
        statusVBox.setOnMouseClicked(event -> {
            event.consume();
            statusVBox.setVisible(true);
        });
        // Khi click vào statusComboBox thì hiển thị statusComboBox gom 1 hbox trong do
        // se hien ra
        // cac hbox con gom 1 label de chua ten status va 1 label de check trang thai
        // chon, neu duoc chon thi label se thay doi mau
        statusComboBox.setOnMouseClicked(event -> {
            event.consume(); // Ngăn sự kiện lan ra ngoài
            if (statusVBox.isVisible()) {
                statusVBox.setVisible(false);
            } else {
                statusVBox.setVisible(true);
            }
        });
        selectedStatusesHBox.setPadding(new Insets(5));
        selectedStatusesHBox.setSpacing(5);
        selectedStatuses.addListener((ListChangeListener.Change<? extends Label> change) -> {
            selectedStatusesHBox.getChildren().clear();
            if (selectedStatuses.size() > 0) {
                // Tạo bản sao của label đầu tiên
                Label originalLabel = selectedStatuses.get(0);
                Label displayLabel = new Label(originalLabel.getText());
                String style = "-fx-background-color: #3D3D3D; -fx-background-radius: 6px; -fx-padding: 5px; -fx-text-fill: #E0E0E0; -fx-font-size: 13px;";
                displayLabel.setStyle(style);
                selectedStatusesHBox.getChildren().add(displayLabel);
            }
            if (selectedStatuses.size() > 1) {
                Label statusLabel = new Label();
                statusLabel.setText("+" + (selectedStatuses.size() - 1));
                String style2 = "-fx-background-color: #3D3D3D; -fx-background-radius: 6px; -fx-padding: 5px; -fx-text-fill: #E0E0E0; -fx-font-size: 13px;";
                statusLabel.setStyle(style2);
                selectedStatusesHBox.getChildren().add(statusLabel);
            }
            for (Node label : statusVBox.getChildren()) {
                if (selectedStatuses.contains(label)) {
                    label.setStyle("-fx-background-color: #4CAF50; -fx-background-radius: 6px; -fx-text-fill: white; -fx-padding: 8px 12px; -fx-font-size: 13px; -fx-cursor: hand;");
                } else {
                    label.setStyle("-fx-background-color: #3D3D3D; -fx-background-radius: 6px; -fx-text-fill: #E0E0E0; -fx-padding: 8px 12px; -fx-font-size: 13px; -fx-cursor: hand;");
                }
            }
        });

        PauseTransition pause = new PauseTransition(Duration.seconds(0.75));

        // ham search
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            pause.stop(); // Dừng transition nếu đang chạy
            pause.setOnFinished(event -> {
                if (newValue != null) {
                    String cleaned = newValue.trim().replaceAll("\s+", " ");
                    if (cleaned.isEmpty()) {
                        updateOrderDisplay(orders);
                        return;
                    }

                    String keyword = TextNormalizer.normalize(cleaned);

                    List<Order> filtered = new ArrayList<>();
                    for (Order order : orders) {
                        if (TextNormalizer.normalize(order.getStaffName()).contains(keyword)) {
                            filtered.add(order);
                        }
                    }

                    updateOrderDisplay(filtered);
                }
            });
            pause.playFromStart(); // Bắt đầu đếm lại 1s sau mỗi lần nhập

        });

        loadData();
        lastPage.setText(String.valueOf(orders.size() / ordersPerPage +
                (orders.size() % ordersPerPage != 0 ? 1 : 0)));
    }

    // tao hbox an trong statusComboBox
    private void createStatusOptions() {
        // set layout x = 735, y = 290
        statusVBox.setLayoutX(735);
        statusVBox.setLayoutY(290);
        statusVBox.setPadding(new Insets(10));
        statusVBox.setSpacing(8); // Khoảng cách giữa các label

        // Style cho container
        statusVBox.setStyle(
                "-fx-background-color: #2D2D2D; -fx-background-radius: 8px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 0, 2);");

        // Style cho các trạng thái
        String defaultStyle = "-fx-background-color: #3D3D3D; -fx-background-radius: 6px; -fx-text-fill: #E0E0E0; -fx-padding: 8px 12px; -fx-font-size: 13px; -fx-cursor: hand;";
        String selectedStyle = "-fx-background-color: #4CAF50; -fx-background-radius: 6px; -fx-text-fill: white; -fx-padding: 8px 12px; -fx-font-size: 13px; -fx-cursor: hand;";

        List<Label> statusLabels = new ArrayList<>();
        Label statusLabel1 = new Label("Đang chờ xử lí");
        Label statusLabel2 = new Label("Đang xử lí");
        Label statusLabel3 = new Label("Đã xử lí");
        Label statusLabel4 = new Label("Đã hủy");

        statusLabels.add(statusLabel1);
        statusLabels.add(statusLabel2);
        statusLabels.add(statusLabel3);
        statusLabels.add(statusLabel4);

        for (Label statusLabel : statusLabels) {
            // Set style mặc định
            statusLabel.setStyle(defaultStyle);

            // Xử lý click
            statusLabel.setOnMouseClicked(event -> {
                event.consume();
                if (selectedStatuses.contains(statusLabel)) {
                    statusLabel.setStyle(defaultStyle);
                    selectedStatuses.remove(statusLabel);
                    statusLabel.setStyle(defaultStyle);
                } else {
                    statusLabel.setStyle(selectedStyle);
                    selectedStatuses.add(statusLabel);
                    statusLabel.setStyle(selectedStyle);
                }
            });
        }

        statusVBox.getChildren().addAll(statusLabel1, statusLabel2, statusLabel3, statusLabel4);

        // add vao rootPane
        rootPane.getChildren().add(statusVBox);
    }

    private void updateOrderDisplay(List<Order> l) {
        List<Order> o = orders;
        orders = l;
        loadPage(currentPage);
        orders = o;
    }

    @FXML
    void nextPage(ActionEvent event) {
        if ((currentPage + 1) * ordersPerPage < orders.size()) {
            currentPage++;
            idCheckBox.setSelected(false);
            handleTickAll();
            loadPage(currentPage);
        }
    }

    @FXML
    void prevPage(ActionEvent event) {
        if (currentPage > 0) {
            currentPage--;
            idCheckBox.setSelected(false);
            handleTickAll();
            loadPage(currentPage);
        }
    }

    @FXML
    void addOrder(ActionEvent event) {
        System.out.println("Add order button clicked!");
        msc.handleAddOrder();
    }

    // @FXML
    // void filterAction(ActionEvent event) {
    // String selectedStatus = (String) statusComboBox.getValue();
    // if (selectedStatus.equals("Tất cả"))
    // orders = orderService.getAllOrder();
    // else {
    // String s = "";
    // if (selectedStatus.equals("Đang chờ xử lí"))
    // s = "Pending";
    // else if (selectedStatus.equals("Đang xử lí"))
    // s = "Processing";
    // else if (selectedStatus.equals("Đã xử lí"))
    // s = "Completed";
    // else
    // s = "Cancelled";
    // orders = orderService.getOrderByStatus(s);
    // }
    // if (orders.isEmpty() || orders == null) {
    // System.out.println("Khong lay du lieu duoc!");
    // }
    // currentPage = 0;
    // loadPage(currentPage);
    // }

    @FXML
    void searchOrder(ActionEvent event) {

    }

    public void deleteOrder(int id) {
        orders.removeIf(od -> od.getId() == id);
        loadPage(0);
    }

    @FXML
    void handleTickAll() {
        if (idCheckBox.isSelected()) {
            for (int i = currentPage * ordersPerPage; i < Math.min(currentPage * ordersPerPage + ordersPerPage,
                    orders.size()); i++) {
                Order order = orders.get(i);
                mp.get(order.getId()).setSelected(true);
                mp.get(order.getId()).handleTick();
            }
        } else {
            // Thu thập tất cả ID cần xử lý trước
            List<Integer> idsToProcess = new ArrayList<>(mpTickedOrder.keySet());
            // Sau đó mới xử lý từng ID
            for (Integer id : idsToProcess) {
                if (mp.containsKey(id)) { // Kiểm tra xem controller có tồn tại không
                    mp.get(id).setSelected(false);
                    mp.get(id).handleTick();
                }
            }
        }
    }

    public void putMpTickedOrder(int id) {
        mpTickedOrder.put(id, true);
    }

    public void removeMpTickedOrder(int id) {
        mpTickedOrder.remove(id);
    }

    public void setIdCheckBox(boolean selected) {
        idCheckBox.setSelected(selected);
    }

    public boolean checkTickAll() {
        return mpTickedOrder.size() == Math.min(currentPage * ordersPerPage + ordersPerPage, orders.size())
                - currentPage * ordersPerPage;
    }

    @FXML
    void deleteTicked() {
        if (AlertInfo.confirmAlert("Bạn có thật sự muốn xoá? Hành động này sẽ không thể hoàn tác.")) {
            // xoá dữ liệu trong databse
            List<Integer> idsToProcess = new ArrayList<>(mpTickedOrder.keySet());
            for (Integer id : idsToProcess) {
                orderService.deleteOrder(id);
            }
            mpTickedOrder.clear();
            orders = orderService.getAllOrder();
            loadPage(currentPage);
        }
    }

    @FXML
    void cancelTicked() {
        List<Integer> idsToProcess = new ArrayList<>(mpTickedOrder.keySet());
        if (idCheckBox.isSelected()) {
            idCheckBox.setSelected(false);
            handleTickAll();
        } else
            for (Integer id : idsToProcess) {
                mp.get(id).setSelected(false);
                mp.get(id).handleTick();
            }
    }

    @FXML
    void filt() {
        vboxFilter.setVisible(true);
    }

    @FXML
    void applyFilt() {
        String roleCheckStr = (String) roleCheck.getValue();
        String statusCheckStr = (String) statusCheck.getValue();
        String fromPriceStr = fromPrice.getText();
        if (fromPriceStr.isEmpty()) {
            fromPriceStr = "0";
        }
        try {
            fromPriceStr = String.valueOf(Double.parseDouble(fromPriceStr));
        } catch (NumberFormatException e) {
            fromPriceStr = "0";
        }
        String toPriceStr = toPrice.getText();
        if (toPriceStr.isEmpty()) {
            toPriceStr = "1000000000";
        }
        try {
            toPriceStr = String.valueOf(Double.parseDouble(toPriceStr));
        } catch (NumberFormatException e) {
            toPriceStr = "1000000000";
        }
        LocalDate fromDateStr = fromDate.getValue();
        if (fromDateStr == null) {
            fromDateStr = LocalDate.of(1, 1, 1);
        }
        LocalDate toDateStr = toDate.getValue();
        if (toDateStr == null) {
            toDateStr = LocalDate.now();
        }
        toDateStr = toDateStr.plusDays(1);
        String roleStr = (String) roleComboBox.getValue();
        if (roleStr == null) {
            roleStr = "";
        }
        List<String> statuses = new ArrayList<>();
        for (Label statusLabel : selectedStatuses) {
            String statusText = statusLabel.getText();
            if (statusText.equals("Đang chờ xử lí")) {
                statuses.add("Pending");
            } else if (statusText.equals("Đang xử lí")) {
                statuses.add("Processing");
            } else if (statusText.equals("Đã xử lí")) {
                statuses.add("Completed");
            } else if (statusText.equals("Đã hủy")) {
                statuses.add("Cancelled");
            }
        }
        orders = orderService.getOrder(roleCheckStr, roleStr, fromPriceStr, toPriceStr, statusCheckStr,
                statuses, fromDateStr, toDateStr);
        loadPage(0);
    }

    @FXML
    void resetFilt() {
        roleCheck.setValue("is");
        statusCheck.setValue("is");
        fromPrice.setText("");
        toPrice.setText("");
        fromDate.setValue(null);
        toDate.setValue(null);
        roleComboBox.setValue(null);
        selectedStatuses.clear();
        orders = orderService.getAllOrder();
        for (Node node : statusVBox.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                label.setStyle("-fx-background-color: #3D3D3D; -fx-background-radius: 6px; -fx-text-fill: #E0E0E0; -fx-padding: 8px 12px; -fx-font-size: 13px; -fx-cursor: hand;");
            }
        }
        loadPage(0);
    }

}
