package org.example.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.example.DTO.EntryDTO;
import org.example.model.Entry;
import org.example.service.RequestService;
import org.example.util.EntryContainer;
import org.example.util.WindowManager;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Logger;

@Slf4j
public class ListController implements Initializable {

    @FXML
    private ListView<Entry> itemList;

    @FXML
    private TextField tfSelectedItems;

    public static String selectedList;

    private Logger logger = Logger.getLogger(ListController.class.getName());
    private String subItem;
    private static int idMainItem;
    public static boolean isSub;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Выбран список - " + selectedList);
        subItem = EntryContainer.getSubItem(selectedList);
        logger.info("Дочерний список - " + subItem);
        initList();
        if(subItem != null) {
            setOpenAction();
        }
    }

    @FXML
    void addAct(ActionEvent event) {
        EntryDTO entry = new EntryDTO(tfSelectedItems.getText());
        tfSelectedItems.clear();
        if(isSub)
            entry.setParentId(idMainItem);
        RequestService.addList(entry, selectedList);
        initList();
    }

    @FXML
    void changeAct(ActionEvent event) {
        EntryDTO entry = new EntryDTO(tfSelectedItems.getText());
        tfSelectedItems.clear();
        if(isSub)
            entry.setParentId(idMainItem);
        RequestService.updateList(entry, selectedList,
                itemList.getSelectionModel().getSelectedItem().getId());
        initList();
    }

    @FXML
    void deleteAct(ActionEvent event) {
        Entry selectedItem = itemList.getSelectionModel().getSelectedItem();
        if (!checkGroup(selectedItem))
            if (subItem == null || RequestService.getList(subItem, selectedItem.getId(), true).isEmpty())
                deleteList(selectedItem);
            else
                WindowManager.error("Невозможно удалить объект", "У выбранного объекта есть дочерние элементы.");
    }

    @FXML
    void exitAct(ActionEvent event) {
        WindowManager.close(itemList);
        isSub = false;
    }

    private void initList() {
        itemList.setItems(FXCollections.observableList(RequestService.getList(selectedList, idMainItem, isSub)));
        itemList.getItems().forEach(item -> System.out.println(item.getId() + " " + item.getName() + " " + item.getSubEntry()));
        itemList.getSelectionModel().selectedItemProperty().addListener(ChangeListener -> {
            Entry selectedItem = itemList.getSelectionModel().getSelectedItem();
            if (selectedItem != null)
                tfSelectedItems.setText(selectedItem.getName());
            else tfSelectedItems.clear();
        });
    }

    private void setOpenAction() {
        itemList.setOnMouseClicked(mouseEvent -> {
            Entry selectedItem = itemList.getSelectionModel().getSelectedItem();
            if (selectedItem != null && mouseEvent.getClickCount() == 2) {
                try {
                    selectedList = subItem;
                    isSub = true;
                    idMainItem = EntryContainer.getIdByName("itemList", selectedItem.getName());
                    WindowManager.open("list", EntryContainer.getSubTitle(subItem), false, true);
                } catch (NullPointerException ignored) {}
            }
        });
    }

    private void deleteList(Entry selectedItem) {
        if(selectedItem != null) {
            RequestService.deleteList(selectedList, selectedItem.getId());
            initList();
        }
    }

    private boolean checkGroup(Entry selectedItem) {
        //Костыль
        RequestService.getGroupList();
        if(selectedItem != null && Objects.equals(selectedList, "group")) {
            if (!RequestService.checkGroup(selectedItem.getId()))
                WindowManager.error("В группе есть студенты", "Очистите группу, перед тем как её удалить.");
            else
                deleteList(selectedItem);
            return true;
        }
        return false;
    }
}
