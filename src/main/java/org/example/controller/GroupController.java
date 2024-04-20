package org.example.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.example.DTO.student.ShortStudent;
import org.example.service.RequestService;
import org.example.util.EntryContainer;
import org.example.util.WindowManager;

import java.util.List;
import java.util.stream.Collectors;

public class GroupController {

    @FXML
    private ComboBox<String> cbGroup;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<ShortStudent> tvStudents;

    @FXML
    private Label lbCount;

    @FXML
    private TableColumn<ShortStudent, String> clName;

    @FXML
    private TableColumn<ShortStudent, String> clPatronymic;

    @FXML
    private TableColumn<ShortStudent, String> clSurname;

    public static String title;

    public void initialize(){
        initGroups();
        initTable();
    }

    private void initGroups(){
        cbGroup.setItems(FXCollections.observableArrayList(RequestService.getGroupList(title)));
        cbGroup.getSelectionModel().selectFirst();
    }

    private void initTable(){
        tvStudents.setRowFactory(tv -> {
            TableRow<ShortStudent> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    ShortStudent rowData = row.getItem();
                    StudentController.INITIALS = rowData.getInitials();
                    WindowManager.open("studentDetails", rowData.getInitials(), false, true);
                }
            });
            return row;
        });
        clName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        clPatronymic.setCellValueFactory(new PropertyValueFactory<>("patronymic"));
        try {
            List<ShortStudent> list = RequestService.getStudentListByGroupName(cbGroup.getSelectionModel().getSelectedItem());
            setTableItems(list);
            EntryContainer.addList("initList", list.stream().map(ShortStudent::getInitials).collect(Collectors.toList()));
        } catch (NullPointerException ignored){}
        lbCount.setText(String.valueOf(tvStudents.getItems().size()));
    }

    private void setTableItems(List<ShortStudent> studentList){
        tvStudents.setItems(FXCollections.observableArrayList(studentList));
    }

    @FXML
    void isSelected(ActionEvent event) {
        initTable();
    }

    @FXML
    void addAct(ActionEvent event) {
        StudentController.setGroup(cbGroup.getSelectionModel().getSelectedItem());
        WindowManager.open("studentDetails", "Добавление", false, true);
    }

    @FXML
    void cartAct(ActionEvent event) {
        String initials = tvStudents.getSelectionModel().getSelectedItem().getInitials();
        StudentController.INITIALS = initials;
        WindowManager.open("studentDetails", initials, false, true);
    }

    @FXML
    void delAct(ActionEvent event) {

    }

    @FXML
    void moveAct(ActionEvent event) {

    }

    @FXML
    void generateAct(ActionEvent event) {

    }

    @FXML
    void exitAct(ActionEvent event) {
        WindowManager.close(tvStudents);
    }

}
