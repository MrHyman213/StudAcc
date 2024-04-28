package org.example.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.example.DTO.entries.*;
import org.example.DTO.student.Address;
import org.example.DTO.student.Student;
import org.example.service.RequestService;
import org.example.service.MappingService;
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
    private ComboBox<String> cbDistrict;

    @FXML
    private ComboBox<String> cbEducation;

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
    private ComboBox<String> cbOrderNumber;

    @FXML
    private TextField tfCity;

    @FXML
    private TextField tfGraduationYear;

    @FXML
    private TextField tfCaseNumber;

    @FXML
    private Label lbInitials;

    @FXML
    private ComboBox<String> cbRegion;

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
    public static boolean isNew;
    public static String groupName;

    public static void setGroup(String group){
        isNew = true;
        groupName = group;
    }

    private static List<String> initList;
    private int index;
    private int idAddress = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(!isNew) {
            initList = EntryContainer.getList("initList");
            index = EntryContainer.getIndexByName(initList, INITIALS);
            initLists();
            setValues(RequestService.getStudentByInitials(INITIALS));
        } else {
            isNew = false;
            idAddress = 0;
            lbGroup.setText("Группа: " + groupName);
            lbInitials.setText("Добавление студента");
            initLists();
        }
    }

    @FXML
    void prevAct(ActionEvent event) {
        prev();
    }

    @FXML
    void nextAct(ActionEvent event) {
//        tfCaseNumber.setOnKeyPressed(keyEvent -> {
//            if(keyEvent.getCode() == KeyCode.KP_LEFT)
//                prev();
//            if(keyEvent.getCode() == KeyCode.KP_RIGHT)
//                next();
//        });
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
        if(cbRegion.getSelectionModel().getSelectedItem() == null || cbRegion.getSelectionModel().getSelectedItem().equals(""))
            cbDistrict.setItems(FXCollections.observableArrayList(Collections.emptyList()));
        else {
            cbDistrict.setItems(FXCollections.observableArrayList(
                    RequestService.getDistrictListByRegion(cbRegion.getSelectionModel().getSelectedItem())
            ));
            cbDistrict.setValue("");
        }
    }

    private void initLists(){
        cbOrderNumber.setItems(FXCollections.observableArrayList(
                RequestService.getOrderList()
        ));
        cbSex.setItems(FXCollections.observableArrayList(EntryContainer.getList("sex")));
        cbEducation.setItems(FXCollections.observableArrayList(
                RequestService.getEducationList()));
        cbRegion.setItems(FXCollections.observableArrayList(
                RequestService.getRegionList()
        ));
    }

    private Student readComponents(){
        Student student = new Student();
        try {
            student.setId(EntryContainer.getIdByName("studentList", lbInitials.getText().replace("Студент: ", "")));
        } catch (NullPointerException ignored){}
        student.setGroup(new Group(EntryContainer.getIdByName("groupList", lbGroup.getText().replace("Группа: ", "")), lbGroup.getText().replace("Группа: ", "")));
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
        String orderNumber = cbOrderNumber.getSelectionModel().getSelectedItem();
        String education = cbEducation.getSelectionModel().getSelectedItem();
        try {
            String district = cbDistrict.getSelectionModel().getSelectedItem();
            String region = cbRegion.getSelectionModel().getSelectedItem();
            student.setAddress(new Address(idAddress,
                    new District(EntryContainer.getIdByName("districtList", district),
                            district, new Region(EntryContainer.getIdByName("regionList", region), region)),
                    tfCity.getText(), tfStreet.getText(), tfHome.getText(), tfFlat.getText(), tfIndex.getText()));
        } catch (NullPointerException e){
            student.setAddress(null);
        }
        student.setOrderNumber(new OrderNumber(EntryContainer.getIdByName("orderList", orderNumber), orderNumber));
        student.setEducation(new Education(EntryContainer.getIdByName("educationList", education), education));
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
        cbOrderNumber.setValue(student.getOrderNumber().getName());
        try {
            cbSex.setValue(getSex(student.getSex()));
            cbEducation.setValue(student.getEducation().getName());
            dpBirthDate.setValue(LocalDate.parse(student.getBirthDate()));
            tfGraduationYear.setText(String.valueOf(student.getGraduationYear()));
            tfSnils.setText(student.getSnils());
            tfPhone.setText(student.getPhone());
            tfCaseNumber.setText(student.getCaseNumber());
        } catch (NullPointerException ignored){}

        try {
            Address address = student.getAddress();
            if(address != null) {
                String region = address.getDistrict().getRegion().getName();
                cbDistrict.setItems(FXCollections.observableArrayList(
                        RequestService.getDistrictListByRegion(region)
                ));
                cbRegion.setValue(region);
                cbDistrict.setValue(address.getDistrict().getName());
                tfIndex.setText(address.getIndex());
                tfCity.setText(address.getCity());
                tfStreet.setText(address.getStreet());
                tfHome.setText(address.getHouseNumber());
                tfFlat.setText(address.getFlatNumber());
            } else {
                cbDistrict.setValue("");
                cbRegion.setValue("");
                tfIndex.setText("");
                tfCity.setText("");
                tfStreet.setText("");
                tfHome.setText("");
                tfFlat.setText("");
            }
        } catch (NullPointerException ignored) {}
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
