package com.progettooo.rubrica.DAOImplementation;

import com.progettooo.rubrica.DAO.newContact;
import com.progettooo.rubrica.Model.newContactModel;
import com.progettooo.rubrica.database.Connessione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.progettooo.rubrica.controller.PasswordController.PrivateR;

public class newContactImplementation implements newContact {

    Connection connection;
    String idContact;

    public newContactImplementation() {
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
    public void AggiungiContatto(newContactModel newcontact) {
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
}
