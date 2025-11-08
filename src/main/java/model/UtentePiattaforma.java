package model;

/**
 * Classe astratta che rappresenta un utente generico della piattaforma.
 * Contiene le informazioni base e i metodi comuni a tutti gli utenti.
 */
public class UtentePiattaforma {
    // attributi
    /**
     * Il nome dell'Utente.
     */
    protected String nome;
    /**
     * Il cognome dell'Utente.
     */
    protected String cognome;
    /**
     * L'email dell'Utente.
     */
    protected String email;
    /**
     * La password dell'Utente
     */
    protected String pw;
    /**
     * Il ruolo dell'Utente
     */
    protected String ruolo;

    // metodi
    /**
     * Costruttore che crea un'istanza di un nuovo Utente.
     *
     * @param nome il nome dell'Utente
     * @param cognome il cognome dell'Utente
     * @param email l'email dell'Utente
     * @param pw la password dell'Utente
     * @param ruolo il ruolo dell'Utente (concorrente/giudice/organizzatore)
     */
    public UtentePiattaforma(String nome, String cognome, String email, String pw, String ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.pw = pw;
        this.ruolo = ruolo;
    }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return nome
     */
    public String getNome() { return nome; }

    /**
     * Imposta il nome dell'utente.
     *
     * @param nome nuovo nome
     */
    public void setNome(String nome) { this.nome = nome; }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return cognome
     */
    public String getCognome() { return cognome; }

    /**
     * Imposta il cognome dell'utente.
     *
     * @param cognome nuovo cognome
     */
    public void setCognome(String cognome) { this.cognome = cognome; }

    /**
     * Restituisce l'email dell'utente.
     *
     * @return email
     */
    public String getEmail() { return email; }

    /**
     * Imposta l'email dell'utente.
     *
     * @param email nuova email
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Restituisce la password dell'utente.
     * Utilizzato dal DAO
     *
     * @return password
     */
    public String getPw() { return pw; }

    /**
     * Restituisce il ruolo dell'utente.
     * Utilizzato dal DAO
     *
     * @return ruolo
     */
    public String getRuolo() { return ruolo; }

    /**
     * Verifica la corrispondenza della password.
     *
     * @param passwordInserita password da verificare
     * @return true se corrisponde, false altrimenti
     */
    public boolean verificaPassword(String passwordInserita) {
        return this.pw.equals(passwordInserita);
    }
}
