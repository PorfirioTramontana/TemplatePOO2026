package model;

import java.time.LocalDate;
import java.util.*;

/**
 * Classe che rappresenta un hackathon nella piattaforma.
 * Gestisce tutte le operazioni relative alla gestione di un hackathon.
 */
public class Hackathon {
    // attributi
    private String titolo;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private int numMaxIscritti;
    private int dimMaxTeam;
    private LocalDate inizioIscrizioni;
    private LocalDate fineIscrizioni;
    private String descrizioneProblema;
    private List<String> classifica;
    private String indirizzoSede;

    // attributi derivati (contatori)
    private int numIscritti;
    private int numVotiAssegnati;

    // rappresentazione relazioni
    private final Organizzatore creatore;  // riferimento all'organizzatore
    private final List<Giudice> giudiceList = new ArrayList<>();
    private final List<Team> teamList = new ArrayList<>();

    // metodi
    /**
     * Costruttore che crea un'istanza di un nuovo Hackathon.
     *
     * @param creatore il creatore dell'Hackathon
     * @param titolo il titolo dell'Hacakthon
     * @param numMaxIscritti il numero massimo di iscritti all'Hackathon
     * @param dimMaxTeam il numero massimo di iscritti ai team dell'Hackathon
     * @param inizioIscrizioni il giorno di inizio delle iscrizioni dell'Hackathon
     * @param dataInizio il giorno di inizio gara dell'Hackathon
     * @param dataFine il giorno di fine gara dell'Hackathon
     * @param indirizzoSede la sede dell'Hackathon
     */
    public Hackathon(Organizzatore creatore, String titolo, int numMaxIscritti, int dimMaxTeam, LocalDate inizioIscrizioni, LocalDate dataInizio, LocalDate dataFine, String indirizzoSede) {
        if (creatore == null) {
            throw new IllegalArgumentException("Creatore mancante!");
        }
        this.creatore = creatore;
        this.titolo = titolo;
        this.numMaxIscritti = numMaxIscritti;
        this.dimMaxTeam = dimMaxTeam;
        this.inizioIscrizioni = inizioIscrizioni;
        this.fineIscrizioni = dataInizio.minusDays(2); // le iscrizioni chiudono 2 giorni prima l'inizio della gara
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.descrizioneProblema = null;
        this.classifica = null;
        this.indirizzoSede = indirizzoSede;
        this.numIscritti = 0;
        this.numVotiAssegnati = 0;
    }

    /**
     * Costruttore che crea un'istanza di un nuovo Hackathon.
     * Utilizzato dal DAO
     *
     * @param creatore il creatore dell'Hackathon
     * @param titolo il titolo dell'Hacakthon
     * @param numMaxIscritti il numero massimo di iscritti all'Hackathon
     * @param dimMaxTeam il numero massimo di iscritti ai team dell'Hackathon
     * @param inizioIscrizioni il giorno di inizio delle iscrizioni dell'Hackathon
     * @param fineIscrizioni il giorno di fine delle iscrizioni dell'Hackathon
     * @param dataInizio il giorno di inizio gara dell'Hackathon
     * @param dataFine il giorno di fine gara dell'Hackathon
     * @param descrizioneProblema la descrizione del problema dell'Hackathon
     * @param classifica la classifica dell'Hackathon
     * @param indirizzoSede la sede dell'Hackathon
     * @param numIscritti all'Hackathon
     * @param numVotiAssegnati ai team dell'Hackathon
     */
    public Hackathon(Organizzatore creatore, String titolo, int numMaxIscritti, int dimMaxTeam, LocalDate inizioIscrizioni, LocalDate fineIscrizioni, LocalDate dataInizio, LocalDate dataFine, String descrizioneProblema, String classifica, String indirizzoSede, int numIscritti, int numVotiAssegnati ) {
        this.creatore = creatore;
        this.titolo = titolo;
        this.numMaxIscritti = numMaxIscritti;
        this.dimMaxTeam = dimMaxTeam;
        this.inizioIscrizioni = inizioIscrizioni;
        this.fineIscrizioni = fineIscrizioni;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.descrizioneProblema = descrizioneProblema;
        this.classifica = convertStringToList(classifica);
        this.indirizzoSede = indirizzoSede;
        this.numIscritti = numIscritti;
        this.numVotiAssegnati = numVotiAssegnati;
    }

