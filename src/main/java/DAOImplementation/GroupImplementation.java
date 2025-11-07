package com.progettooo.rubrica.DAOImplementation;

import com.progettooo.rubrica.DAO.Group;
import com.progettooo.rubrica.Model.Contact;
import com.progettooo.rubrica.Model.Groups;
import com.progettooo.rubrica.database.Connessione;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupImplementation implements Group {

    Connection connection;
    ObservableList<Groups> GroupObservableList = FXCollections.observableArrayList();

    public GroupImplementation() {
        try {
            connection = Connessione.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ObservableList<Groups> getGroup() {
        try {

            PreparedStatement GroupQuery = connection.prepareStatement("SELECT * FROM GROUPS");
            ResultSet result = GroupQuery.executeQuery();

            while (result.next()){

                String name = result.getString("name");
                String description = result.getString("description");


                GroupObservableList.add(new Groups(name,description));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return GroupObservableList;
    }

    @Override
    public void addGroup(Groups group) {
        try {

            PreparedStatement GroupQuery = connection.prepareStatement("Insert INTO GROUPS VALUES(?,?)");
            GroupQuery.setString(1,group.getName());
            GroupQuery.setString(2,group.getDescription());
            GroupQuery.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean addToGroup(Contact contact, Groups group) {

        String name = group.getName();
        int id = contact.getIdContact();

        try {

            PreparedStatement CheckQuery = connection.prepareStatement("SELECT IdContact,name from CONTACT_GROUP WHERE IdContact ="+"'"+ id +"'"+"AND name= "+"'"+ name +"'");
            ResultSet result = CheckQuery.executeQuery();

            if (result.next()) {
                return true;
            }

            PreparedStatement GroupQuery = connection.prepareStatement("INSERT INTO CONTACT_GROUP VALUES(?,?)");
            GroupQuery.setString(1,name);
            GroupQuery.setInt(2,id);
            GroupQuery.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void editGroup(Groups group, String name) {
        try {
            PreparedStatement statement = connection.prepareStatement("Select Name from Groups where name= "+"'"+name+"'");
            ResultSet result = statement.executeQuery();
            result.next();


            PreparedStatement queryN = connection.prepareStatement("Update Groups set name ="+ "'" + group.getName() + "'"+"where name ="+"'"+result.getString("name")+"'");
            queryN.executeUpdate();
            PreparedStatement queryD = connection.prepareStatement("Update Groups set description ="+ "'" + group.getDescription() + "'"+"where name ="+"'"+group.getName()+"'");
            queryD.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ObservableList<Contact> getPartecipants(Groups group) {

        ObservableList<Contact> contact = FXCollections.observableArrayList();

        try {
            PreparedStatement statement = connection.prepareStatement("Select C.idContact,C.first_name,C.last_name,E.email,A.street,A.city,A.postal_code,A.country,M.number as mobile,L.number as landline FROM CONTACT C,email E,landline L,mobile M,ADDRESS A,Contact_Group CG where C.idContact = E.idContact AND E.idContact = L.idContact AND L.idContact = M.idContact and M.idContact = A.idContact and CG.name = "+"'"+group.getName()+"'"+" and C.idContact = CG.IdContact");
            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {

                String name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                Integer idContact = resultSet.getInt("idContact");
                String email = resultSet.getString("email");
                String mobile = resultSet.getString("mobile");
                String landline = resultSet.getString("landline");
                String Address = resultSet.getString("street") + "," + resultSet.getString("city") + "," + resultSet.getString("postal_code") + "," + resultSet.getString("country");


                contact.add(new Contact(idContact, email, name, last_name, mobile, landline, Address));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return contact;
    }

    @Override
    public void removePartecipant(Groups group, Contact contact) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Contact_Group WHERE idContact = "+ "'" + contact.getIdContact() + "'"+"AND name = "+"'"+group.getName()+"'");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteGroup(Groups group) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Groups WHERE name = "+ "'" + group.getName()+ "'");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
