package com.progettooo.rubrica.controller;

import com.progettooo.rubrica.AddressBook;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class PasswordController implements Controller, Initializable {

    public static boolean PrivateR = false;
    private Stage stage = null;
    @FXML
    private Button connectBtn;
    @FXML
    private PasswordField passwordPF;
    @FXML
    private Label FailPassword;
    @FXML
    private Label PasswordLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        File newFile = new File(System.getProperty("user.home")+"/Documents/password_progetto.txt");
        if (newFile.length() == 0) {
            try {
                PasswordLabel.setText("Inserisci la nuova password");
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        connectBtn.setOnAction((event)->{
            String result = passwordPF.getText();

            try {
                if (newFile.length() == 0) {
                    FileWriter writer = new FileWriter(newFile);
                    writer.write(result);
                    writer.flush();
                    writer.close();
                    PrivateR = true;
                    closeStage();
                }

                File myObj = new File(System.getProperty("user.home")+"/Documents/password_progetto.txt");
                Scanner myReader = new Scanner(myObj);
                String data = myReader.nextLine();
                if (result.isEmpty()) {
                    FailPassword.setText("Il campo password é vuoto");
                    FailPassword.setVisible(true);
                } else if(!data.equals(result)){
                    FailPassword.setText("La password é sbagliata");
                    FailPassword.setVisible(true);
                }else if(data.equals(result)){
                    PrivateR = true;
                    closeStage();
                }
                myReader.close();
            }catch (IOException e){
                e.printStackTrace();
            }

        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void closeStage() {
        if(stage!=null) {
            stage.close();
        }
    }
}
