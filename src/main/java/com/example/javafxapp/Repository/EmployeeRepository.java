package com.example.javafxapp.Repository;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository implements JDBCRepository<Employee> {

    // add employee .
    @Override
    public void add(Employee employee) {
        String sql = "INSERT INTO employee (fullName, phone) VALUES  (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, employee.getFullName());
            stmt.setString(2, employee.getPhone());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // update employee .
    @Override
    public void update(Employee employee) {
        String sql = "UPDATE employee SET fullName = ?, phone = ? WHERE employee_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, employee.getFullName());
            stmt.setString(2, employee.getPhone());
            stmt.setInt(3, employee.getEmployee_id());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // delete employee .
    @Override
    public void delete(int employeeId) {
        String sql = "UPDATE employee SET deleted = TRUE WHERE employee_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);

            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // getAll employee .
    @Override
    public List<Employee> getAll() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee WHERE deleted = FALSE";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("fullName"),
                        rs.getString("phone"),
                        rs.getBoolean("deleted")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // find employee by id .
    @Override
    public Employee findByID(int employeeId) {
        String sql = "SELECT * FROM employee WHERE employee_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("fullName"),
                        rs.getString("phone"),
                        rs.getBoolean("deleted")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // find employee by name .
    @Override
    public Employee findByName(String employeeName) {
        String sql = "SELECT * FROM employee WHERE fullName = ? AND deleted = FALSE";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1,employeeName);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("fullName"),
                        rs.getString("phone"),
                        rs.getBoolean("deleted")
                );
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // find all employee by keyword .
    public List<Employee> findAllByKeyword(String keyword) {
        String sql = "SELECT * FROM employee WHERE fullName LIKE ? and deleted = ?" ;
        List<Employee> list = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1,"%" + keyword + "%");
            stmt.setBoolean(2,false);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("fullName"),
                        rs.getString("phone"),
                        rs.getBoolean("deleted")
                ));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list ;
    }
}
