package model;

/**
 * Classe che rappresenta un passeggero in una prenotazione.
 * Possiede dati anagrafici, ossia nome e cognome.
 */

public class Passeggero {
    private String nome;
    private String cognome;

    /**
     * Costruttore per la creazione del passeggero.
     * @param nome del passeggero
     * @param cognome del passeggero
     */
    public Passeggero(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

}
