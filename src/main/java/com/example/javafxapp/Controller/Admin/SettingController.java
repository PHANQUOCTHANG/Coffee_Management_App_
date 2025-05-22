package com.example.javafxapp.Controller.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

//    @FXML
//    private TextField currentUsernameField;
//
//    @FXML
//    private TextField newUsernameField;
//
//    @FXML
//    private Button saveUsernameButton;
//
//    @FXML
//    private PasswordField currentPasswordField;
//
//    @FXML
//    private PasswordField newPasswordField;
//
//    @FXML
//    private PasswordField confirmPasswordField;
//
//    @FXML
//    private Button savePasswordButton;
//
//    @FXML
//    private ComboBox<String> languageComboBox;
//
//    @FXML
//    private Button saveLanguageButton;
//
//    @FXML
//    private Button cancelButton;
//
//    private String currentUsername;
//    private String currentPassword;
//    private String currentLanguage;
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        // Khởi tạo danh sách ngôn ngữ
//        ObservableList<String> languages = FXCollections.observableArrayList(
//                "Tiếng Việt",
//                "English",
//                "中文 (Chinese)",
//                "日本語 (Japanese)",
//                "한국어 (Korean)"
//        );
//        languageComboBox.setItems(languages);
//
//        // Lấy thông tin người dùng hiện tại từ database hoặc cài đặt
//        loadUserSettings();
//    }
//
//    /**
//     * Phương thức để tải thông tin người dùng hiện tại
//     * Trong ứng dụng thực tế, bạn sẽ lấy dữ liệu từ cơ sở dữ liệu hoặc từ file cài đặt
//     */
//    private void loadUserSettings() {
//        // Code này sẽ được thay thế bằng việc truy vấn database hoặc đọc file cấu hình
//        currentUsername = "admin"; // Giả sử username hiện tại
//        currentPassword = "password123"; // Giả sử password hiện tại
//        currentLanguage = "Tiếng Việt"; // Giả sử ngôn ngữ hiện tại
//
//        // Đặt ngôn ngữ hiện tại vào ComboBox
//        languageComboBox.setValue(currentLanguage);
//    }
//
//    @FXML
//    void handleSaveUsername(ActionEvent event) {
//        String enteredCurrentUsername = currentUsernameField.getText().trim();
//        String newUsername = newUsernameField.getText().trim();
//
//        if (enteredCurrentUsername.isEmpty() || newUsername.isEmpty()) {
//            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Vui lòng điền đầy đủ thông tin");
//            return;
//        }
//
//        if (!enteredCurrentUsername.equals(currentUsername)) {
//            showAlert(Alert.AlertType.ERROR, "Lỗi xác thực", "Tên đăng nhập hiện tại không chính xác");
//            return;
//        }
//
//        if (newUsername.length() < 5) {
//            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Tên đăng nhập mới phải có ít nhất 5 ký tự");
//            return;
//        }
//
//        // Lưu tên đăng nhập mới vào cơ sở dữ liệu hoặc file cấu hình
//        saveNewUsername(newUsername);
//
//        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Tên đăng nhập đã được thay đổi thành công");
//
//        // Cập nhật thông tin hiện tại
//        currentUsername = newUsername;
//
//        // Xóa nội dung trường nhập liệu
//        currentUsernameField.clear();
//        newUsernameField.clear();
//    }
//
//    @FXML
//    void handleSavePassword(ActionEvent event) {
//        String enteredCurrentPassword = currentPasswordField.getText();
//        String newPassword = newPasswordField.getText();
//        String confirmPassword = confirmPasswordField.getText();
//
//        if (enteredCurrentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
//            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Vui lòng điền đầy đủ thông tin");
//            return;
//        }
//
//        if (!enteredCurrentPassword.equals(currentPassword)) {
//            showAlert(Alert.AlertType.ERROR, "Lỗi xác thực", "Mật khẩu hiện tại không chính xác");
//            return;
//        }
//
//        if (newPassword.length() < 8) {
//            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Mật khẩu mới phải có ít nhất 8 ký tự");
//            return;
//        }
//
//        if (!newPassword.equals(confirmPassword)) {
//            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Mật khẩu xác nhận không khớp");
//            return;
//        }
//
//        // Lưu mật khẩu mới vào cơ sở dữ liệu hoặc file cấu hình
//        saveNewPassword(newPassword);
//
//        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Mật khẩu đã được thay đổi thành công");
//
//        // Cập nhật thông tin hiện tại
//        currentPassword = newPassword;
//
//        // Xóa nội dung trường nhập liệu
//        currentPasswordField.clear();
//        newPasswordField.clear();
//        confirmPasswordField.clear();
//    }
//
//    @FXML
//    void handleSaveLanguage(ActionEvent event) {
//        String selectedLanguage = languageComboBox.getValue();
//
//        if (selectedLanguage == null || selectedLanguage.isEmpty()) {
//            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Vui lòng chọn ngôn ngữ");
//            return;
//        }
//
//        // Lưu ngôn ngữ mới vào cơ sở dữ liệu hoặc file cấu hình
//        saveNewLanguage(selectedLanguage);
//
//        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Ngôn ngữ đã được thay đổi thành công");
//
//        // Cập nhật thông tin hiện tại
//        currentLanguage = selectedLanguage;
//    }
//
//    @FXML
//    void handleCancel(ActionEvent event) {
//        // Đóng cửa sổ cài đặt
//        Stage stage = (Stage) cancelButton.getScene().getWindow();
//        stage.close();
//    }
//
//    /**
//     * Phương thức lưu tên đăng nhập mới
//     * Trong ứng dụng thực tế, bạn sẽ lưu dữ liệu vào cơ sở dữ liệu hoặc file cấu hình
//     */
//    private void saveNewUsername(String newUsername) {
//        // Code này sẽ được thay thế bằng việc cập nhật database hoặc file cấu hình
//        System.out.println("Đã lưu tên đăng nhập mới: " + newUsername);
//    }
//
//    /**
//     * Phương thức lưu mật khẩu mới
//     * Trong ứng dụng thực tế, bạn sẽ lưu dữ liệu vào cơ sở dữ liệu hoặc file cấu hình
//     */
//    private void saveNewPassword(String newPassword) {
//        // Code này sẽ được thay thế bằng việc cập nhật database hoặc file cấu hình
//        System.out.println("Đã lưu mật khẩu mới");
//    }
//
//    /**
//     * Phương thức lưu ngôn ngữ mới
//     * Trong ứng dụng thực tế, bạn sẽ lưu dữ liệu vào cơ sở dữ liệu hoặc file cấu hình
//     */
//    private void saveNewLanguage(String newLanguage) {
//        // Code này sẽ được thay thế bằng việc cập nhật database hoặc file cấu hình
//        System.out.println("Đã lưu ngôn ngữ mới: " + newLanguage);
//
//        // Trong ứng dụng thực tế, bạn sẽ cần load lại các ResourceBundle để thay đổi ngôn ngữ
//        // Ví dụ: ResourceBundle.getBundle("messages", new Locale("vi", "VN"));
//    }
//
//    /**
//     * Phương thức hiển thị thông báo
//     */
//    private void showAlert(Alert.AlertType alertType, String title, String content) {
//        Alert alert = new Alert(alertType);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(content);
//        alert.showAndWait();
//    }
}