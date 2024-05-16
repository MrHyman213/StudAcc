package org.example.controller.report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.DTO.ReportDTO;
import org.example.DTO.TemplatesDTO;
import org.example.controller.GroupController;
import org.example.model.Entry;
import org.example.model.student.ShortStudent;
import org.example.service.RequestService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SingleReportController implements Initializable {

    @FXML
    private ComboBox<Entry> cbDiscipline;

    @FXML
    private ComboBox<ShortStudent> cbStudent;

    @FXML
    private ComboBox<Entry> cbTeacher;

    @FXML
    private ComboBox<String> cbTerm;

    @FXML
    private TextField tfYear;

    @FXML
    private TextField tfProtocol;

    @FXML
    private Label lbName;

    public static ShortStudent selectedStudent;
    public static ObservableList<ShortStudent> studentList;
    public static TemplatesDTO selectedTemplate;
    public static String name;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbName.setText(name);
        initLists();
    }

    @FXML
    void generateAct(ActionEvent event) {
        ReportDTO report = new ReportDTO();
        report.setStudentId(cbStudent.getSelectionModel().getSelectedItem().getId());
        report.setEmployeeIdList(List.of(cbTeacher.getSelectionModel().getSelectedItem().getId()));
        report.setSemester(cbTerm.getSelectionModel().getSelectedItem());
        if (tfYear.getText() != null)
            report.setYearStudy(tfYear.getText());
        if (tfProtocol.getText() != null)
            report.setNumberProtocol(tfProtocol.getText());
        report.setDisciplineId(cbDiscipline.getSelectionModel().getSelectedItem().getId());
        RequestService.generateFile(0, selectedTemplate, report);
    }

    private void initLists() {
        cbTeacher.setItems(FXCollections.observableArrayList(RequestService.getList("employee", 0, false)));
        selectFirst(cbTeacher);
        cbDiscipline.setItems(FXCollections.observableArrayList(RequestService.getList("discipline", GroupController.selectedSpecialization.getId(), true)));
        selectFirst(cbDiscipline);
        cbTerm.setItems(FXCollections.observableArrayList(List.of("первый", "второй")));
        selectFirst(cbTerm);
        cbStudent.setItems(studentList);
        cbStudent.setValue(selectedStudent);
    }

    public <T extends ComboBox> void selectFirst(T box){
        box.getSelectionModel().selectFirst();
    }
}
