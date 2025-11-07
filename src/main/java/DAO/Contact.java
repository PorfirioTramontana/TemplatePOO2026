package com.progettooo.rubrica.DAO;

import com.progettooo.rubrica.Model.Account;
import com.progettooo.rubrica.Model.newContact;
import javafx.collections.ObservableList;

public interface Contact {

    void AggiungiContatto(newContact newcontact);
    ObservableList<Account> getAccount(com.progettooo.rubrica.Model.Contact newcontact);
     String getIdContact();
     void addAccount(Account account);

    void addSecondaryNumber(newContact contact);
    public ObservableList<com.progettooo.rubrica.Model.Contact> getSecondaryAddress(com.progettooo.rubrica.Model.Contact newcontact);
     ObservableList<com.progettooo.rubrica.Model.Contact> getSecondaryNumber(com.progettooo.rubrica.Model.Contact newcontact,String landline,String mobile);

    void addSecondaryAddress(newContact contact);

    void removeAccount(Account account);

    void removeAddress(com.progettooo.rubrica.Model.Contact contact, String idAddress);

    void removeNumbers(com.progettooo.rubrica.Model.Contact contact);
}
