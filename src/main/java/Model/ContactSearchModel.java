package com.progettooo.rubrica.Model;

import com.progettooo.rubrica.database.Connessione;

public class ContactSearchModel {
    Integer idContact;
    String email;
    String first_name;
    String last_name;
    String mobile;
    String Address;
    String landline;


    public ContactSearchModel(Integer idContact, String email, String first_name, String last_name, String mobile, String landline, String Address) {
        this.idContact = idContact;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.mobile = mobile;
        this.landline = landline;
        this.Address = Address;
    }

    public Integer getIdContact() {
        return idContact;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getLandline() {
        return landline;
    }

    public String getAddress() {return Address;}


}
