package dao;

import model.Concorrente;
import model.Team;
import java.util.List;

/**
 * Interfaccia che definisce le operazioni WORM (Write-Once, Read-Many)
 * per la gestione dei team nella piattaforma.
 */
public interface TeamDAO {
    /**
     * Aggiunge un nuovo team al database.
     *
     * @param team il team da aggiungere
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    void aggiungiTeam(Team team);

    /**
     * Recupera tutti i team presenti nel database.
     *
     * @return lista di tutti i team
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    List<Team> getTuttiTeam();

    /**
     * Recupera tutti i concorrenti membri di un team specifico.
     *
     * @param team il team di cui recuperare i membri
     * @return lista di concorrenti membri del team
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    List<Concorrente> getConcorrentiOfTeam(Team team);
}
