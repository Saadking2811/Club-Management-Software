module com.example.demo1 {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.mail;
    requires protobuf.java;
    requires com.google.zxing;
    requires com.jfoenix;
    requires com.google.zxing.javase;
    requires java.desktop;
    requires webcam.capture;
    opens com.club.Controller to javafx.fxml;
    exports com.club.Controller;
}