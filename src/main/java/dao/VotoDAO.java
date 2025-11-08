package dao;

import model.Voto;
import java.util.List;

/**
 * Interfaccia che definisce le operazioni WORM (Write-Once, Read-Many)
 * per la gestione dei voti assegnati ai team dai giudici durante gli hackathon.
 */
public interface VotoDAO {
    /**
     * Aggiunge un nuovo voto al database.
     *
     * @param voto il voto da aggiungere
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    void aggiungiVoto(Voto voto);

    /**
     * Recupera tutti i voti presenti nel database.
     *
     * @return lista di tutti i voti
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    List<Voto> getTuttiVoti();
}
