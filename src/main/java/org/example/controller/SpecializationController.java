package org.example.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import org.example.DTO.entries.Specialization;
import org.example.service.RequestService;
import org.example.util.EntryContainer;
import org.example.util.WindowManager;
import org.example.util.exception.UnauthorizedException;

import java.util.List;
import java.util.stream.Collectors;

public class SpecializationController {

    @FXML
    private ListView<String> specList;

    @FXML
    private ComboBox<String> cbLists;

    @FXML
    void moveAct(ActionEvent event) {
        WindowManager.open("move", "Перевод", false, true);
    }

    @FXML
    void exitAct(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    public void initialize() {
        cbLists.setItems(FXCollections.observableArrayList(EntryContainer.getList("lists")));
        cbLists.setOnAction((event) -> WindowManager.open(
                "lists/" + EntryContainer.getViewByListName(cbLists.getSelectionModel().getSelectedItem()),
                cbLists.getSelectionModel().getSelectedItem(), false, true));
        try {
            List<Specialization> items = RequestService.getSpecList();
            specList.setItems(FXCollections.observableList(items.stream().map(Specialization::getName).collect(Collectors.toList())));
        } catch (UnauthorizedException e){
            WindowManager.close(specList);
            WindowManager.open("login", "Вход", false, false);
        }
        specList.setOnMouseClicked(mouseEvent -> {
            GroupController.title = specList.getSelectionModel().getSelectedItem();
            WindowManager.open("studentList", specList.getSelectionModel().getSelectedItem(), false, true);
        });
    }
}
