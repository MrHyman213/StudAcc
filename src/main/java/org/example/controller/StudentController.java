package org.example.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.example.model.Entry;
import org.example.model.student.Address;
import org.example.model.student.Student;
import org.example.service.MappingService;
import org.example.service.RequestService;
import org.example.util.EntryContainer;
import org.example.util.WindowManager;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class StudentController implements Initializable {

    @FXML
    private TextField tfHome;

    @FXML
    private ComboBox<Entry> cbDistrict;

    @FXML
    private ComboBox<Entry> cbEducation;

    @FXML
    private TextField tfPhone;

    @FXML
    private TextField tfSnils;

    @FXML
    private ComboBox<String> cbSex;

    @FXML
    private TextField tfSurname;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfStreet;

    @FXML
    private DatePicker dpBirthDate;

    @FXML
    private CheckBox cbAcadem;

    @FXML
    private Label lbGroup;

    @FXML
    private ComboBox<Entry> cbOrderNumber;

    @FXML
    private TextField tfCity;

    @FXML
    private TextField tfGraduationYear;

    @FXML
    private TextField tfCaseNumber;

    @FXML
    private Label lbInitials;

    @FXML
    private ComboBox<Entry> cbRegion;

    @FXML
    private TextField tfPatronymic;

    @FXML
    private TextField tfIndex;

    @FXML
    private CheckBox cbFree;

    @FXML
    private TextField tfFlat;

    @FXML
    private Button btNext;

    @FXML
    private Button btPrev;

    @FXML
    private AnchorPane root;

    public static String INITIALS;
    private static boolean isNew;
    public static Entry selectedGroup;

    public static void setGroup(Entry group){
        isNew = true;
        selectedGroup = group;
    }

    private static List<String> initList;
    private int index;
    private int idAddress = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(!isNew) {
            initList = EntryContainer.getList("initList");
            index = EntryContainer.getIndexByName(initList, INITIALS);
            setValues(RequestService.getStudentByInitials(INITIALS));
        } else {
            isNew = false;
            idAddress = 0;
            lbGroup.setText("Группа: " + selectedGroup.getName());
            lbInitials.setText("Добавление студента");
        }
        initLists();
    }

    @FXML
    void prevAct(ActionEvent event) {
        prev();
    }

    @FXML
    void nextAct(ActionEvent event) {
        next();
    }

    @FXML
    void exitAct(ActionEvent event) {
        WindowManager.close(lbGroup);
    }

    @FXML
    void saveAct(ActionEvent event) {
        Student student = readComponents();
        Address address = student.getAddress();
        if(address != null)
            if (address.getId() != 0)
                RequestService.updateAddress(MappingService.addressToDTO(address), address.getId());
            else
                student.setAddress(new Address(RequestService.createAddress(MappingService.addressToDTO(address))));
        if(!Objects.equals(lbInitials.getText(), "Добавление студента")) {
            RequestService.updateStudent(MappingService.studentToDTO(student), student.getId());
        }
        else
            RequestService.createStudent(MappingService.studentToDTO(student));
        WindowManager.close(lbGroup);
    }

    @FXML
    void regionSelect(ActionEvent event) {
        if(cbRegion.getSelectionModel().getSelectedItem() == null)
            cbDistrict.setItems(FXCollections.observableArrayList(Collections.emptyList()));
        else {
            cbDistrict.setItems(FXCollections.observableArrayList(
                    RequestService.getList("district", cbRegion.getSelectionModel().getSelectedItem().getId(), true)
            ));
            cbDistrict.setValue(null);
        }
    }

    private void initLists(){
        cbOrderNumber.setItems(FXCollections.observableArrayList(
                RequestService.getList("order", 0, false)
        ));
        cbSex.setItems(FXCollections.observableArrayList(EntryContainer.getList("sex")));
        cbEducation.setItems(FXCollections.observableArrayList(
                RequestService.getList("education", 0, false)));
        cbRegion.setItems(FXCollections.observableArrayList(
                RequestService.getList("region", 0, false)
        ));
    }

    private Student readComponents(){
        Student student = new Student();
        try {
            student.setId(EntryContainer.getIdByName("studentList", lbInitials.getText().replace("Студент: ", "")));
        } catch (NullPointerException ignored){}
        student.setGroup(selectedGroup);
        student.setSurname(tfSurname.getText());
        student.setName(tfName.getText());
        student.setPatronymic(tfPatronymic.getText());
        student.setCaseNumber(tfCaseNumber.getText());
        student.setSex(getSex(cbSex.getSelectionModel().getSelectedItem()));
        student.setGraduationYear(Integer.parseInt(tfGraduationYear.getText().replaceAll("[^\\d,]", "")));
        student.setFreeVisit(cbFree.isSelected());
        student.setAkadem(cbAcadem.isSelected());

        student.setSnils(tfSnils.getText());
        student.setBirthDate(dpBirthDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        try {
            student.setAddress(new Address(idAddress,
                    cbDistrict.getSelectionModel().getSelectedItem(), tfCity.getText(),
                    tfStreet.getText(), tfHome.getText(), tfFlat.getText(), tfIndex.getText()));
        } catch (NullPointerException e){
            student.setAddress(null);
        }
        student.setOrderNumber(cbOrderNumber.getSelectionModel().getSelectedItem());
        student.setEducation(cbEducation.getSelectionModel().getSelectedItem());
        student.setPhone(tfPhone.getText());
        return student;
    }

    private void setValues(Student student) {
        INITIALS = student.getInitials();
        if(student.getAddress() != null)
            idAddress = student.getAddress().getId();
        lbInitials.setText("Студент: " + INITIALS);
        lbGroup.setText("Группа: " + student.getGroup().getName());
        tfSurname.setText(student.getSurname());
        tfName.setText(student.getName());
        tfPatronymic.setText(student.getPatronymic());
        cbAcadem.setSelected(student.isAkadem());
        cbFree.setSelected(student.isFreeVisit());
        cbOrderNumber.setValue(student.getOrderNumber());
        try {
            cbSex.setValue(getSex(student.getSex()));
            cbEducation.setValue(student.getEducation());
            dpBirthDate.setValue(LocalDate.parse(student.getBirthDate()));
            tfGraduationYear.setText(String.valueOf(student.getGraduationYear()));
            tfSnils.setText(student.getSnils());
            tfPhone.setText(student.getPhone());
            tfCaseNumber.setText(student.getCaseNumber());
        } catch (NullPointerException ignored){}

        try {
            Address address = student.getAddress();
            if(address != null) {
                cbRegion.setValue(address.getDistrict().getSubEntry());
                cbDistrict.setValue(address.getDistrict());
                tfIndex.setText(address.getIndex());
                tfCity.setText(address.getCity());
                tfStreet.setText(address.getStreet());
                tfHome.setText(address.getHouseNumber());
                tfFlat.setText(address.getFlatNumber());
            } else {
                cbDistrict.setValue(null);
                cbRegion.setValue(null);
                tfIndex.setText("");
                tfCity.setText("");
                tfStreet.setText("");
                tfHome.setText("");
                tfFlat.setText("");
            }
        } catch (NullPointerException ignored) {
            ignored.printStackTrace();
        }
        btNext.setDisable(index == initList.size()-1);
        btPrev.setDisable(index == 0);
    }

    private void next(){
        EntryContainer.getIndexByName(initList, INITIALS);
        setValues(RequestService.getStudentByInitials(initList.get(++index)));
    }

    private void prev(){
        EntryContainer.getIndexByName(initList, INITIALS);
        setValues(RequestService.getStudentByInitials(initList.get(--index)));
    }

    private String getSex(boolean sex){
        if(sex) return "Женский";
        else return "Мужской";
    }

    private Boolean getSex(String sex){
        if(Objects.equals(sex, "Мужской"))
            return false;
        else if (Objects.equals(sex, "Женский"))
            return true;
        else return null;
    }
}
