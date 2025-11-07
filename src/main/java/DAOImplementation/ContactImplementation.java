package com.progettooo.rubrica.DAOImplementation;

import com.progettooo.rubrica.DAO.Contact;
import com.progettooo.rubrica.Model.Account;
import com.progettooo.rubrica.Model.newContact;
import com.progettooo.rubrica.database.Connessione;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.progettooo.rubrica.controller.PasswordController.PrivateR;

public class ContactImplementation implements Contact {

    Connection connection;
    String idContact;
    Integer idAccount;
    String nameF;
    String nickname;
    String bio;
    String emailF;
    String street;
    String city;
    String cap;
    String country;
    String landline;
    String mobile;

    public ObservableList<Account> account = FXCollections.observableArrayList();
    public ObservableList<com.progettooo.rubrica.Model.Contact> address = FXCollections.observableArrayList();
    public ObservableList<com.progettooo.rubrica.Model.Contact> numbers = FXCollections.observableArrayList();

    public ContactImplementation() {
        try {
            connection = Connessione.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String getIdContact(){
        return idContact;
    }


    @Override
    public void AggiungiContatto(newContact newcontact) {
        try {

            PreparedStatement ContactQuery = connection.prepareStatement("CALL create_contact(?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?,?,?,?)");
            ContactQuery.setString(1,newcontact.getFirst_name());
            ContactQuery.setString(2,newcontact.getLast_name());
            ContactQuery.setString(3,"null");
            if (!PrivateR) {
                ContactQuery.setString(4, "public");
            }else{
                ContactQuery.setString(4, "private");
            }
            ContactQuery.setString(5,newcontact.getEmail());
            ContactQuery.setString(6,newcontact.getStreet());
            ContactQuery.setString(7,newcontact.getCity());
            ContactQuery.setInt(8,newcontact.getCap());
            ContactQuery.setString(9,newcontact.getCountry());
            ContactQuery.setString(10,"primary");
            ContactQuery.setString(11,newcontact.getLandline());
            ContactQuery.setString(12,newcontact.getMobile());
            ContactQuery.setString(13,newcontact.getMobile());
            ContactQuery.setString(14,newcontact.getLandline());
            ContactQuery.executeUpdate();

            PreparedStatement query = connection.prepareStatement("Select MAX(idContact) as IdContact from Contact where first_name = " + "'"+newcontact.getFirst_name() + "'"+"and last_name ="+ "'" + newcontact.getLast_name() + "'");
            ResultSet result = query.executeQuery();
            result.next();
            idContact = result.getString("idContact");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ObservableList<Account> getAccount(com.progettooo.rubrica.Model.Contact newcontact) {

        try {
            PreparedStatement query = connection.prepareStatement("Select idAccount,namef,nickname,bio,email from Messaging_account where idContact = " + "'"+newcontact.getIdContact() + "'");
            ResultSet result = query.executeQuery();

            while (result.next()) {
                idAccount = result.getInt("idAccount");
                nameF = result.getString("namef");
                nickname = result.getString("nickname");
                bio = result.getString("bio");
                emailF = result.getString("email");

                account.add(new Account(newcontact.getIdContact(),idAccount,nameF,nickname,bio,emailF));
            }


        }catch (SQLException e){
            e.printStackTrace();
        }

        return account;
    }

    @Override
    public ObservableList<com.progettooo.rubrica.Model.Contact> getSecondaryAddress(com.progettooo.rubrica.Model.Contact newcontact) {

        try {
            PreparedStatement query = connection.prepareStatement("Select idAddress,street,city,postal_code,country from Address where TypeA = 'secondary' AND idContact = " + "'"+newcontact.getIdContact() + "'");
            ResultSet result = query.executeQuery();

            while (result.next()) {
                street = result.getString("street");
                city = result.getString("city");
                cap = result.getString("postal_code");
                country = result.getString("country");
                String idAddress = result.getString("idAddress");
                String addres = street+','+city+','+cap+','+country;

                address.add(new com.progettooo.rubrica.Model.Contact(newcontact.getIdContact(),idAddress,null,null,null,null,addres));
            }


        }catch (SQLException e){
            e.printStackTrace();
        }

        return address;
    }

    @Override
    public ObservableList<com.progettooo.rubrica.Model.Contact> getSecondaryNumber(com.progettooo.rubrica.Model.Contact newcontact, String numero1,String numero2) {
        try {
            PreparedStatement query = connection.prepareStatement("Select M.Number as mobile,L.Number as Landline from Landline L,Mobile M where M.idContact = " + "'"+newcontact.getIdContact() + "'"+"AND L.Mobile=M.Number AND L.idContact =M.idContact"+" AND L.Number <> "+"'"+numero1+"'"+" AND M.Number <> "+"'"+numero2+"'");
            ResultSet result = query.executeQuery();

            while (result.next()) {

                landline = result.getString("landline");
                mobile = result.getString("mobile");

                numbers.add(new com.progettooo.rubrica.Model.Contact(newcontact.getIdContact(),null,null,null,mobile,landline,null));
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return numbers;
    }

    @Override
    public void addAccount(Account account) {
        try {

            PreparedStatement query = connection.prepareStatement("INSERT INTO MESSAGING_ACCOUNT(idContact, NameF, Nickname, Bio, Email) VALUES (?,?,?,?,?)");
            query.setInt(1,account.getIdContact());
            query.setString(2,account.getNameF());
            query.setString(3,account.getNickname());
            query.setString(4,account.getBio());
            query.setString(5,account.getEmailF());
            query.executeUpdate();

            PreparedStatement query2 = connection.prepareStatement("Select MAX(idAccount) as IdAccount from MESSAGING_ACCOUNT where idContact = " + "'" + account.getIdContact() + "'");
            ResultSet result = query2.executeQuery();
            result.next();
            idAccount = result.getInt("idAccount");


        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void addSecondaryNumber(newContact contact) {
        try {

            PreparedStatement query = connection.prepareStatement("INSERT INTO MOBILE(Number, idContact) VALUES (?,?)");
            query.setString(1,contact.getMobile());
            query.setInt(2,contact.getIdContact());
            query.executeUpdate();
            PreparedStatement query2 = connection.prepareStatement("INSERT INTO LANDLINE(Number, idContact,Mobile) VALUES (?,?,?)");
            query2.setString(1,contact.getLandline());
            query2.setInt(2,contact.getIdContact());
            query2.setString(3,contact.getMobile());
            query2.executeUpdate();



        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void addSecondaryAddress(newContact contact) {
        try {

            PreparedStatement query = connection.prepareStatement("INSERT INTO ADDRESS(idContact, street, city, Postal_Code, Country, typeA) VALUES (?,?,?,?,?,'secondary')");
            query.setInt(1,contact.getIdContact());
            query.setString(2,contact.getStreet());
            query.setString(3,contact.getCity());
            query.setInt(4,contact.getCap());
            query.setString(5,contact.getCountry());
            query.executeUpdate();



        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void removeAccount(Account account) {
        try {

            PreparedStatement query = connection.prepareStatement("DELETE FROM MESSAGING_ACCOUNT WHERE idAccount = " + "'"+ account.getIdAccount() + "'");
            query.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void removeAddress(com.progettooo.rubrica.Model.Contact contact, String idAddress) {
        try {

            PreparedStatement query = connection.prepareStatement("DELETE FROM Address WHERE idContact= " + "'"+ contact.getIdContact() + "'"+"AND idAddress = "+ "'"+idAddress+ "'");
            query.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void removeNumbers(com.progettooo.rubrica.Model.Contact contact) {
        try {

            PreparedStatement query = connection.prepareStatement("DELETE FROM LANDLINE WHERE idContact= " + "'"+ contact.getIdContact() + "'"+"AND Number ="+ "'"+contact.getLandline()+ "'");
            query.executeUpdate();
            PreparedStatement query2 = connection.prepareStatement("DELETE FROM Mobile WHERE idContact= " + "'"+ contact.getIdContact() + "'"+"AND Number ="+ "'"+ contact.getMobile()+ "'");
            query2.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
