package com.progettooo.rubrica.controller;

import com.progettooo.rubrica.Model.newContact;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class addSecondaryNumberController implements Initializable,Controller {

    @FXML
    private TextField mobileTextField;
    @FXML
    private TextField landlineTextField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public newContact SecondaryNumberProperty(Integer idContact) {

        newContact contact = new newContact(null, null, null, mobileTextField.getText(), landlineTextField.getText(), null,null,null,null);
        contact.setIdContact(idContact);

        return contact;
    }
}
