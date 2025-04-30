package com.example.javafxapp.Controller.Client;

import com.example.javafxapp.Controller.Admin.AuthController;
import com.example.javafxapp.Helpper.UploadImage;
import com.example.javafxapp.Model.Cart_Product;
import com.example.javafxapp.Model.InformationUser;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.Cart_ProductService;
import com.example.javafxapp.Service.InformationUserService;
import com.example.javafxapp.Service.ProductService;
import com.example.javafxapp.Utils.SaveAccountUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class CheckoutController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextArea addressField;
    @FXML
    private TextArea noteField;

    @FXML
    private RadioButton standardDeliveryRadio;
    @FXML
    private RadioButton expressDeliveryRadio;

    @FXML
    private RadioButton codRadio;
    @FXML
    private RadioButton bankTransferRadio;
    @FXML
    private RadioButton creditCardRadio;
    @FXML
    private RadioButton momoRadio;

    @FXML
    private VBox orderSummaryContainer;
    @FXML
    private Label subtotalLabel;
    @FXML
    private Label shippingFeeLabel;
    @FXML
    private Label discountLabel;
    @FXML
    private Label totalAmountLabel;

    @FXML
    private CheckBox agreeTermsCheckBox;
    @FXML
    private Button placeOrderBtn, backBtn;

    @FXML
    private ProductService productService = new ProductService();
    @FXML
    private Cart_ProductService cartProductService = new Cart_ProductService();

    private double subtotal = 0;
    private double discount = 0;
    private double shippingFee = 30000; // Mặc định là phí giao hàng tiêu chuẩn
    private NumberFormat currencyFormatter;
    List<Cart_Product> cartProducts;
    private InformationUserService informationUserService = new InformationUserService();

    //    private OrderService orderService;
//
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Khởi tạo formatter cho tiền tệ Việt Nam
        currencyFormatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        cartProducts = new ArrayList<>();
        cartProducts = cartProductService.getAll(SaveAccountUtils.cart_id);
//        // Khởi tạo service
//        orderService = new OrderService();
//
        // Lấy thông tin người dùng hiện tại từ session và điền vào form
        InformationUser informationUser = informationUserService.getInformationUserByAccountId(SaveAccountUtils.account_id) ;
        if (informationUser != null) {
            nameField.setText(informationUser.getFullName());
            phoneField.setText(informationUser.getPhone());
            addressField.setText(informationUser.getAddress());
        }

        // Hiển thị thông tin đơn hàng
        loadOrderSummary();

        calculateTotals();

        // Liên kết nút đặt hàng với checkbox điều khoản
        BooleanProperty agreeTermsProperty = new SimpleBooleanProperty();
        agreeTermsCheckBox.selectedProperty().

                bindBidirectional(agreeTermsProperty);
        placeOrderBtn.disableProperty().

                bind(agreeTermsProperty.not());

    }

    private void loadOrderSummary() {
        orderSummaryContainer.getChildren().clear();

        for (Cart_Product cartProduct : cartProducts) {
            HBox itemBox = createOrderItemBox(cartProduct);
            orderSummaryContainer.getChildren().add(itemBox);
        }
    }

    private HBox createOrderItemBox(Cart_Product cartProduct) {
        HBox itemBox = new HBox();
        itemBox.setAlignment(Pos.CENTER_LEFT);
        itemBox.setSpacing(10);
        itemBox.setPadding(new Insets(5));
        itemBox.setStyle("-fx-border-color: #F0F0F0; -fx-border-radius: 5;");

        Product product = productService.findProductByID(cartProduct.getProduct_id());

        // Ảnh sản phẩm
        ImageView productImageView = new ImageView();
        try {
            Image productImage = UploadImage.loadImage(product.getImgSrc());
            productImageView.setImage(productImage);
        } catch (Exception e) {
            // Sử dụng hình ảnh mặc định nếu không tải được
            productImageView.setImage(new Image(getClass().getResourceAsStream("/images/default-product.jpg")));
        }
        productImageView.setFitHeight(50);
        productImageView.setFitWidth(50);
        productImageView.setPreserveRatio(true);

        // Tên sản phẩm
        Label nameLabel = new Label(product.getProduct_name());
        nameLabel.setStyle("-fx-font-weight: bold;");

        // Số lượng
        Label quantityLabel = new Label("x" + cartProduct.getQuantity());

        // Giá
        Label priceLabel = new Label(currencyFormatter.format(product.getPrice() * cartProduct.getQuantity()) + " VNĐ");
        priceLabel.setStyle("-fx-font-weight: bold;");

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        itemBox.getChildren().addAll(productImageView, nameLabel, spacer, quantityLabel, priceLabel);

        return itemBox;
    }

    private void calculateTotals() {
        // Tính tổng tiền hàng
        subtotal = 0;
        List<Cart_Product> cartProducts = cartProductService.getAll(SaveAccountUtils.cart_id);
        for (Cart_Product item : cartProducts) {
            Product product = productService.findProductByID(item.getProduct_id());
            subtotal += product.getPrice() * item.getQuantity();
        }


        // Áp dụng khuyến mãi nếu có
        if (subtotal > 200000) {
            discount = subtotal * 0.1; // Giảm 10% cho đơn hàng trên 200,000 VNĐ
        } else {
            discount = 0;
        }

        // Cập nhật giao diện
        subtotalLabel.setText(currencyFormatter.format(subtotal) + " VNĐ");
        discountLabel.setText("-" + currencyFormatter.format(discount) + " VNĐ");
        updateShippingFee();
    }

    private void updateShippingFee() {
        shippingFeeLabel.setText(currencyFormatter.format(shippingFee) + " VNĐ");
        double total = subtotal - discount + shippingFee;
        totalAmountLabel.setText(currencyFormatter.format(total) + " VNĐ");
    }


    @FXML
    private void handleBackAction() {
        // Quay lại trang trước đó
        Stage stage = (Stage) backBtn.getScene().getWindow() ;
        stage.close();
    }

    @FXML
    private void handleBackToCart(ActionEvent event) {
        // Quay lại trang giỏ hàng
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
    }
