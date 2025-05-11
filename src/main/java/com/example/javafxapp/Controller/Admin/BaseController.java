package com.example.javafxapp.Controller.Admin;
// dùng để đưa controller của mainscreen cho controller con

// giúp chuyển đổi fxml(phần center của mainscreen) ngay trong controller con 

import com.example.javafxapp.Utils.LogUtils;
import com.example.javafxapp.Utils.NotificationUtils;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class BaseController implements Initializable {
    protected MainScreenController msc;

    protected void setController(MainScreenController msc) {
        this.msc = msc;
    }

    protected MainScreenController getParentController() {
        return msc;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initializeComponents();
            setupEventHandlers();
            loadData();
            LogUtils.logInfo("Initialized " + this.getClass().getSimpleName());
        } catch (Exception e) {
            LogUtils.logError("Error initializing " + this.getClass().getSimpleName(), e);
            showError("Lỗi khởi tạo", "Không thể khởi tạo giao diện. Vui lòng thử lại sau.");
        }
    }

    // Các phương thức abstract để các controller con phải implement
    protected abstract void initializeComponents();

    protected abstract void setupEventHandlers();

    protected abstract void loadData();

    // Phương thức tiện ích chung
    protected void showError(String title, String message) {
        NotificationUtils.showError(title, message);
        LogUtils.logError(title, new Exception(message));
    }

    protected void showSuccess(String title, String message) {
        NotificationUtils.showSuccess(title, message);
        LogUtils.logInfo(title + ": " + message);
    }

    protected void showWarning(String title, String message) {
        NotificationUtils.showWarning(title, message);
        LogUtils.logWarning(title + ": " + message);
    }

    protected boolean showConfirmation(String title, String message) {
        return NotificationUtils.showConfirmation(title, message);
    }

    protected void refreshData() {
        try {
            loadData();
            LogUtils.logInfo("Refreshed data in " + this.getClass().getSimpleName());
        } catch (Exception e) {
            LogUtils.logError("Error refreshing data in " + this.getClass().getSimpleName(), e);
            showError("Lỗi làm mới dữ liệu", "Không thể làm mới dữ liệu. Vui lòng thử lại sau.");
        }
    }
}
