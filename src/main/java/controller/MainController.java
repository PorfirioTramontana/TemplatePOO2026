package com.progettooo.rubrica.controller;

import com.progettooo.rubrica.AddressBook;
import com.progettooo.rubrica.DAOImplementation.GroupImplementation;
import com.progettooo.rubrica.DAOImplementation.PrivateContactImplementation;
import com.progettooo.rubrica.DAOImplementation.editContactImplementation;
import com.progettooo.rubrica.DAOImplementation.ContactImplementation;
import com.progettooo.rubrica.Model.Account;
import com.progettooo.rubrica.Model.Contact;
import com.progettooo.rubrica.Model.Groups;
import com.progettooo.rubrica.Model.newContact;
import com.progettooo.rubrica.database.Connessione;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.progettooo.rubrica.controller.PasswordController.PrivateR;

public class MainController implements Controller, Initializable {
    @FXML
    private BorderPane headerBorderPane;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private BorderPane BorderPane;

    @FXML
    private TableView<Contact> contactTableView;
    @FXML
    private TableView<Groups> GroupTableView;
    @FXML
    private TableColumn<Contact,String> firstNameTableColumn;
    @FXML
    private TableColumn<Contact,String> LastNameTableColumn;
    @FXML
    private TableColumn<Contact,String> idContactTableColumn;
    @FXML
    private TableColumn<Contact,String> emailTableColumn;
    @FXML
    private TableColumn<Contact,String> addressTableColumn;
    @FXML
    private TableColumn<Contact,String> mobileTableColumn;
    @FXML
    private TableColumn<Contact,String> landlineTableColumn;
    @FXML
    private TableColumn<Groups,String> GroupTableColumn;
    @FXML
    private TableColumn<Groups,String> DescriptionTableColumn;

    ObservableList<Contact> contactObservableList = FXCollections.observableArrayList();
    ObservableList<Contact> data;
    ObservableList<Groups> groups = FXCollections.observableArrayList();

    @FXML
    private Label applicationTitle;
    @FXML
    private BorderPane contactBorderPane;
    @FXML
    private BorderPane contactActionBorderPane;
    @FXML
    private Label contactActionLabel;
    @FXML
    private BorderPane firstActionBorderPane;
    @FXML
    private BorderPane secondActionBorderPane;
    @FXML
    private BorderPane thirdActionBorderPane;
    @FXML
    private TextField searchContactTextField;
    @FXML
    private Button savebutton;
    private Button cancelbutton;
    @FXML
    private Button groupButton;
    @FXML
    private Button addGroupButton;
    @FXML
    private Button deletebutton;
    @FXML
    private Button privateButton;
    private FontIcon addGroupFontIcon;
    @FXML
    private Button addContactButton;
    @FXML
    private FontIcon addContactFontIcon;
    @FXML
    private FontIcon GroupFontIcon;
    private FontIcon editFontIcon;
    private FontIcon deleteFontIcon;
    private FontIcon cancelFontIcon;
    private FontIcon saveFontIcon;
    private Connection connection;

    @FXML
    private FontIcon editGroupFontIcon;
    @FXML
    private Button deleteGroupbutton;
    @FXML
    private FontIcon deleteGroupFontIcon;

    private ResourceBundle resourceBundle;
    private Button editbutton;
    private Button editGroupbutton;
    @FXML
    private PasswordField passwordPF;
    static boolean Check = false;

