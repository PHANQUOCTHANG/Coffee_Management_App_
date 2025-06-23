package com.example.javafxapp.Controller.Admin.WareHouse;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.UploadImage;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.WareHouse;
import com.example.javafxapp.Service.CategoryService;
import com.example.javafxapp.Service.WareHouseService;
import com.example.javafxapp.Validation.ValidationProduct;
import com.example.javafxapp.Validation.ValidationWareHouse;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddWareHouseController {

    @FXML private TextField productNameField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField quantityField;
    @FXML private JFXCheckBox activeCheckBox;
    @FXML private JFXCheckBox inactiveCheckBox;
    @FXML private Label statusLabel;
    @FXML private ImageView imgView;
    @FXML private Button btnPathImg;
    @FXML private JFXButton btnAdd;
    @FXML private JFXButton btnUpload;

    private String imagePath;
    private final WareHouseService wareHouseService = new WareHouseService();
    private final CategoryService categoryService = new CategoryService();
    private WareHouseController warehouseController;

    public void setWarehouseController(WareHouseController controller) {
        this.warehouseController = controller;
    }

    @FXML
    private void initialize() {
        activeCheckBox.setSelected(true);
        categoryComboBox.getItems().addAll("Nguyên liệu" , "Vật dụng");
    }

    @FXML
    private void handleActive(ActionEvent event) {
        if (activeCheckBox.isSelected()) {
            inactiveCheckBox.setSelected(false);
            statusLabel.setText("Hoạt động");
        }
    }

    @FXML
    private void handleInactive(ActionEvent event) {
        if (inactiveCheckBox.isSelected()) {
            activeCheckBox.setSelected(false);
            statusLabel.setText("Không hoạt động");
        }
    }

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Ảnh (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            imgView.setImage(image);
            imagePath = UploadImage.saveImageToFolder(file);
            btnPathImg.setText(imagePath);
        }
    }

    @FXML
    public void addWarehousePost() {
        try {
            String name = productNameField.getText().trim();
            if (wareHouseService.findWareHouseByName(name) != null) {
                AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Tên sản phẩm đã tồn tại trong kho.");
                return;
            }
            if (!ValidationWareHouse.validationName(name)) return;

            String categoryName = categoryComboBox.getValue();
            if (!ValidationProduct.validationCategory(categoryName)) return;

            int quantity = ValidationWareHouse.validationQuantity(quantityField.getText().trim()) ;
            if (quantity == -1) return ;

            boolean status = quantity > 0;
            String imgSrc = btnPathImg.getText();

            WareHouse wareHouse = new WareHouse(name, categoryName, imgSrc, status, quantity, false);
            wareHouseService.addWareHouse(wareHouse);
            warehouseController.loadWarehouses(); // Load lại danh sách
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm sản phẩm kho thành công");

            Stage stage = (Stage) btnAdd.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Không thể thêm sản phẩm kho");
            e.printStackTrace();
        }
    }
}
