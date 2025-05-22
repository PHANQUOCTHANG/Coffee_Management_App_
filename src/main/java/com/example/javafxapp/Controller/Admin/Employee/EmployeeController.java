package com.example.javafxapp.Controller.Admin.Employee;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Model.Employee;
import com.example.javafxapp.Service.EmployeeService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXCheckBox checkBoxAll;
    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, Boolean> checkBoxColumn;
    @FXML
    private TableColumn<Employee, Integer> indexColumn;
    @FXML
    private TableColumn<Employee, String> nameColumn;
    @FXML
    private TableColumn<Employee, String> phoneColumn;
    @FXML
    private TableColumn<Employee, HBox> actionColumn;
    @FXML
    private Label employeeCountLabel, statusLabel;

    private EmployeeService employeeService = new EmployeeService();
    private ObservableList<Employee> employeeList;
    private ObservableList<Employee> filteredList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        employeeList = FXCollections.observableArrayList();
        filteredList = FXCollections.observableArrayList();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());

        setupTableColumns();
        loadEmployees();
    }

    private void setupTableColumns() {
        checkBoxColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        checkBoxColumn.setCellFactory(column -> new TableCell<Employee, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }

                CheckBox checkBox = new CheckBox();
                checkBox.setAlignment(Pos.CENTER);
                setAlignment(Pos.CENTER);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                Employee employee = getTableRow().getItem();
                checkBox.selectedProperty().unbindBidirectional(employee.selectedProperty());
                checkBox.selectedProperty().bindBidirectional(employee.selectedProperty());

                setGraphic(checkBox);
            }
        });

        indexColumn.setCellValueFactory(cellData -> {
            int index = employeeTable.getItems().indexOf(cellData.getValue()) + 1;
            return new SimpleObjectProperty<>(index);
        });
        indexColumn.setStyle("-fx-alignment: CENTER;");

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFullName()));
        nameColumn.setStyle("-fx-alignment: CENTER");

        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
        phoneColumn.setStyle("-fx-alignment: CENTER");

        actionColumn.setCellValueFactory(cellData -> {
            Employee employee = cellData.getValue();
            HBox actionBox = new HBox(10);
            actionBox.setAlignment(Pos.CENTER);

            JFXButton updateButton = new JFXButton("Sửa");
            updateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding : 10px");
            updateButton.setOnAction(event -> updateEmployee(employee));

            JFXButton deleteButton = new JFXButton("Xóa");
            deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding : 10px");
            deleteButton.setOnAction(event -> deleteEmployee(employee));

            actionBox.getChildren().addAll(updateButton, deleteButton);
            return new SimpleObjectProperty<>(actionBox);
        });
    }

    public void loadEmployees() {
        try {
            employeeList.clear();
            employeeList.addAll(employeeService.getAllEmployee());
            filteredList.clear();
            filteredList.addAll(employeeList);
            employeeTable.setItems(filteredList);

            updateDisplayStatus();
        } catch (Exception e) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách nhân viên");
        }
    }

    private void updateDisplayStatus() {
        int totalEmployees = filteredList.size();
        employeeCountLabel.setText("Tổng số nhân viên: " + totalEmployees);
        if (statusLabel != null) statusLabel.setText(totalEmployees > 0 ? "Đã tải " + totalEmployees + " nhân viên" : "Không có nhân viên nào");
    }

    @FXML
    private void checkBoxAll(ActionEvent event) {
        boolean selected = checkBoxAll.isSelected();
        for (Employee employee : employeeList) {
            employee.setSelected(selected);
        }
        employeeTable.refresh();
    }

    @FXML
    private void addEmployee(ActionEvent event) {
        Pages.pageAddEmployee(this);
    }

    private void updateEmployee(Employee employee) {
        Pages.pageUpdateEmployee(employee.getEmployee_id(), this);
    }

    private void deleteEmployee(Employee employee) {
        if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa không?")) {
            try {
                employeeService.deleteEmployee(employee.getEmployee_id());
                loadEmployees();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa thành công");
            } catch (Exception e) {
                e.printStackTrace();
                AlertInfo.showAlert(Alert.AlertType.ERROR, "Thất bại", "Xóa thất bại");
            }
        }
    }

    @FXML
    private void deleteAll(ActionEvent event) {
        List<Employee> selectedEmployees = new ArrayList<>();
        for (Employee employee : filteredList) {
            if (employee.isSelected()) {
                selectedEmployees.add(employee);
            }
        }

        if (selectedEmployees.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Cảnh báo", "Vui lòng chọn ít nhất một nhân viên để xóa");
            return;
        }

        if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa không?")) {
            try {
                for (Employee employee : selectedEmployees) {
                    employeeService.deleteEmployee(employee.getEmployee_id());
                }
                loadEmployees();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa thành công");
            } catch (Exception e) {
                e.printStackTrace();
                AlertInfo.showAlert(Alert.AlertType.ERROR, "Thất bại", "Xóa thất bại");
            }
        }
    }

    private void applyFilters() {
        String searchText = searchField.getText().trim();
        filteredList.clear();
        employeeTable.getItems().clear();
        employeeTable.refresh();

        Collator collator = Collator.getInstance(new Locale("vi", "VN"));
        collator.setStrength(Collator.PRIMARY);

        List<Employee> list = (searchText.isEmpty()) ? employeeList : employeeService.findEmployeeByKeyword(searchText);

        filteredList.addAll(list);
        updateDisplayStatus();
    }

    @FXML
    private void searchEmployee() {
        applyFilters();
    }
}
