package com.progettooo.rubrica.controller;

import com.progettooo.rubrica.AddressBook;
import com.progettooo.rubrica.DAOImplementation.ContactImplementation;
import com.progettooo.rubrica.DAOImplementation.GroupImplementation;
import com.progettooo.rubrica.Model.Account;
import com.progettooo.rubrica.Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewContactController implements Controller, Initializable {
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label mobileNumberLabel;
    @FXML
    private Label emailAddressLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label landlineNumberLabel;
    @FXML
    private TableView<Account> MessagingTableView;
    @FXML
    private TableView<Contact> secondaryAddressTableView;
    @FXML
    private TableView<Contact> NumberTableView;
    @FXML
    private TableColumn<Contact,String> addressTableColumn;
    @FXML
    private TableColumn<Contact,String> landline2TableColumn;
    @FXML
    private TableColumn<Contact,String> mobile2TableColumn;
    @FXML
    private TableColumn<Account,Integer> idAccountTableColumn;
    @FXML
    private TableColumn<Account,Integer> idContactTableColumn;
    @FXML
    private TableColumn<Account,String> namefTableColumn;
    @FXML
    private TableColumn<Account,Integer> nicknameTableColumn;
    @FXML
    private TableColumn<Account,Integer> bioTableColumn;
    @FXML
    private TableColumn<Account,Integer> emailMTableColumn;
    private Contact contactt;
    private ObservableList<Account> accounts = FXCollections.observableArrayList();
    private ObservableList<Contact> address = FXCollections.observableArrayList();
    private ObservableList<Contact> numbers = FXCollections.observableArrayList();
    @FXML
    private ImageView imageView;

    private static FXMLLoader controller;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            InputStream stream = AddressBook.class.getResource("Images/default.png").openStream();
            Image image = new Image(stream);
            imageView.setImage(image);
        }catch (IOException e){
            e.printStackTrace();
        }

        this.setContextAccount();
        this.setContextAddress();
        this.setContextNumber();


    }

    private void setContextAddress(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Aggiungi Indirizzo Secondario");
        MenuItem menuItem2 = new MenuItem("Elimina Indirizzo Secondario");
        ContactImplementation contact = new ContactImplementation();

        menuItem1.setOnAction((eventt) -> {
            MainController test = controller.getController();
            try {
                test.addSecondaryAddress(contactt.getIdContact().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        menuItem2.setOnAction((eventt) -> {
            Contact address2 = secondaryAddressTableView.getSelectionModel().getSelectedItem();
            address.remove(address2);
            contact.removeAddress(address2,address2.getEmail());
        });
        contextMenu.getItems().addAll(menuItem1,menuItem2);
        secondaryAddressTableView.setContextMenu(contextMenu);

    }

    private void setContextNumber(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Aggiungi numero Secondario");
        MenuItem menuItem2 = new MenuItem("Elimina numero Secondario");
        ContactImplementation contact = new ContactImplementation();

        menuItem1.setOnAction((eventt) -> {
            MainController test = controller.getController();
            try {
                test.addSecondaryNumber(contactt.getIdContact().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        menuItem2.setOnAction((eventt) -> {
            Contact address2 = NumberTableView.getSelectionModel().getSelectedItem();
            numbers.remove(address2);
            contact.removeNumbers(address2);
        });
        contextMenu.getItems().addAll(menuItem1,menuItem2);
        NumberTableView.setContextMenu(contextMenu);

    }

    private void setContextAccount(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Aggiungi Account");
        MenuItem menuItem2 = new MenuItem("Elimina account");
        ContactImplementation contact = new ContactImplementation();

        menuItem1.setOnAction((eventt) -> {
            MainController test = controller.getController();
            try {
                test.addAccount(contactt.getIdContact().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        menuItem2.setOnAction((eventt) -> {
            Account account = MessagingTableView.getSelectionModel().getSelectedItem();
            accounts.remove(account);
            contact.removeAccount(account);
        });
        contextMenu.getItems().addAll(menuItem1,menuItem2);
        MessagingTableView.setContextMenu(contextMenu);
    }

    public void setContactProperty(Contact contact) {
        this.contactt = contact;
        this.setContact();
    }
    public void setContact() {
        this.firstNameLabel.setText(contactt.getFirst_name());
        this.lastNameLabel.setText(contactt.getLast_name());
        this.addressLabel.setText(contactt.getAddress());
        this.mobileNumberLabel.setText(contactt.getMobile());
        this.landlineNumberLabel.setText(contactt.getLandline());
        this.emailAddressLabel.setText(contactt.getEmail());
    }

    public void setMessaging(ObservableList<Account> account){
        this.accounts = account;
        idAccountTableColumn.setCellValueFactory(new PropertyValueFactory<>("idAccount"));
        idContactTableColumn.setCellValueFactory(new PropertyValueFactory<>("idContact"));
        namefTableColumn.setCellValueFactory(new PropertyValueFactory<>("nameF"));
        nicknameTableColumn.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        bioTableColumn.setCellValueFactory(new PropertyValueFactory<>("bio"));
        emailMTableColumn.setCellValueFactory(new PropertyValueFactory<>("emailF"));
        MessagingTableView.setItems(account);

    }

    public void setAddress(ObservableList<Contact> contact){
        this.address = contact;
        addressTableColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        secondaryAddressTableView.setItems(contact);
    }

    public void setNumber(ObservableList<Contact> contact){
        this.numbers = contact;
        landline2TableColumn.setCellValueFactory(new PropertyValueFactory<>("landline"));
        mobile2TableColumn.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        NumberTableView.setItems(contact);
    }

    public void getController(FXMLLoader loader){
        this.controller = loader;
    }
}
