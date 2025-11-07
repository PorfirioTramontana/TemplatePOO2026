package model;

/**
 * Questa classe rappresenta l'oggetto generico Utenti.
 * Consente la creazione dell'utente tramite username e password, ottenendo l'id successivamente dal database, oppure
 * dall'overload del costruttore nel caso di Utente già presente.
 */

public class Utenti {
    private final String username;
    private final String password;
    private int idUtente;


    /**
     * Costruttore base per la creazione dell'Utente.
     *
     * @param username dell'utente
     * @param password dell'utente
     */
    public Utenti(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Costruttore copia, crea un oggetto Utenti partendo da un utente esistente.
     *
     * @param utente esistente
     */
    public Utenti(Utenti utente){
        this.username = utente.getUsername();
        this.password = utente.getPassword();
        this.idUtente = utente.getIdUtente();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }
}