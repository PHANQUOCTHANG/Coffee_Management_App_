package com.example.javafxapp.Helpper;

import com.example.javafxapp.Controller.Admin.*;
import com.example.javafxapp.Controller.Admin.Account.AccountController;
import com.example.javafxapp.Controller.Admin.Account.AddAccountController;
import com.example.javafxapp.Controller.Admin.Account.UpdateAccountController;
import com.example.javafxapp.Controller.Admin.Category.AddCategoryController;
import com.example.javafxapp.Controller.Admin.Category.CategoryController;
import com.example.javafxapp.Controller.Admin.Category.UpdateCategoryController;
import com.example.javafxapp.Controller.Admin.Product.AddProductController;
import com.example.javafxapp.Controller.Admin.Product.ProductController;
import com.example.javafxapp.Controller.Admin.Product.UpdateProductController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Pages {

    public Pages() {
    }

    // Chuyển qua page dashboard .
    public static void pagesMainScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource("/com/example/javafxapp/View/Admin/MainScreen/mainScreen.fxml"));
            Pane root = loader.load();
            MainScreenController mainScreenController = loader.getController() ;
            mainScreenController.setAccount();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            // Full màn hình
            stage.setMaximized(true);
            stage.show();
            scene.getStylesheets().add(Pages.class.getResource("/com/example/javafxapp/view/styles/style.css").toExternalForm());

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang Dashboard.");
        }
    }

    // chuyển qua trang người dùng .
    public static void pageUser() {
        try {
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource("/com/example/javafxapp/View/Client/Home/home.fxml"));
            Pane root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            // Full màn hình
            stage.setMaximized(true);
            stage.show();
            scene.getStylesheets().add(Pages.class.getResource("/com/example/javafxapp/view/styles/style.css").toExternalForm());

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang.");
        }
    }


    //// Auth .
    // chuyển qua trang login .
    public static void pageLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource("/com/example/javafxapp/View/Auth/auth.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.getIcons().add(new Image(Pages.class.getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
            stage.setScene(scene);
            stage.setTitle("Coffee Shop Management");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang Login.");
        }
    }

    // chuyển qua trang sign-up .
    public static void pageSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource("/com/example/javafxapp/View/Auth/signup.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.getIcons().add(new Image(Pages.class.getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
            stage.setScene(scene);
            stage.setTitle("Coffee Shop Management");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang Sign up.");
        }
    }


    //// Product .

    // chuyển qua trang thêm mới sản phẩm .
    public static void pageAddProduct(ProductController productController) {
        try {
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource("/com/example/javafxapp/View/Admin/Product/addProduct.fxml"));
            Parent root = loader.load();
            AddProductController controller = loader.getController();
            controller.setProductController(productController);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.getIcons().add(new Image(Pages.class.getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
            stage.setScene(scene);
            stage.setTitle("Coffee Shop Management");
            stage.setResizable(false);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang.");
        }
    }

    // chuyển qua trang chi tiết sản phẩm
    public static void pageUpdateProduct(int productId , ProductController productController) {
        try {
            UpdateProductController.product_id = productId ;
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource("/com/example/javafxapp/View/Admin/Product/updateProduct.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            UpdateProductController controller = loader.getController();
            controller.setProductController(productController);

            Stage stage = new Stage();
            stage.getIcons().add(new Image(Pages.class.getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
            stage.setScene(scene);
            stage.setTitle("Coffee Shop Management");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang.");
        }
    }


    //// Category .

    // chuyển qua trang thêm danh mục
    public static void pageAddCategory(CategoryController categoryController) {
        try {
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource("/com/example/javafxapp/View/Admin/Category/addCategory.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            AddCategoryController controller = loader.getController();
            controller.setCategoryController(categoryController);

            Stage stage = new Stage();
            stage.getIcons().add(new Image(Pages.class.getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
            stage.setScene(scene);
            stage.setTitle("Coffee Shop Management");
            stage.setResizable(true);
            stage.setMaximized(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang.");
        }
    }
    // chuyển qua trang chi tiết damh mục
    public static void pageUpdateCategory(int categoryId , CategoryController categoryController) {
        try {
            UpdateCategoryController.category_id = categoryId ;
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource("/com/example/javafxapp/View/Admin/Category/updateCategory.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            UpdateCategoryController controller = loader.getController();
            controller.setCategoryController(categoryController);


            Stage stage = new Stage();
            stage.getIcons().add(new Image(Pages.class.getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
            stage.setScene(scene);
            stage.setTitle("Coffee Shop Management");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang.");
        }
    }


    //// Employee
    // chuyển qua trang thêm nhân viên .
    public static void pageAddEmployee() {
        try {
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource("/com/example/javafxapp/View/Admin/employee/addEmployee.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.getIcons().add(new Image(Pages.class.getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
            stage.setScene(scene);
            stage.setTitle("Coffee Shop Management");
            stage.setResizable(true);
            stage.setMaximized(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang.");
        }
    }

    // chuyển qua trang chi tiết nhân viên.
    public static void pageDetailEmployee(int employeeId) {
        try {
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource("/com/example/javafxapp/View/Admin/employee/detailEmployee.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Lấy controller của trang chi tiết và truyền ID sản phẩm vào
            EmployeeController controller = loader.getController();
            controller.loadDataDetailEmployee(employeeId);


            Stage stage = new Stage();
            stage.getIcons().add(new Image(Pages.class.getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
            stage.setScene(scene);
            stage.setTitle("Coffee Shop Management");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang.");
        }
    }



    //// Role

    // chuyển qua trang thêm vai trò
    public static void pageAddRole() {
        try {
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource("/com/example/javafxapp/View/Admin/Role/addRole.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);


            Stage stage = new Stage();
            stage.getIcons().add(new Image(Pages.class.getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
            stage.setScene(scene);
            stage.setTitle("Coffee Shop Management");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang.");
        }
    }

    // chuyển qua trang chi tiết vai trò
    public static void pageDetailRole(int roleId) {
        try {
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource("/com/example/javafxapp/View/Admin/Role/detailRole.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Lấy controller của trang chi tiết và truyền ID sản phẩm vào
            RoleController controller = loader.getController();
            controller.loadDataDetailRole(roleId);


            Stage stage = new Stage();
            stage.getIcons().add(new Image(Pages.class.getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
            stage.setScene(scene);
            stage.setTitle("Coffee Shop Management");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang.");
        }
    }

    // chuyển qua trang phân quyền .
    public static void pageRolePermission(int roleId) {
        try {
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource("/com/example/javafxapp/View/Admin/Role/role_permission.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            RoleController controller = loader.getController();
            controller.loadDataRolePermission(roleId);


            Stage stage = new Stage();
            stage.getIcons().add(new Image(Pages.class.getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
            stage.setScene(scene);
            stage.setTitle("Coffee Shop Management");
            stage.setResizable(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang.");
        }
    }

    //// Account .

    // chuyển qua trang thêm
    public static void pageAddAccount(AccountController accountController) {
        try {
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource("/com/example/javafxapp/View/Admin/Account/addAccount.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            AddAccountController controller = loader.getController();
            controller.setAccountController(accountController);

            Stage stage = new Stage();
            stage.getIcons().add(new Image(Pages.class.getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
            stage.setScene(scene);
            stage.setTitle("Coffee Shop Management");
            stage.setResizable(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang.");
        }
    }

    // chuyển qua trang chi tiết
    public static void pageUpdateAccount(int accountId , AccountController accountController) {
        try {
            UpdateAccountController.account_id = accountId ;
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource("/com/example/javafxapp/View/Admin/Account/updateAccount.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            UpdateAccountController controller = loader.getController();
            controller.setAccountController(accountController);


            Stage stage = new Stage();
            stage.getIcons().add(new Image(Pages.class.getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
            stage.setScene(scene);
            stage.setTitle("Coffee Shop Management");
            stage.setResizable(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang.");
        }
    }


    //// Permission .
    // chuyển qua trang thêm permission .
    public static void pageAddPermission() {
        try {
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource("/com/example/javafxapp/View/Admin/Permission/addPermission.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.getIcons().add(new Image(Pages.class.getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
            stage.setScene(scene);
            stage.setTitle("Coffee Shop Management");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang.");
        }
    }

    // chuyển qua trang chi tiết permission .
    public static void pageDetailPermission(int permissionId) {
        try {
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource("/com/example/javafxapp/View/Admin/Permission/detailPermission.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Lấy controller của trang chi tiết và truyền ID sản phẩm vào
            PermissionController controller = loader.getController();
            controller.loadDataDetailPermission(permissionId);


            Stage stage = new Stage();
            stage.getIcons().add(new Image(Pages.class.getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
            stage.setScene(scene);
            stage.setTitle("Coffee Shop Management");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang.");
        }
    }

    // hiện 1 cửa sổ mới
    public static FXMLLoader loadPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.getIcons().add(new Image(Pages.class.getResourceAsStream("/com/example/javafxapp/view/images/icons.jpg")));
            stage.setScene(scene);
            stage.setTitle("Coffee Shop Management");
            stage.setResizable(true);
            stage.show();
            return loader;

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang.");
        }
        return null;
    }
    //// Shopping cart
    public static void pageShoppingCart() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource("/com/example/javafxapp/View/Client/Cart/cart.fxml"));
            Pane root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            // Full màn hình
            stage.setMaximized(true);
            stage.show();
            scene.getStylesheets().add(Pages.class.getResource("/com/example/javafxapp/view/styles/style.css").toExternalForm());

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang Dashboard.");
        }
    }

    //// CheckOut
    public static void pageCheckOut() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(Pages.class.getResource("/com/example/javafxapp/View/Client/Checkout/checkout.fxml"));
            Pane root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            // Full màn hình
            stage.setMaximized(true);
            stage.show();
            scene.getStylesheets().add(Pages.class.getResource("/com/example/javafxapp/view/styles/style.css").toExternalForm());

        } catch (IOException e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở trang .");
        }
    }


}
