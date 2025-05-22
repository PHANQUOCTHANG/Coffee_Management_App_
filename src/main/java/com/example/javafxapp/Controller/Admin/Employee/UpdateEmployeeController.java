package com.example.javafxapp.Controller.Admin.Employee;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Model.Employee;
import com.example.javafxapp.Service.EmployeeService;
import com.example.javafxapp.Validation.ValidationEmployee;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateEmployeeController {

    @FXML private TextField employeeNameField;
    @FXML private TextField employeePhoneField;
    @FXML private JFXButton btnUpdate;

    private EmployeeService employeeService;
    private EmployeeController employeeController;
    public static int employee_id = -1;

    public void setEmployeeController(EmployeeController controller) {
        this.employeeController = controller;
    }

    @FXML
    private void initialize() {
        employeeService = new EmployeeService();
        Employee employee = employeeService.findEmployeeByID(employee_id);
        if (employee != null) {
            employeeNameField.setText(employee.getFullName());
            employeePhoneField.setText(employee.getPhone());
        }
    }

    @FXML
    private void updateEmployee() {
        String name = employeeNameField.getText().trim();
        String phone = employeePhoneField.getText().trim();

        if (!ValidationEmployee.validationEmployeeName(name) || !ValidationEmployee.validationPhone(phone)) return;

        try {
            Employee employee = new Employee(employee_id, name, phone , false);
            employeeService.updateEmployee(employee);
            employeeController.loadEmployees();
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật nhân viên thành công");

            // Close the window
            Stage stage = (Stage) btnUpdate.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật nhân viên");
        }
    }
}
