package com.example.javafxapp.Controller.Admin.Order;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.UploadImage;
import com.example.javafxapp.Model.Order;
import com.example.javafxapp.Model.OrderDetail;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.OrderDetailService;
import com.example.javafxapp.Service.ProductService;
import com.example.javafxapp.Utils.LogUtils;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class OrderItemView {
    // Order view components
    @FXML
    private HBox root;
    @FXML
    private Label idLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label staffLabel;

    // Order detail view components
    @FXML
    private Label cntItem;
    @FXML
    private JFXButton decBtn;
    @FXML
    private JFXButton deleteBtn;
    @FXML
    private JFXButton incBtn;
    @FXML
    private Label unitName;
    @FXML
    private Label unitPrice;

    // Product view components
    @FXML
    private JFXButton addBtn;
    @FXML
    private ImageView img;
    @FXML
    private Label nameLabel;
    @FXML
    private Label priceLabel;

    private Order order;
    private OrderDetail orderDetail;
    private Product product;
    private OrderItemViewListener listener;
    private double price;

    private final OrderDetailService orderDetailService = new OrderDetailService();
    private final ProductService productService = new ProductService();

    public interface OrderItemViewListener {
        void onItemClick(OrderItemView view);

        void onStatusChange(OrderItemView view, String newStatus);

        void onQuantityChange(OrderItemView view, int newQuantity);

        void onRemove(OrderItemView view);

        void onAddProduct(OrderItemView view, Product product);

        void updateTotalPrice();
    }

    public void setOrder(Order order) {
        this.order = order;
        updateOrderView();
    }

    public void setOrderDetail(OrderDetail detail) {
        this.orderDetail = detail;
        updateOrderDetailView();
    }

    public void setProduct(Product product) {
        this.product = product;
        updateProductView();
    }

    public void setListener(OrderItemViewListener listener) {
        this.listener = listener;
    }

    public Order getOrder() {
        return order;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public Product getProduct() {
        return product;
    }

    private void updateOrderView() {
        if (order != null) {
            idLabel.setText("#" + order.getId());
            statusLabel.setText(order.getStatus());
            totalLabel.setText(String.format("%,.0f VNĐ", order.getTotalAmount()));
            timeLabel.setText(order.getOrderTime().toString());
            staffLabel.setText(order.getStaffName());
        }
    }

    private void updateOrderDetailView() {
        if (orderDetail != null) {
            Product product = productService.findProductByID(orderDetail.getProductId());
            unitName.setText(product.getProduct_name());
            cntItem.setText(String.valueOf(orderDetail.getQuantity()));
            unitPrice.setText(String.format("%,.0f đ", orderDetail.getUnitPrice()));
            price = product.getPrice();
        }
    }

    private void updateProductView() {
        if (product != null) {
            img.setImage(UploadImage.loadImage(product.getImgSrc()));
            nameLabel.setText(product.getProduct_name());
            priceLabel.setText(String.format("%,.0f đ", product.getPrice()));
        }
    }

    @FXML
    private void handleItemClick() {
        if (listener != null) {
            listener.onItemClick(this);
        }
    }

    @FXML
    private void handleStatusChange(String newStatus) {
        if (listener != null) {
            listener.onStatusChange(this, newStatus);
        }
    }

    @FXML
    private void handleQuantityChange(int newQuantity) {
        if (listener != null) {
            listener.onQuantityChange(this, newQuantity);
        }
    }

    @FXML
    private void handleRemove() {
        if (listener != null) {
            listener.onRemove(this);
        }
    }

    @FXML
    private void handleAddProduct() {
        if (listener != null && product != null) {
            listener.onAddProduct(this, product);
        }
    }

    @FXML
    private void edit() {
        if (listener != null) {
            listener.onItemClick(this);
        }
    }

    @FXML
    private void del() {
        if (listener != null) {
            listener.onRemove(this);
        }
    }
}