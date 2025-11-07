package com.progettooo.rubrica.controller;

import com.progettooo.rubrica.Model.Account;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class addAccountController implements Initializable,Controller {

    @FXML
    private TextField nameFTextField;
    @FXML
    private TextField nicknameTextField;
    @FXML
    private TextField bioTextField;
    @FXML
    private TextField emailTextField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public Account AccountProperty(Integer idContact) {

    return new Account(idContact,0,nameFTextField.getText(),nicknameTextField.getText(),bioTextField.getText(),emailTextField.getText());
    }

}
