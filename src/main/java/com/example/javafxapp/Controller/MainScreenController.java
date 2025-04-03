package com.example.javafxapp.Controller;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainScreenController {

    @FXML
    private JFXButton btnOverview, btnProducts, btnCategories, btnEmployees, btnOrders,
            btnAccounts, btnRole , btnLogOut;

    @FXML
    private StackPane centerPane;

    private List<JFXButton> menuButtons;

    @FXML
    public void initialize() {
        // Danh sách các nút trong menu
        menuButtons = new ArrayList<>();
        menuButtons.add(btnOverview);
        menuButtons.add(btnProducts);
        menuButtons.add(btnCategories);
        menuButtons.add(btnOrders);
        menuButtons.add(btnAccounts);
        menuButtons.add(btnRole);
        menuButtons.add(btnEmployees);
        menuButtons.add(btnLogOut) ;

        // Mặc định chọn "Tổng Quan"
        setActiveButton(btnOverview);
    }

    public void setActiveButton(JFXButton activeButton) {
        for (JFXButton btn : menuButtons) {
            btn.getStyleClass().remove("selected-button");
        }
        activeButton.getStyleClass().add("selected-button");
    }


    public void loadCenterContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent newContent = loader.load();

            // Xóa nội dung cũ và thêm nội dung mới
            centerPane.getChildren().setAll(newContent);

            // Đảm bảo nội dung mới chiếm hết phần StackPane
            StackPane.setMargin(newContent, new Insets(0, 0, 0, 0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleOverview() {
        System.out.println("Overview button clicked");
        loadCenterContent("/com/example/javafxapp/view/dashboard/dashboard.fxml");
        setActiveButton(btnOverview);
    }

    @FXML
    private void handleProducts() {
        System.out.println("Products button clicked");
        loadCenterContent("/com/example/javafxapp/view/product/products.fxml");
        // Thêm logic chuyển sang trang sản phẩm
        setActiveButton(btnProducts);
    }

    @FXML
    private void handleCategories() {
        System.out.println("Product Categories button clicked");
        // Thêm logic chuyển sang trang danh mục sản phẩm
        setActiveButton(btnCategories);
    }

    @FXML
    private void handleEmployees() {
        System.out.println("Employees button clicked");
        // Thêm logic chuyển sang trang quản lý nhân viên
        setActiveButton(btnEmployees);
    }

    @FXML
    private void handleOrders() {
        System.out.println("Order Management button clicked");
        // Thêm logic chuyển sang trang quản lý đơn hàng
        setActiveButton(btnOrders);
    }

    @FXML
    private void handleAccounts() {
        System.out.println("Account Management button clicked");
        loadCenterContent("/com/example/javafxapp/view/account/account.fxml");
        // Thêm logic chuyển sang trang quản lý tài khoản
        setActiveButton(btnAccounts);
    }

    @FXML
    private void handleRole() {
        System.out.println("Role button clicked");
        // Thêm logic chuyển sang trang phân quyền
        setActiveButton(btnRole);
    }

    @FXML
    private void handleLogOut() {
        System.out.println("LogOut button clicked");
        // Thêm logic log out .
        if (AlertInfo.confirmAlert("Bạn có chắc muốn đăng xuất")) {
            Stage stage = (Stage) btnLogOut.getScene().getWindow() ;
            Pages.pageLogin();
            stage.close();
        };
        setActiveButton(btnLogOut);
    }
}