    /**
     * Restituisce il titolo dell'Hackathon.
     *
     * @return titolo
     */
    public String getTitolo() { return titolo; }

    /**
     * Imposta il titolo con verifica del creatore.
     *
     * @param creatore creatore che modifica
     * @param titolo nuovo titolo
     * @throws SecurityException se non autorizzato
     */
    public void setTitolo(Organizzatore creatore, String titolo) {
        if (!getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.titolo = titolo;
    }

    /**
     * Restituisce la data di inizio.
     *
     * @return data di inizio
     */
    public LocalDate getDataInizio() { return dataInizio; }

    /**
     * Imposta la data di inizio con verifica del creatore.
     *
     * @param creatore creatore che modifica
     * @param dataInizio nuova data
     * @throws SecurityException se non autorizzato
     */
    public void setDataInizio(Organizzatore creatore, LocalDate dataInizio) {
        if (!getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.dataInizio = dataInizio;
    }

    /**
     * Restituisce la data di fine.
     *
     * @return data di fine
     */
    public LocalDate getDataFine() { return dataFine; }

    /**
     * Imposta la data di fine con verifica del creatore.
     *
     * @param creatore creatore che modifica
     * @param dataFine nuova data
     * @throws SecurityException se non autorizzato
     */
    public void setDataFine(Organizzatore creatore, LocalDate dataFine) {
        if (!getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.dataFine = dataFine;
    }

    /**
     * Restituisce il numero massimo di iscritti.
     *
     * @return numero massimo iscritti
     */
    public int getNumMaxIscritti() { return numMaxIscritti; }

    /**
     * Imposta il numero massimo di iscritti con verifica del creatore.
     *
     * @param creatore creatore che modifica
     * @param numMaxIscritti nuovo numero
     * @throws SecurityException se non autorizzato
     */
    public void setNumMaxIscritti(Organizzatore creatore, int numMaxIscritti) {
        if (!getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.numMaxIscritti = numMaxIscritti;
    }

    /**
     * Restituisce il numero corrente di iscritti.
     *
     * @return numero iscritti
     */
    public int getNumIscritti() { return numIscritti; }

    /**
     * Aggiunge un iscritto al conteggio.
     *
     * @param team team che si iscrive
     */
    public void addIscritto(Team team) {
        if (getTeamList().contains(team)) {
            this.numIscritti++;
        }
    }

    /**
     * Restituisce il numero di voti assegnati.
     *
     * @return numero voti
     */
    public int getNumVotiAssegnati() { return numVotiAssegnati; }

    /**
     * Incrementa il numero di voti assegnati.
     */
    public void addNumVotiAssegnati() {
        numVotiAssegnati++;
    }

    /**
     * Aggiunge un voto al conteggio.
     *
     * @param team team che riceve il voto
     */
    public void addVoto(Team team) {
        if (getTeamList().contains(team)) {
            this.numVotiAssegnati++;
        }
    }

    /**
     * Restituisce la dimensione massima dei team.
     *
     * @return dimensione massima team
     */
    public int getDimMaxTeam() { return dimMaxTeam; }

    /**
     * Imposta la dimensione massima dei team con verifica del creatore.
     *
     * @param creatore creatore che modifica
     * @param dimMaxTeam nuova dimensione
     * @throws SecurityException se non autorizzato
     */
    public void setDimMaxTeam(Organizzatore creatore, int dimMaxTeam) {
        if (getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.dimMaxTeam = dimMaxTeam;
    }

    /**
     * Restituisce la data di inizio iscrizioni.
     *
     * @return data inizio iscrizioni
     */
    public LocalDate getInizioIscrizioni() { return inizioIscrizioni; }

    /**
     * Imposta la data di inizio iscrizioni con verifica del creatore.
     *
     * @param creatore creatore che modifica
     * @param inizioIscrizioni nuova data
     * @throws SecurityException se non autorizzato
     */
    public void setInizioIscrizioni(Organizzatore creatore, LocalDate inizioIscrizioni) {
        if (getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.inizioIscrizioni = inizioIscrizioni;
    }

    /**
     * Restituisce la data di fine iscrizioni.
     *
     * @return data fine iscrizioni
     */
    public LocalDate getFineIscrizioni() { return fineIscrizioni; }

    /**
     * Imposta la data di fine iscrizioni con verifica del creatore.
     *
     * @param creatore creatore che modifica
     * @param fineIscrizioni nuova data
     * @throws SecurityException se non autorizzato
     */
    public void setFineIscrizioni(Organizzatore creatore, LocalDate fineIscrizioni) {
        if (!getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.fineIscrizioni = fineIscrizioni;
    }

    /**
     * Restituisce la descrizione del problema.
     *
     * @return descrizione problema
     */
    public String getDescrizioneProblema() { return descrizioneProblema; }

    /**
     * Imposta la descrizione del problema con verifica del giudice.
     *
     * @param giudice giudice che modifica
     * @param descrizioneProblema nuova descrizione
     * @throws SecurityException se non autorizzato
     */
    public void setDescrizioneProblema(Giudice giudice, String descrizioneProblema) {
        for (Hackathon hackathonG : giudice.getHackathonAssegnati()) {
            if (hackathonG.getTitolo().equals(this.titolo)) {
                this.descrizioneProblema = descrizioneProblema;
                return;
            }
        }
        throw new SecurityException("Accesso negato!");
    }

    /**
     * Restituisce la classifica con verifica dell'utente.
     *
     * @param utente utente che richiede
     * @return lista della classifica
     * @throws SecurityException se utente non valido
     */
    public List<String> getClassifica(UtentePiattaforma utente) {
        // verifica che la richiesta sia effettuata da un utente
        if (utente == null) {
            throw new SecurityException("Accesso negato: utente non esistente!");
        }
        // verifica se la classifica è già stata calcolata
        if (classifica.isEmpty()) {
            setClassifica(calcolaClassifica());
        }
        return this.classifica;
    }
    // setter privato, invocabile solo dalla funzione calcolaClassifica
    private void setClassifica(List<String> classifica) {
        this.classifica = classifica;
    }

    /**
     * Restituisce l'indirizzo della sede con verifica dell'utente.
     *
     * @param utente utente che richiede
     * @return indirizzo sede
     * @throws SecurityException se utente non valido
     */
    public String getIndirizzoSede(UtentePiattaforma utente) {
        // verifica che la richiesta sia effettuata da un utente
        if (utente == null) {
            throw new SecurityException("Accesso negato: utente non esistente!");
        }
        return indirizzoSede;
    }

    /**
     * Imposta l'indirizzo della sede con verifica del creatore.
     *
     * @param creatore creatore che modifica
     * @param indirizzoSede nuovo indirizzo
     * @throws SecurityException se non autorizzato
     */
    public void setIndirizzoSede(Organizzatore creatore, String indirizzoSede) {
        if (!getCreatore().equals(creatore)) {
            throw new SecurityException("Accesso negato!");
        }
        this.indirizzoSede = indirizzoSede;
    }

    /**
     * Restituisce l'indirizzo della sede (senza verifica).
     * Utilizzato dal DAO
     *
     * @return indirizzo sede
     */
    public String getIndirizzoSede() {
        return indirizzoSede;
    }

    // metodo utilizzato per il calcolo della classifica di un hackathon
    private List<String> calcolaClassifica() throws IllegalStateException {
        // verifica che l'Hackathon sia concluso
        if (!isTerminato()) {
            throw new IllegalStateException("L'Hackathon è ancora in corso. Impossibile calcolare la classifica!");
        }
        // verifica che la classifica sia calcolabile, ovvero che tutti i giudici abbiano assegnato i propri voti
        if (!isClassificaCalcolabile()) {
            throw new IllegalStateException("L'Hackathon è terminato. In attesa delle valutazioni dei giudici. Impossibile calcolare la classifica!");
        }

        // prende i team della lista, li ordina per punteggio (decrescente)
        teamList.sort(Comparator.comparing(Team::getPunteggio).reversed());
        return getClassificaFormattata();
    }

    private List<String> getClassificaFormattata() {
        List<String> classificaFormattata = new ArrayList<>();
        int posizione = 1;
        for (int i = 0; i < teamList.size(); i++) {
            // verifica dei pari merito (verifica a partire dal secondo classificato)
            if (i > 0 && teamList.get(i).getPunteggio() != teamList.get(i - 1).getPunteggio()) { // non c'è il pari merito, cambio la posizione in classifica
                posizione = i + 1;
            }
            classificaFormattata.add("\n" + posizione + "\t| " + teamList.get(i).getPunteggio() + "\t| " + teamList.get(i).getNome());
        }
        return classificaFormattata;
    }

    /**
     * Verifica se esiste un team con il nome specificato.
     *
     * @param nomeTeam nome da verificare
     * @return true se esiste, false altrimenti
     */
    public boolean esisteTeam(String nomeTeam) {
        return getTeamList().stream().anyMatch(team -> team.getNome().equalsIgnoreCase(nomeTeam)); // ignora le lettere maiuscole
    }

    /**
     * Cerca e restituisce un team per nome.
     *
     * @param nomeTeam nome del team
     * @return team trovato o null
     */
    public Team getTeamByNome(String nomeTeam) {
        for (Team team : getTeamList()) {
            if (team.getNome().equalsIgnoreCase(nomeTeam)) {
                return team;
            }
        }
        return null;
    }

    /**
     * Aggiunge un team con verifica del creatore.
     *
     * @param team team da aggiungere
     * @param creatore creatore che aggiunge
     * @throws SecurityException se non autorizzato
     */
    public void aggiungiTeam(Team team, Concorrente creatore) {
        // verifica che il team venga aggiunto all'Hackathon solo tramite il creatore
        if(!team.getCreatore().equals(creatore)) {
            throw new SecurityException("Impossibile aggiungere team!");
        }
        this.teamList.add(team);
        // aggiunge il team ai teamGiudicati dei giudici appartenenti all'Hackathon
        for (Giudice giudice : getGiudiceList()) {
            giudice.aggiungiTeam(team);
        }
    }

    /**
     * Aggiunge un giudice con verifica dell'organizzatore.
     *
     * @param giudice giudice da aggiungere
     * @param organizzatore organizzatore che aggiunge
     * @throws SecurityException se non autorizzato
     */
    public void aggiungiGiudice(Giudice giudice, Organizzatore organizzatore) {
        // verifica che il giudice venga aggiunto all'Hackathon solo tramite il creatore
        if(!getCreatore().getEmail().equals(organizzatore.getEmail())) {
            throw new SecurityException("Impossibile aggiungere giudice!");
        }
        this.giudiceList.add(giudice);
    }

    /**
     * Verifica se l'Hackathon è terminato.
     *
     * @return true se terminato, false altrimenti
     */
    public boolean isTerminato() {
        return LocalDate.now().isAfter(getDataFine());
    }

    /**
     * Verifica se le iscrizioni sono terminate.
     *
     * @return true se terminate, false altrimenti
     */
    public boolean iscrizioniTerminate() {
        return LocalDate.now().isAfter(getFineIscrizioni());
    }

    /**
     * Verifica se l'Hackathon è al completo.
     *
     * @return true se completo, false altrimenti
     */
    public boolean isCompleto() {
        return getNumMaxIscritti() == getNumIscritti();
    }

    /**
     * Verifica se la classifica è calcolabile.
     *
     * @return true se calcolabile, false altrimenti
     */
    // una classifica è calcolabile quando l'Hackathon è terminato e sono stati assegnati tutti i voti (V = T * G), dove V è il numero di voti, T è il numero di team e G è il numero di giudici (ogni giudice dà un voto ad ogni team)
    public boolean isClassificaCalcolabile() {
        return isTerminato() && (getNumVotiAssegnati() == getTeamList().size() * getGiudiceList().size());
    }

    /**
     * Restituisce il creatore dell'Hackathon.
     *
     * @return organizzatore creatore
     */
    public Organizzatore getCreatore() {
        return creatore;
    }

    /**
     * Restituisce la lista dei giudici.
     *
     * @return lista di Giudice
     */
    public List<Giudice> getGiudiceList() {
        return new ArrayList<>(giudiceList); // restituisce una copia per sicurezza
    }

    /**
     * Aggiunge un giudice alla lista.
     *
     * @param giudice giudice da aggiungere
     */
    public void addGiudice(Giudice giudice) {
        giudiceList.add(giudice);
    }

    /**
     * Restituisce la lista dei team.
     *
     * @return lista di Team
     */
    public List<Team> getTeamList() {
        return new ArrayList<>(teamList);
    }

    /**
     * Aggiunge un team alla lista.
     *
     * @param team team da aggiungere
     */
    public void addTeam(Team team) {
        teamList.add(team);
    }

    /**
     * Determina lo stato corrente della gara.
     *
     * @return stringa descrittiva dello stato
     */
    public String statoGara() {
        if (isClassificaCalcolabile()) {
            return "Valutata";
        }
        if (isTerminato()) {
            return "Terminata";
        }
        if (LocalDate.now().isAfter(getDataInizio().minusDays(1))) {
            return "In corso";
        }
        if (iscrizioniTerminate()) {
            return "Iscrizioni terminate. In attesa dell'inizio della gara";
        }
        if (isCompleto()) {
            return "Numero massimo concorrenti raggiunto. In attesa dell'inizio della gara";
        }
        if (LocalDate.now().isAfter(getInizioIscrizioni().minusDays(1))) {
            return "Iscrizioni aperte";
        }
        return "Iscrizioni non ancora aperte";
    }

    /**
     * Verifica se la gara è nello stato richiesto.
     *
     * @param statoRichiesto stato da verificare
     * @return true se corrisponde, false altrimenti
     */
    public boolean verificaStatoGara(String statoRichiesto) {
        String statoGara = statoGara();

        // se lo stato della gara è quello richiesto, allora non ci sono problemi
        return statoRichiesto.equals(statoGara);
    }

    /**
     * Verifica lo stato della gara con messaggio di errore specifico.
     *
     * @param statoRichiesto stato richiesto
     * @param azioneDaFare azione che si stava tentando
     * @throws IllegalStateException se lo stato non corrisponde
     */
    public void verificaStatoGara(String statoRichiesto, String azioneDaFare) {
        String statoGara = statoGara();

        // se lo stato della gara è quello richiesto, allora non ci sono problemi
        if (statoRichiesto.equals(statoGara)) {
            return;
        }
        // se lo stato della gara NON è quello richiesto, lancia eccezione in base al problema riscontrato
        switch (statoGara) {
            case ("Valutata") :
                throw new IllegalStateException("Impossibile " + azioneDaFare + ": Hackathon concluso e valutato");
            case ("Conclusa") :
                throw new IllegalStateException("Impossibile " + azioneDaFare + ": Hackathon terminato");
            case ("In corso") :
                throw new IllegalStateException("Impossibile " + azioneDaFare + ": Hackathon in corso");
            case ("Iscrizioni terminate. In attesa dell'inizio della gara") :
                throw new IllegalStateException("Impossibile " + azioneDaFare + ": periodo di iscrizioni terminato");
            case ("Numero massimo concorrenti raggiunto. In attesa dell'inizio della gara") :
                throw new IllegalStateException("Impossibile " + azioneDaFare + ": Hackathon al completo");
            case ("Iscrizioni aperte") :
                throw new IllegalStateException("Impossibile " + azioneDaFare + ": periodo di iscrizioni aperto");
            case ("Iscrizioni non ancora aperte") :
                throw new IllegalStateException("Impossibile " + azioneDaFare + ": iscrizioni non ancora aperte");
            default :
                throw new IllegalArgumentException("Impossibile " + azioneDaFare + ": STATO GARA NON VALIDO!");
        }
    }

    /**
     * Converte una stringa formattata in lista.
     * Utilizzato per il Costruttore del DAO
     *
     * @param input stringa da convertire
     * @return lista risultante
     */
    public static List<String> convertStringToList(String input) {
        if (input == null || input.length() < 2) {
            return new ArrayList<>();
        }

        // rimuove le parentesi quadre iniziali e finali
        String trimmed = input.substring(1, input.length() - 1).trim();

        // se la stringa è vuota dopo aver rimosso le parentesi
        if (trimmed.isEmpty()) {
            return new ArrayList<>();
        }
        String[] elements = trimmed.split(",");
        List<String> result = new ArrayList<>();
        for (String s : elements) {
            result.add(s.trim());
        }

        return result;
    }
}
