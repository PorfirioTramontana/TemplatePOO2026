package dao;

import model.*;

import java.util.List;

/**
 * Interfaccia che definisce le operazioni WORM (Write-Once, Read-Many)
 * per la gestione degli utenti della piattaforma.
 */
public interface UtentePiattaformaDAO {
    // metodi dell'utente
    /**
     * Aggiunge un nuovo utente al database.
     *
     * @param utente l'utente da aggiungere
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    void aggiungiUtente(UtentePiattaforma utente);

    /**
     * Recupera tutti gli utenti presenti nel database.
     *
     * @return lista di tutti gli utenti
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    List<UtentePiattaforma> getTuttiUtenti();

    // metodi dell'organizzatore
    /**
     * Aggiunge una convocazione di un giudice da parte di un organizzatore per un Hackathon specifico.
     *
     * @param organizzatore l'organizzatore che effettua la convocazione
     * @param giudice il giudice convocato
     * @param hackathon l'Hackathon per cui il giudice è convocato
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    void convocaGiudice(Organizzatore organizzatore, Giudice giudice, Hackathon hackathon);

    /**
     * Recupera tutti gli Hackathon assegnati a un giudice specifico.
     *
     * @param giudice il giudice di cui recuperare gli Hackathon assegnati
     * @return lista di Hackathon assegnati al giudice
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    List<Hackathon> getHackathonAssegnatiToGiudice(Giudice giudice);

    /**
     * Recupera tutti gli organizzatori che hanno invitato un giudice specifico.
     *
     * @param giudice il giudice di cui recuperare gli organizzatori invitanti
     * @return lista di organizzatori che hanno invitato il giudice
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    List<Organizzatore> getOrganizzatoriInvitantiToGiudice(Giudice giudice);

    // metodi del concorrente
    /**
     * Aggiunge la partecipazione di un concorrente a un team specifico di un hackathon.
     *
     * @param concorrente il concorrente che partecipa al team
     * @param nomeTeam il nome del team a cui partecipare
     * @param titoloHackathon il titolo dell'Hackathon a cui il team partecipa
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    void partecipaTeam(Concorrente concorrente, String nomeTeam, String titoloHackathon);
}
