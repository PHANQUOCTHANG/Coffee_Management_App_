module com.example.javafxapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;

    requires com.dlsc.formsfx;
    requires com.jfoenix;
    requires org.controlsfx.controls;
    requires javafx.base;
    requires bcrypt;

    opens com.example.javafxapp to javafx.fxml;
    exports com.example.javafxapp;
    exports com.example.javafxapp.Controller.Admin;
    exports com.example.javafxapp.Controller.User;
    opens com.example.javafxapp.Controller.Admin to javafx.fxml;
    opens com.example.javafxapp.Controller.User to javafx.fxml;
}
