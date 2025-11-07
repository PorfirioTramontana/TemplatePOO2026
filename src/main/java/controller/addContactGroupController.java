package com.progettooo.rubrica.controller;

import com.progettooo.rubrica.DAOImplementation.GroupImplementation;
import com.progettooo.rubrica.Model.Contact;
import com.progettooo.rubrica.Model.Groups;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class addContactGroupController implements Controller, Initializable {

    private Stage stage = null;
    @FXML
    private TableView<Groups> GroupTableView;
    @FXML
    private TableColumn<Groups,String> GroupTableColumn;
    @FXML
    private TableColumn<Groups,String> DescriptionTableColumn;
    @FXML
    public Label checkE;

    private Contact contact;

    public addContactGroupController(Contact contact) {
        this.contact = contact;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.addGroup();
    }

    public void addGroup(){
        GroupTableView.setPlaceholder(new Label("Nessun gruppo presente"));
        GroupImplementation group = new GroupImplementation();
        GroupTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        DescriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        GroupTableView.setItems(group.getGroup());
        GroupTableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2 && GroupTableView.getSelectionModel().getSelectedItem() != null) {
                    GroupImplementation group = new GroupImplementation();
                    boolean groupCheck = group.addToGroup(contact,GroupTableView.getSelectionModel().getSelectedItem());
                    if (groupCheck) checkE.setVisible(true);
                    else
                    closeStage();
                }
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
