package com.progettooo.rubrica;

import com.progettooo.rubrica.controller.ViewContactController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;

public class AddressBook extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(AddressBook.class.getResource("MainController.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 800);
        ViewContactController test = new ViewContactController();
        test.getController(fxmlLoader);
        stage.setMaximized(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Rubrica");
        JMetro jMetro = new JMetro(Style.LIGHT);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        jMetro.setScene(scene);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}