    public MainController() {
        try
        {
            connection = Connessione.getInstance().getConnection();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        this.initContactAction();
        this.initContactActionFontIcons();
        this.ContactSearch();
        this.editContact();
        this.deleteContact();
        privateButton.setOnAction((event)->{
            this.PrivateContact();
        });
        this.addContactGroup();
        this.editGroup();
        this.deleteGroup();

    }

    @FXML
    public void ContactSearch(){

        try {
        PreparedStatement statement = connection.prepareStatement("Select C.idContact,C.first_name,C.last_name,E.email,A.street,A.city,A.postal_code,A.country,M.number as mobile,L.number as landline FROM CONTACT C,email E,landline L,mobile M,ADDRESS A where C.idContact = E.idContact AND E.idContact = L.idContact AND L.idContact = M.idContact and M.idContact = A.idContact and C.type = 'public' and A.typeA = 'primary'");
        ResultSet resultSet = statement.executeQuery();


        while (resultSet.next()) {

            String name = resultSet.getString("first_name");
            String last_name = resultSet.getString("last_name");
            Integer idContact = resultSet.getInt("idContact");
            String email = resultSet.getString("email");
            String mobile = resultSet.getString("mobile");
            String landline = resultSet.getString("landline");
            String Address = resultSet.getString("street")+","+resultSet.getString("city")+","+resultSet.getString("postal_code")+","+resultSet.getString("country");


            contactObservableList.add(new Contact(idContact,email,name,last_name,mobile,landline,Address));
        }

        firstNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        LastNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        idContactTableColumn.setCellValueFactory(new PropertyValueFactory<>("idContact"));
        addressTableColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
        mobileTableColumn.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        landlineTableColumn.setCellValueFactory(new PropertyValueFactory<>("landline"));

        data = FXCollections.observableArrayList(contactObservableList);
        contactTableView.setItems(contactObservableList);

            FilteredList<Contact> filteredList = new FilteredList<>(contactObservableList, b-> true);
            searchContactTextField.textProperty().addListener((observable, oldValue, newValue) ->
                    filteredList.setPredicate(Contact -> {
                            if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                                return true;
                            }

                            String searchKeyword = newValue.toLowerCase();

                            if (Contact.getFirst_name().toLowerCase().contains(searchKeyword)){
                                return true;
                            } else if (Contact.getLast_name().toLowerCase().contains(searchKeyword)){
                                return true;
                            } else if (Contact.getEmail().toLowerCase().contains(searchKeyword)) {
                                return true;
                            }else if (Contact.getIdContact().toString().contains(searchKeyword)) {
                                return true;
                            }else if (Contact.getLandline().replaceAll(" ","").contains(searchKeyword)) {
                                return true;
                            }else if (Contact.getMobile().replaceAll(" ","").contains(searchKeyword)) {
                                return true;
                            }else {
                                return false;
                            }

                    }));

            SortedList<Contact> sortedData = new SortedList<>(filteredList);
            sortedData.comparatorProperty().bind(contactTableView.comparatorProperty());
            contactTableView.setItems(sortedData);

    } catch (SQLException e){
    }

    }



    public void Tableclick() throws IOException{
        Contact contact = contactTableView.getSelectionModel().getSelectedItem();
        if (contact != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(AddressBook.class.getResource("ViewContactController.fxml"));
            AnchorPane pane = fxmlLoader.load();
            contactBorderPane.setCenter(pane);
            ViewContactController view = fxmlLoader.getController();
            view.setContactProperty(contact);
            ContactImplementation account = new ContactImplementation();
            view.setMessaging(account.getAccount(contact));
            view.setAddress(account.getSecondaryAddress(contact));
            view.setNumber(account.getSecondaryNumber(contact,contact.getLandline(),contact.getMobile()));
            contactActionBorderPane.setVisible(true);
            contactActionLabel.setVisible(true);
            contactActionBorderPane.setStyle("-fx-background-color: #CCCCCC");
            contactActionLabel.setStyle("-fx-text-fill: #3F51B5");
            contactActionLabel.setText("Visualizza Contatto");
            contactActionLabel.setVisible(true);
            firstActionBorderPane.setCenter(editbutton);
            secondActionBorderPane.setCenter(cancelbutton);
            thirdActionBorderPane.setCenter(addGroupButton);
        }
    }

    public void GroupTableclick() throws IOException{
        Groups group = GroupTableView.getSelectionModel().getSelectedItem();
        if (group != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(AddressBook.class.getResource("ViewGroupController.fxml"));
            AnchorPane pane = fxmlLoader.load();
            contactBorderPane.setCenter(pane);
            ViewGroupController view = fxmlLoader.getController();
            view.setGroupProperty(group);
            contactActionBorderPane.setVisible(true);
            contactActionLabel.setVisible(true);
            contactActionBorderPane.setStyle("-fx-background-color: #CCCCCC");
            contactActionLabel.setStyle("-fx-text-fill: #3F51B5");
            contactActionLabel.setText("Visualizza Gruppo");
            GroupImplementation contact = new GroupImplementation();
            view.setTablePartecipants(contact.getPartecipants(group));
            contactActionLabel.setVisible(true);
            firstActionBorderPane.setCenter(editGroupbutton);
            secondActionBorderPane.setCenter(cancelbutton);
            thirdActionBorderPane.setCenter(deleteGroupbutton);
        }
    }

