package com.example.javafxapp.Controller.Admin.Product;


import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.ExcelExporter;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Helpper.UploadImage;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.CategoryService;
import com.example.javafxapp.Service.ProductService;
//import com.example.javafxapp.Util.AlertUtil;
//import com.example.javafxapp.Util.ExcelExporter;
//import com.example.javafxapp.Util.FormatUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

public class ProductController implements Initializable {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Boolean> checkBoxColumn;
    @FXML private TableColumn<Product, Integer> indexColumn;
    @FXML private TableColumn<Product, ImageView> imageColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> priceColumn;
    @FXML private TableColumn<Product, String> statusColumn;
    @FXML private TableColumn<Product, String> featuredColumn;
    @FXML private TableColumn<Product, HBox> actionColumn;

    @FXML private JFXCheckBox checkBoxAll;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private TextField searchField;
    @FXML private JFXButton resetButton;
    @FXML private JFXButton exportButton;
    @FXML private Pagination pagination;
    @FXML private Label productCountLabel;
    @FXML private Label statusLabel;

    private ProductService productService;
    private CategoryService categoryService;
    private ObservableList<Product> productList;
    private ObservableList<Product> filteredList;
    private List<Boolean> checkBoxList ;

    private static final int ITEMS_PER_PAGE = 10;
    private int totalPages;

    // mặc định sẽ gọi .
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Khởi tạo services
        productService = new ProductService();
        categoryService = new CategoryService();

        // Khởi tạo danh sách sản phẩm
        productList = FXCollections.observableArrayList();
        filteredList = FXCollections.observableArrayList();

        // Đảm bảo TableView luôn giữ chiều cao tối thiểu
        productTable.setMinHeight(600);

        // Cấu hình các cột cho TableView
        setupTableColumns();

        // Khởi tạo các ComboBox
        setupComboBoxes();

