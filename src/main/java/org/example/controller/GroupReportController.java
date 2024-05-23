package org.example.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.example.model.Entry;
import org.example.service.RequestService;
import org.example.util.WindowManager;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GroupReportController implements Initializable {

    @FXML
    private ComboBox<Entry> cbDiscipline;

    @FXML
    private TextField tfEmployee;

    @FXML
    private DatePicker dateEvent;

    @FXML
    private CheckBox withoutDate;

    private final int specId = GroupController.selectedSpecialization.getId();
    public static Entry selectedGroup;

    public static List<Entry> employeeList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        withoutDate.setOnAction(actionEvent -> dateEvent.setDisable(withoutDate.isSelected()));

        initList();
    }

    private void initList(){
        cbDiscipline.setItems(FXCollections.observableArrayList(RequestService.getList("discipline", specId, true)));
    }

    @FXML
    void addEmployeeAct(ActionEvent event) {
        try {
            WindowManager.open("selectEmployee", "Список преподавателей", false, true);
            if (employeeList != null && !employeeList.isEmpty()) {
                tfEmployee.setText(tfEmployee.getText());
            }
        } catch (NullPointerException ignored){
        }
    }

    @FXML
    void generateAct(ActionEvent event) {

    }

    @FXML
    void moreDisciplineAct(ActionEvent event) {
        ListController.selectedList = "discipline";
        ListController.isSub = true;
        ListController.idMainItem = specId;
        WindowManager.open("list", "Дисциплины", false, false);
    }

    @FXML
    void moreEmployeeAct(ActionEvent event) {
        ListController.selectedList = "employee";
        ListController.isSub = false;
        WindowManager.open("list", "Преподаватели", false, false);
    }
}
