package com.progettooo.rubrica.Model;

public class newContactModel {

    String first_name;
    String last_name;
    String email;
    String mobile;
    String landline;
    String street;
    Integer cap;
    String city;
    String country;
    Integer idContact;

    public newContactModel(String first_name, String last_name, String email, String mobile, String landline, String street, Integer cap, String city, String country) {
        this.idContact = null;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.mobile = mobile;
        this.landline = landline;
        this.street = street;
        this.cap = cap;
        this.city = city;
        this.country = country;
    }

    public Integer getIdContact() {return idContact;}

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getLandline() {
        return landline;
    }

    public String getStreet() {
        return street;
    }

    public Integer getCap() {
        return cap;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public Contact newContactToContact(newContactModel contact){
        String Address = this.getStreet()+","+this.getCity()+","+this.getCap()+","+this.getCountry();
        return new Contact(this.getIdContact(),this.getEmail(),this.getFirst_name(),this.getLast_name(),this.getMobile(),this.getLandline(),Address);

    }

    public void setIdContact(int idContact) {
        this.idContact = idContact;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCap(Integer cap) {
        this.cap = cap;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
