package dao;

import model.Hackathon;
import java.util.List;

/**
 * Interfaccia che definisce le operazioni WORM (Write-Once, Read-Many)
 * per la gestione degli Hackathon nella piattaforma.
 */
public interface HackathonDAO {
    /**
     * Aggiunge un nuovo hackathon al database.
     *
     * @param hackathon l'Hackathon da aggiungere
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    void aggiungiHackathon(Hackathon hackathon);

    /**
     * Recupera tutti gli Hackathon presenti nel database.
     *
     * @return lista di tutti gli Hackathon
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    List<Hackathon> getTuttiHackathon();

    /**
     * Aggiorna la descrizione del problema per un Hackathon specifico.
     *
     * @param hackathon l'Hackathon da aggiornare
     * @param descrizione la nuova descrizione del problema
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    void setDescrizioneHackathon(Hackathon hackathon, String descrizione);

    /**
     * Aggiorna la classifica per un hackathon specifico.
     *
     * @param hackathon l'Hackathon da aggiornare
     * @param classifica la nuova classifica
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    void setClassificaHackathon(Hackathon hackathon, String classifica);

    /**
     * Incrementa il contatore dei team iscritti a un hackathon.
     *
     * @param titoloHackathon il titolo dell'Hackathon da aggiornare
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    void incrementaTeam(String titoloHackathon);

    /**
     * Incrementa il contatore dei voti assegnati per un hackathon.
     *
     * @param titoloHackathon il titolo dell'Hackathon da aggiornare
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    void incrementaVoti(String titoloHackathon);
}
