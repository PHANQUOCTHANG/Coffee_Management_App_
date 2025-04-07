package com.example.javafxapp.Model;

public class Account {
    private int id;
    private String accountName;
    private String password;
    private int roleId;

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
}
