package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta un team nella piattaforma.
 * Gestisce i membri, i documenti e i voti ricevuti dal team.
 */
public class Team {
    // attributi
    private String nome;
    private String pw;

    // rappresentazione relazioni
    private final Hackathon hackathon;
    private final Concorrente creatore;
    private final List<Concorrente> membri = new ArrayList<>(); // lista concorrenti
    private final List<Voto> voti = new ArrayList<>(); // lista di voti ricevuti
    private final List<Documento> documenti = new ArrayList<>(); // lista documenti pubblicati dal team

    // metodi
    /**
     * Costruttore che crea un'istanza di un nuovo Team.
     *
     * @param nome il nome del Team
     * @param pw la password del Team
     * @param hackathon l'Hackathon associato al Team
     * @param creatore il creatore del Team
     */
    public Team(String nome, String pw, Hackathon hackathon, Concorrente creatore) {
        this.nome = nome;
        this.pw = pw;
        this.hackathon = hackathon; // imposta l'Hackathon di riferimento
        this.creatore = creatore;
    }

    /**
     * Restituisce il nome del team.
     *
     * @return nome del team
     */
    public String getNome() { return nome; }

    /**
     * Imposta il nome del team con verifica del creatore.
     *
     * @param creatore creatore che modifica
     * @param nome nuovo nome
     * @throws SecurityException se non autorizzato
     */
    public void setNome(Concorrente creatore, String nome) {
        if (getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.nome = nome;
    }

    /**
     * Restituisce la password del team con verifica del creatore.
     *
     * @param creatore creatore che richiede
     * @return password del team
     * @throws SecurityException se non autorizzato
     */
    public String getPw(Concorrente creatore) {
        if (!getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        return pw;
    }

    /**
     * Imposta la password del team con verifica del creatore.
     *
     * @param creatore creatore che modifica
     * @param pw nuova password
     * @throws SecurityException se non autorizzato
     */
    public void setPw(Concorrente creatore, String pw) {
        if (!getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.pw = pw;
    }

    /**
     * Restituisce la password del team (senza verifica).
     * Utilizzato dal DAO
     *
     * @return password del team
     */
    public String getPw() {
        return pw;
    }

    /**
     * Restituisce l'Hackathon a cui partecipa il team.
     *
     * @return Hackathon di riferimento
     */
    public Hackathon getHackathon() { return hackathon; }

    /**
     * Restituisce il creatore del team.
     *
     * @return concorrente creatore
     */
    public Concorrente getCreatore() { return creatore; }

    /**
     * Restituisce la lista dei voti ricevuti.
     *
     * @return lista di Voto
     */
    public List<Voto> getVoti() { return voti; }

    /**
     * Aggiunge un voto alla lista.
     *
     * @param voto voto da aggiungere
     */
    public void addVoti(Voto voto) {
        voti.add(voto);
    }

    /**
     * Aggiunge un voto con verifica del giudice.
     *
     * @param giudice giudice che vota
     * @param voto voto da aggiungere
     * @throws SecurityException se il giudice non è autorizzato
     */
    public void aggiungiVoto(Giudice giudice, Voto voto) {
        if(!giudice.isAssegnato(this)) {
            throw new SecurityException("Accesso negato!");
        }
        voti.add(voto);
    }

    /**
     * Calcola il punteggio medio del team.
     *
     * @return punteggio medio
     */
    public double getPunteggio() {
        if (getVoti().isEmpty()) {
            return 0.0;
        }
        int somma = 0;
        for (Voto voto : getVoti()) {
            somma += voto.getValore();
        }
        return (double) somma / getVoti().size();
    }

    /**
     * Restituisce la lista dei membri del team.
     *
     * @return lista di Concorrente
     */
    public List<Concorrente> getMembri() { return membri; }

    /**
     * Aggiunge un membro alla lista.
     *
     * @param concorrente membro da aggiungere
     */
    public void addMembri(Concorrente concorrente) {
        membri.add(concorrente);
    }

    /**
     * Aggiunge un nuovo membro al team.
     *
     * @param concorrente membro da aggiungere
     * @throws IllegalStateException se il team è al completo
     */
    public void aggiungiMembro(Concorrente concorrente) {
        // verifica che il team non sia al completo
        if (isCompleto()) {
            throw new IllegalStateException("Team al completo!");
        }
        membri.add(concorrente);
    }

    /**
     * Verifica la corrispondenza della password.
     *
     * @param passwordInserita password da verificare
     * @return true se corrisponde, false altrimenti
     */
    public boolean verificaPassword(String passwordInserita) {
        return this.pw.equals(passwordInserita);
    }

    /**
     * Restituisce la lista dei documenti del team.
     *
     * @return lista di Documento
     */
    public List<Documento> getDocumenti() { return documenti; }

    /**
     * Aggiunge un documento alla lista.
     *
     * @param documento documento da aggiungere
     */
    public void addDocumenti(Documento documento) {
        documenti.add(documento);
    }

    /**
     * Aggiunge un nuovo documento al team.
     *
     * @param documento documento da aggiungere
     * @throws IllegalArgumentException se il documento esiste già
     */
    public void aggiungiDocumento(Documento documento) {
        for (Documento d : getDocumenti()) {
            if (documento.getNomeFile().equals(d.getNomeFile())) {
                throw new IllegalArgumentException("Il documento selezionato è già stato caricato");
            }
        }
        this.documenti.add(documento);
    }

    /**
     * Verifica se il team è al completo.
     *
     * @return true se completo, false altrimenti
     */
    public boolean isCompleto() {
        return (getMembri().size() == getHackathon().getDimMaxTeam());
    }

    /**
     * Verifica se un concorrente fa parte del team.
     *
     * @param concorrente concorrente da verificare
     * @return true se partecipa, false altrimenti
     */
    public  boolean isPartecipante(Concorrente concorrente) {
        return getMembri().contains(concorrente);
    }
}
