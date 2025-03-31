module com.example.javafxapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires com.dlsc.formsfx;
    requires javafx.graphics;
    requires com.jfoenix;
    requires org.controlsfx.controls;
    requires javafx.base;

    opens com.example.javafxapp to javafx.fxml;

    exports com.example.javafxapp;
    exports com.example.javafxapp.controller;

    opens com.example.javafxapp.controller to javafx.fxml;
}