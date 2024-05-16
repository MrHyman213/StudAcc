package org.example.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.example.model.Entry;
import org.example.model.student.ShortStudent;
import org.example.service.RequestService;
import org.example.util.WindowManager;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class MoveController implements Initializable {

    @FXML
    private ListView<Entry> listIn;

    @FXML
    private ListView<Entry> listOut;

    private static boolean isSingle = false;
    private static ShortStudent student;

    public static void setStudent(ShortStudent selectedStudent){
        isSingle = true;
        student = selectedStudent;
    }

    @FXML
    void moveAct(ActionEvent event) {
        Entry in = listIn.getSelectionModel().getSelectedItem();
        if(isSingle){
            RequestService.moveStudent(student.getId(), in.getId());
            isSingle = false;
        } else {
            Entry out = listOut.getSelectionModel().getSelectedItem();
            if (!Objects.equals(in, out)) {
                if (RequestService.checkGroup(in.getId()))
                    RequestService.moveGroup(out.getName(), in.getName());
                else
                    WindowManager.error("В группе есть студенты.", "Невозможно перевести студентов в группу, где уже есть студенты.");
            } else
                WindowManager.error("Выбраны одинаковые группы.", "Невозможно перевести студентов в ту же группу.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initLists();
    }

    public void initLists(){
        List<Entry> groupList = RequestService.getGroupList();
        listIn.setItems(FXCollections.observableList(groupList));
        if(!isSingle)
            listOut.setItems(FXCollections.observableList(groupList));
        else
            listOut.setVisible(false);
    }
}
