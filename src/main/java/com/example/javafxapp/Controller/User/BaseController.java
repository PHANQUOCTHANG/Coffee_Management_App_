package com.example.javafxapp.Controller.User;
// dùng để đưa controller của mainscreen cho controller con
// giúp chuyển đổi fxml(phần center của mainscreen) ngay trong controller con 
public abstract class BaseController {
    protected UserMainScreenController umsc;
    protected int userId;
    public void setUserId(int userId){
        this.userId = userId;
    }
    public void setController(UserMainScreenController umsc){
        this.umsc = umsc;
    }
}
