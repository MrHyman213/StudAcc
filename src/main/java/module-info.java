module org.example.desktopaccounting {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;

    requires java.net.http;
    requires static lombok;
    requires modelmapper;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    opens org.example to javafx.fxml;
    opens org.example.controller;
    opens org.example.DTO.student.Output;
    opens org.example.controller.lists;
    opens org.example.service;
    opens org.example.DTO;
    opens org.example.util;

    exports org.example;
    opens org.example.DTO.entries;
    opens org.example.DTO.student;
}