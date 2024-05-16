module org.example.desktopaccounting {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;

    requires java.net.http;
    requires unirest.java;
    requires static lombok;
    requires modelmapper;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires org.slf4j;

    opens org.example to javafx.fxml;
    opens org.example.controller;
    opens org.example.DTO.student;
    opens org.example.model.student;
    opens org.example.controller.lists;
    opens org.example.service;
    opens org.example.DTO;
    opens org.example.model;
    opens org.example.util;
    opens org.example.controller.report;

    exports org.example;
}