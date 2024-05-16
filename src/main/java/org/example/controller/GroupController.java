package org.example.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.example.controller.report.ImportController;
import org.example.model.Entry;
import org.example.DTO.TemplatesDTO;
import org.example.model.student.ShortStudent;
import org.example.controller.report.GroupReportController;
import org.example.controller.report.SingleReportController;
import org.example.service.RequestService;
import org.example.util.EntryContainer;
import org.example.util.WindowManager;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GroupController implements Initializable {

    @FXML
    private ComboBox<Entry> cbGroup;

    @FXML
    private ComboBox<TemplatesDTO> cbReport;

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

    public static Entry selectedSpecialization;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initLists();
        initTable();
    }

    private void initLists(){
        try {
            cbReport.setItems(FXCollections.observableArrayList(RequestService.getReportList()));
            cbGroup.setItems(FXCollections.observableArrayList(RequestService.getList("group", selectedSpecialization.getId(), true)));
            cbGroup.getSelectionModel().selectFirst();
        } catch (NullPointerException ignored){}
    }

    private void initTable(){
        tvStudents.setRowFactory(tv -> {
            TableRow<ShortStudent> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    ShortStudent rowData = row.getItem();
                    StudentController.INITIALS = rowData.getInitials();
                    StudentController.selectedGroup = cbGroup.getSelectionModel().getSelectedItem();
                    WindowManager.open("studentDetails", rowData.getInitials(), false, true);
                }
            });
            return row;
        });
        clName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        clPatronymic.setCellValueFactory(new PropertyValueFactory<>("patronymic"));
        try {
            List<ShortStudent> list = RequestService.getStudentListByGroupId(cbGroup.getSelectionModel().getSelectedItem().getId());
            setTableItems(list);
            EntryContainer.addList("initList", list.stream().map(ShortStudent::getInitials).collect(Collectors.toList()));
        } catch (NullPointerException ignored){}
        lbCount.setText(String.valueOf(tvStudents.getItems().size()));
    }

    private void setTableItems(List<ShortStudent> studentList){
        tvStudents.setItems(FXCollections.observableArrayList(studentList));
    }

    @FXML
    void groupSelected(ActionEvent event) {
        initTable();
    }

    @FXML
    void reportSelected(ActionEvent event) {
        TemplatesDTO template = cbReport.getSelectionModel().getSelectedItem();
        if(!template.getDocType()) {
            GroupReportController.selectedTemplate = template;
            GroupReportController.selectedGroup = cbGroup.getSelectionModel().getSelectedItem();
            WindowManager.open("report/forGroup", template.getName(), false, true);
        } else {
            SingleReportController.name = template.getName();
            SingleReportController.selectedStudent = tvStudents.getSelectionModel().getSelectedItem();
            SingleReportController.studentList = tvStudents.getItems();
            SingleReportController.selectedTemplate = cbReport.getSelectionModel().getSelectedItem();
            WindowManager.open("report/single", template.getName(), false, true);
        }
    }

    @FXML
    void updateAct(ActionEvent event) {
        initTable();
    }

    @FXML
    void importAct(ActionEvent event) {
        ImportController.selectedGroup = cbGroup.getSelectionModel().getSelectedItem();
        WindowManager.open("report/import", "Импорт", false, true);
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
        ShortStudent student = tvStudents.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Удаление студента");
        alert.setContentText("Вы уверены что хотите удалить студента?");
        alert.setContentText(String.format("Вы уверены что хотите удалить %s?", student.getInitials()));
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> RequestService.deleteStudent(student.getId()));
    }

    @FXML
    void moveAct(ActionEvent event) {
        ShortStudent selectedStudent = tvStudents.getSelectionModel().getSelectedItem();
        if(selectedStudent != null) {
            MoveController.setStudent(selectedStudent);
            WindowManager.open("move", "Перевод студента", false, true);
        }
    }

    @FXML
    void exitAct(ActionEvent event) {
        WindowManager.close(tvStudents);
    }
}
