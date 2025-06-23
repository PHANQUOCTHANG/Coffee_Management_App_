package com.example.javafxapp.Controller.Admin.WareHouse;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.ExcelExporter;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Model.WareHouse;
import com.example.javafxapp.Service.CategoryService;
import com.example.javafxapp.Service.ProductService;
import com.example.javafxapp.Service.WareHouseService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class WareHouseController implements Initializable {

    @FXML private TableView<WareHouse> warehouseTable;
    @FXML private TableColumn<WareHouse, Boolean> checkBoxColumn;
    @FXML private TableColumn<WareHouse, Integer> indexColumn;
    @FXML private TableColumn<WareHouse, ImageView> imageColumn;
    @FXML private TableColumn<WareHouse, String> nameColumn;
    @FXML private TableColumn<WareHouse, String> quantityColumn;
    @FXML private TableColumn<WareHouse, String> statusColumn;
    @FXML private TableColumn<WareHouse, HBox> actionColumn;

    @FXML private JFXCheckBox checkBoxAll;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private TextField searchField;
    @FXML private JFXButton resetButton;
    @FXML private JFXButton exportButton;
    @FXML private JFXButton importButton;
    @FXML private Pagination pagination;
    @FXML private Label warehouseCountLabel;
    @FXML private Label statusLabel;

    private WareHouseService wareHouseService;
    private ObservableList<WareHouse> warehouseList;
    private ObservableList<WareHouse> filteredList;
    private int totalPages;
    private static final int ITEMS_PER_PAGE = 10;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        wareHouseService = new WareHouseService();
        warehouseList = FXCollections.observableArrayList();
        filteredList = FXCollections.observableArrayList();

        setupTableColumns();
        setupComboBoxes();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        sortComboBox.setOnAction(e -> applyFilters());
        statusComboBox.setOnAction(e -> applyFilters());

        loadWarehouses();
    }

    private void setupTableColumns() {
        checkBoxColumn.setCellValueFactory(cell -> cell.getValue().selectedProperty());
        checkBoxColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    CheckBox cb = new CheckBox();
                    cb.setAlignment(Pos.CENTER);
                    setAlignment(Pos.CENTER);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    cb.selectedProperty().unbindBidirectional(getTableRow().getItem().selectedProperty());
                    cb.selectedProperty().bindBidirectional(getTableRow().getItem().selectedProperty());
                    setGraphic(cb);
                }
            }
        });

        indexColumn.setCellValueFactory(cell -> {
            int index = warehouseTable.getItems().indexOf(cell.getValue()) + 1;
            return new SimpleObjectProperty<>(index + (pagination.getCurrentPageIndex() * ITEMS_PER_PAGE));
        });
        indexColumn.setStyle("-fx-alignment: CENTER;");

        imageColumn.setCellValueFactory(cell -> {
            WareHouse item = cell.getValue();
            ImageView iv = new ImageView();

            if (item.getImgSrc() != null && new File(item.getImgSrc()).exists()) {
                iv.setImage(new Image(new File(item.getImgSrc()).toURI().toString()));
            } else {
                iv.setImage(new Image(new File("Images/product-placeholder.png").toURI().toString()));
            }
            iv.setFitWidth(80);
            iv.setFitHeight(80);
            iv.setPreserveRatio(true);
            return new SimpleObjectProperty<>(iv);
        });
        imageColumn.setStyle("-fx-alignment: CENTER;");

        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProductWareHouse_name()));
        nameColumn.setStyle("-fx-alignment: CENTER;");
        quantityColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getQuantity())));
        quantityColumn.setStyle("-fx-alignment: CENTER;");
        statusColumn.setCellValueFactory(cell -> {
            boolean outOfStock = cell.getValue().getQuantity() <= 0;
            return new SimpleStringProperty(outOfStock ? "Hết hàng" : "Còn hàng");
        });
        statusColumn.setStyle("-fx-alignment: CENTER;");
        statusColumn.setCellFactory(column -> new TableCell<>() {
            private final JFXButton button = new JFXButton();

            {
                button.setMaxWidth(Double.MAX_VALUE);
            }

            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    WareHouse item = getTableRow().getItem();
                    boolean out = item.getQuantity() <= 0;
                    button.setText(out ? "Hết hàng" : "Còn hàng");
                    button.getStyleClass().clear();
                    button.getStyleClass().add("status-button");
                    button.getStyleClass().add(out ? "status-inactive" : "status-active");
                    setGraphic(button);
                }
            }
        });

        actionColumn.setCellValueFactory(cell -> {
            WareHouse item = cell.getValue();
            HBox box = new HBox(10);
            box.setAlignment(Pos.CENTER);

            JFXButton edit = new JFXButton("Sửa");
            edit.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
            edit.setOnAction(e -> editWarehouse(item));

            JFXButton delete = new JFXButton("Xóa");
            delete.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
            delete.setOnAction(e -> deleteWarehouse(item));

            box.getChildren().addAll(edit, delete);
            return new SimpleObjectProperty<>(box);
        });
        actionColumn.setStyle("-fx-alignment: CENTER;");
    }

    private void setupComboBoxes() {
        categoryComboBox.getItems().addAll("Nuyên liệu" , "Vật dụng")  ;
        statusComboBox.getItems().addAll("Tất cả", "Còn hàng", "Hết hàng");
        statusComboBox.setValue("Tất cả");

        sortComboBox.getItems().addAll("Mặc định", "Tên: A-Z", "Tên: Z-A", "Số lượng tăng dần", "Số lượng giảm dần");
        sortComboBox.setValue("Mặc định");
    }

    public void loadWarehouses() {
        warehouseList.clear();
        warehouseList.addAll(wareHouseService.getAllWareHouses());
        applyFilters();
    }

    @FXML
    public void filterAction() {applyFilters();}

    @FXML
    public void searchWareHouse() {applyFilters();}

    private void applyFilters() {
        filteredList.clear();
        String keyword = searchField.getText().toLowerCase();
        String status = statusComboBox.getValue();

        for (WareHouse wh : warehouseList) {
            boolean matchStatus = status.equals("Tất cả") ||
                    (status.equals("Còn hàng") && wh.getQuantity() > 0) ||
                    (status.equals("Hết hàng") && wh.getQuantity() <= 0);

            boolean matchKeyword = wh.getProductWareHouse_name().toLowerCase().contains(keyword);

            if (matchStatus && matchKeyword) {
                filteredList.add(wh);
            }
        }

        sortData();
        setupPagination();
    }

    private void sortData() {
        String option = sortComboBox.getValue();
        switch (option) {
            case "Tên: A-Z" -> filteredList.sort(Comparator.comparing(WareHouse::getProductWareHouse_name));
            case "Tên: Z-A" -> filteredList.sort((a, b) -> b.getProductWareHouse_name().compareTo(a.getProductWareHouse_name()));
            case "Số lượng tăng dần" -> filteredList.sort(Comparator.comparingInt(WareHouse::getQuantity));
            case "Số lượng giảm dần" -> filteredList.sort((a, b) -> b.getQuantity() - a.getQuantity());
            default -> {} // giữ nguyên
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
            ObservableList<WareHouse> pageData = FXCollections.observableArrayList(
                    filteredList.subList(fromIndex, toIndex)
            );
            warehouseTable.setItems(pageData);
            warehouseTable.refresh();
            updateDisplayStatus();
        }
    }

    // hiển thị số sản phẩm đang hiển thị .
    private void updateDisplayStatus() {
        int totalItems = filteredList.size();
        warehouseCountLabel.setText("(" + totalItems + " sản phẩm)");

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

    @FXML
    private void checkBoxAll() {
        boolean selected = checkBoxAll.isSelected();
        for (WareHouse item : filteredList) {
            item.setSelected(selected);
        }
        warehouseTable.refresh();
    }

    @FXML
    private void resetFilters() {
        searchField.clear();
        statusComboBox.setValue("Tất cả");
        sortComboBox.setValue("Mặc định");
        loadWarehouses();
    }

    @FXML
    private void exportData() {
        try {
            String filePath = new ExcelExporter().exportWarehousesToExcel(filteredList);
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Xuất Excel", "Xuất thành công: " + filePath);
        } catch (Exception e) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xuất file Excel: " + e.getMessage());
        }
    }

    @FXML
    private void addWarehouse() {
        Pages.pageAddWareHouse(this);
    }

    private void editWarehouse(WareHouse item) {
        Pages.pageUpdateWareHouse(item.getProductWareHouse_id(), this);
    }

    private void deleteWarehouse(WareHouse item) {
        if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa?")) {
            try {
                wareHouseService.deleteWareHouse(item.getProductWareHouse_id());
                loadWarehouses();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa thành công");
            } catch (Exception e) {
                AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Xóa thất bại");
            }
        }
    }

    @FXML
    private void deleteAll() {
        List<WareHouse> selectedItems = filteredList.stream()
                .filter(WareHouse::isSelected)
                .collect(Collectors.toList());

        if (selectedItems.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Chú ý", "Vui lòng chọn sản phẩm để xóa.");
            return;
        }

        if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa các sản phẩm đã chọn?")) {
            try {
                for (WareHouse item : selectedItems) {
                    wareHouseService.deleteWareHouse(item.getProductWareHouse_id());
                }
                loadWarehouses();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa thành công");
            } catch (Exception e) {
                AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Xóa thất bại");
            }
        }
    }

    @FXML
    private void addWarehouseFromExcel () {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn file Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showOpenDialog(importButton.getScene().getWindow());

        if (file != null) {
            try (FileInputStream fis = new FileInputStream(file);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheetAt(0); // sheet đầu tiên
                List<WareHouse> wareHouseList = new ArrayList<>();

                for (int i = 1; i <= sheet.getLastRowNum(); i++) { // bỏ dòng tiêu đề
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        WareHouse item = new WareHouse();
                        item.setProductWareHouse_name(row.getCell(1).getStringCellValue());
                        item.setCategory_name(row.getCell(2).getStringCellValue());
                        int quantity = Integer.parseInt(row.getCell(3).getStringCellValue());
                        item.setQuantity(quantity);
                        item.setStatus(quantity > 0);
                        wareHouseList.add(item);
                    }
                }

                // Lưu vào database
                for (WareHouse wh : wareHouseList) {
                    WareHouse wareHouse = wareHouseService.findWareHouseByName(wh.getProductWareHouse_name()    ) ;
                    // nếu sp đã tồn tại thì tăng số lượng lên  .
                    if (wareHouse != null) {
                        wareHouse.setQuantity(wareHouse.getQuantity() + wh.getQuantity());
                        wareHouseService.updateWareHouse(wareHouse);
                    }
                    // Còn chưa thì thêm vào .
                    else  wareHouseService.addWareHouse(wh);
                    loadWarehouses();
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Nhập dữ liệu thành công!", ButtonType.OK);
                alert.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Lỗi khi đọc file Excel!", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }
}
