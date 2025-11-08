package dao;

import model.Documento;
import model.Giudice;

import java.util.List;

/**
 * Interfaccia che definisce le operazioni WORM (Write-Once, Read-Many)
 * per la gestione dei documenti nella piattaforma.
 */
public interface DocumentoDAO {
    /**
     * Aggiunge un nuovo documento al database.
     *
     * @param documento il documento da aggiungere
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    void aggiungiDocumento(Documento documento);

    /**
     * Recupera tutti i documenti presenti nel database.
     *
     * @return lista di tutti i documenti
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    List<Documento> getTuttiDocumenti();

    /**
     * Aggiunge un commento a un documento specifico.
     *
     * @param giudice il giudice che aggiunge il commento
     * @param documento il documento a cui aggiungere il commento
     * @param commento il testo del commento
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    void addCommento(Giudice giudice, Documento documento, String commento);

    /**
     * Recupera tutti i commenti associati a un documento specifico.
     *
     * @param documento il documento di cui recuperare i commenti
     * @return lista di commenti per il documento
     * @throws IllegalStateException se si verifica un errore SQL durante l'operazione
     */
    List<String> getTuttiCommentiByDocumento(Documento documento);
}