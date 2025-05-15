package com.example.javafxapp.Model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Account {
    private int id;
    private String accountName;
    private String password;
    private int roleId;
    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public Account() {
    }

    public Account(int id, String accountName, String password, int roleId) {
        this.id = id;
        this.accountName = accountName;
        this.password = password;
        this.roleId = roleId;
    }

    public Account(String accountName, String password, int roleId) {
        this.accountName = accountName;
        this.password = password;
        this.roleId = roleId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
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
