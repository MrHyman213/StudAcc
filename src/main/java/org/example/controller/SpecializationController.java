package org.example.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import org.example.model.Entry;
import org.example.service.RequestService;
import org.example.util.EntryContainer;
import org.example.util.WindowManager;
import org.example.util.exception.UnauthorizedException;

import java.net.URL;
import java.util.ResourceBundle;

public class SpecializationController implements Initializable {

    @FXML
    private ListView<Entry> specList;

    @FXML
    private ComboBox<String> cbLists;

    @FXML
    private Button btAdmin;

    @FXML
    void adminPanelAct(ActionEvent event) {

    }

    @FXML
    void moveAct(ActionEvent event) {
        WindowManager.open("move", "Перевод", false, true);
    }

    @FXML
    void exitAct(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void updateList(ActionEvent event) {
        initSpecList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!RequestService.isAdmin())
            btAdmin.setVisible(false);
        initLists();
        initSpecList();
    }

    private void initSpecList(){
        try {
            specList.setItems(FXCollections.observableList(RequestService.getList("spec", 0, false)));
        } catch (UnauthorizedException e){
            WindowManager.close(specList);
            WindowManager.open("login", "Вход", false, false);
        }
        specList.setOnMouseClicked(mouseEvent -> {
            if(specList.getSelectionModel().getSelectedItem() != null && mouseEvent.getClickCount() == 2) {
                GroupController.selectedSpecialization = specList.getSelectionModel().getSelectedItem();
                WindowManager.open("studentList", specList.getSelectionModel().getSelectedItem().getName(), false, true);
            }
        });
    }

    private void initLists(){
        cbLists.setItems(FXCollections.observableArrayList(EntryContainer.getList("titles")));
        cbLists.setOnAction((actionEvent -> {
            String selectedList = cbLists.getSelectionModel().getSelectedItem();
            ListController.selectedList = EntryContainer.getListByListTitle(selectedList);
            ListController.isSub = false;
            WindowManager.open("list", selectedList, false, false);
        }));
    }
}
