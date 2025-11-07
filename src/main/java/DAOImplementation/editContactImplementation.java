package com.progettooo.rubrica.DAOImplementation;

import com.progettooo.rubrica.DAO.editContact;
import com.progettooo.rubrica.Model.Contact;
import com.progettooo.rubrica.Model.newContact;
import com.progettooo.rubrica.database.Connessione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class editContactImplementation implements editContact {

    Connection connection;

    public editContactImplementation() {
        try {
            connection = Connessione.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void EditContact(newContact contact) {

        try {
            PreparedStatement statement = connection.prepareStatement("Select C.idContact,C.first_name,C.last_name,E.email,A.street,A.city,A.postal_code,A.country,M.number as mobile,L.number as landline FROM CONTACT C,email E,landline L,mobile M,ADDRESS A where C.idContact = E.idContact AND E.idContact = L.idContact AND L.idContact = M.idContact and M.idContact = A.idContact and A.typeA = 'primary' And C.IdContact= "+ contact.getIdContact());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

                        PreparedStatement queryF = connection.prepareStatement("Update Contact set first_name ="+ "'" + contact.getFirst_name() + "'"+"where idContact ="+"'"+contact.getIdContact()+"'"+" AND first_name = "+"'"+resultSet.getString("first_name")+"'");
                        queryF.executeUpdate();
                        PreparedStatement queryLa = connection.prepareStatement("Update Contact set last_name ="+ "'" + contact.getLast_name() + "'"+"where idContact ="+"'"+contact.getIdContact()+"'"+" AND last_name = "+"'"+resultSet.getString("last_name")+"'");
                        queryLa.executeUpdate();
                        PreparedStatement queryE = connection.prepareStatement("Update Email set email ="+ "'" + contact.getEmail() + "'"+"where idContact ="+"'"+contact.getIdContact()+"'"+" AND email = "+"'"+resultSet.getString("email")+"'");
                        queryE.executeUpdate();
                        PreparedStatement queryS = connection.prepareStatement("Update Address set street ="+ "'" + contact.getStreet() + "'"+"where idContact ="+"'"+contact.getIdContact()+"'"+" AND street = "+"'"+resultSet.getString("street")+"'");
                        queryS.executeUpdate();
                        PreparedStatement queryC = connection.prepareStatement("Update Address set city ="+ "'" + contact.getCity() + "'"+"where idContact ="+"'"+contact.getIdContact()+"'"+" AND City = "+"'"+resultSet.getString("city")+"'");
                        queryC.executeUpdate();
                        PreparedStatement queryCap = connection.prepareStatement("Update Address set postal_code ="+ "'" + contact.getCap() + "'"+"where idContact ="+"'"+contact.getIdContact()+"'"+" AND postal_code = "+"'"+resultSet.getString("postal_code")+"'");
                        queryCap.executeUpdate();
                        PreparedStatement queryM = connection.prepareStatement("Update Mobile set number ="+ "'" + contact.getMobile() + "'"+"where idContact ="+"'"+contact.getIdContact()+"'"+" AND Number = "+"'"+resultSet.getString("mobile")+"'");
                        queryM.executeUpdate();
                        PreparedStatement queryL = connection.prepareStatement("Update landline set number ="+ "'" + contact.getLandline() + "'"+"where idContact ="+"'"+contact.getIdContact()+"' AND Number = "+"'"+resultSet.getString("landline")+"'");
                        queryL.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void DeleteContact(Contact contact) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM CONTACT WHERE idContact = "+ "'" + contact.getIdContact() + "'");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
