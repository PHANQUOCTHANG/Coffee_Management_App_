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
    requires itextpdf;
    requires java.desktop;

    opens com.example.javafxapp to javafx.fxml;
    exports com.example.javafxapp;
    exports com.example.javafxapp.Controller.Admin;
    exports com.example.javafxapp.Controller.Admin.Order to javafx.fxml;
    exports com.example.javafxapp.Controller.Admin.Order.User to javafx.fxml;
    opens com.example.javafxapp.Controller.Admin to javafx.fxml;
    exports com.example.javafxapp.Controller.Client;
    opens com.example.javafxapp.Controller.Client to javafx.fxml;
    opens com.example.javafxapp.Controller.Admin.Order to javafx.fxml;
    opens com.example.javafxapp.Controller.Admin.Order.User to javafx.fxml;
}
