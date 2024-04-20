package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.service.RequestService;
import org.example.util.WindowManager;


public class LoginController {

    @FXML
    private Button btOk;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private TextField tfLogin;

    @FXML
    private Label lbError;


    @FXML
    void loginAct(ActionEvent event) {
        if(RequestService.login(tfLogin.getText(), tfPassword.getText())){
            WindowManager.open("specList", "Список специальностей", false, false);
            WindowManager.close(btOk);
        }else {
            warning();
        }
    }

    public void warning(){
        lbError.setText("Неправильный логин или пароль");
        lbError.setVisible(true);
    }
}
