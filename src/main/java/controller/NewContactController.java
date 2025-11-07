package com.progettooo.rubrica.controller;

import com.progettooo.rubrica.Model.newContact;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class NewContactController implements Controller, Initializable {
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
    private TextField mobileNumberTextField;
    @FXML
    private TextField landlineNumberTextField;
    @FXML
    private TextField emailAddressTextField;
    @FXML
    private Label firstNameMessageLabel;
    @FXML
    private Label lastNameMessageLabel;
    @FXML
    private Label streetMessageLabel;
    @FXML
    private Label capMessageLabel;
    @FXML
    private Label cityMessageLabel;
    @FXML
    private Label countryMessageLabel;
    @FXML
    private Label landlineNumberMessageLabel;
    @FXML
    private Label mobileNumberMessageLabel;
    @FXML
    private Label emailAddressMessageLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    }

    public newContact contactProperty() {
        return new newContact(firstNameTextField.getText(),lastNameTextField.getText(),emailAddressTextField.getText(),mobileNumberTextField.getText(),landlineNumberTextField.getText(),streetTextField.getText(),Integer.parseInt(capTextField.getText()),cityTextField.getText(),countryTextField.getText());
    }

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
            this.streetTextField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            this.streetMessageLabel.setVisible(true);
            this.streetMessageLabel.setText("il campo indirizzo é vuoto");
            this.streetMessageLabel.setTextFill(Color.RED);
        } else {
            this.streetMessageLabel.setText(null);
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
    }
}
