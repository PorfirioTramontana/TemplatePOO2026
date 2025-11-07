package controller;
import implementazioni.postgres.dao.ImplementazioneDatabase;
import dao.DAO;
import model.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classe fonamentale per l'interazione tra l'interfaccia grafica e il database, con logica complessa.
 */

public class Controller {

    private final DAO dao = new ImplementazioneDatabase();
    Random random = new Random();

    /**
     * Metodo per la creazione del numero del biglietto di una prenotazione.
     * Genera una stringa casuale formata da lettere e numeri.
     * @return la stringa da assegnare alla prenotazione.
     */
    public String creaNumeroBiglietto() {
        String chars = "ABCDEFGHIGKLMNOPQRSTUVWXYZ";
        String num = "0123456789";

        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < 3; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        for (int i = 0; i < 4; i++) {
            sb.append(num.charAt(random.nextInt(num.length())));
        }

        return sb.toString();
    }

    /**
     * Metodo per la creazione di una prenotazione.
     * Crea l'oggetto prenotazione mettendo insieme le informazioni possedute e le invia al database, insieme
     * all'identificativo dell'utente.
     * @param idUtente rappresenta l'identificativo dell'utente che sta effettuando la prenotazione.
     * @param volo rappresenta il volo prenotato
     * @param nome del passeggero
     * @param cognome del passeggero
     * @param posto indica il posto scelto dall'utente
     * @return {@code true} se la prenotazione è stata creata, {@code false} se non lo è.
     */
    public boolean creaPrenotazione(int idUtente, Voli volo, String nome, String cognome, String posto) {
        String codiceVolo = volo.getCodiceVolo();
        String numeroBiglietto = creaNumeroBiglietto();
        Passeggero passeggero = new Passeggero(nome, cognome);
        StatoVolo statoVolo= volo.getStatoEnum();
        StatoPrenotazione stato;
        if(statoVolo == StatoVolo.programmato){
            stato = StatoPrenotazione.inAttesa;
        }
        else{
            throw new IllegalArgumentException("Non puoi prenotare un volo non \"Programmato\"");
        }
        Prenotazione prenotazione = new Prenotazione(passeggero, codiceVolo, posto, numeroBiglietto, volo.getProvenienza(), volo.getDestinazione(),
        volo.getDataPartenza(), volo.getOrarioPartenza(), stato);
        return dao.creaPrenotazione(prenotazione, idUtente);

    }

    /**
     * Metodo che restituisce una lista di tutte le prenotazioni dell'utente.
     * @param idUtente per identificare l'utente
     * @return la lista delle prenotazioni legate a quell'utente
     */
    public List<Prenotazione> visualizzaPrenotazioni(int idUtente) {
        return dao.getPrenotazioni(idUtente);
    }

    /**
     * Metodo per la modifica di una prenotazione, controlla se la nuova prenotazione deve avere lo stesso posto e nome diverso,
     * oppure stesso nome e posto diverso, oppure entrambi diversi.
     * @param nuovoPasseggero le nuove credenziali del passeggero
     * @param vecchiaPrenotazione la prenotazione da modificare
     * @param nuovoPosto il nuovo posto scelto
     * @return {@code true} se la prenotazione è stata modificata, {@code false} se non è stata modificata.
     * @throws SQLException nel caso in cui c'è un errore nel database
     */
    public boolean modificaPrenotazione(Passeggero nuovoPasseggero, Prenotazione vecchiaPrenotazione, String nuovoPosto) throws SQLException{
        if(nuovoPosto.isEmpty() && !nuovoPasseggero.getNome().isEmpty()){
            vecchiaPrenotazione.setPasseggero(nuovoPasseggero);
            return dao.modificaPrenotazione(vecchiaPrenotazione, true, false);
        }
        else if(!nuovoPosto.isEmpty() && nuovoPasseggero.getNome().isEmpty()){
            vecchiaPrenotazione.setPostoAssegnato(nuovoPosto);
            return dao.modificaPrenotazione(vecchiaPrenotazione, false, true);
        }
        else{
            vecchiaPrenotazione.setPasseggero(nuovoPasseggero);
            vecchiaPrenotazione.setPostoAssegnato(nuovoPosto);
            return dao.modificaPrenotazione(vecchiaPrenotazione, false, false);
        }
    }

    /**
     * Metodo per la cancellazione di una prenotazione da parte dell'utente
     * @param prenotazione da cancellare
     * @return {@code true} se la prenotazione è stata cancellata, {@code false} se non è stata cancellata
     * @throws SQLException lancia in caso di problemi al database
     */
    public boolean cancellaPrenotazione(Prenotazione prenotazione) throws SQLException{
        return dao.cancellaPrenotazione(prenotazione);
    }

    /**
     * Metodo per la ricerca di un volo da parte dell'utente.
     * @param partenza del volo desiderato
     * @param destinazione del volo desiderato
     * @return una lista di voli che rispettano i criteri stabiliti dalla partenza e dalla destinazione
     * @throws SQLException lancia in caso di problemi al database.
     */
    public List<Voli> ricercaVoli(String partenza, String destinazione) throws SQLException{
        if(partenza.isEmpty() && destinazione.isEmpty()){
            return dao.getTuttiVoli();
        }
        return dao.ricercaVoli(partenza, destinazione);
    }

    /**
     * Metodo per ottenere una lista di tutti i voli.
     * @return l lista di tutti i voli
     * @throws SQLException in caso di problemi al database
     */
    public List<Voli> visualizzaVoli() throws SQLException {
        return dao.getTuttiVoli();
    }

