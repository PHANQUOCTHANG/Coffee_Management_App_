package com.example.javafxapp.Controller.Admin.WareHouse;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.UploadImage;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.WareHouse;
import com.example.javafxapp.Service.CategoryService;
import com.example.javafxapp.Service.WareHouseService;
import com.example.javafxapp.Validation.ValidationWareHouse;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UpdateWareHouseController {

    @FXML private Button btnId;
    @FXML private Button btnCategoryId;
    @FXML private Button btnPathImg;

    @FXML private TextField nameField;
    @FXML private TextField quantityField;
    @FXML private ComboBox<String> categoryComboBox;

    @FXML private JFXCheckBox activeCheckBox;
    @FXML private JFXCheckBox inactiveCheckBox;
    @FXML private Label statusLabel;

    @FXML private ImageView imgView;
    @FXML private JFXButton btnUploadImage;
    @FXML private JFXButton btnUpdate;

    private final WareHouseService wareHouseService = new WareHouseService();
    private final CategoryService categoryService = new CategoryService();

    private String imagePath;
    public static int warehouse_id = -1;
    private WareHouseController wareHouseController;

    public void setWareHouseController(WareHouseController controller) {
        this.wareHouseController = controller;
    }

    @FXML
    public void initialize() {
        try {
            WareHouse wareHouse = wareHouseService.findWareHouseByID(warehouse_id);
            if (wareHouse != null) {
                nameField.setText(wareHouse.getProductWareHouse_name());
                quantityField.setText(String.valueOf(wareHouse.getQuantity()));
                imgView.setImage(UploadImage.loadImage(wareHouse.getImgSrc()));
                btnPathImg.setText(wareHouse.getImgSrc());

                categoryComboBox.setValue(wareHouse.getCategory_name());

                List<String> categories = new ArrayList<>();
                categories.add("Nguyên liệu") ;
                categories.add("Vật dụng") ;
                categoryComboBox.getItems().addAll(categories);

                if (wareHouse.isStatus()) {
                    inactiveCheckBox.setSelected(true);
                    activeCheckBox.setSelected(false);
                    statusLabel.setText("Còn hàng");
                } else {
                    inactiveCheckBox.setSelected(false);
                    activeCheckBox.setSelected(true);
                    statusLabel.setText("Hết hàng");
                }
            } else {
                System.out.println("Không tìm thấy sản phẩm kho!");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleActive(ActionEvent event) {
        if (activeCheckBox.isSelected()) {
            inactiveCheckBox.setSelected(false);
            statusLabel.setText("Còn hàng");
        } else {
            statusLabel.setText("Chọn trạng thái");
        }
    }

    @FXML
    private void handleInactive(ActionEvent event) {
        if (inactiveCheckBox.isSelected()) {
            activeCheckBox.setSelected(false);
            statusLabel.setText("Hết hàng");
        } else {
            statusLabel.setText("Chọn trạng thái");
        }
    }

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh sản phẩm");
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
    public void categoryAction() {
        Category category = categoryService.findCategoryByName(categoryComboBox.getValue());
        btnCategoryId.setText(String.valueOf(category.getCategory_id()));
    }

    @FXML
    public void updateWareHouse() {
        try {
            WareHouse wareHouseFind = wareHouseService.findWareHouseByID(warehouse_id);
            String productName = nameField.getText().trim();
            if (!ValidationWareHouse.validationName(productName)) return;

            int quantity = ValidationWareHouse.validationQuantity(quantityField.getText().trim());
            if (quantity == -1) return;

            if (!ValidationWareHouse.validationCategory(categoryComboBox.getValue())) return;
            String categoryName = categoryComboBox.getValue();

            String imgSrc = btnPathImg.getText();
            boolean status = quantity > 0 ;

            WareHouse updatedWareHouse = new WareHouse(warehouse_id, productName, categoryName, imgSrc, status, quantity, false);
            wareHouseService.updateWareHouse(updatedWareHouse);

            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật kho hàng thành công!");
            wareHouseController.loadWarehouses();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
