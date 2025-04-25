package com.example.javafxapp.Controller.Admin;

import com.example.javafxapp.Service.AuthService;
import com.example.javafxapp.Utils.ValidationUtils;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.example.javafxapp.Controller.User.UserMainScreenController;
import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Model.Account;
import com.example.javafxapp.Helpper.Pages;
import javafx.scene.control.*;

public class AuthController{
    @FXML
    private TextField loginNameField ;

    @FXML
    private PasswordField passWordField ;

    @FXML
    private TextField signUpLoginNameField ;

    @FXML
    private PasswordField signUpPassWordField ;

    @FXML
    private PasswordField confirmPassWordField ;

    @FXML
    private StackPane authContainer;

    @FXML
    private BorderPane loginPane, signUpPane;

    @FXML
    private Label statusLabel ;

    private boolean isLoginVisible = true; // Biến kiểm tra trạng thái
    private AuthService authService = new AuthService() ;

    @FXML
    public void initialize() {
        // Đẩy Sign Up Pane ra ngoài phải khi bắt đầu
        signUpPane.setTranslateX(authContainer.getWidth());

        // Đảm bảo layout ổn định trước khi chạy hiệu ứng
        authContainer.widthProperty().addListener((obs, oldVal, newVal) -> {
            signUpPane.setTranslateX(newVal.doubleValue());
        });
    }

    @FXML
    public void showSignUp() {
        if (isLoginVisible) {
            TranslateTransition slideOut = new TranslateTransition(Duration.seconds(0.5), loginPane);
            slideOut.setToX(-authContainer.getWidth()); // Trượt Login ra ngoài trái

            TranslateTransition slideIn = new TranslateTransition(Duration.seconds(0.5), signUpPane);
            slideIn.setToX(0); // Đưa Sign Up vào màn hình

            slideOut.play();
            slideIn.play();

            signUpLoginNameField.clear();
            signUpPassWordField.clear();

            isLoginVisible = false;
        }
    }

    @FXML
    public void showLogin() {
        if (!isLoginVisible) {
            TranslateTransition slideOut = new TranslateTransition(Duration.seconds(0.5), signUpPane);
            slideOut.setToX(authContainer.getWidth()); // Trượt Sign Up ra ngoài phải

            TranslateTransition slideIn = new TranslateTransition(Duration.seconds(0.5), loginPane);
            slideIn.setToX(0); // Đưa Login vào màn hình

            slideOut.play();
            slideIn.play();

            signUpLoginNameField.clear();
            signUpPassWordField.clear();
            confirmPassWordField.clear();

            isLoginVisible = true;
        }
    }



    // login .
    @FXML
    public void Login(){
        String loginName = loginNameField.getText().trim();
        String password = passWordField.getText().trim();

        if(!ValidationUtils.loginUtils(loginName,password)) return ;
        boolean result = authService.Login(loginName,password);

        // check account .
        if (result) {
            loginNameField.clear();
            passWordField.clear();
            Stage stage = (Stage) loginNameField.getScene().getWindow() ;
            int userId = authService.getId(loginName);
            String role = authService.getRole(userId);
            if (role.equals("")){
                AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể load phân quyền.");
                return;
            }
            stage.close();
            if (role.equals("User")){
                FXMLLoader loader = Pages.loadPage("/com/example/javafxapp/view/mainScreen/userMainScreen.fxml");
                UserMainScreenController umsc = loader.getController();
                umsc.setUserId(userId);
            }
            else Pages.pagesMainScreen(loginName);
            AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Đăng nhập thành công");
        } else {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Tên đăng nhập hoặc mật khẩu không đúng.");
        }
    }

    // sign up .
    @FXML
    public void signUp() {
        String loginName = signUpLoginNameField.getText().trim();
        String password = signUpPassWordField.getText().trim();
        String confirmPassword = confirmPassWordField.getText().trim();

        if (!ValidationUtils.signUpUtils(loginName,password,confirmPassword)) return ;

        Account newAccount = new Account(loginName, password,2);
        int generatedId = authService.signUp(newAccount);

        if (generatedId != -1) {
            signUpLoginNameField.clear();
            signUpPassWordField.clear();
            confirmPassWordField.clear();
            AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Đăng kí thành công");
            showLogin();
        } else {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Đăng kí thất bại");
        }



    }


}

