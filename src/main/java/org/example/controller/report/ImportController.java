package org.example.controller.report;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import org.example.model.Entry;
import org.example.service.RequestService;
import org.example.util.WindowManager;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ImportController implements Initializable {


    @FXML
    private Button btSelect;

    @FXML
    private ComboBox<Entry> cbGroup;

    @FXML
    private TextField tfPath;

    @FXML
    private Label lbWarning;

    public static Entry selectedGroup;

    private File selectedFile;

    @FXML
    void importAct(ActionEvent event) {
        if(selectedFile != null) {
            RequestService.importStudents(selectedFile, cbGroup.getSelectionModel().getSelectedItem().getId());
            WindowManager.close(cbGroup);
        }
        else
            lbWarning.setVisible(true);
    }

    @FXML
    void chooseFileAct(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выбор файла");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel Files", "*.csv"));
        chooser.setInitialDirectory(new File(System.getProperty("user.home") + "\\Desktop"));
        selectedFile = chooser.showOpenDialog(btSelect.getScene().getWindow());
        if (selectedFile != null)
            tfPath.setText(selectedFile.getAbsolutePath());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbGroup.setItems(FXCollections.observableArrayList(RequestService.getGroupList()));
        cbGroup.setValue(selectedGroup);
        initHandlers();
    }

    private void initHandlers(){
        tfPath.setOnDragOver(event -> {
            if (event.getGestureSource() != tfPath && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        tfPath.setOnDragDropped(event -> {
            if (event.getGestureSource() != tfPath && event.getDragboard().hasFiles()) {
                event.getDragboard().getFiles().forEach(file -> {
                    if (file.getName().endsWith(".csv")) {
                        selectedFile = file;
                        tfPath.setText(file.getAbsolutePath());
                    }
                });
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });
    }
}
