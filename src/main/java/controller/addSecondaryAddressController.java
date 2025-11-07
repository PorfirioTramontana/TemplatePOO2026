package com.progettooo.rubrica.controller;

import com.progettooo.rubrica.Model.Account;
import com.progettooo.rubrica.Model.Contact;
import com.progettooo.rubrica.Model.newContact;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class addSecondaryAddressController implements Initializable,Controller {

    @FXML
    private TextField streetTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField capTextField;
    @FXML
    private TextField countryTextField;
    @FXML
    private TextField nicknameTextField;
    @FXML
    private TextField bioTextField;
    @FXML
    private TextField emailTextField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public newContact SecondaryAddressProperty(Integer idContact) {

        newContact contact = new newContact(null, null, null, null, null, streetTextField.getText(),Integer.parseInt(capTextField.getText()),cityTextField.getText(),countryTextField.getText());
        contact.setIdContact(idContact);

        return contact;
    }

}
