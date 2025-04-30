package com.example.javafxapp.Controller.Admin;
// dùng để đưa controller của mainscreen cho controller con
// giúp chuyển đổi fxml(phần center của mainscreen) ngay trong controller con 
public abstract class BaseController {
    protected MainScreenController msc;
    protected void setController(MainScreenController msc){
        this.msc = msc;
    }
}
