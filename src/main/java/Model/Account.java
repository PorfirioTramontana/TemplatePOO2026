package com.progettooo.rubrica.Model;

public class Account {
    Integer idContact;
    Integer idAccount;
    String nameF;
    String nickname;
    String bio;
    String emailF;

    public Account(Integer idContact, Integer idAccount, String nameF, String nickname, String bio, String emailF) {
        this.idContact = idContact;
        this.idAccount = idAccount;
        this.nameF = nameF;
        this.nickname = nickname;
        this.bio = bio;
        this.emailF = emailF;
    }

    public Integer getIdContact() {
        return idContact;
    }

    public Integer getIdAccount() {
        return idAccount;
    }

    public String getNameF() {
        return nameF;
    }

    public String getNickname() {
        return nickname;
    }

    public String getBio() {
        return bio;
    }

    public String getEmailF() {
        return emailF;
    }
}