//
//    @FXML
//    private void handlePlaceOrder(ActionEvent event) {
//        // Kiểm tra dữ liệu đầu vào
//        if (!validateInputs()) {
//            return;
//        }
//
//        try {
//            // Lấy phương thức giao hàng được chọn
//            String deliveryMethod = standardDeliveryRadio.isSelected() ? "Standard" : "Express";
//
//            // Lấy phương thức thanh toán được chọn
//            String paymentMethod;
//            if (codRadio.isSelected()) {
//                paymentMethod = "COD";
//            } else if (bankTransferRadio.isSelected()) {
//                paymentMethod = "BankTransfer";
//            } else if (creditCardRadio.isSelected()) {
//                paymentMethod = "CreditCard";
//            } else {
//                paymentMethod = "MoMo";
//            }
//
//            // Tạo đối tượng Order
//            Order order = new Order();
//            order.setUserId(SessionManager.getCurrentUser().getId());
//            order.setRecipientName(nameField.getText().trim());
//            order.setRecipientPhone(phoneField.getText().trim());
//            order.setRecipientEmail(emailField.getText().trim());
//            order.setShippingAddress(addressField.getText().trim());
//            order.setNote(noteField.getText().trim());
//            order.setDeliveryMethod(deliveryMethod);
//            order.setPaymentMethod(paymentMethod);
//            order.setSubtotal(subtotal);
//            order.setShippingFee(shippingFee);
//            order.setDiscount(discount);
//            order.setTotalAmount(subtotal - discount + shippingFee);
//            order.setStatus("Chờ xác nhận");
//
//            // Thêm các mục đơn hàng
//            for (CartItem item : cartItems) {
//                OrderItem orderItem = new OrderItem();
//                orderItem.setProductId(item.getProduct().getId());
//                orderItem.setQuantity(item.getQuantity());
//                orderItem.setPrice(item.getProduct().getPrice());
//                order.addOrderItem(orderItem);
//            }
//
//            // Lưu đơn hàng vào cơ sở dữ liệu
//            boolean success = orderService.saveOrder(order);
//
//            if (success) {
//                // Xóa giỏ hàng sau khi đặt hàng thành công
//                SessionManager.clearCart();
//
//                // Hiển thị thông báo thành công
//                AlertHelper.showInformation("Đặt hàng thành công",
//                        "Đơn hàng của bạn đã được đặt thành công!\n" +
//                                "Mã đơn hàng: " + order.getId() + "\n" +
//                                "Cảm ơn bạn đã mua sắm tại cửa hàng chúng tôi.");
//
//                // Chuyển đến trang xác nhận đơn hàng
//                SceneManager.loadScene("OrderConfirmation.fxml", order);
//            } else {
//                AlertHelper.showError("Lỗi", "Có lỗi xảy ra khi đặt hàng. Vui lòng thử lại.");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            AlertHelper.showError("Lỗi", "Có lỗi xảy ra: " + e.getMessage());
//        }
//    }
//
//    private boolean validateInputs() {
//        // Kiểm tra các trường bắt buộc
//        if (nameField.getText().trim().isEmpty()) {
//            AlertHelper.showError("Lỗi", "Vui lòng nhập họ và tên người nhận.");
//            nameField.requestFocus();
//            return false;
//        }
//
//        if (phoneField.getText().trim().isEmpty()) {
//            AlertHelper.showError("Lỗi", "Vui lòng nhập số điện thoại liên hệ.");
//            phoneField.requestFocus();
//            return false;
//        }
//
//        if (emailField.getText().trim().isEmpty() || !isValidEmail(emailField.getText().trim())) {
//            AlertHelper.showError("Lỗi", "Vui lòng nhập địa chỉ email hợp lệ.");
//            emailField.requestFocus();
//            return false;
//        }
//
//        if (addressField.getText().trim().isEmpty()) {
//            AlertHelper.showError("Lỗi", "Vui lòng nhập địa chỉ giao hàng.");
//            addressField.requestFocus();
//            return false;
//        }
//
//        return true;
//    }

    @FXML
    private void handleMethodShip(Event event) {
        RadioButton clickedButton = (RadioButton) event.getSource();
        standardDeliveryRadio.setSelected(false);
        expressDeliveryRadio.setSelected(false);
        if (clickedButton.equals(standardDeliveryRadio)) {
            standardDeliveryRadio.setSelected(true);
            shippingFee = 30000;
        } else {
            expressDeliveryRadio.setSelected(true);
            shippingFee = 60000;
        }
        updateShippingFee();
    }

    @FXML
    private void handleMethodCheckOut(Event event) {
        RadioButton clickedButton = (RadioButton) event.getSource();
        codRadio.setSelected(false);
        bankTransferRadio.setSelected(false);
        creditCardRadio.setSelected(false);
        momoRadio.setSelected(false);
        if (clickedButton.equals(codRadio)) codRadio.setSelected(true);
        else if (clickedButton.equals(bankTransferRadio)) bankTransferRadio.setSelected(true);
        else if (clickedButton.equals(creditCardRadio)) creditCardRadio.setSelected(true);
        else momoRadio.setSelected(true);
    }
}