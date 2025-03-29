package com.example.javafxapp.controller;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.PopOver;

import com.example.javafxapp.dao.ProductDAO;
import com.example.javafxapp.model.Product;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProductsController implements Initializable {
    // new product
    @FXML
    private JFXButton btnFilter;

    @FXML
    private JFXComboBox showBox;

    @FXML
    private ScrollPane scroll;

    @FXML
    private GridPane grid;

    private int showcnt = 20;

    private PopOver poFilter;

    private ProductDAO productDAO = new ProductDAO();

    @FXML
    public void initFilter() {
        try {
            // load noi dung popup tu file fxml
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/javafxapp/view/product/popupFilter.fxml"));
            VBox popupFilter = loader.load();

            // tao popover
            poFilter = new PopOver(popupFilter);
            // khong hien thi mui ten
            poFilter.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
            poFilter.setArrowSize(0);
            // xu ly su kien khi click vao nut filter
            btnFilter.setOnAction(e -> {
                if (poFilter.isShowing()) {
                    poFilter.hide();
                } else {
                    poFilter.show(btnFilter);
                }
            });

            // xu li khi nhan nut dong hoac click ra ngoai
            JFXButton btnCloseFilter = (JFXButton) popupFilter.lookup("#btnCloseFilter");
            btnCloseFilter.setOnAction(e -> {
                poFilter.hide();
            });
            // xu li khi click ra ngoai
            // poFilter.detachedProperty(): Đây là một BooleanProperty (thuộc tính trạng
            // thái) của PopOver, cho biết PopOver có bị tách khỏi nút gốc không.
            // .addListener((obs, oldDetached, newDetached) -> { ... }): Đăng ký một
            // listener để theo dõi khi giá trị của thuộc tính thay đổi.
            // newDetached == true → Nghĩa là PopOver đã bị tách (người dùng click ra ngoài,
            // hoặc mất focus).
            // Gọi poFilter.hide(); để ẩn popup.
            poFilter.detachedProperty().addListener((obs, oldDetached, newDetached) -> {
                if (newDetached) {
                    poFilter.hide();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML 
    private void initShowBox() {
        ObservableList<String> items = FXCollections.observableArrayList("Hiển thị 10", "Hiển thị 20", "Hiển thị 50");
        showBox.setItems(items);
        // Tạo các MenuItem (nếu cần sử dụng MenuButton)
        MenuItem item10 = new MenuItem("Hiển thị 10");
        MenuItem item20 = new MenuItem("Hiển thị 20");
        MenuItem item50 = new MenuItem("Hiển thị 50");

        showBox.setOnAction(event -> {
            String selectedItem = (String) showBox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                handleComboBoxSelection(selectedItem);
            }
        });
    }

    // Hàm xử lý khi chọn một mục từ ComboBox
    private void handleComboBoxSelection(String selectedItem) {
        System.out.println("Đã chọn: " + selectedItem);
        // Ví dụ: Cập nhật số lượng hiển thị dựa theo lựa chọn
        if (selectedItem.equals("Hiển thị 10")) {
            updateDisplay(10);
        } else if (selectedItem.equals("Hiển thị 20")) {
            updateDisplay(20);
        } else if (selectedItem.equals("Hiển thị 50")) {
            updateDisplay(50);
        }
    }

    private void updateDisplay(int number) {
        System.out.println("Cập nhật hiển thị với số lượng: " + number);
        showcnt = number;
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initShowBox();

        // Ví dụ: Gán sự kiện cho nút
        if (btnFilter != null) {
            btnFilter.setOnAction(e -> initFilter());
        }

        loadProducts();
    }

    private void loadProducts(){
        // List<Product> products = productDAO.getAllProduct();

        // test
        List<Product> products = new ArrayList<>();
        Product pd;

    //     private int product_id;
    // private String product_name;
    // private String description;
    // private double price;
    // private int category_id;
    // private String imgSrc;
    // private int stock;
        for (int i = 0; i < 20; i++) {
            pd = new Product();
            pd.setProduct_id(i);
            pd.setProduct_name("Product " + i);
            pd.setDescription("Description " + i);
            pd.setPrice(25000 + i);
            pd.setCategory_id(i);
            pd.setImgSrc("/com/example/javafxapp/view/images/milkcoffee.png");
            pd.setStock(20 + i);
            products.add(pd);
        }
        
        int row = 1;
        for (Product product : products) {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxapp/view/product/product.fxml"));
                HBox productItem = loader.load();

                ProductController controller = loader.getController();
                controller.setProductData(
                    product.getImgSrc(),
                    product.getProduct_name(),
                    product.getStock(),
                    product.getCategory_id(),
                    product.getPrice()
                );

                grid.add(productItem, 0, row++);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // -------------- prev product----------------------------
    // --------------------------------------------------------

    // dashboard
    // @FXML
    // private Label lblWelcome;

    // @FXML
    // private StackPane contentArea;

    // @FXML
    // private Button btnManageUsers, btnManageRoles, btnReports, btnSettings,
    // btnLogout;

    // @FXML
    // private void handleManageUsers() {
    // if (lblWelcome != null)
    // lblWelcome.setText("Đây là trang Quản lý người dùng");
    // }

    // @FXML
    // private void handleManageRoles() {
    // if (lblWelcome != null)
    // lblWelcome.setText("Đây là trang Quản lý vai trò");
    // }

    // @FXML
    // private void handleReports() {
    // if (lblWelcome != null)
    // lblWelcome.setText("Đây là trang Báo cáo");
    // }

    // @FXML
    // private void handleSettings() {
    // if (lblWelcome != null)
    // lblWelcome.setText("Đây là trang Cài đặt");
    // }
    // @FXML
    // private void handleLogout() {
    // // Đóng cửa sổ Dashboard
    // Stage stage = (Stage) btnLogout.getScene().getWindow();
    // stage.close();

    // // Quay lại màn hình đăng nhập
    // System.out.println("Đã đăng xuất!");
    // }

    // // phan xu li product
    // @FXML
    // private JFXButton btnManageUsers1;

    // @FXML
    // private ScrollPane scroll;

    // @FXML
    // private GridPane gridItem;

    // @FXML
    // private TextField nameText;

    // @FXML
    // private TextField priceText;

    // @FXML
    // private TextField desText;

    // @FXML
    // private TextField typeText;

    // @FXML
    // private HBox ImgBox;

    // @FXML
    // private ImageView imgSelected;

    // private List<Product> products = new ArrayList<>();

    // test data
    // private List<Product> getData() {
    //     List<Product> products = new ArrayList<>();
    //     Product product;

    //     for (int i = 0; i < 20; i++) {
    //         product = new Product();
    //         product.setId(i);
    //         product.setName("Product " + i);
    //         product.setDescription("Description " + i);
    //         product.setPrice(25000 + i);
    //         product.setCategory("Category " + i);
    //         product.setImgSrc("/com/example/javafxapp/view/images/milkcoffee.png");
    //         product.setStock(10 + i);
    //         products.add(product);
    //     }

    //     return products;
    // }

    // // ham select item o ben duoi ham nay
    // public void setChosenProduct(Product product){
        // nameText.setText(product.getName());
        // nameText.setEditable(false);
        // priceText.setText(String.valueOf(product.getPrice()));
        // priceText.setEditable(false);
        // desText.setText(product.getDescription());
        // desText.setEditable(false);
        // typeText.setText(product.getCategory());
        // typeText.setEditable(false);
        // Image imageSelected = new
        // Image(getClass().getResourceAsStream(product.getImgSrc()));
        // imgSelected.setImage(imageSelected);

        // // tinh trung binh mau de lam nen cho hinh duoc chon
        // PixelReader pixelReader = imageSelected.getPixelReader();
        // int width = (int) imageSelected.getWidth();
        // int height = (int) imageSelected.getHeight();

        // int sumRed = 0;
        // int sumGreen = 0;
        // int sumBlue = 0;
        // int pixelCount = 0;

        // for (int y = 0; y < height; y += 3){
        // for (int x = 0; x < width; x += 3){
        // int argb = pixelReader.getArgb(x, y);
        // sumRed += (argb >> 16) & 0xFF;
        // sumGreen += (argb >> 8) & 0xFF;
        // sumBlue += argb & 0xFF;
        // pixelCount++;
        // }
    // }

    // // // Tinh trung binh mau
    // int avgRed = sumRed / pixelCount;
    // int avgGreen = sumGreen / pixelCount;
    // int avgBlue = sumBlue / pixelCount;

    // String avgColor = String.format("-fx-background-color: rgb(%d, %d, %d);",
    // avgRed, avgGreen, avgBlue);
    // ImgBox.setStyle(avgColor + ";\n"+
    // "-fx-background-radius: 30;");

    // }

    // @Override
    // public void initialize(URL url, ResourceBundle rb) {
    // products.addAll(getData());

    // // so hang va cot hien thi san pham
    // int column = 0;
    // int row = 1;

    // try{
    // for (int i = 0; i < products.size(); i++){
    // FXMLLoader fxmlLoader = new FXMLLoader();
    // fxmlLoader.setLocation(getClass().getResource("/com/example/javafxapp/view/product/itemsProduct.fxml"));
    // AnchorPane anchorPane = fxmlLoader.load();

    // ItemsProductController itemsProductController = fxmlLoader.getController();
    // itemsProductController.setData(products.get(i), this::setChosenProduct);

    // if (column == 4){
    // column = 0;
    // row++;
    // }

    // gridItem.add(anchorPane, column++, row);
    // // set item grid width
    // gridItem.setMinWidth(Region.USE_COMPUTED_SIZE);
    // gridItem.setPrefWidth(Region.USE_COMPUTED_SIZE);
    // gridItem.setMaxWidth(Region.USE_PREF_SIZE);

    // // set item grid height
    // gridItem.setMinHeight(Region.USE_COMPUTED_SIZE);
    // gridItem.setPrefHeight(Region.USE_COMPUTED_SIZE);
    // gridItem.setMaxHeight(Region.USE_PREF_SIZE);

    // GridPane.setMargin(anchorPane, new Insets(10));

    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }

    // }
}
