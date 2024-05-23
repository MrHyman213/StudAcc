package org.example.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class WindowManager {

    private static final String path = "/org/example/view/";

    public static void open(String viewName, String title, Boolean resizable, Boolean withCloseButton){
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(WindowManager.class.getResource(path + viewName + ".fxml")));
            Stage stage = new Stage();
            stage.setScene(new Scene(pane));
            stage.setTitle(title);
            stage.setResizable(resizable);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.initModality(Modality.APPLICATION_MODAL);
            if(withCloseButton)
                stage.initStyle(StageStyle.UTILITY);
            else stage.initStyle(StageStyle.UNDECORATED);
            stage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(Node node){
        ((Stage) node.getScene().getWindow()).close();
    }

    public static void error(String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }
}
