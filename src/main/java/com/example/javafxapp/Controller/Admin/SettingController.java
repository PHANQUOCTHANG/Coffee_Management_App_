package com.example.javafxapp.Controller.Admin;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Model.Account;
import com.example.javafxapp.Service.AccountService;
import com.example.javafxapp.Utils.SaveAccountUtils;
import com.example.javafxapp.Validation.ValidationAccount;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingController implements Initializable {

    // Cài đặt chung
    @FXML private ComboBox<String> languageComboBox;
    @FXML private ToggleButton themeToggle;


    // Cài đặt thông báo
    @FXML private CheckBox pushNotificationCheckBox;
    @FXML private CheckBox emailNotificationCheckBox;
    @FXML private CheckBox soundEffectsCheckBox;

    // Cài đặt quyền riêng tư và bảo mật
    @FXML private ComboBox<String> autoLockComboBox;
    @FXML private CheckBox analyticsCheckBox;

    // Cài đặt hiệu suất
    @FXML private ComboBox<String> performanceModeComboBox;
    @FXML private Slider cacheSizeSlider;
    @FXML private Label cacheSizeLabel;

    // Nút hành động
    @FXML private Button resetButton;
    @FXML private Button cancelButton;
    @FXML private Button saveButton;

    @FXML private TextField accountNameTextField;
    @FXML private Button changeAccountNameButton;
    @FXML private TextField currentPasswordField;
    @FXML private TextField newPasswordField;
    @FXML private TextField confirmPasswordField;

    private SettingsData originalSettings;
    private SettingsData currentSettings;

    private AccountService accountService = new AccountService() ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupInitialValues();
        setupEventHandlers();
        loadSettings();
    }

    private void setupInitialValues() {
        languageComboBox.getItems().addAll("Tiếng Việt", "Tiếng Anh");
        languageComboBox.setValue("Tiếng Việt");

        autoLockComboBox.getItems().addAll("5 phút", "15 phút", "30 phút", "Không bao giờ");
        autoLockComboBox.setValue("15 phút");

        performanceModeComboBox.getItems().addAll("Hiệu suất cao", "Tiết kiệm pin", "Cân bằng");
        performanceModeComboBox.setValue("Cân bằng");

        themeToggle.setSelected(false);
        themeToggle.setText("Chế độ Sáng");

        pushNotificationCheckBox.setSelected(true);
        emailNotificationCheckBox.setSelected(false);
        soundEffectsCheckBox.setSelected(true);
        analyticsCheckBox.setSelected(false);

        cacheSizeSlider.setValue(512.0);
        updateCacheSizeLabel(512.0);

        accountNameTextField.setText(SaveAccountUtils.loginName) ;
    }

    private void setupEventHandlers() {
        themeToggle.setOnAction(e -> {
            if (themeToggle.isSelected()) {
                themeToggle.setText("Chế độ Tối");
                themeToggle.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-background-radius: 20px; -fx-padding: 8px 16px;");
            } else {
                themeToggle.setText("Chế độ Sáng");
                themeToggle.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50; -fx-background-radius: 20px; -fx-padding: 8px 16px;");
            }
        });

        cacheSizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateCacheSizeLabel(newValue.doubleValue());
        });

        languageComboBox.setOnAction(e -> {
            String selectedLanguage = languageComboBox.getValue();
            showInfoAlert("Đã thay đổi ngôn ngữ", "Ngôn ngữ đã chuyển sang: " + selectedLanguage + "\nKhởi động lại ứng dụng để áp dụng thay đổi.");
        });

        performanceModeComboBox.setOnAction(e -> {
            String mode = performanceModeComboBox.getValue();
            updatePerformanceMode(mode);
        });
    }

    private void updateCacheSizeLabel(double value) {
        int cacheSize = (int) value;
        if (cacheSize >= 1024) {
            double gb = cacheSize / 1024.0;
            cacheSizeLabel.setText(String.format("%.1f GB", gb));
        } else {
            cacheSizeLabel.setText(cacheSize + " MB");
        }
    }

    private void updatePerformanceMode(String mode) {
        switch (mode) {
            case "Hiệu suất cao":
                cacheSizeSlider.setValue(1024.0);
                break;
            case "Tiết kiệm pin":
                cacheSizeSlider.setValue(256.0);
                break;
            case "Cân bằng":
            default:
                cacheSizeSlider.setValue(512.0);
                break;
        }
    }

    @FXML
    private void handleSave() {
        try {
            currentSettings = collectCurrentSettings();
            if (validateSettings(currentSettings)) {
                saveSettingsToStorage(currentSettings);
                originalSettings = currentSettings.copy();
                showSuccessAlert("Đã lưu cài đặt", "Cài đặt đã được lưu thành công!");
            }
        } catch (Exception e) {
            showErrorAlert("Lỗi lưu", "Không thể lưu cài đặt: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        if (originalSettings != null) {
            restoreSettings(originalSettings);
            showInfoAlert("Đã hủy thay đổi", "Mọi thay đổi đã được hoàn tác.");
        }
    }

    @FXML
    private void handleReset() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Khôi phục cài đặt");
        alert.setHeaderText("Khôi phục về mặc định");
        alert.setContentText("Bạn có chắc chắn muốn khôi phục tất cả cài đặt về giá trị mặc định không?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                resetToDefaults();
                showInfoAlert("Đã khôi phục", "Tất cả cài đặt đã được khôi phục về mặc định.");
            }
        });
    }

    private SettingsData collectCurrentSettings() {
        SettingsData settings = new SettingsData();
        settings.language = languageComboBox.getValue();
        settings.darkMode = themeToggle.isSelected();
        settings.pushNotifications = pushNotificationCheckBox.isSelected();
        settings.emailNotifications = emailNotificationCheckBox.isSelected();
        settings.soundEffects = soundEffectsCheckBox.isSelected();
        settings.autoLockTime = autoLockComboBox.getValue();
        settings.analytics = analyticsCheckBox.isSelected();
        settings.performanceMode = performanceModeComboBox.getValue();
        settings.cacheSize = (int) cacheSizeSlider.getValue();
        return settings;
    }

    private boolean validateSettings(SettingsData settings) {
        if (settings.cacheSize < 128 || settings.cacheSize > 2048) {
            showErrorAlert("Lỗi xác thực", "Dung lượng bộ nhớ đệm phải nằm trong khoảng từ 128 MB đến 2 GB.");
            return false;
        }
        return true;
    }

    private void saveSettingsToStorage(SettingsData settings) {
        System.out.println("Đang lưu cài đặt: " + settings.toString());
    }

    private void loadSettings() {
        originalSettings = collectCurrentSettings();
    }

    private void restoreSettings(SettingsData settings) {
        languageComboBox.setValue(settings.language);
        themeToggle.setSelected(settings.darkMode);
        pushNotificationCheckBox.setSelected(settings.pushNotifications);
        emailNotificationCheckBox.setSelected(settings.emailNotifications);
        soundEffectsCheckBox.setSelected(settings.soundEffects);
        autoLockComboBox.setValue(settings.autoLockTime);
        analyticsCheckBox.setSelected(settings.analytics);
        performanceModeComboBox.setValue(settings.performanceMode);
        cacheSizeSlider.setValue(settings.cacheSize);
    }

    private void resetToDefaults() {
        languageComboBox.setValue("Tiếng Việt");
        themeToggle.setSelected(false);
        pushNotificationCheckBox.setSelected(true);
        emailNotificationCheckBox.setSelected(false);
        soundEffectsCheckBox.setSelected(true);
        autoLockComboBox.setValue("15 phút");
        analyticsCheckBox.setSelected(false);
        performanceModeComboBox.setValue("Cân bằng");
        cacheSizeSlider.setValue(512.0);
    }

    // Cảnh báo
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class SettingsData {
        public String language = "Tiếng Việt";
        public boolean darkMode = false;
        public boolean autoStart = false;
        public boolean pushNotifications = true;
        public boolean emailNotifications = false;
        public boolean soundEffects = true;
        public String autoLockTime = "15 phút";
        public boolean analytics = false;
        public String performanceMode = "Cân bằng";
        public int cacheSize = 512;

        public SettingsData copy() {
            SettingsData copy = new SettingsData();
            copy.language = this.language;
            copy.darkMode = this.darkMode;
            copy.autoStart = this.autoStart;
            copy.pushNotifications = this.pushNotifications;
            copy.emailNotifications = this.emailNotifications;
            copy.soundEffects = this.soundEffects;
            copy.autoLockTime = this.autoLockTime;
            copy.analytics = this.analytics;
            copy.performanceMode = this.performanceMode;
            copy.cacheSize = this.cacheSize;
            return copy;
        }

        @Override
        public String toString() {
            return "SettingsData{" +
                    "language='" + language + '\'' +
                    ", darkMode=" + darkMode +
                    ", autoStart=" + autoStart +
                    ", pushNotifications=" + pushNotifications +
                    ", emailNotifications=" + emailNotifications +
                    ", soundEffects=" + soundEffects +
                    ", autoLockTime='" + autoLockTime + '\'' +
                    ", analytics=" + analytics +
                    ", performanceMode='" + performanceMode + '\'' +
                    ", cacheSize=" + cacheSize +
                    '}';
        }
    }

    // thay đổi tên tài khoản .
    @FXML public void handleChangeAccountName(){
        try {
            String accountName = accountNameTextField.getText().trim() ;
            if (!ValidationAccount.loginNameUtils(accountName , SaveAccountUtils.account_id)) return ;
            if (accountName.equals(SaveAccountUtils.loginName)) return ;
            accountService.updateAccount(new Account(SaveAccountUtils.account_id , accountName , SaveAccountUtils.password , SaveAccountUtils.role_id));
            AlertInfo.showAlert(AlertType.INFORMATION , "Thành công" , "Cập nhật thành công");
            SaveAccountUtils.loginName = accountName;
            accountNameTextField.setText(accountName);
        }catch (Exception e){
            AlertInfo.showAlert(AlertType.ERROR , "Lỗi" ,  e.getMessage());
        }
    }

    // thay đổi mật khẩu .
    @FXML public void handleChangePassword(){
        try {
            String currentPassword = currentPasswordField.getText().trim() ;
            String newPassword = newPasswordField.getText().trim() ;
            String confirmPassword = confirmPasswordField.getText().trim() ;
            if (!ValidationAccount.passwordUtils(currentPassword) || !ValidationAccount.passwordUtils(newPassword) || !ValidationAccount.passwordUtils(confirmPassword)) return ;
            if (!confirmPassword.equals(newPassword)) {
                AlertInfo.showAlert(AlertType.ERROR , "lỗi" , "Mật khẩu xác nhận không trùng khớp");
                return ;
            }
            accountService.updateAccount(new Account(SaveAccountUtils.account_id , SaveAccountUtils.loginName , newPassword , SaveAccountUtils.role_id));
            AlertInfo.showAlert(AlertType.INFORMATION , "Thành công" , "Cập nhật thành công");
            SaveAccountUtils.password = newPassword;
            currentPasswordField.clear();
            newPasswordField.clear();
            confirmPasswordField.clear();
        }catch (Exception e){
            AlertInfo.showAlert(AlertType.ERROR , "Lỗi" ,  e.getMessage());
        }
    }
}