    /**
     * Metodo per aggiungere un volo manualmente, per conto dell'amministratore.
     * @param volo il volo da inserire
     * @return {@code true} se è stato aggiunto, {@code false} se non è stato aggiunto
     * @throws SQLException in caso di problemi di connessione
     */
    public boolean aggiungiVolo(Voli volo) throws SQLException{
        return dao.aggiungiVolo(volo);
    }

    /**
     * Metodo per ottenere una lista di tutti i voli senza gate
     * @return una lista di voli il cui gate non è stato ancora assegnato
     * @throws SQLException in caso di problemi al database
     */
    public List<Voli> getVoliGate() throws SQLException{
        return dao.getVoliGate();
    }

    /**
     * Metodo per salvare l'assegnazione di un gate ad un volo.
     * @param gate il gate da assegnare
     * @param volo il volo da assegnare al gate
     * @return {@code true} se è stato assegnato, {@code false} se non è stato assegnato
     */
    public boolean salvaGate(Gate gate, Voli volo) {
        return dao.assegnaGate(gate, volo);
    }

    /**
     * Metodo per l'accesso al sistema. Manda al DAO l'utente per effettuare il controllo
     * @param utente che desidera accedere
     * @return {@code true} se l'utente è presente e può accedere, {@code false} se non è presente
     */
    public boolean accessoUtente(Utenti utente){
        try{
            return dao.accessoUtente(utente);
        }
        catch(SQLException _){
            return false;
        }

    }

    /**
     * Metodo per registrare un nuovo utente nel database.
     * @param utente il nuovo utente
     * @return {@code true} se l'utente è stato aggiunto con successo, {@code false} se non è stato aggiunto
     */
    public boolean registraUtente(Utenti utente) {
        return dao.registrazioneUtente(utente);
    }

    /**
     * Metodo per ottenere la flag di identificazione dell'admin. Serve a controllare che l'utente che effettua l'accesso
     * sia un Amministratore
     * @param utente l'utente da verificare
     * @return un oggetto Amministratori, se l'utente ha la flag dell'amministratore nel database, altrimenti un oggetto
     * UtenteGenerico.
     */
    public Utenti getAdminFlag(Utenti utente){
        if(dao.isAdmin(utente)){
            return new Amministratori(utente);
        }
        else{
            return new UtenteGenerico(utente);
        }
    }

    /**
     * Metodo per la modifica di un volo da parte dell'admin.
     * Prende in input i nuovi dati del volo e crea l'oggetto volo del nuovo volo.
     * @param vecchioVolo rappresenta il volo da modificare
     * @param partenza nuova partenza
     * @param destinazione nuova destinazione
     * @param data nuova data
     * @param ora nuova ora
     * @param stato nuovo stato del volo
     * @throws IllegalArgumentException nel caso di parametri non corretti
     */
    public void modificaVolo(Voli vecchioVolo, String partenza, String destinazione, LocalDate data, LocalTime ora, StatoVolo stato) throws IllegalArgumentException{
        Voli nuovoVolo = new Voli(vecchioVolo.getCodiceVolo(), partenza, destinazione,
                vecchioVolo.getCompagniaAerea(), data, ora, StatoVolo.getStatoStatic(stato), vecchioVolo.getGate());

        dao.aggiornaVolo(vecchioVolo, nuovoVolo);
    }

    /**
     * Metodo per ottenere gli stati liberi escludendo quello attuale del volo.
     * @param volo da cui prendere lo stato attuale
     * @return una lista degli altri stati disponibili
     */
    public List<String> getStatoVolo(Voli volo){
        StatoVolo stato = volo.getStatoEnum();
        return new ArrayList<>(StatoVolo.getStatoVolo(stato));
    }

    /**
     * Metodo per ottenere la lista completa degli stati di un volo
     * @return la lista di tutti i possibili stati di un volo
     */
    public List<String> getStatoVolo(){
        return StatoVolo.getStatoVolo();
    }

    /**
     * Metodo per ottenere una lista dei gate liberi, ovvero quelli senza nessun volo assegnato.
     * @return una lista di gate, filtrati per i liberi
     */
    public List<Gate> getGateLiberi(){
        return dao.getGateLiberi();
    }

    /**
     * Metodo per ottenere il gate di un volo prenotato
     * @param p la prenotazione dell'utente che desidera vedere il gate che è stato assegnato
     * @return una stringa contenente l'informazione "Non Assegnato" se il gate non è stato ancora assegnato, e l'id del
     * gate nel caso in cui sia stato già assegnato
     * @throws SQLException per problemi di connesione al database
     */
    public String getGatePrenotazione(Prenotazione p) throws SQLException{
        if(dao.getGateVolo(p) == 0){
            return "Non Assegnato";
        }
        else{
            return "" + dao.getGateVolo(p);
        }
    }

    /**
     * Metodo per generare un codice volo una volta aggiunto manualmente un volo.
     * Genera una stringa di caratteri e numeri formattato in modo da essere conforme ai codici volo
     * @return una stringa rappresentante il codice volo appena creato del nuovo volo aggiunto.
     */
    public String generaCodiceVolo() {

        char lettera1 = (char) ('A' + random.nextInt(26));
        char lettera2 = (char) ('A' + random.nextInt(26));

        int numero = random.nextInt(1000); // da 0 a 999
        String numeroFormattato = String.format("%03d", numero); //formatta la stringa

        return "" + lettera1 + lettera2 + numeroFormattato;
    }
}
