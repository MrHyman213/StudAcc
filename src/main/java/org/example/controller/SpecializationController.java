package org.example.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
    private Label lbLists;
    private boolean switcher = true;
    private boolean anotherSwitcher = true;

    @FXML
    void adminPanelAct(ActionEvent ignoredEvent) {

    }

    @FXML
    void moveAct(ActionEvent ignoredEvent) {
        WindowManager.open("move", "Перевод", false, true);
    }

    @FXML
    void exitAct(ActionEvent ignoredEvent) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void updateList(ActionEvent ignoredEvent) {
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
        lbLists.setOnMouseClicked(event -> {
            if (!cbLists.isShowing() && anotherSwitcher) {
                anotherSwitcher = false;
                cbLists.show();
            } else {
                anotherSwitcher = true;
                cbLists.hide();
            }
        });
        cbLists.setItems(FXCollections.observableArrayList(EntryContainer.getList("titles")));
        cbLists.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && switcher) {
                anotherSwitcher = true;
                switcher = false;
                ListController.selectedList = EntryContainer.getListByListTitle(newValue);
                ListController.isSub = false;
                WindowManager.open("list", newValue, false, false);
                Platform.runLater(() -> {
                    cbLists.getSelectionModel().clearSelection();
                    switcher = true;
                });
            }
        });
    }
}
