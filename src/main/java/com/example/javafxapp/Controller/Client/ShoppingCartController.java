package com.example.javafxapp.Controller.Client;

import com.example.javafxapp.Controller.Admin.AuthController;
import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Helpper.UploadImage;
import com.example.javafxapp.Model.Cart_Product;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.CartService;
import com.example.javafxapp.Service.Cart_ProductService;
import com.example.javafxapp.Service.ProductService;
import com.example.javafxapp.Utils.SaveAccountUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ShoppingCartController implements Initializable {
    @FXML
    private VBox cartItemsContainer;

    @FXML
    private CheckBox selectAllCheckBox;

    @FXML
    private Label subtotalLabel;

    @FXML
    private Label discountLabel;

    @FXML
    private Label totalLabel;

    @FXML
    private VBox promotionContainer;

    @FXML
    private Label promotionLabel;

    @FXML
    private Button checkoutBtn , backBtn;

    private CartService cartService; // Assume you have a service to manage cart
    private List<HBox> cartItemRows = new ArrayList<>();
    private Map<Integer, CheckBox> productCheckboxes = new HashMap<>();
    private ProductService productService = new ProductService() ;
    private Cart_ProductService cartProductService = new Cart_ProductService() ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize cart service
        cartService = new CartService();

        // Load cart items
        loadCartItems();

        // Update totals
        updateTotals();

//        // Check for promotions
//        checkForPromotions();
    }

    private void loadCartItems() {
        try {
            cartItemsContainer.getChildren().clear();
            cartItemRows.clear();
            productCheckboxes.clear();

            List<Cart_Product> cartProducts= cartProductService.getAll(SaveAccountUtils.cart_id) ;

            if (cartProducts.isEmpty()) {
                Label emptyLabel = new Label("Giỏ hàng của bạn đang trống");
                emptyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #757575; -fx-padding: 20;");
                cartItemsContainer.getChildren().add(emptyLabel);
                return;
            }

            for (Cart_Product item : cartProducts) {
                HBox cartItemRow = createCartItemRow(item);
                cartItemRows.add(cartItemRow);
                cartItemsContainer.getChildren().add(cartItemRow);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HBox createCartItemRow(Cart_Product item) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10));
        row.setStyle("-fx-background-color: white; -fx-border-color: #EEEEEE; -fx-border-radius: 5;");

        // Checkbox
        CheckBox selectBox = new CheckBox();
        selectBox.setSelected(true);
        selectBox.setOnAction(e -> updateTotals());
        Product product = productService.findProductByID(item.getProduct_id()) ;
        productCheckboxes.put(product.getProduct_id() , selectBox);

        // Product image
        ImageView productImage = new ImageView(UploadImage.loadImage(product.getImgSrc()));
        productImage.setFitWidth(80);
        productImage.setFitHeight(80);
        productImage.setPreserveRatio(true);

        // Product info
        VBox productInfo = new VBox(5);
        productInfo.setAlignment(Pos.CENTER_LEFT);
        productInfo.setPrefWidth(250);

        Label nameLabel = new Label(product.getProduct_name());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label priceLabel = new Label(String.format("%,.0f VNĐ", product.getPrice()));
        priceLabel.setStyle("-fx-text-fill: #FF5722;");

        productInfo.getChildren().addAll(nameLabel, priceLabel);

        // Spacer


        // Quantity selector
        HBox quantitySelector = createQuantitySelector(item);
        HBox.setMargin(quantitySelector , new Insets(0,20,0,810));

        // Subtotal
        Label subtotalLabel = new Label(String.format("%,.0f VNĐ", product.getPrice() * item.getQuantity()));
        subtotalLabel.setStyle("-fx-font-weight: bold;");
        subtotalLabel.setPrefWidth(100);

        // Delete button
        Button deleteBtn = new Button("✕");
        deleteBtn.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white");
        deleteBtn.setOnAction(e -> removeCartItem(item));

        row.getChildren().addAll(selectBox, productImage, productInfo, quantitySelector, subtotalLabel, deleteBtn);

        return row;
    }
    private HBox createQuantitySelector(Cart_Product item) {
        HBox quantitySelector = new HBox(0);
        quantitySelector.setStyle("-fx-border-color: #CCCCCC; -fx-border-radius: 3px;");
        quantitySelector.setPrefSize(120, 30); // 30 cao, 120 ngang (mỗi button 30 + label 60)
        quantitySelector.setMaxSize(120, 30);
        quantitySelector.setAlignment(Pos.CENTER);

        // Nút giảm số lượng
        Button decreaseBtn = new Button("-");
        decreaseBtn.setStyle("-fx-background-color: #EEEEEE; -fx-text-fill: #5D4037; -fx-min-width: 40px; -fx-min-height: 30px; -fx-font-weight: bold;");

        // Hiển thị số lượng

//        TextField quantityTextField = new TextField(String.valueOf(item.getQuantity()));
//        quantityTextField.setAlignment(Pos.CENTER);
//        quantityTextField.setPrefWidth(20);
//        quantityTextField.setPrefHeight(20);
//        quantityTextField.setStyle("-fx-alignment: center; -fx-background-color: white; -fx-text-fill: #5D4037; -fx-font-weight: bold;-fx-min-width: 60px   ");
        Label quantityLabel = new Label(String.valueOf(item.getQuantity()));
        quantityLabel.setAlignment(Pos.CENTER);
        quantityLabel.setPrefWidth(20);
        quantityLabel.setPrefHeight(20);
        quantityLabel.setStyle("-fx-alignment: center; -fx-background-color: white; -fx-text-fill: #5D4037; -fx-font-weight: bold;-fx-min-width: 60px   ");

        // Nút tăng số lượng
        Button increaseBtn = new Button("+");
        increaseBtn.setStyle("-fx-background-color: #EEEEEE; -fx-text-fill: #5D4037; -fx-min-width: 40px; -fx-min-height: 30px; -fx-font-weight: bold;");

        // Xử lý sự kiện cho nút giảm
        decreaseBtn.setOnAction(e -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                quantityLabel.setText(String.valueOf(item.getQuantity()));
                updateCartItem(item);
            }
        });

