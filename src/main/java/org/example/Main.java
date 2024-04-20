package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.util.WindowManager;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        WindowManager.open("login", "Вход", false, false);
    }

    public static void main(String[] args) {
        launch();
    }
}