package org.example.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.example.model.Entry;
import org.example.service.RequestService;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectController implements Initializable {

    @FXML
    private ListView<Entry> lvEmployee;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void initList(){
        lvEmployee.setItems(FXCollections.observableArrayList(RequestService.getList("employee", 0, false)));
        lvEmployee.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
    }

    @FXML
    void selectAct(ActionEvent event) {

    }
}
