package com.example.javafxapp.Model;

public class InformationUser {
    private int information_id ;
    private String fullName ;
    private String email ;
    private String phone ;
    private String address ;
    private int account_id ;
    private boolean deleted = false ;

    public InformationUser(String fullName, String email, String phone, String address , int account_id) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.account_id = account_id ;
    }

    public InformationUser(int information_id, String fullName, String email, String phone, String address) {
        this.information_id = information_id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public InformationUser(int information_id, String fullName, String email, String phone, String address, int account_id, boolean deleted) {
        this.information_id = information_id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.account_id = account_id;
        this.deleted = deleted;
    }

    public int getInformation_id() {
        return information_id;
    }

    public void setInformation_id(int information_id) {
        this.information_id = information_id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
