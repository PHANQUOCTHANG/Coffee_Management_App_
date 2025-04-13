package com.example.javafxapp.Controller.User;

public abstract class BaseController {
    protected UserMainScreenController umsc;
    public void setController(UserMainScreenController umsc){
        this.umsc = umsc;
    }
}
