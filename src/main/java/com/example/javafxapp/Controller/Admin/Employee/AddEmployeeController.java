package com.example.javafxapp.Controller.Admin.Employee;

import com.example.javafxapp.Controller.Admin.Employee.EmployeeController;
import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Model.Employee;
import com.example.javafxapp.Service.EmployeeService;
import com.example.javafxapp.Validation.ValidationEmployee;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEmployeeController {

    @FXML private TextField employeeNameField;
    @FXML private TextField phoneField;

    @FXML private JFXButton btnAdd;

    private EmployeeService employeeService;
    private EmployeeController employeeController;

    public void setEmployeeController(EmployeeController controller) {
        this.employeeController = controller;
    }

    @FXML
    private void initialize() {
        employeeService = new EmployeeService();
    }

    @FXML
    private void addEmployee() {
        String name = employeeNameField.getText().trim();
        String phone = phoneField.getText().trim();

        if (!ValidationEmployee.validationEmployeeName(name) || !ValidationEmployee.validationPhone(phone)) return;

        try {
            Employee employee = new Employee(name  , phone);
            employeeService.addEmployee(employee);
            employeeController.loadEmployees();
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm nhân viên thành công");

            // Close the window
            Stage stage = (Stage) btnAdd.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm nhân viên");
        }
    }
}
