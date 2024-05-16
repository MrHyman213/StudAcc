package org.example.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.model.Entry;
import org.example.model.User;
import org.example.service.RequestService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private ComboBox<Entry> cbRole;

    @FXML
    private ListView<Entry> roleListView;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfLogin;

    @FXML
    private TextField tfPassword;

    @FXML
    private TextField tfPhone;

    @FXML
    private ListView<User> userListView;

    private List<Entry> selectedRoles = new ArrayList<>();
    private List<Entry> roles = new ArrayList<>();
    private List<Entry> allRoles = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initLists();
        initUserList();
    }

    @FXML
    void closeAct(ActionEvent event) {

    }

    @FXML
    void createUserAct(ActionEvent event) {
        clearForm();
    }

    @FXML
    void deleteRoleAct(ActionEvent event) {
        Entry selectedRole = roleListView.getSelectionModel().getSelectedItem();
        if(selectedRole != null)
            moveItem(selectedRoles, roles, selectedRole);
    }

    @FXML
    void deleteUserAct(ActionEvent event) {
        clearForm();
    }

    private void moveItem(List<Entry> from, List<Entry> to, Entry entry){
        from.remove(entry);
        to.add(entry);
        cbRole.setItems(FXCollections.observableArrayList(roles));
        roleListView.setItems(FXCollections.observableArrayList(selectedRoles));
    }

    private void initLists(){
        allRoles = RequestService.getList("role", 0, false);
        cbRole.setItems(FXCollections.observableArrayList(allRoles));
    }

    private void initUserList(){
        userListView.setItems(FXCollections.observableArrayList(RequestService.getUserList()));
        userListView.setOnMouseClicked(mouseEvent -> {
            if(userListView.getSelectionModel().getSelectedItem() != null) {
                setValues(userListView.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void setValues(User user){
        tfPhone.setText(user.getPhone());
        tfLogin.setText(user.getUsername());
        tfEmail.setText(user.getEmail());
        roleListView.setItems(FXCollections.observableArrayList(user.getRoleList()));
        cbRole.setItems(FXCollections.observableArrayList(subtractionRoles(user.getRoleList())));
    }

    private List<Entry> subtractionRoles(List<Entry> selectedRoles) {
        List<Entry> nonSelectedRoles = new ArrayList<>();
        for (Entry role: allRoles)
            if(!selectedRoles.contains(role))
                nonSelectedRoles.add(role);
        return nonSelectedRoles;
    }

    private void clearForm(){
        tfPhone.clear();
        tfLogin.clear();
        tfEmail.clear();
        tfPassword.clear();
        roleListView.setItems(FXCollections.observableArrayList());
        cbRole.setItems(FXCollections.observableArrayList(allRoles));
    }
}
