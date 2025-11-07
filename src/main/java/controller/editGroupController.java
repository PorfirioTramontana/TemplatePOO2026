package com.progettooo.rubrica.controller;

import com.progettooo.rubrica.Model.Groups;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class editGroupController implements Initializable,Controller {
        @FXML
        private TextField GroupNameTextField;
        @FXML
        private TextField DescriptionTextField;


        private Groups group;

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

        }

        public void setGroupProperty(Groups group) {
            this.group = group;
            this.setGroup();
        }

        public void setGroup() {
            this.GroupNameTextField.setText(this.group.getName());
            this.DescriptionTextField.setText(this.group.getDescription());
        }


        public Groups groupProperty() {
            Groups group;
            group = new Groups(GroupNameTextField.getText(),DescriptionTextField.getText());

            return group;
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
