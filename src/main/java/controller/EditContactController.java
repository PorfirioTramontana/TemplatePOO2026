package com.progettooo.rubrica.controller;

import com.progettooo.rubrica.Model.Contact;
import com.progettooo.rubrica.Model.newContact;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EditContactController implements Controller, Initializable {
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField streetTextField;
    @FXML
    private TextField capTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField countryTextField;
    @FXML
    private TextField landlineNumberTextField;
    @FXML
    private TextField mobileNumberTextField;
    @FXML
    private TextField emailAddressTextField;
    @FXML
    private Label firstNameMessageLabel;
    @FXML
    private Label lastNameMessageLabel;
    @FXML
    private Label addressMessageLabel;
    @FXML
    private Label mobileNumberMessageLabel;
    @FXML
    private Label emailAddressMessageLabel;


    private Contact contactt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setContactProperty(Contact contact) {
        this.contactt = contact;
        this.setContact();
    }
    public void setContact() {
        this.firstNameTextField.setText(this.contactt.getFirst_name());
        this.lastNameTextField.setText(this.contactt.getLast_name());
        this.mobileNumberTextField.setText(this.contactt.getMobile());
        this.emailAddressTextField.setText(this.contactt.getEmail());
        this.landlineNumberTextField.setText(this.contactt.getLandline());
        String address = this.contactt.getAddress();
        String[] array = address.split(",");
        this.streetTextField.setText(array[0]);
        this.cityTextField.setText(array[1]);
        this.capTextField.setText(array[2]);
        this.countryTextField.setText(array[3]);
    }


    public newContact contactProperty() {
        newContact contact;
        contact = new newContact(
                firstNameTextField.getText(),
                lastNameTextField.getText(),
                emailAddressTextField.getText(),
                mobileNumberTextField.getText(),
                landlineNumberTextField.getText(),
                streetTextField.getText(),
                Integer.parseInt(capTextField.getText()),
                cityTextField.getText(),
                countryTextField.getText());
        contact.setIdContact(this.contactt.getIdContact());


        return contact;
    }
    /*
    public void validation(Map<String, String> violations) {
        if (violations.containsKey("firstName")) {
            this.firstNameMessageLabel.setVisible(true);
            this.firstNameMessageLabel.setText("il nome non é valido");
            this.firstNameMessageLabel.setTextFill(Color.RED);
        } else {
            this.firstNameMessageLabel.setText(null);
        }

        if (violations.containsKey("lastName")) {
            this.lastNameTextField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            this.lastNameMessageLabel.setVisible(true);
            this.lastNameMessageLabel.setText("il cognome non é valido");
            this.lastNameMessageLabel.setTextFill(Color.RED);
        } else {
            this.lastNameMessageLabel.setText(null);
        }

        if (violations.containsKey("address")) {
            this.addressTextField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            this.addressMessageLabel.setVisible(true);
            this.addressMessageLabel.setText("l'indirizzo non é valido");
            this.addressMessageLabel.setTextFill(Color.RED);
        } else {
            this.addressMessageLabel.setText(null);
        }

        if (violations.containsKey("mobileNumber")) {
            this.mobileNumberTextField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            this.mobileNumberMessageLabel.setVisible(true);
            this.mobileNumberMessageLabel.setText("il numero di telefono non é valido");
            this.mobileNumberMessageLabel.setTextFill(Color.RED);
        } else {
            this.mobileNumberMessageLabel.setText(null);
        }

        if (violations.containsKey("emailAddress")) {
            this.emailAddressTextField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            this.emailAddressMessageLabel.setVisible(true);
            this.emailAddressMessageLabel.setText("L'email non é valida");
            this.emailAddressMessageLabel.setTextFill(Color.RED);
        } else {
            this.emailAddressMessageLabel.setText(null);
        }
    }*/
}
