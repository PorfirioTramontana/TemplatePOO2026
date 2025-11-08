package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta un concorrente nella piattaforma, estendendo UtentePiattaforma.
 * Gestisce la partecipazione ai team e le relative operazioni.
 */
public class Concorrente extends UtentePiattaforma {
    // rappresentazione relazioni
    private final List<Team> teamsAppartenenza = new ArrayList<>();

    // metodi
    /**
     * Costruttore che crea un'istanza di un nuovo Concorrente.
     *
     * @param nome il nome del Concorrente
     * @param cognome il cognome del Concorrente
     * @param email l'email del Concorrente
     * @param password la password del Concorrente
     */
    public Concorrente(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password, "concorrente"); // chiama il costruttore padre
    }

    /**
     * Restituisce la lista dei team a cui appartiene il concorrente.
     *
     * @return lista di Team a cui il concorrente partecipa
     */
    public List<Team> getListTeamAppartenenza() { return teamsAppartenenza; }

    /**
     * Aggiunge un team alla lista di appartenenza del concorrente.
     *
     * @param team il team da aggiungere
     */
    public void addTeamAppartenenza(Team team) {
        teamsAppartenenza.add(team);
    }

    /**
     * Crea un nuovo team per l'Hackathon specificato.
     *
     * @param nome nome del team
     * @param password password del team
     * @param hackathon hackathon a cui partecipare
     * @return il team creato
     * @throws IllegalArgumentException se esiste già un team con lo stesso nome
     * @throws IllegalStateException se il concorrente è già iscritto all'Hackathon
     */
    public Team creaTeam(String nome, String password, Hackathon hackathon) {
        hackathon.verificaStatoGara("Iscrizioni aperte", "creare un nuovo team");

        // verifica se esiste già un team con lo stesso nome nello stesso Hackathon
        if (hackathon.esisteTeam(nome)) {
            throw new IllegalArgumentException("Esiste già un team con nome '" + nome + "' iscritto all'Hackathon: '" + hackathon.getTitolo()+ "'!");
        }
        // verifica che il concorrente non partecipi già all'hackathon con un altro team
        String teamEsistente = isPartecipanteOfHackathon(hackathon); // variabile che individua il team, se esiste, a cui è già iscritto il concorrente
        if (teamEsistente != null) {
            throw new IllegalStateException("Impossibile creare un nuovo team: concorrente già iscritto al team '" + teamEsistente + "'");
        }

        Team team = new Team(nome, password, hackathon, this);
        hackathon.aggiungiTeam(team, this);
        // aggiunge il team alla lista teamAppartenenza del concorrente
        teamsAppartenenza.add(team);
        // aggiunge il concorrente al team
        team.aggiungiMembro(this);
        // incrementa di uno il numero di concorrenti partecipanti all'Hackathon
        hackathon.addIscritto(team);
        return team;
    }

    /**
     * Partecipa a un team esistente.
     *
     * @param nomeTeam nome del team
     * @param password password del team
     * @param hackathon hackathon a cui partecipare
     * @throws IllegalStateException se il concorrente è già iscritto all'Hackathon
     * @throws IllegalArgumentException se il team non esiste
     * @throws SecurityException se la password è errata
     */
    public void partecipaTeam(String nomeTeam, String password, Hackathon hackathon) {
        hackathon.verificaStatoGara("Iscrizioni aperte", "partecipare al team");

        // verifica che il concorrente non partecipi già all'hackathon con un altro team
        String teamEsistente = isPartecipanteOfHackathon(hackathon); // variabile che individua il team, se esiste, a cui è già iscritto il concorrente
        if (teamEsistente != null) {
            throw new IllegalStateException("Impossibile partecipare: concorrente già iscritto al team '" + teamEsistente + "'");
        }
        // cerca il team nell'Hackathon
        Team teamTrovato = hackathon.getTeamByNome(nomeTeam);
        if (teamTrovato == null) {
            throw new IllegalArgumentException("Team non trovato!");
        }
        // verifica che il team non sia al completo
        if (teamTrovato.isCompleto()) {
            throw new IllegalStateException("Impossibile partecipare: team al completo!");
        }
        // verifica la password
        if (!teamTrovato.verificaPassword(password)) {
            throw new SecurityException("Impossibile partecipare: password errata!");
        }
        // aggiunto il concorrente al team
        teamTrovato.aggiungiMembro(this);
        // aggiunge il team alla lista teamAppartenenza del concorrente
        teamsAppartenenza.add(teamTrovato);
        // incrementa di uno il numero di concorrenti partecipanti all'Hackathon
        hackathon.addIscritto(teamTrovato);
    }

    /**
     * Verifica l'accesso a un team.
     *
     * @param team team a cui accedere
     * @throws IllegalArgumentException se il concorrente non partecipa al team
     */
    public void accediTeam(Team team) {
        for (Team teamC : teamsAppartenenza) {
            if (team == teamC) {
                return;
            }
        }
        throw new IllegalArgumentException("Il concorrente non partecipa al team!");
    }

    /**
     * Verifica se il concorrente partecipa a un determinato hackathon.
     *
     * @param hackathon hackathon da verificare
     * @return nome del team se partecipa, null altrimenti
     */
    public String isPartecipanteOfHackathon(Hackathon hackathon) {
        for (Team teamAppartenenza : getListTeamAppartenenza()) { // ricerca dei team a cui partecipa il concorrente
            if (teamAppartenenza.getHackathon().getTitolo().equals(hackathon.getTitolo())) {
                return teamAppartenenza.getNome();
            }
        }
        return null;
    }
}
