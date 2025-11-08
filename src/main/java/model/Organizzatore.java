package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta un organizzatore nella piattaforma, estendendo UtentePiattaforma.
 * Gestisce la creazione di Hackathon e la convocazione dei giudici.
 */
public class Organizzatore extends UtentePiattaforma {
    // rappresentazione relazioni
    private final List<Hackathon> hackathonCreati = new ArrayList<>();
    private final List<Giudice> giudiciConvocati = new ArrayList<>();

    //metodi
    /**
     * Costruttore che crea un'istanza di un nuovo Organizzatore.
     *
     * @param nome il nome del Organizzatore
     * @param cognome il cognome del Organizzatore
     * @param email l'email del Organizzatore
     * @param password la password del Organizzatore
     */
    public Organizzatore(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password, "organizzatore"); // chiama il costruttore padre
    }

    /**
     * Restituisce la lista degli Hackathon creati.
     *
     * @return lista di Hackathon
     */
    public List<Hackathon> getHackathonCreati() { return hackathonCreati; }

    /**
     * Aggiunge un Hackathon alla lista dei creati.
     *
     * @param hackathon Hackathon da aggiungere
     */
    public void addHackathonCreati(Hackathon hackathon) {
        hackathonCreati.add(hackathon);
    }

    /**
     * Crea un nuovo Hackathon con i parametri specificati.
     *
     * @param titolo titolo dell'Hackathon
     * @param numMaxIscritti numero massimo iscritti
     * @param dimMaxTeam dimensione massima team
     * @param inizioIscrizioni data inizio iscrizioni
     * @param dataInizio data inizio Hackathon
     * @param dataFine data fine Hackathon
     * @param indirizzoSede indirizzo della sede
     * @return hackathon creato
     * @throws IllegalArgumentException se le date non sono valide
     */
    public Hackathon creaHackathon(String titolo, int numMaxIscritti, int dimMaxTeam, LocalDate inizioIscrizioni, LocalDate dataInizio, LocalDate dataFine, String indirizzoSede) {
        // verifica che la data di inizio Hackathon non venga dopo la data di fine Hackathon
        if (dataInizio.isAfter(dataFine)) {
            throw new IllegalArgumentException("Errore: date di inizio/fine non valide");
        }
        // verifica che la data di inizio iscrizioni sia almeno 2 giorni prima dalla data di inizio Hackathon
        if (!inizioIscrizioni.isBefore(dataInizio.minusDays(1))) {
            throw new IllegalArgumentException("Errore: data di inizio iscrizioni non valida");
        }
        Hackathon hackathon = new Hackathon(this, titolo, numMaxIscritti, dimMaxTeam, inizioIscrizioni, dataInizio, dataFine, indirizzoSede);  // Passa l'organizzatore come creatore
        hackathonCreati.add(hackathon);  // aggiunta dell'Hackathon creato alla lista hackathonCreati
        return hackathon;
    }

    /**
     * Restituisce la lista dei giudici convocati.
     *
     * @return lista di Giudice
     */
    public List<Giudice> getGiudiciConvocati() { return giudiciConvocati; }

    /**
     * Convoca un giudice a un Hackathon specifico.
     *
     * @param hackathon Hackathon a cui convocare
     * @param giudice giudice da convocare
     * @throws IllegalStateException se lo stato della gara non è valido
     */
    public void convocaGiudice(Hackathon hackathon, Giudice giudice) {
        hackathon.verificaStatoGara("Iscrizioni non ancora aperte", "convocare il giudice");

        giudice.aggiungiHackathonAssegnato(hackathon, this); // aggiunta dell'Hackathon alla lista hackathonAssegnati del giudice
        giudice.aggiungiOrganizzatoriInvitanti(this); // aggiunta dell'organizzatore alla lista organizzatoriInvitanti del giudice
        hackathon.aggiungiGiudice(giudice, this); // aggiunta del giudice alla lista giudiceList dell'Hackathon
        giudiciConvocati.add(giudice); // aggiunta del giudice assegnato alla lista giudiciConvocati
    }
}