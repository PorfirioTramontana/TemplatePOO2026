package model;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Classe che rappresenta un Volo.
 * Ogni volo ha informazioni di identificazione, come il codice volo, e informazioni utili all'utente, come
 * lo stato, l'orario e la data di partenza e la destinazione/provenienza.
 */

public class Voli {
    private String codiceVolo;
    private String provenienza;
    private String destinazione;
    private String compagniaAerea;
    private LocalDate dataPartenza;
    private LocalTime orarioPartenza;
    private StatoVolo stato;
    private Gate gate;

    public Gate getGate() {
        return gate;
    }

    public void setGate(Gate gate) {
        this.gate = gate;
    }

    /**
     * Versione base del costruttore, che memorizza un volo dalle informazioni specificate, comprendendo anche il gate.
     *
     * @param codiceVolo identifica il volo
     * @param provenienza indica il luogo di partenza
     * @param destinazione indica il luogo di destinazione
     * @param compagniaAerea rappresenta l'azienda proprietaria della linea
     * @param dataPartenza indica il giorno/mese/anno di partenza
     * @param orarioPartenza indica l'orario programmato della partenza
     * @param stato indica lo stato del volo (cancellato, inRitardo, programmato, decollato, atterrato)
     * @param gate indica il gate a cui il volo è stato assegnato
     */

    public Voli(String codiceVolo, String provenienza, String destinazione, String compagniaAerea, LocalDate dataPartenza, LocalTime orarioPartenza, String stato, Gate gate) {
        this.codiceVolo = codiceVolo;
        this.provenienza = provenienza;
        this.destinazione = destinazione;
        this.compagniaAerea = compagniaAerea;
        this.dataPartenza = dataPartenza;
        this.orarioPartenza = orarioPartenza;
        this.stato = StatoVolo.fromString(stato);
        this.gate = gate;
    }

    /**
     * Overload per la creazione di un volo nel caso in cui non venga secificato il gate.
     * In questo caso, viene impostato a null con la possibilità in futuro di assegnarne uno.
     *
     */
    public Voli(String codiceVolo, String provenienza, String destinazione, String compagniaAerea, LocalDate dataPartenza, LocalTime orarioPartenza, String stato) {
        this.codiceVolo = codiceVolo;
        this.provenienza = provenienza;
        this.destinazione = destinazione;
        this.compagniaAerea = compagniaAerea;
        this.dataPartenza = dataPartenza;
        this.orarioPartenza = orarioPartenza;
        this.stato = StatoVolo.fromString(stato);
        this.gate = null;
    }

    /**
     * Metodo per la restituzione dello stato del volo del tipo StatoVolo (Enum).
     *
     * @return lo stato in tipo enum.
     */
    public StatoVolo getStatoEnum(){
        return stato;
    }

    public String getDestinazione() {
        return destinazione;
    }

    public String getCodiceVolo() {
        return codiceVolo;
    }

    public String getProvenienza() {
        return provenienza;
    }

    public void setDestinazione(String destinazione) {
        this.destinazione = destinazione;
    }

    public String getCompagniaAerea() {
        return compagniaAerea;
    }

    public LocalDate getDataPartenza() {
        return dataPartenza;
    }

    public LocalTime getOrarioPartenza() {
        return orarioPartenza;
    }

    /**
     * Metodo per ottenere la stringa contenente lo stato del volo.
     * @return lo stato sotto forma di String.
     */
    public String getStato() {
        if(stato == null){
            return null;
        }
        return String.valueOf(stato);
    }

    public void setStato(StatoVolo stato) {
        this.stato = stato;
    }

    public String toString() {
        return codiceVolo;
    }

}
