package com.example.javafxapp.Controller.Admin;

import com.example.javafxapp.Model.Cart;
import com.example.javafxapp.Model.Role;
import com.example.javafxapp.Service.AccountService;
import com.example.javafxapp.Service.AuthService;
import com.example.javafxapp.Service.CartService;
import com.example.javafxapp.Service.RoleService;
import com.example.javafxapp.Utils.SaveAccountUtils;
import com.example.javafxapp.Validation.ValidationAccount;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
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
    private AccountService accountService = new AccountService() ;
    private RoleService roleService = new RoleService() ;
    private CartService cartSevice = new CartService() ;

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
       try {
           String loginName = loginNameField.getText().trim();
           String password = passWordField.getText().trim();

           if(!ValidationAccount.loginUtils(loginName,password)) return ;
           boolean result = authService.Login(loginName,password);
           System.out.println(result);
           // Nếu đăng nhập thành công .
           if (result) {
               loginNameField.clear();
               passWordField.clear();
               SaveAccountUtils.loginName = loginName ;
               SaveAccountUtils.password = password ;
               Account account = accountService.findAccountByName(loginName);
               SaveAccountUtils.account_id = account.getId() ;
               SaveAccountUtils.role_id = account.getRoleId() ;
               Role role = roleService.findRoleByID(account.getRoleId()) ;
               System.out.println(role.getRole_name());
               if (role.getRole_name().equals("Customer")) {
                   if (cartSevice.checkAccountHaveCart(account.getId()) == null) {
                       Cart cart = new Cart(account.getId()) ;
                       cartSevice.add(cart);
                   }
                   SaveAccountUtils.cart_id = cartSevice.checkAccountHaveCart(account.getId()) ; // id giỏ hàng của tài khoản .
                   Pages.pageUser() ;
               }
               else Pages.pagesMainScreen();
               Stage stage = (Stage) loginNameField.getScene().getWindow() ;
               stage.close();
               AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Đăng nhập thành công");
           } else {
               AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Tên đăng nhập hoặc mật khẩu không đúng.");
           }
       }catch(Exception e) {

       }
    }

    // sign up .
    @FXML
    public void signUp() {
        try {
            String loginName = signUpLoginNameField.getText().trim();
            String password = signUpPassWordField.getText().trim();
            String confirmPassword = confirmPassWordField.getText().trim();

            if (!ValidationAccount.signUpUtils(loginName,password,confirmPassword)) return ;
            Role role = roleService.findRoleByName("Customer") ;
            Account newAccount = new Account(loginName, password, role.getRole_id());
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
        catch(Exception e) {

        }



    }


}

