package dao;
import model.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Classe interfaccia con i metodi da implementare per la comunicazione al database.
 */
public interface DAO {

    //Metodi di login
    boolean registrazioneUtente(Utenti utente);
    boolean isAdmin(Utenti utente);
    boolean accessoUtente(Utenti utente) throws SQLException;

    //Metodi generali
    List<Voli> getTuttiVoli() throws SQLException;
    int getGateVolo(Prenotazione p) throws SQLException;

    //Metodi user
    List<Voli> ricercaVoli(String partenza, String destinazione) throws SQLException;
    List<Prenotazione> getPrenotazioni(int idUtente);
    boolean creaPrenotazione(Prenotazione prenotazione, int idUtente);
    boolean modificaPrenotazione(Prenotazione p, boolean stessoPosto, boolean stessoPasseggero) throws SQLException;
    boolean cancellaPrenotazione(Prenotazione p) throws SQLException;


    //Metodi admin
    boolean aggiungiVolo(Voli nuovoVolo) throws SQLException;
    void aggiornaVolo(Voli voloCorrente, Voli nuovoVolo);
    boolean assegnaGate(Gate gate, Voli volo);
    List<Voli> getVoliGate() throws SQLException;
    List<Gate> getGateLiberi();
}
