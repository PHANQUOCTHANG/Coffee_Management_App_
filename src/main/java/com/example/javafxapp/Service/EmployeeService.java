package com.example.javafxapp.Service;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Employee;
import com.example.javafxapp.Repository.EmployeeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    private EmployeeRepository employeeRepository = new EmployeeRepository() ;
    // add employee .
    public void addEmployee(Employee employee) {
        employeeRepository.add(employee);
    }

    // update employee .
    public void updateEmployee(Employee employee) {
        employeeRepository.update(employee);
    }

    // delete employee .
    public void deleteEmployee(int employeeId) {
       employeeRepository.delete(employeeId);
    }

    // getAll employee .
    public List<Employee> getAllEmployee() {
        return employeeRepository.getAll() ;
    }

    // find employee by Id .
    public Employee findEmployeeByID(int employeeId) {
        return employeeRepository.findByID(employeeId) ;
    }

    // find employee by name .
    public Employee findEmployeeByName(String employeeName) {
        return employeeRepository.findByName(employeeName) ;
    }
    // find all employee by keyword .
    public List<Employee> findAllByKeyword(String keyword) {
        return employeeRepository.findAllByKeyword(keyword) ;
    }
}

