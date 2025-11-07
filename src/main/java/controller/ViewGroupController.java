package com.progettooo.rubrica.controller;

import com.progettooo.rubrica.AddressBook;
import com.progettooo.rubrica.DAOImplementation.GroupImplementation;
import com.progettooo.rubrica.Model.Contact;
import com.progettooo.rubrica.Model.Groups;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class ViewGroupController implements Controller, Initializable {
    @FXML
    private Label GroupNameLabel;
    @FXML
    private Label DescriptionLabel;
    @FXML
    private TableView<Contact> GroupContactTableView;
    @FXML
    private TableColumn<Contact,String> NamegTableColumn;
    @FXML
    private TableColumn<Contact,String> lastnamegTableColumn;
    ObservableList<Contact> contacts = FXCollections.observableArrayList();

    private Groups group;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){



                ContextMenu contextMenu = new ContextMenu();
                MenuItem menuItem1 = new MenuItem("Elimina");

                menuItem1.setOnAction((eventt) -> {
                    Contact contact = GroupContactTableView.getSelectionModel().getSelectedItem();
                    contacts.remove(contact);
                    GroupImplementation groupImplementation = new GroupImplementation();
                    groupImplementation.removePartecipant(group,contact);
                });
                contextMenu.getItems().addAll(menuItem1);
                GroupContactTableView.setContextMenu(contextMenu);

    }

    public void setTablePartecipants(ObservableList<Contact> contact){
        this.contacts = contact;
        NamegTableColumn.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        lastnamegTableColumn.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        GroupContactTableView.setItems(contact);
    }

    public void setGroupProperty(Groups group) {
        this.group = group;
        this.setContact();
    }

    public void setContact() {
        this.GroupNameLabel.setText(group.getName());
        this.DescriptionLabel.setText(group.getDescription());
    }


}