    private void initContactAction() {
        this.contactActionLabel.setVisible(false);
    }

    @FXML
    private void action() throws IOException {
        Stage stage = (Stage) headerBorderPane.getScene().getWindow();
        headerBorderPane.setOnMousePressed(mouseEvent -> {
            if (!stage.isMaximized()) {
                xOffset = mouseEvent.getSceneX();
                yOffset = mouseEvent.getSceneY();
            }
        });

        headerBorderPane.setOnMouseDragged(mouseEvent -> {
            if (!stage.isMaximized()) {
                stage.setX(mouseEvent.getScreenX() - xOffset);
                stage.setY(mouseEvent.getScreenY() - yOffset);
            }
        });
    }

    @FXML private javafx.scene.control.Button closeButton;

    @FXML
    private void closeButtonAction() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Chiudi applicazione");
        alert.setHeaderText("Stai per chiudere l'applicazione.");
        alert.setContentText("sei sicuro?");

        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(stage);

        alert.showAndWait()
                .filter(buttonType -> buttonType == ButtonType.OK)
                .ifPresent(buttonType -> Platform.exit());
    }

    @FXML private javafx.scene.control.Button minimizeButton;

    @FXML
    private void minimizeButtonAction() {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void PrivateContact() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        if (!PrivateR) {
            FXMLLoader loader = new FXMLLoader(AddressBook.class.getResource("PasswordController.fxml"));
            PasswordController popupController = new PasswordController();
            loader.setController(popupController);
            Parent layout;
            try {
                layout = loader.load();
                Scene scene = new Scene(layout);
                Stage popupStage = new Stage();
                popupStage.initOwner(stage);
                popupController.setStage(popupStage);
                popupStage.initModality(Modality.WINDOW_MODAL);
                JMetro jMetro = new JMetro(Style.LIGHT);
                jMetro.setScene(scene);
                popupStage.setScene(scene);
                popupStage.showAndWait();
                if (PrivateR) {

                    if (Check){
                        this.groupContact();
                    }
                    PrivateContactImplementation test = new PrivateContactImplementation();
                    this.contactActionBorderPane.setVisible(false);
                    this.contactActionLabel.setVisible(false);
                    this.contactActionLabel.setText(null);
                    this.contactBorderPane.setCenter(null);
                    contactObservableList.setAll(test.getPrivateContact());
                    applicationTitle.setText("Rubrica Privata");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Ritorno alla rubrica pubblica");
            alert.setHeaderText("Stai per alla rubrica pubblica.");
            alert.setContentText("sei sicuro?");

            alert.initModality(Modality.WINDOW_MODAL);
            alert.initOwner(stage);
            alert.showAndWait()
                    .filter(buttonType -> buttonType == ButtonType.OK)
                    .ifPresent(buttonType -> ToPublic());
        }
    }

    private void ToPublic(){
        PrivateR = false;
        this.contactActionBorderPane.setVisible(false);
        this.contactActionLabel.setVisible(false);
        this.contactActionLabel.setText(null);
        this.contactBorderPane.setCenter(null);
        contactObservableList.setAll(data);
        applicationTitle.setText("Rubrica");
    }

    @FXML
    private void addContact() throws IOException{
            FXMLLoader fxmlLoader = new FXMLLoader(AddressBook.class.getResource("NewContactController.fxml"));
            AnchorPane pane = fxmlLoader.load();
            NewContactController newContactController = fxmlLoader.getController();
            contactBorderPane.setCenter(pane);
            contactActionBorderPane.setVisible(true);
            contactActionLabel.setVisible(true);
            contactActionBorderPane.setStyle("-fx-background-color: #CCCCCC");
            contactActionLabel.setStyle("-fx-text-fill: #3F51B5");
            contactActionLabel.setText("Nuovo Contatto");
            contactActionLabel.setVisible(true);
            firstActionBorderPane.setCenter(cancelbutton);
            secondActionBorderPane.setCenter(savebutton);
            thirdActionBorderPane.setCenter(null);

        this.savebutton.setOnMouseClicked(null);
        this.savebutton.setOnMouseClicked(mouseEvent1 -> {
            newContact newContact = newContactController.contactProperty();
            ContactImplementation add = new ContactImplementation();

            add.AggiungiContatto(newContact);
            String Address = newContact.getStreet()+","+newContact.getCity()+","+newContact.getCap()+","+newContact.getCountry();
            Contact contact = new Contact(Integer.parseInt(add.getIdContact()),newContact.getEmail(),newContact.getFirst_name(),newContact.getLast_name(),newContact.getMobile(),newContact.getLandline(),Address);
            //Set<ConstraintViolation<ContactProperty>> contactConstraintViolations = this.validator.validate(contactProperty);
                this.contactObservableList.add(contact);
                this.contactTableView.getSelectionModel().select(contact);
                this.contactActionBorderPane.setVisible(false);
                this.contactActionLabel.setVisible(false);
                this.contactActionLabel.setText(null);
                this.contactBorderPane.setCenter(null);
    });
    }

    @FXML
    public void addAccount(String idContact) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(AddressBook.class.getResource("addAccountController.fxml"));
        AnchorPane pane = fxmlLoader.load();
        addAccountController addAccountController = fxmlLoader.getController();
        contactBorderPane.setCenter(pane);
        contactActionBorderPane.setVisible(true);
        contactActionLabel.setVisible(true);
        contactActionBorderPane.setStyle("-fx-background-color: #CCCCCC");
        contactActionLabel.setStyle("-fx-text-fill: #3F51B5");
        contactActionLabel.setText("Nuovo Contatto");
        contactActionLabel.setVisible(true);
        firstActionBorderPane.setCenter(cancelbutton);
        secondActionBorderPane.setCenter(savebutton);
        thirdActionBorderPane.setCenter(null);

        this.savebutton.setOnMouseClicked(null);
        this.savebutton.setOnMouseClicked(mouseEvent1 -> {
            ContactImplementation add = new ContactImplementation();
            Account account = addAccountController.AccountProperty(Integer.parseInt(idContact));
            add.addAccount(account);
            //Set<ConstraintViolation<ContactProperty>> contactConstraintViolations = this.validator.validate(contactProperty);
            this.contactActionBorderPane.setVisible(false);
            this.contactActionLabel.setVisible(false);
            this.contactActionLabel.setText(null);
            this.contactBorderPane.setCenter(null);
        });
    }

    @FXML
    public void addSecondaryAddress(String idContact) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(AddressBook.class.getResource("addSecondaryAddressController.fxml"));
        AnchorPane pane = fxmlLoader.load();
        addSecondaryAddressController addSecondaryAddressController = fxmlLoader.getController();
        contactBorderPane.setCenter(pane);
        contactActionBorderPane.setVisible(true);
        contactActionLabel.setVisible(true);
        contactActionBorderPane.setStyle("-fx-background-color: #CCCCCC");
        contactActionLabel.setStyle("-fx-text-fill: #3F51B5");
        contactActionLabel.setText("Nuovo Contatto");
        contactActionLabel.setVisible(true);
        firstActionBorderPane.setCenter(cancelbutton);
        secondActionBorderPane.setCenter(savebutton);
        thirdActionBorderPane.setCenter(null);

        this.savebutton.setOnMouseClicked(null);
        this.savebutton.setOnMouseClicked(mouseEvent1 -> {
            ContactImplementation add = new ContactImplementation();
            newContact contact = addSecondaryAddressController.SecondaryAddressProperty(Integer.parseInt(idContact));
            add.addSecondaryAddress(contact);
            //Set<ConstraintViolation<ContactProperty>> contactConstraintViolations = this.validator.validate(contactProperty);
            this.contactActionBorderPane.setVisible(false);
            this.contactActionLabel.setVisible(false);
            this.contactActionLabel.setText(null);
            this.contactBorderPane.setCenter(null);
        });
    }


    @FXML
    public void addSecondaryNumber(String idContact) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(AddressBook.class.getResource("addSecondaryNumberController.fxml"));
        AnchorPane pane = fxmlLoader.load();
        addSecondaryNumberController addSecondaryNumberController = fxmlLoader.getController();
        contactBorderPane.setCenter(pane);
        contactActionBorderPane.setVisible(true);
        contactActionLabel.setVisible(true);
        contactActionBorderPane.setStyle("-fx-background-color: #CCCCCC");
        contactActionLabel.setStyle("-fx-text-fill: #3F51B5");
        contactActionLabel.setText("Nuovo Contatto");
        contactActionLabel.setVisible(true);
        firstActionBorderPane.setCenter(cancelbutton);
        secondActionBorderPane.setCenter(savebutton);
        thirdActionBorderPane.setCenter(null);

        this.savebutton.setOnMouseClicked(null);
        this.savebutton.setOnMouseClicked(mouseEvent1 -> {
            ContactImplementation add = new ContactImplementation();
            newContact contact = addSecondaryNumberController.SecondaryNumberProperty(Integer.parseInt(idContact));
            add.addSecondaryNumber(contact);
            //Set<ConstraintViolation<ContactProperty>> contactConstraintViolations = this.validator.validate(contactProperty);
            this.contactActionBorderPane.setVisible(false);
            this.contactActionLabel.setVisible(false);
            this.contactActionLabel.setText(null);
            this.contactBorderPane.setCenter(null);
        });
    }

    @FXML
    private void addGroup() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(AddressBook.class.getResource("addGroupController.fxml"));
        AnchorPane pane = fxmlLoader.load();
        addGroupController addGroupController = fxmlLoader.getController();
        contactBorderPane.setCenter(pane);
        contactActionBorderPane.setVisible(true);
        contactActionLabel.setVisible(true);
        contactActionBorderPane.setStyle("-fx-background-color: #CCCCCC");
        contactActionLabel.setStyle("-fx-text-fill: #3F51B5");
        contactActionLabel.setText("Nuovo Gruppo");
        contactActionLabel.setVisible(true);
        firstActionBorderPane.setCenter(cancelbutton);
        secondActionBorderPane.setCenter(savebutton);
        thirdActionBorderPane.setCenter(null);

        this.savebutton.setOnMouseClicked(null);
        this.savebutton.setOnMouseClicked(mouseEvent1 -> {
            Groups newGroup = addGroupController.GroupProperty();
            GroupImplementation add = new GroupImplementation();

            add.addGroup(newGroup);
            //Set<ConstraintViolation<ContactProperty>> contactConstraintViolations = this.validator.validate(contactProperty);
            this.groups.add(newGroup);
            this.contactActionBorderPane.setVisible(false);
            this.contactActionLabel.setVisible(false);
            this.contactActionLabel.setText(null);
            this.contactBorderPane.setCenter(null);
        });
    }

    @FXML
    private void groupContact() throws IOException{
            if (PrivateR && !Check){
                PrivateContact();
            }

            if(!Check) {
                GroupImplementation group = new GroupImplementation();
                groups = group.getGroup();
                GroupTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                DescriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                GroupTableView.setItems(groups);
                this.contactActionBorderPane.setVisible(false);
                this.contactActionLabel.setVisible(false);
                this.contactActionLabel.setText(null);
                this.contactBorderPane.setCenter(null);
                BorderPane.setLeft(GroupTableView);
                GroupFontIcon.setIconLiteral("mdi-account");
                GroupFontIcon.setIconColor(Paint.valueOf("white"));
                GroupFontIcon.setIconSize(18);
                groupButton.setGraphic(GroupFontIcon);
                addContactFontIcon.setIconLiteral("mdi-account-multiple-plus");
                addContactFontIcon.setIconColor(Paint.valueOf("white"));
                addContactFontIcon.setIconSize(18);
                addContactButton.setGraphic(addContactFontIcon);
                addContactButton.setTooltip(new Tooltip("Crea Gruppo"));
                addContactButton.setOnAction(e -> {
                    try {
                        addGroup();
                    }catch (IOException err){
                        err.printStackTrace();
                    }
                });
                Check = true;
            }else{
                BorderPane.setLeft(contactTableView);
                GroupFontIcon.setIconLiteral("mdi-account-multiple");
                GroupFontIcon.setIconColor(Paint.valueOf("white"));
                GroupFontIcon.setIconSize(18);
                groupButton.setGraphic(GroupFontIcon);
                addContactFontIcon.setIconLiteral("mdi-account-plus");
                addContactFontIcon.setIconColor(Paint.valueOf("white"));
                addContactFontIcon.setIconSize(18);
                addContactButton.setGraphic(addContactFontIcon);
                addContactButton.setTooltip(new Tooltip("Aggiungi un contatto"));
                addContactButton.setOnAction(e -> {
                    try {
                        addContact();
                    }catch (IOException err){
                        err.printStackTrace();
                    }

                });
                Check = false;
            }
    }

    @FXML
    private void editGroup() {
        this.editGroupbutton.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(AddressBook.class.getResource("editGroupController.fxml"));
            AnchorPane pane = null;
            Groups group = GroupTableView.getSelectionModel().getSelectedItem();
            try {
                pane = fxmlLoader.load();
                editGroupController editGroupController = fxmlLoader.getController();
                editGroupController.setGroupProperty(group);
                contactBorderPane.setCenter(pane);
                contactActionBorderPane.setVisible(true);
                contactActionLabel.setVisible(true);
                contactActionBorderPane.setStyle("-fx-background-color: #CCCCCC");
                contactActionLabel.setStyle("-fx-text-fill: #3F51B5");
                contactActionLabel.setText("Modifica Contatto");
                contactActionLabel.setVisible(true);
                firstActionBorderPane.setCenter(cancelbutton);
                secondActionBorderPane.setCenter(savebutton);

                this.savebutton.setOnMouseClicked(null);
                this.savebutton.setOnMouseClicked(mouseEvent1 -> {
                    GroupImplementation edit = new GroupImplementation();
                    Groups model = editGroupController.groupProperty();

                    edit.editGroup(model,group.getName());
                    this.groups.set(this.groups.indexOf(group),model);
                    this.contactActionBorderPane.setVisible(false);
                    this.contactActionLabel.setVisible(false);
                    this.contactActionLabel.setText(null);
                    this.contactBorderPane.setCenter(null);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }


        });
    }

    @FXML
    private void editContact() {
        this.editbutton.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(AddressBook.class.getResource("EditContactController.fxml"));
            AnchorPane pane = null;
            Contact contact = contactTableView.getSelectionModel().getSelectedItem();
            try {
                pane = fxmlLoader.load();
                EditContactController newContactController = fxmlLoader.getController();
                newContactController.setContactProperty(contact);
                contactBorderPane.setCenter(pane);
                contactActionBorderPane.setVisible(true);
                contactActionLabel.setVisible(true);
                contactActionBorderPane.setStyle("-fx-background-color: #CCCCCC");
                contactActionLabel.setStyle("-fx-text-fill: #3F51B5");
                contactActionLabel.setText("Modifica Contatto");
                contactActionLabel.setVisible(true);
                firstActionBorderPane.setCenter(cancelbutton);
                secondActionBorderPane.setCenter(savebutton);
                thirdActionBorderPane.setCenter(deletebutton);

                this.savebutton.setOnMouseClicked(null);
                this.savebutton.setOnMouseClicked(mouseEvent1 -> {
                    editContactImplementation edit = new editContactImplementation();
                    newContact model = newContactController.contactProperty();

                    edit.EditContact(model);
                    this.contactObservableList.set(this.contactObservableList.indexOf(contact),model.newContactToContact(model));
                    this.contactActionBorderPane.setVisible(false);
                    this.contactActionLabel.setVisible(false);
                    this.contactActionLabel.setText(null);
                    this.contactBorderPane.setCenter(null);
                    });
            } catch (IOException e) {
                e.printStackTrace();
            }


        });
    }

    @FXML
    private void addContactGroup() {
        this.addGroupButton.setOnMouseClicked(mouseEvent -> {
            Contact contact = contactTableView.getSelectionModel().getSelectedItem();
            Stage stage = (Stage) closeButton.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(AddressBook.class.getResource("addContactGroupController.fxml"));
                addContactGroupController popupController = new addContactGroupController(contact);
                loader.setController(popupController);
                Parent layout;
                try {
                    layout = loader.load();
                    Scene scene = new Scene(layout);
                    Stage popupStage = new Stage();
                    popupStage.initOwner(stage);
                    popupController.setStage(popupStage);
                    popupStage.initModality(Modality.WINDOW_MODAL);
                    JMetro jMetro = new JMetro(Style.LIGHT);
                    jMetro.setScene(scene);
                    popupStage.setScene(scene);
                    popupStage.showAndWait();

        } catch (IOException e) {
                    e.printStackTrace();
                }
        });
    }

    private void deleteContact() {
        this.deletebutton.setOnMouseClicked(mouseEvent -> {
            Contact contact = contactTableView.getSelectionModel().getSelectedItem();
            Stage stage = (Stage) closeButton.getScene().getWindow();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cancella Contatto");
            alert.setHeaderText("Sei sicuro di voler cancellare il contatto?");
            alert.setContentText("vuoi continuare?");

            alert.initModality(Modality.WINDOW_MODAL);
            alert.initOwner(stage);

            alert.showAndWait()
                    .filter(buttonType -> buttonType == ButtonType.OK)
                    .ifPresent(buttonType -> {
                        editContactImplementation del = new editContactImplementation();
                        del.DeleteContact(contact);
                        this.contactObservableList.remove(contact);
                        this.contactTableView.getSelectionModel().clearSelection();
                        this.contactActionBorderPane.setVisible(false);
                        this.contactActionLabel.setVisible(false);
                        this.contactActionLabel.setText(null);
                        this.contactBorderPane.setCenter(null);
                    });
        });
    }

    private void deleteGroup() {
        this.deleteGroupbutton.setOnMouseClicked(mouseEvent -> {
            Groups group = GroupTableView.getSelectionModel().getSelectedItem();
            Stage stage = (Stage) closeButton.getScene().getWindow();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cancella Contatto");
            alert.setHeaderText("Sei sicuro di voler cancellare il contatto?");
            alert.setContentText("vuoi continuare?");

            alert.initModality(Modality.WINDOW_MODAL);
            alert.initOwner(stage);

            alert.showAndWait()
                    .filter(buttonType -> buttonType == ButtonType.OK)
                    .ifPresent(buttonType -> {
                        GroupImplementation del = new GroupImplementation();
                        del.deleteGroup(group);
                        this.groups.remove(group);
                        this.GroupTableView.getSelectionModel().clearSelection();
                        this.contactActionBorderPane.setVisible(false);
                        this.contactActionLabel.setVisible(false);
                        this.contactActionLabel.setText(null);
                        this.contactBorderPane.setCenter(null);
                    });
        });
    }

    private void initContactActionFontIcons() {
        this.editbutton = new Button();
        this.editFontIcon = new FontIcon();
        this.editFontIcon.setIconColor(Paint.valueOf("#3F51B5"));
        this.editFontIcon.setIconLiteral("mdi-pencil-box-outline");
        this.editFontIcon.setIconSize(18);
        this.editbutton.setGraphic(editFontIcon);

        this.editGroupbutton = new Button();
        this.editGroupFontIcon = new FontIcon();
        this.editGroupFontIcon.setIconColor(Paint.valueOf("#3F51B5"));
        this.editGroupFontIcon.setIconLiteral("mdi-pencil-box-outline");
        this.editGroupFontIcon.setIconSize(18);
        this.editGroupbutton.setGraphic(editGroupFontIcon);


        this.deletebutton = new Button();
        this.deleteFontIcon = new FontIcon();
        this.deleteFontIcon.setIconColor(Paint.valueOf("#3F51B5"));
        this.deleteFontIcon.setIconLiteral("mdi-delete");
        this.deleteFontIcon.setIconSize(18);
        this.deletebutton.setGraphic(deleteFontIcon);


        this.deleteGroupbutton = new Button();
        this.deleteGroupFontIcon = new FontIcon();
        this.deleteGroupFontIcon.setIconColor(Paint.valueOf("#3F51B5"));
        this.deleteGroupFontIcon.setIconLiteral("mdi-delete");
        this.deleteGroupFontIcon.setIconSize(18);
        this.deleteGroupbutton.setGraphic(deleteGroupFontIcon);

        this.addGroupButton = new Button();
        this.addGroupFontIcon = new FontIcon();
        this.addGroupFontIcon.setIconColor(Paint.valueOf("#3F51B5"));
        this.addGroupFontIcon.setIconLiteral("mdi-account-multiple-plus");
        this.addGroupFontIcon.setIconSize(18);
        this.addGroupButton.setGraphic(addGroupFontIcon);


        this.cancelbutton = new Button();
        this.cancelFontIcon = new FontIcon();
        this.cancelbutton.setOnMouseClicked(mouseEvent1 -> {
            this.contactActionBorderPane.setVisible(false);
            this.contactActionLabel.setVisible(false);
            this.contactActionLabel.setText(null);
            this.contactBorderPane.setCenter(null);
        });
        this.cancelFontIcon.setIconColor(Paint.valueOf("#3F51B5"));
        this.cancelFontIcon.setIconLiteral("mdi-close");
        this.cancelFontIcon.setIconSize(18);
        this.cancelbutton.setGraphic(cancelFontIcon);

        this.savebutton = new Button();
        this.saveFontIcon = new FontIcon();
        this.saveFontIcon.setIconColor(Paint.valueOf("#3F51B5"));
        this.saveFontIcon.setIconLiteral("mdi-content-save");
        this.saveFontIcon.setIconSize(18);
        this.savebutton.setGraphic(saveFontIcon);
    }

}