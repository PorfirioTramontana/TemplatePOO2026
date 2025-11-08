package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta un documento caricato da un team nella piattaforma.
 * Gestisce i commenti e le informazioni relative al documento.
 */
public class Documento {
    // attributi
    private int idDocumento; // id DB
    private LocalDate dataAggiornamento;
    private String nomeFile;
    private final List<String> commenti;

    // rappresentazione relazioni
    private final Team team;

    // metodi
    /**
     * Costruttore che crea un'istanza di un nuovo Documento.
     *
     * @param nomeFile il nome del file
     * @param team il team che ha caricato il file
     */
    public Documento(String nomeFile,  Team team) {
        this.dataAggiornamento = LocalDate.now(); // la data di aggiornamento viene inserita in automatico appena viene creato il documento
        this.nomeFile = nomeFile;
        this.commenti = new ArrayList<>();
        this.team = team;
    }

    /**
     * Costruttore che crea un'istanza di un nuovo Documento.
     * Utilizzato dal DAO
     *
     * @param idDocumento L'id del documento
     * @param nomeFile Il nome del file
     * @param team Il team che ha caricato il file
     * @param dataAggiornamento La data di aggiornamento del file
     */
    public Documento(int idDocumento, String nomeFile,Team team, LocalDate dataAggiornamento) {
        this.idDocumento = idDocumento;
        this.nomeFile = nomeFile;
        this.commenti = new ArrayList<>();
        this.team = team;
        this.dataAggiornamento = dataAggiornamento;
    }

    /**
     * Restituisce l'ID del documento.
     *
     * @return ID del documento
     */
    public int getIdDocumento() { return idDocumento; }

    /**
     * Imposta l'ID del documento.
     *
     * @param idDocumento ID da impostare
     */
    public void setIdDocumento(int idDocumento) { this.idDocumento = idDocumento; }

    /**
     * Restituisce la data dell'ultimo aggiornamento.
     *
     * @return data di aggiornamento
     */
    public LocalDate getDataAggiornamento() { return dataAggiornamento; }
    // setter privato utilizzabile solo tramite il metodo setFile
    private void setDataAggiornamento(LocalDate dataAggiornamento) {
        this.dataAggiornamento = dataAggiornamento;
    }

    /**
     * Restituisce il nome del file.
     *
     * @return nome del file
     */
    public String getNomeFile() { return nomeFile; }

    /**
     * Imposta il nome del file aggiornando automaticamente la data.
     *
     * @param nomeFile nuovo nome del file
     */
    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
        this.setDataAggiornamento(LocalDate.now()); // aggiorna alla data corrente
    }

    /**
     * Restituisce il team proprietario del documento.
     *
     * @return team proprietario
     */
    public Team getTeam() { return team; }

    /**
     * Restituisce la lista dei commenti.
     *
     * @return lista di commenti
     */
    public List<String> getCommenti() { return commenti; }

    /**
     * Aggiunge tutti i commenti alle liste locali, prelevandoli da Database.
     *
     * @param tuttiCommenti lista di commenti da aggiungere
     */
    public void addTuttiCommenti(List<String> tuttiCommenti) {
        commenti.addAll(tuttiCommenti);
    }

    /**
     * Aggiunge un nuovo commento al documento.
     *
     * @param giudice giudice che aggiunge il commento
     * @param commento testo del commento
     * @throws SecurityException se il giudice non è autorizzato
     */
    public void setCommenti(Giudice giudice, String commento) {
        team.getHackathon().verificaStatoGara("In corso", "aggiungere un commento");
        if (!giudice.isAssegnato(this.team)) {
            throw new SecurityException("Accesso negato!");
        }
        String commentoFormattato = "\n---\n" + commento + "\n---\n";
        commenti.add(commentoFormattato);
    }
}