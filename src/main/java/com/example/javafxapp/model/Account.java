package com.example.javafxapp.model;

public class Account {
    private int id;
    private String accountName;
    private String password;
    private int roleId;

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

    public int getId() { return id; }
    public String getAccountName() { return accountName; }
    public String getPassword() { return password; }
    public int getRoleId() { return roleId; }

    public void setAccountName(String accountName) { this.accountName = accountName; }
    public void setPassword(String password) { this.password = password; }
    public void setRoleId(int roleId) { this.roleId = roleId; }
}
