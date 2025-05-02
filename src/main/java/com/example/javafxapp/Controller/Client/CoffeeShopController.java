package com.example.javafxapp.Controller.Client;

import com.example.javafxapp.Controller.Admin.AuthController;
import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Helpper.UploadImage;
import com.example.javafxapp.Model.*;
import com.example.javafxapp.Service.*;
import com.example.javafxapp.Utils.SaveAccountUtils;
import com.example.javafxapp.Validation.ValidationAccount;
import com.example.javafxapp.Validation.ValidationInformationUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CoffeeShopController implements Initializable {

    @FXML
    private Label cartItemCount;
    @FXML
    private Button homeBtn, menuBtn, promotionBtn, aboutBtn, contactBtn, logoutBtn;

    @FXML
    private GridPane productsGrid;
    @FXML
    private VBox sidebar, cartPanel, settingsPanel;
    @FXML
    private Label currentLoginNameLabel;
    @FXML
    private TextField newLoginNameField, fullNameField, emailField, phoneField, addressField;
    @FXML
    private PasswordField confirmPasswordForLoginName, currentPasswordField, newPasswordField, confirmNewPasswordField;
    @FXML
    private Button saveLoginNameBtn, savePasswordBtn, actionProfileBtn;
    private int productLimit = 4;

    private ProductService productService = new ProductService();
    private CategoryService categoryService = new CategoryService();
    private Cart_ProductService cartProductService = new Cart_ProductService();
    private AccountService accountService = new AccountService();
    private AuthService authService = new AuthService();
    private InformationUserService informationUserService = new InformationUserService();
    // Dữ liệu mẫu để hiển thị sản phẩm
    private Map<String, List<Product>> productsByCategory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Khởi tạo dữ liệu sản phẩm mẫu
        initializeProductData();

        // Hiển thị trang chủ mặc định khi khởi động
        showHomePage();

        // Thiết lập style cho nút đang được chọn (mặc định là trang chủ)
        setActiveButton(homeBtn);
    }

    // Khởi tạo dữ liệu sản phẩm mẫu
    private void initializeProductData() {
        productsByCategory = new HashMap<>();
        List<Category> categories = categoryService.getAllCategory();
        for (Category category : categories) {
            List<Product> products = productService.getAllByCategoryId(category.getCategory_id());
            productsByCategory.put(category.getCategory_name(), products);
        }
        loadCountCart();
        loadSetting();
    }

    public void loadCountCart() {
        List<Cart_Product> cartProducts = cartProductService.getAll(SaveAccountUtils.cart_id);
        int sizeCart = 0;
        if (cartProducts.size() == 0) {
            cartItemCount.setVisible(false);
            cartItemCount.setManaged(false);
        } else {
            cartItemCount.setVisible(true);
            cartItemCount.setManaged(true);
            for (Cart_Product cartProduct : cartProducts) sizeCart += cartProduct.getQuantity();
            cartItemCount.setText(String.valueOf(sizeCart));
        }
    }

    // load danh sách các danh mục .
    private void loadCategoryButtonsToSidebar() {
        List<Category> categories = categoryService.getAllCategory();

        VBox buttonContainer = new VBox(10); // chứa các button
        buttonContainer.setPadding(new Insets(10));

        for (Category category : categories) {
            Button categoryButton = new Button(category.getCategory_name());
            categoryButton.setMaxWidth(Double.MAX_VALUE);
            categoryButton.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #333;");

            // Gán hành động cho button nếu cần
            categoryButton.setOnAction(e -> handleCategoryClick(category));

            buttonContainer.getChildren().add(categoryButton);
        }

        TitledPane titledPane = new TitledPane("DANH MỤC SẢN PHẨM", buttonContainer);
        titledPane.setExpanded(false); // mở sẵn
        sidebar.getChildren().add(titledPane);
    }

    // xử lí khi click vào 1 danh mục
    private void handleCategoryClick(Category category) {
        // Xử lý khi nhấn vào danh mục, ví dụ load sản phẩm ra main content
        System.out.println("Đã chọn danh mục: " + category.getCategory_name());
        showProductsByCategory(category.getCategory_name());
    }

    // Xử lý sự kiện cho các nút menu chính
    @FXML
    private void handleButtonAction(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        // Thiết lập style cho nút được chọn
        setActiveButton(clickedButton);
        // đóng cài đặt .
        closeSetting();
        if (clickedButton.equals(homeBtn)) {
            showHomePage();
        } else if (clickedButton.equals(menuBtn)) {
            showAllProducts();
        } else if (clickedButton.equals(promotionBtn)) {
            showPromotions();
        } else if (clickedButton.equals(aboutBtn)) {
            showAboutPage();
        } else if (clickedButton.equals(contactBtn)) {
            showContactPage();
        }
    }

    // Hiển thị trang chủ
    private void showHomePage() {
        sidebar.getChildren().clear();
        productsGrid.getChildren().clear();

        // Thêm nội dung trang chủ
        VBox homeContent = new VBox(10);
        homeContent.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Chào mừng đến với Coffee Shop!");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label descLabel = new Label("Khám phá thế giới cà phê tuyệt vời cùng chúng tôi");
        descLabel.setStyle("-fx-font-size: 16px;");

        // Thêm ảnh banner
        try {
            ImageView bannerImage = new ImageView(new Image("/images/banner.jpg"));
            bannerImage.setFitWidth(800);
            bannerImage.setPreserveRatio(true);
            homeContent.getChildren().addAll(welcomeLabel, descLabel, bannerImage);
        } catch (Exception e) {
            // Nếu không tìm thấy ảnh, chỉ hiển thị văn bản
            homeContent.getChildren().addAll(welcomeLabel, descLabel);
        }

        // Thêm các sản phẩm nổi bật
        Label featuredLabel = new Label("Sản phẩm nổi bật");
        featuredLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 20px 0 10px 0;");
        homeContent.getChildren().add(featuredLabel);

        // Hiển thị sản phẩm nổi bật (lấy một số sản phẩm từ các danh mục)
        GridPane featuredProducts = new GridPane();
        featuredProducts.setHgap(20);
        featuredProducts.setVgap(20);

        // Lấy một sản phẩm từ mỗi danh mục
        int row = 0, column = 0;
        List<Product> products = productService.getAllIsOutStanding();
        for (Product product : products) {
            featuredProducts.add(createProductCard(product), column++, row);
            if (column == productLimit) {
                row++;
                column = 0;
            }
        }
        homeContent.getChildren().add(featuredProducts);
        productsGrid.add(homeContent, 0, 0);
    }

    // trang sản phẩm .
    private void showAllProducts() {
        sidebar.getChildren().clear();
        loadCategoryButtonsToSidebar();

        productsGrid.getChildren().clear();

        VBox allProductsContent = new VBox(20); // Reduced spacing to fit search bar
        allProductsContent.setPadding(new Insets(40));

        // Add search bar at the top
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);

        TextField searchField = new TextField();
        searchField.setPromptText("Tìm kiếm sản phẩm...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-font-size: 14px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 8px 15px;");

        searchBox.getChildren().addAll(searchField, searchButton);

        // sự kiện của các nút .
        searchButton.setOnAction(e -> performSearch(searchField.getText()));
        searchField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                performSearch(searchField.getText());
            }
        });

        allProductsContent.getChildren().add(searchBox);

        Separator separator = new Separator();
        separator.setPadding(new Insets(10, 0, 10, 0));
        allProductsContent.getChildren().add(separator);

        // Thêm từng danh mục sản phẩm
        for (Map.Entry<String, List<Product>> entry : productsByCategory.entrySet()) {
            String category = entry.getKey();
            List<Product> products = entry.getValue();

            if (products.size() > 0) {
                // Tiêu đề danh mục
                Label categoryLabel = new Label(category);
                categoryLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
                allProductsContent.getChildren().add(categoryLabel);

                // Grid sản phẩm trong danh mục
                GridPane categoryGrid = new GridPane();
                categoryGrid.setHgap(40);
                categoryGrid.setVgap(40);

                // Thêm sản phẩm vào grid
                for (int i = 0; i < products.size(); i++) {
                    categoryGrid.add(createProductCard(products.get(i)), i % productLimit, i / productLimit);
                }

                allProductsContent.getChildren().add(categoryGrid);
            }
        }

        productsGrid.add(allProductsContent, 0, 0);
    }

