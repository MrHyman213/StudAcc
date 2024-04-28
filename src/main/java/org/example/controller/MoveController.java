package org.example.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import org.example.service.RequestService;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class MoveController implements Initializable {

    @FXML
    private ListView<String> listIn;

    @FXML
    private ListView<String> listOut;

    @FXML
    void moveAct(ActionEvent event) {
        String in = listIn.getSelectionModel().getSelectedItem();
        String out = listOut.getSelectionModel().getSelectedItem();
        if(!Objects.equals(in, out)){
            if (RequestService.getStudentListByGroupName(in).isEmpty())
                RequestService.moveGroup(out, in);
            else
                error("В группе есть студенты.", "Невозможно перевести студентов в группу, где уже есть студенты.");
        } else
            error("Выбраны одинаковые группы.", "Невозможно перевести студентов в ту же группу.");
    }

    public void error(String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initLists();
    }

    public void initLists(){
        List<String> groupList = RequestService.getGroupList();
        listIn.setItems(FXCollections.observableList(groupList));
        listOut.setItems(FXCollections.observableList(groupList));
    }
}
