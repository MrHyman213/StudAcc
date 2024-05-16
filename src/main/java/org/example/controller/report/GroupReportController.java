package org.example.controller.report;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.DTO.ReportDTO;
import org.example.DTO.TemplatesDTO;
import org.example.controller.GroupController;
import org.example.model.Entry;
import org.example.service.RequestService;
import org.example.util.WindowManager;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GroupReportController implements Initializable {

    @FXML
    private ComboBox<Entry> cbDiscipline;
    @FXML
    private ComboBox<Entry> cbEmployee;
    @FXML
    private ComboBox<Entry> cbGroup;
    @FXML
    private ComboBox<String> cbTerm;
    @FXML
    private DatePicker dpEndDate;
    @FXML
    private DatePicker dpStartDate;
    @FXML
    private DatePicker dpDateEvent;
    @FXML
    private TextField tfProtocol;
    @FXML
    private TextField tfYearStudy;
    @FXML
    private ListView<Entry> employeeList;

    @FXML
    private Label lbName;

    private final List<Entry> selectedEmployees = new ArrayList<>();
    private List<Entry> employees = new ArrayList<>();
    public static TemplatesDTO selectedTemplate;
    public static Entry selectedGroup;
    private final int specId = GroupController.selectedSpecialization.getId();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
    }

    public void init(){
        employees = RequestService.getList("employee", 0, false);
        lbName.setText(selectedTemplate.getName());
        cbEmployee.setItems(FXCollections.observableArrayList(employees));
        cbDiscipline.setItems(FXCollections.observableArrayList(RequestService.getList("discipline", specId, true)));
        cbGroup.setItems(FXCollections.observableArrayList(RequestService.getList("group", specId, true)));
        cbGroup.setValue(selectedGroup);
        cbTerm.setItems(FXCollections.observableArrayList(List.of("Первый", "Второй")));
        dpDateEvent.setValue(LocalDate.now());
        dpEndDate.setValue(LocalDate.now());
        dpStartDate.setValue(LocalDate.now());
    }

    @FXML
    void cancelAct(ActionEvent event) {
        WindowManager.close(employeeList);
    }

    @FXML
    void delAct(ActionEvent event) {
        Entry selectedEmployee = employeeList.getSelectionModel().getSelectedItem();
        if(selectedEmployee != null)
            moveItem(selectedEmployees, employees, selectedEmployee);
    }

    @FXML
    void employeeSelectedAct(ActionEvent event) {
        moveItem(employees, selectedEmployees, cbEmployee.getSelectionModel().getSelectedItem());
    }

    @FXML
    void generateAct(ActionEvent event) {
        ReportDTO report = new ReportDTO();
        report.setEmployeeIdList(selectedEmployees.stream().map(Entry::getId).collect(Collectors.toList()));
        report.setDateStart(dpStartDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        report.setDateEnd(dpEndDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        report.setSemester(cbTerm.getSelectionModel().getSelectedItem());
        report.setDateEvent(dpDateEvent.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        report.setNumberProtocol(tfProtocol.getText());
        report.setYearStudy(tfYearStudy.getText());
        RequestService.generateFile(cbGroup.getSelectionModel().getSelectedItem().getId(), selectedTemplate, report);
        //WindowManager.close(employeeList);
    }

    private void moveItem(List<Entry> from, List<Entry> to, Entry entry){
        from.remove(entry);
        to.add(entry);
        cbEmployee.setItems(FXCollections.observableArrayList(employees));
        employeeList.setItems(FXCollections.observableArrayList(selectedEmployees));
    }

}
