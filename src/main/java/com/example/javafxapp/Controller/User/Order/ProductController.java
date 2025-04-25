package com.example.javafxapp.Controller.User.Order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.CategoryService;
import com.example.javafxapp.Service.ProductService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ProductController {

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

    private void loadPage(){
        grid.getChildren().clear();
        // 1 trang co 10 product
        for (int i = 0; i < products.size(); i++){
            Product product = products.get(i);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/javafxapp/view/cart/products/product.fxml"));
                VBox vbox = loader.load();

                ProductItemController pic = loader.getController();
                pic.setProduct(product);

                grid.add(vbox, i % 3 + 1, i / 3 + (i % 3 > 0 ? 1 : 0));
                grid.setMargin(vbox, new Insets(6));
                grid.setMaxWidth(214);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    }
}
