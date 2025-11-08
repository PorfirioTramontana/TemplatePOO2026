package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta un giudice nella piattaforma, estendendo UtentePiattaforma.
 * Gestisce la valutazione dei team e le assegnazioni agli hackathon.
 */
public class Giudice extends UtentePiattaforma {
    // rappresentazione relazioni
    private final List<Hackathon> hackathonAssegnati = new ArrayList<>(); // lista di Hackathon supervisionati dal giudice
    private final List<Voto> votiAssegnati = new ArrayList<>(); // lista di voti assegnati
    private final List<Organizzatore> organizzatoriInvitanti = new ArrayList<>(); // lista di organizzatori da cui si è stati invitati
    private final List<Team> teamGiudicabili = new ArrayList<>(); // lista dei team valutabili
    private final List<Team> teamGiudicati = new ArrayList<>(); // lista dei team valutati

    // metodi
    /**
     * Costruttore che crea un'istanza di un nuovo Giudice.
     *
     * @param nome il nome del Giudice
     * @param cognome il cognome del Giudice
     * @param email l'email del Giudice
     * @param password la password del Giudice
     */
    public Giudice(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password, "giudice"); // chiama il costruttore padre
    }

    /**
     * Restituisce la lista degli hackathon assegnati.
     *
     * @return lista di Hackathon
     */
    public List<Hackathon> getHackathonAssegnati() { return hackathonAssegnati; }

    /**
     * Aggiunge un Hackathon alla lista degli assegnati.
     *
     * @param hackathon hackathon da aggiungere
     */
    public void addHackathonAssegnati(Hackathon hackathon) {
        hackathonAssegnati.add(hackathon);
    }

    /**
     * Aggiunge un Hackathon assegnato con verifica dell'organizzatore.
     *
     * @param hackathon hackathon da aggiungere
     * @param organizzatore organizzatore che assegna
     * @throws IllegalArgumentException se Hackathon o organizzatore non validi
     * @throws SecurityException se l'organizzatore non è autorizzato
     */
    public void aggiungiHackathonAssegnato(Hackathon hackathon, Organizzatore organizzatore) {
        // verifica che l'Hackathon esista
        if (hackathon == null) {
            throw new IllegalArgumentException("Hackathon non valido!");
        }
        // verifica che l'organizzatore esista
        if (organizzatore == null) {
            throw new IllegalArgumentException("Organizzatore non valido!");
        }
        // verifica che l'Hackathon dove viene convocato il giudice sia stato creato dall'organizzatore che lo vuole inserire
        if (!organizzatore.getHackathonCreati().contains(hackathon)) {
            throw new SecurityException("Solo l'organizzatore creatore può convocare giudici!");
        }
        // verifica che il giudice non sia stato già convocato nell'Hackathon selezionato
        if (organizzatore.getGiudiciConvocati().contains(this) && this.getHackathonAssegnati().contains(hackathon)) {
            throw new SecurityException("Il giudice selezionato è già stato convocato in questo Hackathon!");
        }
        hackathonAssegnati.add(hackathon); // aggiunta del nuovo Hackathon alla lista hackathonAssegnati
    }

    /**
     * Restituisce la lista dei voti assegnati.
     *
     * @return lista di Voto
     */
    public List<Voto> getVotiAssegnati() { return votiAssegnati; }

    /**
     * Aggiunge un voto alla lista dei voti assegnati.
     *
     * @param voto voto da aggiungere
     */
    public void addVotiAssegnati(Voto voto) {
        votiAssegnati.add(voto);
    }

    /**
     * Assegna un voto a un team.
     *
     * @param team team da valutare
     * @param valore valore del voto (0-10)
     * @throws IllegalArgumentException se il voto non è valido
     * @throws SecurityException se il giudice non è autorizzato
     */
    public void assegnaVoto(Team team, int valore) {
        // verica che il valore da assegnare come voto sia compreso tra 0 e 10
        if (valore < 0 || valore > 10) {
            throw new IllegalArgumentException("Voto non valido!");
        }

        // verifica che il giudice sia abilitato a valutare un team in un determinato Hackathon
        // verifica che team è in teamGiudicabili
        if (!(getTeamGiudicabili().contains(team))) {
            throw new SecurityException("Il giudice selezionato non ha il permesso di giudicare questo team!");
        }

        // verifica che il giudice non abbia già valutato il team
        // verifica che team non è in teamGiudicati
        if (getTeamGiudicati().contains(team)) {
            throw new IllegalArgumentException("Il giudice ha già valutato questo team");
        }

        Voto voto = new Voto(this, team, valore);
        team.aggiungiVoto(this, voto); // potrebbe lanciare eccezione nel caso in cui i team non siano ancora valutabili
        team.getHackathon().addVoto(team);
        votiAssegnati.add(voto);
        giudicaTeam(team);
    }

    /**
     * Restituisce la lista degli organizzatori invitanti.
     *
     * @return lista di Organizzatore
     */
    public List<Organizzatore> getOrganizzatoriInvitanti() { return organizzatoriInvitanti; }

    /**
     * Aggiunge un organizzatore alla lista degli invitanti.
     *
     * @param organizzatore organizzatore da aggiungere
     */
    public void addOrganizzatoriInvitanti(Organizzatore organizzatore) {
        organizzatoriInvitanti.add(organizzatore);
    }

    /**
     * Aggiunge un organizzatore invitante con verifica.
     *
     * @param organizzatore organizzatore da aggiungere
     * @throws IllegalArgumentException se l'organizzatore non è valido
     */
    public void aggiungiOrganizzatoriInvitanti(Organizzatore organizzatore) {
        if (organizzatore == null) {
            throw new IllegalArgumentException("Organizzatore non valido!");
        }
        organizzatoriInvitanti.add(organizzatore);
    }

    /**
     * Restituisce la lista dei team giudicabili.
     *
     * @return lista di Team
     */
    public List<Team> getTeamGiudicabili() { return teamGiudicabili; }

    /**
     * Aggiunge un team alla lista dei giudicabili.
     *
     * @param team team da aggiungere
     */
    public void addTeamGiudicabili(Team team) {
        teamGiudicabili.add(team);
    }

    /**
     * Aggiunge un team giudicabile.
     *
     * @param team team da aggiungere
     */
    public void aggiungiTeam(Team team) {
        teamGiudicabili.add(team);
    }

    /**
     * Restituisce la lista dei team giudicati.
     *
     * @return lista di Team
     */
    public List<Team> getTeamGiudicati() { return teamGiudicati; }

    /**
     * Aggiunge un team alla lista dei giudicati.
     *
     * @param team team da aggiungere
     */
    public void addTeamGiudicati(Team team) {
        teamGiudicati.add(team);
    }

    // metodo per aggiungere un team alla lista teamGiudicati (chiamato esclusivamente dal metodo assegna voto)
    private void giudicaTeam(Team team) {
        teamGiudicati.add(team);
        teamGiudicabili.remove(team);
    }

    /**
     * Verifica se il giudice è assegnato a un team.
     *
     * @param team team da verificare
     * @return true se assegnato, false altrimenti
     */
    public boolean isAssegnato(Team team) {
        for (Team teamG :  teamGiudicabili) {
            if (teamG.getNome().equals(team.getNome()) && teamG.getHackathon().getTitolo().equals(team.getHackathon().getTitolo())) {
                return true;
            }
        }
        return false;
    }
}
