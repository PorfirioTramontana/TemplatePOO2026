package com.progettooo.rubrica.controller;

import com.progettooo.rubrica.Model.Groups;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class addGroupController  implements Initializable,Controller {

    @FXML
    private TextField NameGroupTextField;
    @FXML
    private TextField DescriptionTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public Groups GroupProperty() {
        return new Groups(NameGroupTextField.getText(),DescriptionTextField.getText());
    }
}
