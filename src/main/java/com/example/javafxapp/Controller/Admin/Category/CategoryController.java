package com.example.javafxapp.Controller.Admin.Category;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Service.CategoryService;
import com.example.javafxapp.Validation.ValidationCategory;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {
    @FXML
    private TextField searchField, categoryNameField;

    @FXML
    private Button btnId;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXCheckBox checkBoxAll;
    private List<JFXCheckBox> checkBoxes;

    @FXML
    private TableView<Category> categoryTable;
    @FXML
    private TableColumn<Category, Boolean> checkBoxColumn;
    @FXML
    private TableColumn<Category, Integer> indexColumn;
    @FXML
    private TableColumn<Category, String> nameColumn;
    @FXML
    private TableColumn<Category, HBox> actionColumn;

    private CategoryService categoryService = new CategoryService();
    private ObservableList<Category> categoryList;
    private ObservableList<Category> filteredList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryList = FXCollections.observableArrayList();
        filteredList = FXCollections.observableArrayList();

        // Thêm listener cho các bộ lọc
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());

        setupTableColumns();
        loadCategories();
    }

    private void setupTableColumns() {
        // Checkbox column
        checkBoxColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        checkBoxColumn.setCellFactory(column -> new TableCell<Category, Boolean>() {
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

                Category category = getTableRow().getItem();
                checkBox.selectedProperty().unbindBidirectional(category.selectedProperty());
                checkBox.selectedProperty().bindBidirectional(category.selectedProperty());

                setGraphic(checkBox);
            }
        });

        // Index column
        indexColumn.setCellValueFactory(cellData -> {
            int index = categoryTable.getItems().indexOf(cellData.getValue()) + 1;
            return new SimpleObjectProperty<>(index);
        });
        indexColumn.setStyle("-fx-alignment: CENTER;");

        // Name column
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategory_name()));
        nameColumn.setStyle("-fx-alignment: CENTER");

        // Action column
        actionColumn.setCellValueFactory(cellData -> {
            Category category = cellData.getValue();
            HBox actionBox = new HBox(10);
            actionBox.setAlignment(Pos.CENTER);

            JFXButton editButton = new JFXButton("Sửa");
            editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            editButton.setOnAction(event -> editCategory(category));

            JFXButton deleteButton = new JFXButton("Xóa");
            deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
            deleteButton.setOnAction(event -> deleteCategory(category));

            actionBox.getChildren().addAll(editButton, deleteButton);
            return new SimpleObjectProperty<>(actionBox);
        });
    }

    public void loadCategories() {
        try {
            categoryList.clear();
            categoryList.addAll(categoryService.getAllCategory());
            filteredList.clear();
            filteredList.addAll(categoryList);
            categoryTable.setItems(filteredList);
        } catch (Exception e) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách danh mục");
        }
    }

    @FXML
    private void checkBoxAll(ActionEvent event) {
        boolean selected = checkBoxAll.isSelected();
        for (Category category : categoryList) {
            category.setSelected(selected);
        }
        categoryTable.refresh();
    }


    @FXML
    private void addCategory(ActionEvent event) {
        Pages.pageAddCategory(this);
    }

    private void editCategory(Category category) {
        Pages.pageUpdateCategory(category.getCategory_id(), this);
    }

    private void deleteCategory(Category category) {
        if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa không ?")) {
            try {
                categoryService.deleteCategory(category.getCategory_id());
                loadCategories();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa thành công");
            } catch (Exception e) {
                e.printStackTrace();
                AlertInfo.showAlert(Alert.AlertType.ERROR, "Thất bại", "Xóa thất bại");
            }
        }
    }

    @FXML
    private void deleteAll(ActionEvent event) {
        // Lấy danh sách các sản phẩm đã chọn
        List<Category> selectedProducts = new ArrayList<>();
        for (Category category : filteredList) {
            if (category.isSelected()) {
                selectedProducts.add(category);
            }
        }

        if (selectedProducts.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Cảnh báo", "Vui lòng chọn ít nhất một danh mục để xóa");
            return;
        }
        System.out.println(selectedProducts);
        if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa không ?")) {
            try {
                for (Category category : selectedProducts) {
                    categoryService.deleteCategory(category.getCategory_id());
                }
                loadCategories();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa thành công");
            } catch (Exception e) {
                e.printStackTrace();
                AlertInfo.showAlert(Alert.AlertType.ERROR, "Thất bại", "Xóa thất bại");
            }
        }
    }

    @FXML
    private void searchCategory() {
        applyFilters();
    }

    // áp dụng lọc .
    private void applyFilters() {
        String searchText = searchField.getText().trim();

        // Xóa hết dữ liệu cũ
        filteredList.clear();
        categoryTable.getItems().clear();
        categoryTable.refresh();

        // Khởi tạo Collator cho tiếng Việt
        Collator collator = Collator.getInstance(new Locale("vi", "VN"));
        collator.setStrength(Collator.PRIMARY); // Bỏ qua phân biệt hoa thường và dấu .

        // nếu có dữ liệu tìm kiếm thì lấy danh sách các sản phẩm theo keyworld đó để duyệt các filter khác ,
        // còn nếu không có dữ liệu tìm kiếm thì lấy theo danh sách tất cả sản phẩm từ cơ sở dữ liệu .
        List<Category> list = (searchText.isEmpty()) ? categoryList : categoryService.findCategoriesByKeyword(searchText);

        filteredList.addAll(list);

//        // Áp dụng sắp xếp nếu có
//        sortProducts();
//
//        // Cập nhật trạng thái hiển thị
//        updateDisplayStatus();
    }

}
