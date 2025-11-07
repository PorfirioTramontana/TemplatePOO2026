package com.progettooo.rubrica.DAOImplementation;

import com.progettooo.rubrica.DAO.PrivateContact;
import com.progettooo.rubrica.Model.Contact;
import com.progettooo.rubrica.database.Connessione;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrivateContactImplementation implements PrivateContact {

    Connection connection;
    String idContact;
    ObservableList<Contact> contactObservableList = FXCollections.observableArrayList();

    public PrivateContactImplementation() {
        try {
            connection = Connessione.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public ObservableList<Contact> getPrivateContact() {
        try {
            PreparedStatement statement = connection.prepareStatement("Select C.idContact,C.first_name,C.last_name,E.email,A.street,A.city,A.postal_code,A.country,M.number as mobile,L.number as landline FROM CONTACT C,email E,landline L,mobile M,ADDRESS A where C.idContact = E.idContact AND E.idContact = L.idContact AND L.idContact = M.idContact and M.idContact = A.idContact and C.type = 'private'");
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

        } catch (SQLException e){
        }

        return contactObservableList;
    }
}
