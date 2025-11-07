package com.progettooo.rubrica.DAO;

import com.progettooo.rubrica.Model.Contact;
import com.progettooo.rubrica.Model.Groups;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

public interface Group {
    ObservableList<Groups> getGroup();
    void addGroup(Groups group);
    boolean addToGroup(Contact contact, Groups group);
    void editGroup(Groups group, String name);
    ObservableList<Contact> getPartecipants(Groups group);
    void removePartecipant(Groups group,Contact contact);
    void deleteGroup(Groups group);
}