        // Thêm listener cho các bộ lọc
        categoryComboBox.setOnAction(event -> applyFilters());
        statusComboBox.setOnAction(event -> applyFilters());
        sortComboBox.setOnAction(event -> applyFilters());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());

        // Tải dữ liệu sản phẩm
        loadProducts();

        // Cấu hình phân trang
        setupPagination();

        // Cập nhật trạng thái hiển thị
        updateDisplayStatus();
    }

    // khởi tạo các giá trị cho từng cột .
    private void setupTableColumns() {
        // Cột checkbox

        checkBoxColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());

        checkBoxColumn.setCellFactory(column -> new TableCell<Product, Boolean>() {

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }

                CheckBox checkBox = new CheckBox();
                checkBox.setAlignment(Pos.CENTER);
                setAlignment(Pos.CENTER);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                Product product = (Product) getTableRow().getItem();

                // Bỏ lắng nghe cũ để tránh lỗi lặp binding
                checkBox.selectedProperty().unbindBidirectional(product.selectedProperty());

                // Ràng buộc 2 chiều giữa checkbox và dữ liệu trong Product
                checkBox.selectedProperty().bindBidirectional(product.selectedProperty());

                setGraphic(checkBox);
            }
        });



        // Cột STT
        indexColumn.setCellValueFactory(cellData -> {
            int index = productTable.getItems().indexOf(cellData.getValue()) + 1;
            return new SimpleObjectProperty<>(index + (pagination.getCurrentPageIndex() * ITEMS_PER_PAGE));
        });
        indexColumn.setStyle("-fx-alignment: CENTER;");

        // Cột hình ảnh
        imageColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            ImageView imageView = new ImageView();

            // Nếu sản phẩm có hình ảnh
            if (product.getImgSrc() != null && !product.getImgSrc().isEmpty()) {
                File file = new File(product.getImgSrc());
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    imageView.setImage(image);
                } else {
                    // Hình ảnh mặc định nếu không tìm thấy
                    imageView.setImage(new Image(getClass().getResourceAsStream("/Images/product-placeholder.png")));
                }
            } else {
                // Hình ảnh mặc định nếu không có
                imageView.setImage(new Image(getClass().getResourceAsStream("/Images/product-placeholder.png")));
            }

            // Cấu hình hiển thị hình ảnh
            imageView.setFitHeight(80);
            imageView.setFitWidth(80);
            imageView.setPreserveRatio(true);

            return new SimpleObjectProperty<>(imageView);
        });
        imageColumn.setStyle("-fx-alignment: CENTER;");

        // Cột tên sản phẩm
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProduct_name()));
        nameColumn.setStyle("-fx-alignment: CENTER");

        // Cột giá
        priceColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getPrice())));
        priceColumn.setStyle("-fx-alignment: CENTER");

        // Cột trạng thái
        statusColumn.setCellValueFactory(cellData -> {
            boolean active = cellData.getValue().isStatus();
            return new SimpleStringProperty(active ? "Ngừng hoạt động" : "Hoạt động");
        });
        statusColumn.setCellFactory(column -> new TableCell<Product, String>() {
            private final JFXButton button = new JFXButton();
            
            {
                button.getStyleClass().add("status-button");
                button.setMaxWidth(Double.MAX_VALUE);
                button.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    boolean newStatus = !product.isStatus();
                    product.setStatus(newStatus);
                    try {
                        productService.updateProduct(product);
                        product.setStatus(newStatus);
                        button.setText(newStatus ? "Ngừng hoạt động" : "Hoạt động");
                        button.getStyleClass().clear();
                        button.getStyleClass().add("status-button");
                        button.getStyleClass().add(newStatus ? "status-inactive" : "status-active");
                    } catch (Exception e) {
                        AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật trạng thái");
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Product product = getTableView().getItems().get(getIndex());
                    button.setText(item);
                    button.getStyleClass().clear();
                    button.getStyleClass().add("status-button");
                    button.getStyleClass().add(product.isStatus() ? "status-inactive" : "status-active");
                    setGraphic(button);
                }
            }
        });
        statusColumn.setStyle("-fx-alignment: CENTER;");

        // Cột nổi bật
        featuredColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().isOutstanding())));
        featuredColumn.setCellFactory(column -> new TableCell<Product, String>() {
            private final JFXButton button = new JFXButton();
            
            {
                button.getStyleClass().add("featured-button");
                button.setMaxWidth(Double.MAX_VALUE);
                button.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    boolean newFeatured = !product.isOutstanding();
                    product.setOutstanding(newFeatured);
                    try {
                        productService.updateProduct(product);
                        product.setOutstanding(newFeatured);
                        button.setText(newFeatured ? "Có" : "Không");
                        button.getStyleClass().clear();
                        button.getStyleClass().add("featured-button");
                        button.getStyleClass().add(newFeatured ? "featured-yes" : "featured-no");
                    } catch (Exception e) {
                        AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật trạng thái nổi bật");
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Product product = getTableView().getItems().get(getIndex());
                    button.setText(product.isOutstanding() ? "Có" : "Không");
                    button.getStyleClass().clear();
                    button.getStyleClass().add("featured-button");
                    button.getStyleClass().add(product.isOutstanding() ? "featured-yes" : "featured-no");
                    setGraphic(button);
                }
            }
        });
        featuredColumn.setStyle("-fx-alignment: CENTER;");


        // Cột hành động
        actionColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue() ;
            HBox actionBox = new HBox(10);
            actionBox.setAlignment(Pos.CENTER);

            JFXButton updateButton = new JFXButton("Sửa");
            updateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding : 10px ");
            updateButton.setOnAction(event -> updateProduct(product));

            JFXButton deleteButton = new JFXButton("Xóa");
            deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding : 10px");
            deleteButton.setOnAction(event -> deleteProduct(product));

            actionBox.getChildren().addAll(updateButton, deleteButton);
            return new SimpleObjectProperty<>(actionBox);
        });
    }

    // thiết lập dữ liệu của các thanh lọc .
    private void setupComboBoxes() {
        // Khởi tạo ComboBox danh mục
        categoryComboBox.getItems().add("Tất cả danh mục");
        List<Category> categories = categoryService.getAllCategory() ;
        for (Category category : categories) {
            categoryComboBox.getItems().add(category.getCategory_name());
        }
        categoryComboBox.setValue("Tất cả danh mục");

        // Khởi tạo ComboBox trạng thái
        statusComboBox.getItems().addAll("Tất cả trạng thái", "Hoạt động" , "Ngừng hoạt động");
        statusComboBox.setValue("Tất cả trạng thái");

        // Khởi tạo ComboBox sắp xếp
        sortComboBox.getItems().addAll(
                "Mặc định",
                "Tên: A-Z",
                "Tên: Z-A",
                "Giá: Thấp - Cao",
                "Giá: Cao - Thấp",
                "Mới nhất",
                "Cũ nhất"
        );
        sortComboBox.setValue("Mặc định");
    }

    // load sản phẩm  .
    public void loadProducts() {
        try {
            productList.clear();
            productList.addAll(productService.getAllProduct());
            filteredList.clear();
            filteredList.addAll(productList);

            updateDisplayStatus();
            setupPagination();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // phân trang .
    private void setupPagination() {
        totalPages = (int) Math.ceil((double) filteredList.size() / ITEMS_PER_PAGE);
        pagination.setPageCount(Math.max(1, totalPages));
        
        // Tùy chỉnh CSS cho pagination
        pagination.getStyleClass().add("custom-pagination");
        pagination.setStyle("-fx-font-size: 14px;");
        
        // Thêm listener cho sự kiện thay đổi trang
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            updatePageContent(newIndex.intValue());
        });

        // Hiển thị trang đầu tiên và đánh dấu nó
        pagination.setCurrentPageIndex(0);
        updatePageContent(0);
        
        // Thêm CSS cho pagination
        String css = """
            .custom-pagination .pagination-control {
                -fx-font-size: 14px;
            }
            .custom-pagination .pagination-control .button {
                -fx-min-width: 35px;
                -fx-min-height: 35px;
                -fx-background-radius: 5px;
                -fx-background-color: white;
                -fx-border-color: #cccccc;
                -fx-border-radius: 5px;
            }
            .custom-pagination .pagination-control .button:hover {
                -fx-background-color: #f0f0f0;
            }
            .custom-pagination .pagination-control .button:selected {
                -fx-background-color: #007bff;
                -fx-text-fill: white;
                -fx-border-color: #0056b3;
            }
            .custom-pagination .pagination-control .left-arrow, 
            .custom-pagination .pagination-control .right-arrow {
                -fx-min-width: 25px;
                -fx-min-height: 25px;
                -fx-background-radius: 3px;
                -fx-background-color: white;
                -fx-border-color: #cccccc;
                -fx-border-radius: 3px;
            }
            .custom-pagination .pagination-control .left-arrow:hover, 
            .custom-pagination .pagination-control .right-arrow:hover {
                -fx-background-color: #f0f0f0;
            }
            .custom-pagination .pagination-control .left-arrow .arrow,
            .custom-pagination .pagination-control .right-arrow .arrow {
                -fx-background-color: black;
            }
            """;
        pagination.getStylesheets().add("data:text/css," + css);
    }

    // cập nhật phần tử trang .
    private void updatePageContent(int pageIndex) {
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, filteredList.size());

        if (fromIndex < filteredList.size()) {
            ObservableList<Product> pageData = FXCollections.observableArrayList(
                filteredList.subList(fromIndex, toIndex)
            );
            productTable.setItems(pageData);
            productTable.refresh();
            updateDisplayStatus();
        }
    }

    // hiển thị số sản phẩm đang hiển thị .
    private void updateDisplayStatus() {
        int totalItems = filteredList.size();
        productCountLabel.setText("(" + totalItems + " sản phẩm)");

        int currentPage = pagination.getCurrentPageIndex() + 1;
        int fromItem = (pagination.getCurrentPageIndex() * ITEMS_PER_PAGE) + 1;
        int toItem = Math.min(fromItem + ITEMS_PER_PAGE - 1, totalItems);

        if (totalItems == 0) {
            statusLabel.setText("Không có sản phẩm nào");
        } else {
            statusLabel.setText(String.format("Đang hiển thị %d-%d của %d sản phẩm",
                    fromItem, toItem, totalItems));
        }
    }

    // lọc .
    @FXML
    private void filterAction() {
        applyFilters();
    }

    // tìm kiếm .
    @FXML
    private void searchProduct() {
        applyFilters();
    }

    // áp dụng lọc .
    private void applyFilters() {
        String categoryFilter = categoryComboBox.getValue();
        String statusFilter = statusComboBox.getValue();
        String searchText = searchField.getText().trim();

        // Xóa hết dữ liệu cũ
        filteredList.clear();
        productTable.getItems().clear();
        productTable.refresh();

        // Khởi tạo Collator cho tiếng Việt
        Collator collator = Collator.getInstance(new Locale("vi", "VN"));
        collator.setStrength(Collator.PRIMARY); // Bỏ qua phân biệt hoa thường và dấu .

        // nếu có dữ liệu tìm kiếm thì lấy danh sách các sản phẩm theo keyworld đó để duyệt các filter khác ,
        // còn nếu không có dữ liệu tìm kiếm thì lấy theo danh sách tất cả sản phẩm từ cơ sở dữ liệu .
        List<Product> list = (searchText.isEmpty())?productList:productService.findProductsByKeyword(searchText) ;

        // Lọc theo tất cả các điều kiện
        for (Product product : list) {
            Category category = categoryService.findCategoryByID(product.getCategory_id());
            boolean matchCategory = categoryFilter.equals("Tất cả danh mục") ||
                    (category != null && category.getCategory_name().equals(categoryFilter));

            boolean matchStatus = statusFilter.equals("Tất cả trạng thái") ||
                    (statusFilter.equals("Hoạt động") && product.isStatus() == false) ||
                    (statusFilter.equals("Ngừng hoạt động") && product.isStatus() == true);

            if (matchCategory && matchStatus) {
                filteredList.add(product);
            }
        }

        // Áp dụng sắp xếp nếu có
        sortProducts();

        // Cập nhật phân trang
        setupPagination();
        
        // Cập nhật trạng thái hiển thị
        updateDisplayStatus();
    }

    // đặt lại lọc .
    @FXML
    private void resetFilters() {
        // Đặt lại các bộ lọc về giá trị mặc định
        categoryComboBox.setValue("Tất cả danh mục");
        statusComboBox.setValue("Tất cả trạng thái");
        sortComboBox.setValue("Mặc định");
        searchField.clear();

        // Tải lại dữ liệu
        loadProducts();
    }

    // sắp xếp .
    @FXML
    private void sortProducts() {
        String sortOption = sortComboBox.getValue();

        switch (sortOption) {
            case "Tên: A-Z":
                filteredList.sort((p1, p2) -> p1.getProduct_name().compareToIgnoreCase(p2.getProduct_name()));
                break;
            case "Tên: Z-A":
                filteredList.sort((p1, p2) -> p2.getProduct_name().compareToIgnoreCase(p1.getProduct_name()));
                break;
            case "Giá: Thấp - Cao":
                filteredList.sort(Comparator.comparingDouble(Product::getPrice));
                break;
            case "Giá: Cao - Thấp":
                filteredList.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                break;
            case "Cũ nhất":
                filteredList.sort((p1, p2) ->  Integer.compare(p2.getProduct_id(), p1.getProduct_id()));
                break;
            default: // Mặc định
                filteredList.sort(Comparator.comparingInt(Product::getProduct_id));
                break;
        }
    }

    // chọn tất cả .
    @FXML
    private void checkBoxAll() {
        boolean isSelected = checkBoxAll.isSelected();

        // Áp dụng cho tất cả sản phẩm trong danh sách đã lọc
        for (Product product : filteredList) {
            product.setSelected(isSelected);
        }

        // Cập nhật TableView
        productTable.refresh();
    }

    // qua trang thêm .
    @FXML
    private void addProduct() {
        Pages.pageAddProduct(this);
    }

    // qua trang sửa .
    private void updateProduct(Product product) {
        Pages.pageUpdateProduct(product.getProduct_id() , this);
    }

    // xóa một .
    private void deleteProduct(Product product) {

        if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa không?")) {
            try {
                productService.deleteProduct(product.getProduct_id());
                AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Xóa thành công");
                loadProducts();
            } catch (Exception e) {
                AlertInfo.showAlert(Alert.AlertType.ERROR , "Lỗi" , "Xóa thất bại");
            }
        }
    }

    // xóa nhiều cùng lúc .
    @FXML
    private void deleteAll() {
        // Lấy danh sách các sản phẩm đã chọn
        List<Product> selectedProducts = new ArrayList<>() ;
        for (Product product : filteredList) {
            if (product.isSelected()) {
                selectedProducts.add(product) ;
            }
        }

        if (selectedProducts.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR , "Cảnh báo", "Vui lòng chọn ít nhất một sản phẩm để xóa");
            return;
        }
        System.out.println(selectedProducts);
        if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa không ?")) {
            try {
                for (Product product : selectedProducts) {
                    productService.deleteProduct(product.getProduct_id());
                }
                loadProducts();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Xóa thành công");
            }catch(Exception e) {
                e.printStackTrace() ;
                AlertInfo.showAlert(Alert.AlertType.ERROR , "Thất bại" , "Xóa thất bại");
            }
        }
    }

    // xuất excel .
    @FXML
    private void exportData() {
        try {
            ExcelExporter exporter = new ExcelExporter();
            String filePath = exporter.exportProductsToExcel(filteredList);


            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", 
                "Đã xuất dữ liệu sản phẩm thành công.\nĐường dẫn: " + filePath);
        } catch (Exception e) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xuất dữ liệu: " + e.getMessage());
        }
    }
}