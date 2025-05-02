package com.example.javafxapp.Controller.Admin;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Employee;
import com.example.javafxapp.Service.CategoryService;
import com.example.javafxapp.Service.EmployeeService;
import com.example.javafxapp.Validation.ValidationCategory;
import com.example.javafxapp.Validation.ValidationEmployee;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class EmployeeController {
    @FXML
    private GridPane grid;
    @FXML
    private TextField searchField , employeeNameField , employeePhoneField ;

    @FXML
    private Button btnId ;
    @FXML
    private JFXButton btnAdd ;
    @FXML
    private JFXCheckBox checkBoxAll ;
    @FXML
    private ComboBox showBox;
    private List<JFXCheckBox> checkBoxes;

    private EmployeeService employeeService = new EmployeeService() ;


    // hàm lấy tất cả nhân viên load ra giao diện .
    public void loadData() {
        grid.getChildren().clear();


       List<Employee> employees = employeeService.getAllEmployee() ;

        if (employees == null || employees.isEmpty()) {
            System.out.println("Không có dữ liệu từ database!");
            return;
        }
        checkBoxes = new ArrayList<>();
        int row = 0 , stt = 1 ;
        for (Employee employee : employees) {
            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.setId(String.valueOf(employee.getEmployee_id()));
            checkBoxes.add(checkBox);

            // STT
            Label lblStt = new Label(String.valueOf(stt++) + '.');


            // Cột tên
            Label lblName = new Label(employee.getFullName());

            // Cột tên
            Label lblPhone = new Label(employee.getPhone());

            // Cột hành động (Button)
            JFXButton btnDetail = new JFXButton("Chi tiết");
            btnDetail.getStyleClass().add("detail-button");
            btnDetail.setOnAction(e -> handleDetail(employee.getEmployee_id())) ;

            // Thêm vào GridPane
            grid.add(checkBox, 0, row);
            grid.add(lblStt, 1, row);
            grid.add(lblName, 2, row);
            grid.add(lblPhone, 3, row);
            grid.add(btnDetail , 4, row);

            row++; // Tăng số hàng

            // Thêm Line phân cách
            Line separator = new Line();
            separator.setStartX(0);
            // Ràng buộc chiều rộng của separator theo chiều rộng của GridPane
            separator.endXProperty().bind(grid.widthProperty());
            separator.setStroke(Color.LIGHTGRAY);
            separator.setStrokeWidth(1);

            grid.add(separator, 0, row, 5, 1);
            row++ ;
        }
        showBox.setValue("Hiển thị " + String.valueOf(employees.size()));
    }


    // mặc định khi đi vào file fxml có fx:controller là controller này thì tự động khởi chạy .
    @FXML
    public void initialize() {
        if (grid != null) loadData();
    }

    //// Trang chi tiết .
    // chi tiết nhân viên .
    private void handleDetail(int employeeId) {
        Pages.pageDetailRole(employeeId);
    }

    // khi chuyển qua trang chi tiết sẽ mặc định gọi .
    @FXML
    public void loadDataDetailEmployee(int employeeId) {
        Employee employee = employeeService.findEmployeeByID(employeeId) ;
        if (employee != null) {
            employeeNameField.setText(employee.getFullName());
            employeePhoneField.setText(employee.getPhone());
            btnId.setText(String.valueOf(employeeId));
            btnId.setVisible(false);
        } else {
            System.out.println("Không tìm thấy!");
        }
    }

    //// Trang thêm .
    // chuyển qua trang thêm 1 nhân viên .
    @FXML
    private void addEmployee() {
        Pages.pageAddRole();
    }

    // thêm nhân viên .
    @FXML
    private void addEmployeePost() {
        try {
            String employeeName = employeeNameField.getText().trim() ;
            String employeePhone = employeePhoneField.getText().trim() ;
            if (!ValidationEmployee.validationEmployeeName(employeeName) || !ValidationEmployee.validationPhone(employeePhone)) return ;
            employeeService.addEmployee(new Employee(employeeName,employeePhone));
            AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Thêm thành công");
            Stage stage = (Stage) btnAdd.getScene().getWindow() ;
            stage.close();
        }
        catch (RuntimeException e) {
            AlertInfo.showAlert(Alert.AlertType.WARNING, "Lỗi", "Thêm thất bại");
            e.printStackTrace();
        }
    }


    // hàm tìm kiếm danh mục bằng tên .
    @FXML
    public void searchEmployee() {
        // Lắng nghe nhập liệu trong searchField
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String textSearch = newValue.trim();
            grid.getChildren().clear();
            if (textSearch.isEmpty()) {
                loadData() ;
                return ;
            }

            // Giả sử đây là method bạn thêm vào CategoryService để trả về nhiều kết quả
            List<Employee> employees = employeeService.findAllByKeyword(textSearch) ;
            if(employees == null || employees.isEmpty()) return ;
            int row = 0 , stt = 1 ;
            for (Employee employee : employees) {
                JFXCheckBox checkBox = new JFXCheckBox();
                checkBox.setId(String.valueOf(employee.getEmployee_id()));
                checkBoxes.add(checkBox);

                // STT
                Label lblStt = new Label(String.valueOf(stt++) + '.');


                // Cột tên
                Label lblName = new Label(employee.getFullName());

                // Cột tên
                Label lblPhone = new Label(employee.getPhone());

                // Cột hành động (Button)
                JFXButton btnDetail = new JFXButton("Chi tiết");
                btnDetail.getStyleClass().add("detail-button");
                btnDetail.setOnAction(e -> handleDetail(employee.getEmployee_id())) ;

                // Thêm vào GridPane
                grid.add(checkBox, 0, row);
                grid.add(lblStt, 1, row);
                grid.add(lblName, 2, row);
                grid.add(lblPhone, 3, row);
                grid.add(btnDetail , 4, row);

                row++; // Tăng số hàng

                // Thêm Line phân cách
                Line separator = new Line();
                separator.setStartX(0);
                // Ràng buộc chiều rộng của separator theo chiều rộng của GridPane
                separator.endXProperty().bind(grid.widthProperty());
                separator.setStroke(Color.LIGHTGRAY);
                separator.setStrokeWidth(1);

                grid.add(separator, 0, row, 5, 1);
                row++ ;
            }
            showBox.setValue("Hiển thị " + String.valueOf(employees.size()));
        });
    }

    // xóa 1 danh mục.
    @FXML
    public void deleteEmployee() {
        try {
            int employeeId = Integer.parseInt(btnId.getText()) ;
            if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa không ?")) {
                employeeService.deleteEmployee(employeeId);
                AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Xóa thành công");
                Stage stage = (Stage)btnId.getScene().getWindow() ;
                stage.close() ;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    // update danh mục .
    @FXML
    public void updateEmployee() {
        try {
            int employee_id = Integer.parseInt(btnId.getText()) ;
            String employeeName = employeeNameField.getText().trim() ;
            String employeePhone = employeePhoneField.getText().trim() ;
            if (!ValidationEmployee.validationEmployeeName(employeeName) || !ValidationEmployee.validationPhone(employeePhone)) return ;
            employeeService.updateEmployee(new Employee(employee_id ,employeeName,employeePhone , false));
            AlertInfo.showAlert(Alert.AlertType.INFORMATION , "Thành công" , "Cập nhật sản phẩm thành công");
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
    }


    // checkBox all
    @FXML
    private void checkBoxAll() {
        for (JFXCheckBox jfxCheckBox : checkBoxes) {
            jfxCheckBox.setSelected(checkBoxAll.isSelected());
        }
    }

    // xóa nhiều đối tượng cùng 1 lúc.
    @FXML
    public void deleteAll() {
        try {
            if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa không ?")) {
                boolean check = false ;
                for (JFXCheckBox checkBox : checkBoxes) {
                    if (checkBox.isSelected()) {
                        check = true ;
                        int employeeId = Integer.parseInt(checkBox.getId());
                        employeeService.deleteEmployee(employeeId);
                    }
                }
                if (!check) {
                    AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng chọn ít nhất 1 nhân viên để xóa");
                    return ;
                }
                loadData();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa thành công");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

}