//        // Xử lý sự kiện cho nút tăng
        increaseBtn.setOnAction(e -> {
            item.setQuantity(item.getQuantity() + 1);
            quantityLabel.setText(String.valueOf(item.getQuantity()));
            updateCartItem(item);
        });

        quantitySelector.getChildren().addAll(decreaseBtn, quantityLabel, increaseBtn);
        return quantitySelector;
    }

    private void updateCartItem(Cart_Product item) {
        // cập nhật số lượngsản phẩm đó trong giỏ hàng .
        cartProductService.update(item);

        // load lại các sản phẩm trong giỏ .
        loadCartItems();

       // cập nhật lại giá .
        updateTotals();
    }

    private void removeCartItem(Cart_Product item) {
        // xóa sản phẩm ra khỏi giỏ .
        cartProductService.delete(item);

        // load lại các sản phẩm trong giỏ .
        loadCartItems();

        // Cập nhật lại tổng tiền .
        updateTotals();
    }

    @FXML
    private void handleSelectAll() {
        boolean selectAll = selectAllCheckBox.isSelected();

        for (CheckBox checkbox : productCheckboxes.values()) {
            checkbox.setSelected(selectAll);
        }

        updateTotals();
    }

    @FXML
    private void handleDeleteSelected() {
        List<Integer> selectedIds = new ArrayList<>();

        for (Map.Entry<Integer, CheckBox> entry : productCheckboxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                selectedIds.add(entry.getKey());
            }
        }

        if (!selectedIds.isEmpty()) {
            if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa không ?")) {
                // xóa các sản phẩm đã chọn .
                for (Integer selectedId : selectedIds) {
                    cartProductService.delete(new Cart_Product(SaveAccountUtils.cart_id , selectedId));
                }

                // load lại giỏ thanh toán .
                loadCartItems();

                // cập nhật lại giá .
                updateTotals();
            }
        }
    }

    private void updateTotals() {
        double subtotal = 0;

        for (Cart_Product item : cartProductService.getAll(SaveAccountUtils.cart_id)) {
            CheckBox checkbox = productCheckboxes.get(item.getProduct_id());
            Product product = productService.findProductByID(item.getProduct_id())  ;
            if (checkbox != null && checkbox.isSelected()) {
                subtotal += product.getPrice() * item.getQuantity();
            }
        }

        // Calculate discount if any
        double discount = calculateDiscount(subtotal);

        // Set labels
        subtotalLabel.setText(String.format("%,.0f VNĐ", subtotal));
        discountLabel.setText(String.format("-%,.0f VNĐ", discount));
        totalLabel.setText(String.format("%,.0f VNĐ", subtotal - discount));

        // Update checkout button
        checkoutBtn.setDisable(subtotal <= 0);
    }

    private double calculateDiscount(double subtotal) {
        // Example discount logic
        if (subtotal >= 200000) {
            return subtotal * 0.1; // 10% discount for orders over 200,000 VNĐ
        }
        return 0;
    }
