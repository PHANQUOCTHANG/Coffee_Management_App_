package com.example.javafxapp.Controller.Client;

import com.example.javafxapp.Controller.Admin.AuthController;
import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Helpper.UploadImage;
import com.example.javafxapp.Model.*;
import com.example.javafxapp.Service.*;
import com.example.javafxapp.Utils.ListProductOrderUser;
import com.example.javafxapp.Utils.SaveAccountUtils;
import com.example.javafxapp.Validation.ValidationInformationUser;
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

import java.io.IOException;
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
    private InformationUserService informationUserService = new InformationUserService();
    private OrderUserService orderUserService = new OrderUserService() ;
    private OrderUser_ProductService orderUserProductService = new OrderUser_ProductService() ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Khởi tạo formatter cho tiền tệ Việt Nam
        currencyFormatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
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

    // hiển thị các sản phẩm chọn mua ra giao diện .
    private void loadOrderSummary() {
        orderSummaryContainer.getChildren().clear();

        for (Cart_Product cartProduct : ListProductOrderUser.list) {
            HBox itemBox = createOrderItemBox(cartProduct);
            orderSummaryContainer.getChildren().add(itemBox);
        }
    }

    // tạo giao diện cho từng sản phẩm .
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

    // tính tổng tiền .
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

    // chuyển về trang giỏ thanh toán .
    @FXML
    private void handleBackToCart(ActionEvent event) throws IOException {
        // Quay lại trang giỏ hàng
        Pages.pageShoppingCart();
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
    }

    // thanh toán .
    @FXML
    private void handlePlaceOrder(ActionEvent event) {
        // Kiểm tra dữ liệu đầu vào
        if (!validateInputs()) {
            return ;
        }

        try {
            // Lấy phương thức giao hàng được chọn
            String deliveryMethod = standardDeliveryRadio.isSelected() ? "Standard" : "Express";

            // Lấy phương thức thanh toán được chọn
            String paymentMethod;
            if (codRadio.isSelected()) {
                paymentMethod = "COD";
            } else if (bankTransferRadio.isSelected()) {
                paymentMethod = "BankTransfer";
            } else if (creditCardRadio.isSelected()) {
                paymentMethod = "CreditCard";
            } else if (momoRadio.isSelected()) {
                paymentMethod = "MoMo";
            }
            else {
                AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Chọn phương thức thanh toán");
                return ;
            }
            // lấy thông tin người đặt hàng để giao .
            String fullName = nameField.getText().trim() ;
            String phone = phoneField.getText().trim() ;
            String address = addressField.getText().trim() ;
            String note = noteField.getText().trim() ;
            // lưu đơn hàng vừa đặt của khách .
            OrderUser orderUser = new OrderUser(SaveAccountUtils.account_id , fullName , phone , address , note , shippingFee , paymentMethod , subtotal , discount , "Pending");
            orderUserService.add(orderUser);
            // lấy đơn hàng vừa rồi để lấy id của đơn hàng để thêm các sản phẩm của đơn hàng đó .
            OrderUser orderUserCurrent = orderUserService.getOrderUserCurrent() ;
            // mua thành công thì sẽ thêm các sản phẩm đó vào đơn hàng và xóa các sản phẩm đã mua trong giỏ thanh toán ;
            for (Cart_Product cartProduct : ListProductOrderUser.list) {
                if (orderUserCurrent != null) orderUserProductService.add(new OrderUser_Product(orderUserCurrent.getOrderUser_id() , cartProduct.getProduct_id() , cartProduct.getQuantity()));
                cartProductService.delete(cartProduct);
            }
            // xóa các sản phẩm đã chọn mua từ cart .
            ListProductOrderUser.list.clear();

            AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Đặt hàng thành công");
            Stage stage = (Stage) placeOrderBtn.getScene().getWindow() ;
            stage.close();
            Pages.pageUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
 }

    private boolean validateInputs () {
        if (!ValidationInformationUser.checkFullName(nameField.getText().trim())) return false ;
        if (!ValidationInformationUser.checkPhone(phoneField.getText().trim())) return false ;
        if (!ValidationInformationUser.checkAddress(addressField.getText().trim())) return false;
        return true ;
    }

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