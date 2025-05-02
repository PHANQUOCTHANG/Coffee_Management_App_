package com.example.javafxapp.Controller.Admin.Order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.CategoryService;
import com.example.javafxapp.Service.ProductService;
import com.jfoenix.controls.JFXComboBox;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ProductOrderDetailController {

    @FXML
    private ScrollPane scroll;

    @FXML
    private TextField searchField;

    @FXML
    private JFXComboBox typeComboBox;

    @FXML
    private Label productCnt;

    @FXML
    private GridPane grid;

    // list chua toan bo product theo filter
    private List<Product> products = new ArrayList<>();

    ProductService productService = new ProductService();
    CategoryService categoryService = new CategoryService();

    @FXML 
    private void initialize(){
        loadData();
    }

    private void loadData(){
        // load danh sach sp
        
        products = productService.getAllProduct();

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
        else products = productService.getAllByCategoryId(selected.getCategory_id());
        if (products.isEmpty() || products == null){
            System.out.println("Khong lay du lieu duoc!");
        }
        loadPage();
    }

    private void loadPage() {
        grid.getChildren().clear();
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        for (int i = 0; i < products.size(); i++) {
            int finalI = i;
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    Product product = products.get(finalI);
                    VBox vbox = null;
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafxapp/View/Orders/OrderDetail/products/product.fxml"));
                        vbox = loader.load();  // Load FXML và tạo VBox
                        ProductOrderDetailItemController pic = loader.getController();
                        pic.setProduct(product);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Cập nhật giao diện về thread chính
                    if (vbox != null) {
                        final VBox finalVbox = vbox;
                        Platform.runLater(() -> {
                            grid.add(finalVbox, finalI % 3 + 1, finalI / 3 + (finalI % 3 > 0 ? 1 : 0));
                            grid.setMargin(finalVbox, new Insets(3));
                            grid.setMaxWidth(210);
                        });
                    }
                    return null;
                }
            };

            executorService.submit(task);
        }

        executorService.shutdown();
    }

}
