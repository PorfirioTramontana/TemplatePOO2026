package model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Classe rappresentativa di una Prenotazione.
 * Una prenotazione è formata da un passeggero e dalle informazioni del volo prenotato.
 */

public class Prenotazione {
    private Passeggero passeggero;
    private String codiceVolo;
    private String postoAssegnato;
    private String numeroBiglietto;
    private StatoPrenotazione stato;
    private String partenza;
    private String destinazione;
    private LocalDate data;
    private LocalTime ora;

    public LocalDate getData() {
        return data;
    }

    public LocalTime getOra() {
        return ora;
    }

    public String getPartenza() {
        return partenza;
    }

    public String getDestinazione() {
        return destinazione;
    }

    /**
     * Costruttore per la creazione di una Prenotazione.
     *
     * @param passeggero rappresenta i dati del passeggero (nome e cognome)
     * @param codiceVolo identifica il volo prenotato
     * @param postoAssegnato indica il posto assegnato a bordo
     * @param numeroBiglietto identifica la prenotazione
     * @param partenza indica la partenza del volo
     * @param destinazione indica la destinazione di un volo
     * @param data indica la data di partenza
     * @param ora indica l'ora di partenza
     * @param stato rappresenta lo stato della prenotazione al momento della creazione
     */
    public Prenotazione(Passeggero passeggero, String codiceVolo, String postoAssegnato, String numeroBiglietto,
                        String partenza, String destinazione, LocalDate data, LocalTime ora, StatoPrenotazione stato) {
        this.passeggero = passeggero;
        this.codiceVolo = codiceVolo;
        this.postoAssegnato = postoAssegnato;
        this.numeroBiglietto = numeroBiglietto;
        this.partenza = partenza;
        this.destinazione = destinazione;
        this.data = data;
        this.ora = ora;
        this.stato = stato;
    }

    public Passeggero getPasseggero() {
        return passeggero;
    }

    public void setPasseggero(Passeggero passeggero) {
        this.passeggero = passeggero;
    }

    public String getPostoAssegnato() {
        return postoAssegnato;
    }

    public void setPostoAssegnato(String postoAssegnato) {
        this.postoAssegnato = postoAssegnato;
    }

    public String getNumeroBiglietto() {
        return numeroBiglietto;
    }

    public StatoPrenotazione getStato() {
        return stato;
    }

    public void setStato(StatoPrenotazione stato) {
        this.stato = stato;
    }

    public String getCodiceVolo() {
        return codiceVolo;
    }

}