//
//    private void checkForPromotions() {
//        // Check if there are active promotions
//        List<Promotion> activePromotions = getActivePromotions();
//
//        if (!activePromotions.isEmpty()) {
//            Promotion promotion = activePromotions.get(0);
//            promotionLabel.setText(promotion.getDescription());
//            promotionContainer.setVisible(true);
//            promotionContainer.setManaged(true);
//        } else {
//            promotionContainer.setVisible(false);
//            promotionContainer.setManaged(false);
//        }
//    }
//
//    private List<Promotion> getActivePromotions() {
//        // This would connect to your promotions service
//        // Example mock data
//        List<Promotion> promotions = new ArrayList<>();
//        promotions.add(new Promotion("SPRING2023", "Giảm 10% cho đơn hàng trên 200,000 VNĐ"));
//        return promotions;
//    }
//
    @FXML
    private void handleBackAction() {
        Stage stage = (Stage) backBtn.getScene().getWindow() ;
        stage.close();
    }
//
//    @FXML
//    private void handleCheckout() {
//        // Get selected items
////        List<Cart> selectedItems = new ArrayList<>();
////
////        for (CartItem item : cartService.getCartItems()) {
////            CheckBox checkbox = productCheckboxes.get(item.getProduct().getId());
////            if (checkbox != null && checkbox.isSelected()) {
////                selectedItems.add(item);
////            }
////        }
////
////        if (!selectedItems.isEmpty()) {
////            try {
////                // Load checkout view
////                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Checkout.fxml"));
////                Parent root = loader.load();
////
////                CheckoutController controller = loader.getController();
////                controller.setCartItems(selectedItems);
////
////                // Show in same window or new window
////                Scene scene = cartItemsContainer.getScene();
////                scene.setRoot(root);
////            } catch (IOException e) {
////                e.printStackTrace();
////                showErrorAlert("Không thể mở trang thanh toán");
////            }
////        }
//    }
//
////    private void showErrorAlert(String message) {
////        Alert alert = new Alert(Alert.AlertType.ERROR);
////        alert.setTitle("Lỗi");
////        alert.setHeaderText(null);
////        alert.setContentText(message);
////        alert.showAndWait();
////    }
//
//    // Model classes for reference
//    private static class Promotion {
//        private String code;
//        private String description;
//
//        public Promotion(String code, String description) {
//            this.code = code;
//            this.description = description;
//        }
//
//        public String getCode() {
//            return code;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//    }
    @FXML
    private void handleCheckOut() throws IOException {
        Pages.pageCheckOut();
    }
}