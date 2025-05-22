package com.example.javafxapp.Model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Employee {
    private int employee_id ;
    private String fullName ;
    private String phone ;
    private boolean deleted = false  ;
    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public Employee(String fullName, String phone) {
        this.fullName = fullName;
        this.phone = phone;
    }

    public Employee(int employee_id, String fullName, String phone, boolean deleted) {
        this.employee_id = employee_id;
        this.fullName = fullName;
        this.phone = phone;
        this.deleted = deleted;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    public BooleanProperty selectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
}