//         Tìm kiếm .
    private void performSearch(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            // nếu rỗng thì load ra tất cả sản phẩm .
            showAllProducts();
            return;
        }

        // xóa dữ liệu cũ của productsGrid .
        productsGrid.getChildren().clear();

        VBox searchResultsContent = new VBox(20);
        searchResultsContent.setPadding(new Insets(40));

        // Keep the search bar at the top
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);

        TextField searchField = new TextField(searchText);
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");

        Button searchButton = new Button("Tìm kiếm");
        searchButton.setStyle("-fx-font-size: 14px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 8px 15px;");

        Button clearButton = new Button("Xóa");
        clearButton.setStyle("-fx-font-size: 14px; -fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 8px 15px;");

        searchBox.getChildren().addAll(searchField, searchButton, clearButton);

        searchButton.setOnAction(e -> performSearch(searchField.getText()));
        clearButton.setOnAction(e -> showAllProducts());
        searchField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                performSearch(searchField.getText());
            }
        });

        searchResultsContent.getChildren().add(searchBox);

        // đường kẻ ngang
        Separator separator = new Separator();
        separator.setPadding(new Insets(10, 0, 10, 0));
        searchResultsContent.getChildren().add(separator);

        // label kết quả .
        Label resultsLabel = new Label("Kết quả tìm kiếm: \"" + searchText + "\"");
        resultsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        searchResultsContent.getChildren().add(resultsLabel);

        List<Product> matchingProducts = new ArrayList<>();

        for (Map.Entry<String, List<Product>> entry : productsByCategory.entrySet()) {
            for (Product product : entry.getValue()) {
                // Tìm kiếm từ tên và mô tả của sản phẩm .
                if (product.getProduct_name().toLowerCase().contains(searchText.toLowerCase()) ||
                        product.getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                    matchingProducts.add(product);
                }
            }
        }

        // Hiển thị kết quản .
        if (matchingProducts.isEmpty()) {
            Label noResultsLabel = new Label("Không tìm thất sản phẩm.");
            noResultsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #757575;");
            searchResultsContent.getChildren().add(noResultsLabel);
        } else {
            GridPane resultsGrid = new GridPane();
            resultsGrid.setHgap(40);
            resultsGrid.setVgap(40);

            for (int i = 0; i < matchingProducts.size(); i++) {
                resultsGrid.add(createProductCard(matchingProducts.get(i)), i % 4, i / 4);
            }

            searchResultsContent.getChildren().add(resultsGrid);
        }

        // Thêm scroll để kéo .
        ScrollPane scrollPane = new ScrollPane(searchResultsContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        productsGrid.add(scrollPane, 0, 0);
  }

    // Hiển thị sản phẩm theo danh mục
    private void showProductsByCategory(String category) {
        productsGrid.getChildren().clear();

        if (!productsByCategory.containsKey(category)) {
            return;
        }

        // Tiêu đề danh mục
        Label categoryLabel = new Label(category);
        categoryLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Grid sản phẩm
        GridPane categoryGrid = new GridPane();
        categoryGrid.setHgap(20);
        categoryGrid.setVgap(20);

        List<Product> products = productsByCategory.get(category);

        // Thêm sản phẩm vào grid
        for (int i = 0; i < products.size(); i++) {
            categoryGrid.add(createProductCard(products.get(i)), i % productLimit, i / productLimit);
        }

        // Thêm vào container chính
        VBox categoryContent = new VBox(20);
        categoryContent.setPadding(new Insets(20));
        categoryContent.getChildren().addAll(categoryLabel, categoryGrid);

        productsGrid.add(categoryContent, 0, 0);
    }

    // Hiển thị trang khuyến mãi
    private void showPromotions() {
        sidebar.getChildren().clear();
        productsGrid.getChildren().clear();

        VBox promotionContent = new VBox(20);
        promotionContent.setAlignment(Pos.CENTER);

        Label promotionLabel = new Label("Khuyến mãi đặc biệt");
        promotionLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Thêm các khuyến mãi
        VBox promos = new VBox(15);
        promos.setAlignment(Pos.CENTER_LEFT);
        promos.setMaxWidth(800);

        // Khuyến mãi 1
        VBox promo1 = new VBox(5);
        Label promo1Title = new Label("Mua 1 tặng 1 - Thứ Hai hàng tuần");
        promo1Title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label promo1Desc = new Label("Áp dụng cho tất cả các loại cà phê từ 8h-11h sáng");
        promo1.getChildren().addAll(promo1Title, promo1Desc);

        // Khuyến mãi 2
        VBox promo2 = new VBox(5);
        Label promo2Title = new Label("Giảm 20% - Sinh nhật khách hàng");
        promo2Title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label promo2Desc = new Label("Xuất trình CMND/CCCD trong ngày sinh nhật để hưởng ưu đãi");
        promo2.getChildren().addAll(promo2Title, promo2Desc);

        // Khuyến mãi 3
        VBox promo3 = new VBox(5);
        Label promo3Title = new Label("Tích điểm đổi quà");
        promo3Title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label promo3Desc = new Label("Tích lũy điểm với mỗi giao dịch để đổi lấy đồ uống miễn phí và voucher");
        promo3.getChildren().addAll(promo3Title, promo3Desc);

        promos.getChildren().addAll(promo1, promo2, promo3);
        promotionContent.getChildren().addAll(promotionLabel, promos);

        productsGrid.add(promotionContent, 0, 0);
    }

    // Hiển thị trang giới thiệu
    private void showAboutPage() {
        sidebar.getChildren().clear();
        productsGrid.getChildren().clear();

        VBox aboutContent = new VBox(20);
        aboutContent.setAlignment(Pos.CENTER);

        Label aboutLabel = new Label("Về Coffee Shop");
        aboutLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        VBox aboutText = new VBox(15);
        aboutText.setMaxWidth(800);

        Label storyTitle = new Label("Câu chuyện của chúng tôi");
        storyTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label storyText = new Label("Coffee Shop được thành lập vào năm 2010 với tâm huyết mang đến những tách cà phê chất lượng "
                + "cao nhất cho khách hàng. Chúng tôi trực tiếp làm việc với các nông dân trồng cà phê để đảm bảo "
                + "nguồn nguyên liệu tốt nhất, đồng thời áp dụng các phương pháp rang xay hiện đại nhất.");
        storyText.setWrapText(true);

        Label missionTitle = new Label("Sứ mệnh");
        missionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label missionText = new Label("Sứ mệnh của chúng tôi là tạo ra không gian thân thiện, ấm cúng nơi mọi người "
                + "có thể thưởng thức những tách cà phê hoàn hảo và chia sẻ những khoảnh khắc đáng nhớ cùng nhau.");
        missionText.setWrapText(true);

        aboutText.getChildren().addAll(storyTitle, storyText, missionTitle, missionText);
        aboutContent.getChildren().addAll(aboutLabel, aboutText);

        try {
            ImageView storeImage = new ImageView(new Image("/images/store.jpg"));
            storeImage.setFitWidth(600);
            storeImage.setPreserveRatio(true);
            aboutContent.getChildren().add(storeImage);
        } catch (Exception e) {
            // Bỏ qua nếu không tìm thấy ảnh
        }

        productsGrid.add(aboutContent, 0, 0);
    }

    // Hiển thị trang liên hệ
    private void showContactPage() {
        sidebar.getChildren().clear();
        productsGrid.getChildren().clear();

        VBox contactContent = new VBox(20);
        contactContent.setAlignment(Pos.CENTER);

        Label contactLabel = new Label("Liên hệ");
        contactLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        VBox contactInfo = new VBox(15);
        contactInfo.setMaxWidth(800);

        Label addressTitle = new Label("Địa chỉ");
        addressTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label addressText = new Label("123 Đường Nguyễn Huệ, Quận 1, TP. Hồ Chí Minh");

        Label phoneTitle = new Label("Điện thoại");
        phoneTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label phoneText = new Label("(028) 1234 5678");

        Label emailTitle = new Label("Email");
        emailTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label emailText = new Label("info@coffeeshop.com");

        Label hoursTitle = new Label("Giờ mở cửa");
        hoursTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label hoursText = new Label("Thứ Hai - Chủ Nhật: 7:00 - 22:00");

        contactInfo.getChildren().addAll(
                addressTitle, addressText,
                phoneTitle, phoneText,
                emailTitle, emailText,
                hoursTitle, hoursText
        );

        contactContent.getChildren().addAll(contactLabel, contactInfo);

        productsGrid.add(contactContent, 0, 0);
    }

    // Tạo card hiển thị thông tin sản phẩm
    private VBox createProductCard(Product product) {
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        card.setPrefWidth(300);

        // Ảnh sản phẩm
        try {
            ImageView productImage = new ImageView(UploadImage.loadImage(product.getImgSrc()));
            productImage.setFitWidth(200);
            productImage.setFitHeight(200);
            productImage.setPreserveRatio(true);
            card.getChildren().add(productImage);
        } catch (Exception e) {
            // Nếu không tìm thấy ảnh, hiển thị placeholder
            StackPane imagePlaceholder = new StackPane();
            imagePlaceholder.setPrefSize(200, 200);
            imagePlaceholder.setStyle("-fx-background-color: #f0f0f0;");
            Label placeholderText = new Label("Hình ảnh");
            placeholderText.setStyle("-fx-text-fill: #aaaaaa;");
            imagePlaceholder.getChildren().add(placeholderText);
            card.getChildren().add(imagePlaceholder);
        }

        // Tên sản phẩm
        Label nameLabel = new Label(product.getProduct_name());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        // Giá sản phẩm
        Label priceLabel = new Label(String.format("%,.0f VNĐ", product.getPrice()));
        priceLabel.setStyle("-fx-text-fill: #5D4037; -fx-font-size: 16px;");

        // Mô tả sản phẩm
        Label descLabel = new Label(product.getDescription());
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 14px;");

        // Container cho nút và bộ chọn số lượng
        HBox orderContainer = new HBox(10);
        orderContainer.setAlignment(Pos.CENTER);

        // Nút thêm vào giỏ hàng
        Button orderBtn = new Button("Thêm vào giỏ");
        orderBtn.setStyle("-fx-background-color: #5D4037; -fx-text-fill: white;-fx-font-size: 14px;");
        orderBtn.setPrefWidth(150);

        // Bộ chọn số lượng
        HBox quantitySelector = new HBox(0);
        quantitySelector.setStyle("-fx-border-color: #CCCCCC; -fx-border-radius: 3px;");

        // Nút giảm số lượng
        Button decreaseBtn = new Button("-");
        decreaseBtn.setStyle("-fx-background-color: #EEEEEE; -fx-text-fill: #5D4037; -fx-min-width: 30px; -fx-min-height: 30px; -fx-font-weight: bold;");

        // Hiển thị số lượng
        Label quantityLabel = new Label("1");
        quantityLabel.setAlignment(Pos.CENTER);
        quantityLabel.setPrefWidth(40);
        quantityLabel.setPrefHeight(30);
        quantityLabel.setStyle("-fx-alignment: center; -fx-background-color: white; -fx-text-fill: #5D4037; -fx-font-weight: bold;");

        // Nút tăng số lượng
        Button increaseBtn = new Button("+");
        increaseBtn.setStyle("-fx-background-color: #EEEEEE; -fx-text-fill: #5D4037; -fx-min-width: 30px; -fx-min-height: 30px; -fx-font-weight: bold;");

        // Thêm các thành phần vào bộ chọn số lượng
        quantitySelector.getChildren().addAll(decreaseBtn, quantityLabel, increaseBtn);

        // Xử lý sự kiện cho nút giảm
        AtomicInteger quantity = new AtomicInteger(1);
        decreaseBtn.setOnAction(e -> {
            if (quantity.get() > 1) {
                quantity.decrementAndGet();
                quantityLabel.setText(String.valueOf(quantity.get()));
            }
        });

        // Xử lý sự kiện cho nút tăng
        increaseBtn.setOnAction(e -> {
            if (quantity.get() < 100) {
                quantity.incrementAndGet();
                quantityLabel.setText(String.valueOf(quantity.get()));
            }
        });

        // Xử lý sự kiện đặt hàng với số lượng
        orderBtn.setOnAction(e -> {
            handleOrderProduct(product, quantity.get());
        });

        // Thêm nút và bộ chọn số lượng vào container
        orderContainer.getChildren().addAll(orderBtn, quantitySelector);

        card.getChildren().addAll(nameLabel, priceLabel, descLabel, orderContainer);

        return card;
    }

    // Xử lý sự kiện đặt hàng
    private void handleOrderProduct(Product product, int quantity) {
        try {
            Cart_Product cartProduct = cartProductService.getProductInCart(SaveAccountUtils.cart_id , product.getProduct_id()) ;
            // cập nhật số lượng nếu thêm một sản phẩm đã tồn tại trong giỏ thanh toán , còn không thì thêm vào.
            if (cartProduct != null) {
                cartProductService.update(new Cart_Product(SaveAccountUtils.cart_id , product.getProduct_id() , quantity+cartProduct.getQuantity()));
            }
            else cartProductService.add(new Cart_Product(SaveAccountUtils.cart_id, product.getProduct_id(), quantity));
            loadCountCart();
        } catch (Exception e) {

        }
    }

    // reset chọn của các nút .
    private void removeStyleButton() {
        // Reset style cho tất cả các nút
        homeBtn.getStyleClass().remove("active-button");
        menuBtn.getStyleClass().remove("active-button");
        promotionBtn.getStyleClass().remove("active-button");
        aboutBtn.getStyleClass().remove("active-button");
        contactBtn.getStyleClass().remove("active-button");
    }

    // Thiết lập style cho nút được chọn
    private void setActiveButton(Button button) {
        removeStyleButton();

        // Thêm style cho nút được chọn
        button.getStyleClass().add("active-button");
    }

    // đi vào trang giỏ thanh toán  .
    @FXML
    private void handleCart() throws IOException {
        Pages.pageShoppingCart();
        Stage stage = (Stage) homeBtn.getScene().getWindow() ;
        stage.close();
        System.out.println("Cart");
    }

    // mở cài đặt .
    @FXML
    private void handleSetting() {
        removeStyleButton();
        settingsPanel.setVisible(true);
        settingsPanel.setManaged(true);
        System.out.println("Setting");
    }

    // đóng cài đặt .
    private void closeSetting() {
        settingsPanel.setVisible(false);
        settingsPanel.setManaged(false);
    }

    // đăng xuất .
    @FXML
    private void handleLogout() {
        if (AlertInfo.confirmAlert("Bạn có chắc muốn đăng xuất không?")) {
            Stage stage = (Stage) homeBtn.getScene().getWindow();
            stage.close();
            Pages.pageLogin();
        }
    }

    // load cài đặt
    private void loadSetting() {
        try {
            InformationUser informationUser = informationUserService.getInformationUserByAccountId(SaveAccountUtils.account_id);
            if (informationUser != null) {
                fullNameField.setText(informationUser.getFullName());
                emailField.setText(informationUser.getEmail());
                phoneField.setText(informationUser.getPhone());
                addressField.setText(informationUser.getAddress());
                actionProfileBtn.setOnAction(e -> handleEditProfile(informationUser.getInformation_id()));
                actionProfileBtn.setText("Chỉnh sửa thông tin");
            } else {
                actionProfileBtn.setOnAction(e -> handleAddProfile());
                actionProfileBtn.setText("Thêm thông tin");
            }
            if (SaveAccountUtils.loginName != "") currentLoginNameLabel.setText(SaveAccountUtils.loginName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // sửa thông tin .
    @FXML
    private void handleEditProfile(int informationUserId) {
        try {
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            if (!ValidationInformationUser.checkFullName(fullName)) return;
            if (!ValidationInformationUser.checkEmail(email)) return;
            if (!ValidationInformationUser.checkPhone(phone)) return;
            if (!ValidationInformationUser.checkAddress(address)) return;
            informationUserService.update(new InformationUser(informationUserId, fullName, email, phone, address));
            loadSetting();
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật thông tin thành công");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // thêm thông tin .
    @FXML
    private void handleAddProfile() {
        try {
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            if (!ValidationInformationUser.checkFullName(fullName)) return;
            if (!ValidationInformationUser.checkEmail(email)) return;
            if (!ValidationInformationUser.checkPhone(phone)) return;
            if (!ValidationInformationUser.checkAddress(address)) return;
            informationUserService.add(new InformationUser(fullName, email, phone, address , SaveAccountUtils.account_id));
            loadSetting();
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm thông tin thành công");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // thay đổi tên đăng nhập .
    @FXML
    private void handleChangeLoginName() {
        String newLoginName = newLoginNameField.getText().trim();
        if (!ValidationAccount.loginNameUtils(newLoginName) || !ValidationAccount.passwordUtils(confirmPasswordForLoginName.getText().trim()))
            return;
        if (!authService.Login(SaveAccountUtils.loginName, confirmPasswordForLoginName.getText().trim())) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Mật khẩu không đúng");
            return;
        }
        accountService.updateAccount(new Account(SaveAccountUtils.account_id, newLoginName, SaveAccountUtils.password, SaveAccountUtils.role_id));
        SaveAccountUtils.loginName = newLoginName;
        loadSetting();
        AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đổi tên đăng nhập thành công");
        newLoginNameField.clear();
        confirmPasswordForLoginName.clear();
    }

    // thay đổi mật khẩu .
    @FXML
    private void handleChangePassword() {
        String currentPassword = currentPasswordField.getText().trim();
        String newPassword = newPasswordField.getText().trim();
        String confirmPassword = confirmNewPasswordField.getText().trim();
        if (!ValidationAccount.passwordUtils(currentPassword) || !ValidationAccount.passwordUtils(newPassword) || !ValidationAccount.passwordUtils(confirmPassword))
            return;
        if (!newPassword.equals(confirmPassword)) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Mật khẩu không trùng khớp");
            return;
        }
        if (!authService.Login(SaveAccountUtils.loginName, currentPassword)) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Mật khẩu không đúng");
            return;
        }
        accountService.updateAccount(new Account(SaveAccountUtils.account_id, SaveAccountUtils.loginName, newPassword, SaveAccountUtils.role_id));
        SaveAccountUtils.password = newPassword;
        AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đổi mật khẩu thành công");
        currentPasswordField.clear();
        newPasswordField.clear();
        confirmNewPasswordField.clear();
    }

}