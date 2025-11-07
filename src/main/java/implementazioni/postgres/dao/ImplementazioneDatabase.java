package implementazioni.postgres.dao;
import dao.DAO;
import database.DatabaseConnection;
import model.*;

import java.time.*;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Classe fondamentale per il dialogo tra il controller e il database.
 * Si occupa di tutta la logica legata alle operazioni da fare con il database, come ottenimento dei dati o aggiunta di righe.
 */

public class ImplementazioneDatabase implements DAO{

    private static final Logger logger = Logger.getLogger(ImplementazioneDatabase.class.getName());

    public ImplementazioneDatabase() {
        //Costruttore per la creazione dell'istanza.
    }

    /**
     * Metodo che si occupa di ricevere dal database i voli che hanno la partenza e la destinazione specificata
     * @param partenza da cercare nei voli
     * @param destinazione da cercare nei voli
     * @return una lista di voli che hanno rispettato il fitro di ricerca per destinazione e partenza
     */
    @Override
    public List<Voli> ricercaVoli(String partenza, String destinazione){
        List<Voli> listaRicercaVoli = new ArrayList<>();

        String sql = "select * from \"Voli\" where LOWER(\"Provenienza\") = LOWER(?) and LOWER(\"Destinazione\") = LOWER(?)";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, partenza);
            ps.setString(2, destinazione);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    String codiceVolo = rs.getString("CodiceVolo");
                    String provenienza = rs.getString("Provenienza");
                    String destinazioneFinale = rs.getString("Destinazione");
                    String compagniaAerea = rs.getString("CompagniaAerea");
                    LocalDate data = rs.getDate("DataPartenza").toLocalDate();
                    LocalTime oraPartenza = rs.getTime("OraPartenza").toLocalTime();
                    String stato = rs.getString("StatoVolo");

                    Voli volo = new Voli(codiceVolo, provenienza, destinazioneFinale, compagniaAerea,
                            data, oraPartenza, stato);

                    listaRicercaVoli.add(volo);
                }
            }
        }
        catch (SQLException e){
            logger.severe("SQLException: " + e.getMessage());
            logger.severe("SQLState: " + e.getSQLState());
            logger.severe("ErrorCode: " + e.getErrorCode());
            e.printStackTrace();
        }
        return listaRicercaVoli;
    }

    /**
     * Metodo per uttenere una lista di prenotazioni dell'utente, identificato dal suo id.
     * @param idUtente identifica l'utente che ha chiesto di vedere le sue prenotazioni
     * @return una lista delle prenotazioni che quell'utente ha effettuato
     */
    @Override
    public List<Prenotazione> getPrenotazioni(int idUtente) {
        List<Prenotazione> listaPrenotazioni = new ArrayList<>();

        String sql = "select * from \"Prenotazioni\" where \"id_utente\" = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, idUtente);

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    String numeroBiglietto = rs.getString("NumeroBiglietto");
                    String nome = rs.getString("Nome");
                    String cognome = rs.getString("Cognome");
                    String codiceVolo = rs.getString("CodiceVolo");
                    String postoAssegnato = rs.getString("PostoAssegnato");
                    String stato = rs.getString("StatoPrenotazione");
                    String provenienza = "";
                    String destinazione = "";
                    StatoPrenotazione statoP;
                    LocalDate data = null;
                    LocalTime oraPartenza = null;

                    Passeggero passeggero = new Passeggero(nome, cognome);

                    String sqlVoli = "select \"Provenienza\" , \"Destinazione\", " +
                            "\"DataPartenza\", \"OraPartenza\" from \"Voli\" where \"CodiceVolo\" = ?";

                    try(PreparedStatement psVoli = conn.prepareStatement(sqlVoli)){
                        psVoli.setString(1, codiceVolo);

                        try(ResultSet rsVoli = psVoli.executeQuery()){
                            if(rsVoli.next()){
                                provenienza = rsVoli.getString("Provenienza");
                                destinazione = rsVoli.getString("Destinazione");
                                data = rsVoli.getDate("DataPartenza").toLocalDate();
                                oraPartenza = rsVoli.getTime("OraPartenza").toLocalTime();
                            }
                        }
                    }

                    if(stato != null){
                        statoP = StatoPrenotazione.valueOf(stato);
                    }
                    else{
                        statoP = StatoPrenotazione.inAttesa;
                    }

                    Prenotazione prenotazione = new Prenotazione(passeggero, codiceVolo, postoAssegnato, numeroBiglietto, provenienza, destinazione,
                            data, oraPartenza, statoP);
                    listaPrenotazioni.add(prenotazione);
                }
            }


        }
        catch (SQLException e){
            logger.severe("SQLException: " + e.getMessage());
            logger.severe("SQLState: " + e.getSQLState());
            logger.severe("ErrorCode: " + e.getErrorCode());
            e.printStackTrace();
        }
        return listaPrenotazioni;
    }

    /**
     * Metodo per aggiungere al database una nuova prenotazione richiesta dall'utente
     * @param prenotazione la nuova prenotazione da aggiungere
     * @param idUtente identfica l'utente che sta prenotando
     * @return {@code true} se l'operazione è andata a buon fine, {@code false} se non è andata a buon fine
     */
    @Override
    public boolean creaPrenotazione(Prenotazione prenotazione, int idUtente) {
        String codiceVolo = prenotazione.getCodiceVolo();
        String nome = prenotazione.getPasseggero().getNome();
        String cognome = prenotazione.getPasseggero().getCognome();
        String numeroBiglietto = prenotazione.getNumeroBiglietto();
        String stato = String.valueOf(prenotazione.getStato());
        String postoAssegnato = prenotazione.getPostoAssegnato();

        String sql = "insert into \"Prenotazioni\" values (?,?,?,?,?,?,?)";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, numeroBiglietto);
            ps.setString(2, nome);
            ps.setString(3, cognome);
            ps.setString(4, codiceVolo);
            ps.setString(5, postoAssegnato);
            ps.setString(6, stato);
            ps.setInt(7, idUtente);

            int update = ps.executeUpdate();

            return update != 0;

        }
        catch (SQLException e){
            logger.severe("SQLException: " + e.getMessage());
            logger.severe("SQLState: " + e.getSQLState());
            logger.severe("ErrorCode: " + e.getErrorCode());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Metodo per modificare una prenotazione da parte di un utente
     * Compone una query dinamica con gli argomenti da modificare
     * @param p la prenotazione da modificare
     * @param stessoPosto flag, {@code true} indica che il posto della nuova prenotazione è lo stesso della precendente
     * @param stessoPasseggero flag, {@code true} indica che il passeggero è lo stesso della precedente prenotazione
     * @return {@code true} se la prenotazione è stata modificata, {@code false} se non è stata modificata.
     */
    @Override
    public boolean modificaPrenotazione(Prenotazione p, boolean stessoPosto, boolean stessoPasseggero){

        try(Connection conn = DatabaseConnection.getConnection()){

            StringBuilder query = new StringBuilder("update \"Prenotazioni\" set ");
            List<Object> params = new ArrayList<>();

            if(stessoPosto && !stessoPasseggero){
                query.append("\"Nome\" = ?, ");
                params.add(p.getPasseggero().getNome());
                query.append("\"Cognome\" = ?, ");
                params.add(p.getPasseggero().getCognome());
            }
            if(!stessoPosto && stessoPasseggero){
                query.append("\"PostoAssegnato\" = ?, ");
                params.add(p.getPostoAssegnato());
            }
            if(!stessoPosto && !stessoPasseggero){
                query.append("\"Nome\" = ?, ");
                params.add(p.getPasseggero().getNome());
                query.append("\"Cognome\" = ?, ");
                params.add(p.getPasseggero().getCognome());
                query.append("\"PostoAssegnato\" = ?, ");
                params.add(p.getPostoAssegnato());
            }

            query.setLength(query.length() - 2);
            query.append(" where \"NumeroBiglietto\" = ?");
            params.add(p.getNumeroBiglietto());
            try(PreparedStatement ps = conn.prepareStatement(query.toString())){
                for(int i = 0; i < params.size(); i++){
                    ps.setObject(i+1, params.get(i));
                }

                return ps.executeUpdate() > 0;
            }

        }
        catch (SQLException e){
            logger.severe("SQLException: " + e.getMessage());
            logger.severe("SQLState: " + e.getSQLState());
            logger.severe("ErrorCode: " + e.getErrorCode());
            e.printStackTrace();
        }
        return false;

    }

    /**
     * Metodo per ottenere una lista di tutti i voli.
     * @return una lista completa di voli
     */
    @Override
    public List<Voli> getTuttiVoli(){
        List<Voli> listaVoli = new ArrayList<>();

        String sql = "select * from \"Voli\"";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                String codiceVolo = rs.getString("CodiceVolo");
                String provenienza = rs.getString("Provenienza");
                String destinazione = rs.getString("Destinazione");
                String compagniaAerea = rs.getString("CompagniaAerea");
                LocalDate data = rs.getDate("DataPartenza").toLocalDate();
                LocalTime oraPartenza = rs.getTime("OraPartenza").toLocalTime();
                String stato = rs.getString("StatoVolo");
                int idGate = rs.getInt("Id_Gate");
                if(rs.wasNull()){
                    Voli volo = new Voli(codiceVolo, provenienza, destinazione, compagniaAerea,
                            data, oraPartenza, stato);
                    listaVoli.add(volo);
                }
                else{
                    Gate gate = new Gate(idGate, codiceVolo);
                    Voli volo = new Voli(codiceVolo, provenienza, destinazione, compagniaAerea,
                            data, oraPartenza, stato, gate);
                    listaVoli.add(volo);
                }

            }
        }
        catch(SQLException e){
            logger.severe("SQLException: " + e.getMessage());
            logger.severe("SQLState: " + e.getSQLState());
            logger.severe("ErrorCode: " + e.getErrorCode());
            e.printStackTrace();
        }
        return listaVoli;
    }

    /**
     * Metodo per aggiungere un volo al database.
     * @param nuovoVolo il nuovo volo da aggiungere
     * @return {@code true} se il volo è stato aggiunto al database correttamente, {@code false} se non è stato aggiunto
     * @throws SQLException se c'è stato un problema con la connessione al database
     */
    @Override
    public boolean aggiungiVolo(Voli nuovoVolo) throws SQLException{
        String codiceVolo = nuovoVolo.getCodiceVolo();
        String provenienza = nuovoVolo.getProvenienza();
        String destinazione = nuovoVolo.getDestinazione();
        String compagniaAerea = nuovoVolo.getCompagniaAerea();
        LocalDate data = nuovoVolo.getDataPartenza();
        LocalTime oraPartenza = nuovoVolo.getOrarioPartenza();
        String stato = nuovoVolo.getStato();
        Gate gate = nuovoVolo.getGate();

        try(Connection conn = DatabaseConnection.getConnection()){
            if(!provenienza.equals("Napoli") && !destinazione.equals("Napoli")){
                throw new SQLException("La partenza o la destinazione devono essere \"Napoli\".");
            }
            try(PreparedStatement ps = conn.prepareStatement("insert into \"Voli\" (\"CodiceVolo\", \"Provenienza\", \"Destinazione\", " +
                    "\"CompagniaAerea\", \"DataPartenza\", \"OraPartenza\", \"StatoVolo\", \"Id_Gate\") values (?, ?, ?, ?, ?, ?, ?, ?)")){

                ps.setString(1, codiceVolo);
                ps.setString(2, provenienza);
                ps.setString(3, destinazione);
                ps.setString(4, compagniaAerea);
                ps.setDate(5, Date.valueOf(data));
                ps.setTime(6, Time.valueOf(oraPartenza));
                ps.setString(7, stato);
                if(gate == null){
                    ps.setNull(8, Types.INTEGER);
                }
                else{
                    ps.setInt(8, gate.getidGate());
                }
                int righe = ps.executeUpdate();

                return righe > 0;
            }
        }
        catch(SQLException e){
            logger.severe("SQLException: " + e.getMessage());
            logger.severe("SQLState: " + e.getSQLState());
            logger.severe("ErrorCode: " + e.getErrorCode());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Metodo per cancellare una prenotazione dalla tabella prenotazioni del database.
     * @param p la prenotazione da cancellare
     * @return {@code true} se la prenotazione è stata cancellata, {@code false} se non è stata cancellata
     */
    @Override
    public boolean cancellaPrenotazione(Prenotazione p){
        String numeroBiglietto = p.getNumeroBiglietto();
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("delete from \"Prenotazioni\" where \"NumeroBiglietto\" = ?")){
            ps.setString(1, numeroBiglietto);

            int rigaModificata = ps.executeUpdate();
            if(rigaModificata > 0){
                return true;
            }
        }
        catch(SQLException e){
            logger.severe("SQLException: " + e.getMessage());
            logger.severe("SQLState: " + e.getSQLState());
            logger.severe("ErrorCode: " + e.getErrorCode());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Metodo per aggiornare le informazioni di un volo esistente
     * @param voloCorrente il volo da modificare
     * @param nuovoVolo il nuovo volo da cui prendere le nuove informazioni
     */
    @Override
    public void aggiornaVolo(Voli voloCorrente, Voli nuovoVolo){
        String codiceVolo = voloCorrente.getCodiceVolo();
        String nuovaDestinazione = nuovoVolo.getDestinazione();
        if(nuovaDestinazione == null){
            nuovaDestinazione = "";
        }
        String nuovaPartenza = nuovoVolo.getProvenienza();
        if(nuovaPartenza == null){
            nuovaPartenza = "";
        }
        LocalDate nuovaDataPartenza = nuovoVolo.getDataPartenza();
        LocalTime nuovaOraPartenza = nuovoVolo.getOrarioPartenza();
        String stato = nuovoVolo.getStato();
        if("null".equals(stato)){
           stato = null;
        }

        try(Connection conn = DatabaseConnection.getConnection()){
            StringBuilder query = new StringBuilder("update \"Voli\" set ");
            List<Object> params = new ArrayList<>();

            if(!nuovaDestinazione.isEmpty()){
                query.append("\"Destinazione\" = ?, ");
                params.add(nuovaDestinazione);
            }
            if(!nuovaPartenza.isEmpty()){
                query.append("\"Provenienza\" = ?, ");
                params.add(nuovaPartenza);
            }
            if(stato != null){
                query.append("\"StatoVolo\" = ?, ");
                params.add(stato);
            }
            if(nuovaOraPartenza != null){
                query.append("\"OraPartenza\" = ?, ");
                params.add(Time.valueOf(nuovaOraPartenza));
            }
            if(nuovaDataPartenza != null){
                query.append("\"DataPartenza\" = ?, ");
                params.add(Date.valueOf(nuovaDataPartenza));
            }
            if(params.isEmpty()){
                throw new IllegalArgumentException("Compila almeno un campo.");
            }

            if (("decollato".equals(stato) || "cancellato".equals(stato))
                    && voloCorrente.getGate() != null
                    && voloCorrente.getGate().getidGate() != 0) {

                try (PreparedStatement ps = conn.prepareStatement(
                        "update \"Voli\" set \"Id_Gate\" = null where \"CodiceVolo\" = ?")) {
                    ps.setString(1, codiceVolo);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps2 = conn.prepareStatement(
                        "update \"Gate\" set \"Cod_Volo_Assegnato\" = null where \"Id_Gate\" = ?")) {
                    ps2.setInt(1, voloCorrente.getGate().getidGate());
                    ps2.executeUpdate();
                }
            }
            query.setLength(query.length() - 2);
            query.append(" where \"CodiceVolo\" = ?");
            params.add(codiceVolo);
            try(PreparedStatement ps = conn.prepareStatement(query.toString())){
                for(int i = 0; i < params.size(); i++){
                    ps.setObject(i+1, params.get(i));
                }
                ps.executeUpdate();
            }
        }
        catch(SQLException e){
            logger.severe("SQLException: " + e.getMessage());
            logger.severe("SQLState: " + e.getSQLState());
            logger.severe("ErrorCode: " + e.getErrorCode());
            e.printStackTrace();
        }
    }


    /**
     * Metodo per ottenere l'id del gate assegnato ad un volo
     * @param p la prenotazione, dalla quale estrapolare il codice del volo
     * @return un intero che rappresenta l'id del gate
     */
    @Override
    public int getGateVolo(Prenotazione p){
        String codiceVolo = p.getCodiceVolo();
        int gate = 0;

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("select \"Id_Gate\" from \"Voli\" where \"CodiceVolo\" = ?")){
            ps.setString(1, codiceVolo);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    gate = rs.getInt("Id_Gate");
                }
            }

        }
        catch(SQLException e){
            logger.severe("SQLException: " + e.getMessage());
            logger.severe("SQLState: " + e.getSQLState());
            logger.severe("ErrorCode: " + e.getErrorCode());
            e.printStackTrace();
        }
        return gate;
    }


    /**
     * Netodo per assegnare un gate ad un volo.
     * @param gate il gate da assegnare
     * @param volo il volo a cui assegnare il gate
     * @return {@code true} se il gate è stato assegnato correttamente, {@code false} se non è stato correttamente assegnato
     */
    @Override
    public boolean assegnaGate(Gate gate, Voli volo) {
        String codiceVolo = volo.getCodiceVolo();
        int codiceGate = gate.getidGate();

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("update \"Gate\" set \"Cod_Volo_Assegnato\" = ? where \"Id_Gate\" = ?")){
            ps.setString(1, codiceVolo);
            ps.setInt(2, codiceGate);

            int righeModificate = ps.executeUpdate();

            if(righeModificate > 0){
                try(PreparedStatement ps2 = conn.prepareStatement("update \"Voli\" set \"Id_Gate\" = ? where \"CodiceVolo\" = ?")){
                    ps2.setInt(1, codiceGate);
                    ps2.setString(2, codiceVolo);
                    ps2.executeUpdate();
                    return true;
                }
            }

        }
        catch (SQLException e){
            logger.severe("SQLException: " + e.getMessage());
            logger.severe("SQLState: " + e.getSQLState());
            logger.severe("ErrorCode: " + e.getErrorCode());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Metodo per ottenere una lista di gate liberi, ovvero quelli con nessun codice volo associato
     * @return la lista dei gate liberi
     */
    @Override
    public List<Gate> getGateLiberi(){
        List<Gate> listaGate = new ArrayList<>();

        String sql = "select * from \"Gate\" where \"Cod_Volo_Assegnato\" is null";

        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                String codiceVolo = rs.getString("Cod_Volo_Assegnato");
                int idGate = rs.getInt("Id_Gate");

                Gate gate = new Gate(idGate, codiceVolo);
                listaGate.add(gate);
            }
        }
        catch(SQLException e){
            logger.severe("SQLException: " + e.getMessage());
            logger.severe("SQLState: " + e.getSQLState());
            logger.severe("ErrorCode: " + e.getErrorCode());
            e.printStackTrace();
        }
        return listaGate;
    }

    /**
     * Metodo per ottenere la lista dei voli il cui gate non è stato assegnato.
     * @return la lista dei voli senza gate assegnato
     */
    @Override
    public List<Voli> getVoliGate(){
        List<Voli> listaVoli = new ArrayList<>();

        String sql = "select * from \"Voli\" where \"Id_Gate\" is null and \"StatoVolo\" not in ('cancellato', 'decollato')";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                String codiceVolo = rs.getString("CodiceVolo");
                String provenienza = rs.getString("Provenienza");
                String destinazione = rs.getString("Destinazione");
                String compagniaAerea = rs.getString("CompagniaAerea");
                LocalDate data = rs.getDate("DataPartenza").toLocalDate();
                LocalTime oraPartenza = rs.getTime("OraPartenza").toLocalTime();
                String stato = rs.getString("StatoVolo");
                int idGate = rs.getInt("Id_Gate");
                if(rs.wasNull()){
                    Voli volo = new Voli(codiceVolo, provenienza, destinazione, compagniaAerea,
                            data, oraPartenza, stato);
                    listaVoli.add(volo);
                }
                else{
                    Gate gate = new Gate(idGate, codiceVolo);
                    Voli volo = new Voli(codiceVolo, provenienza, destinazione, compagniaAerea,
                            data, oraPartenza, stato, gate);
                    listaVoli.add(volo);
                }

            }
        }
        catch(SQLException e){
            logger.severe("SQLException: " + e.getMessage());
            logger.severe("SQLState: " + e.getSQLState());
            logger.severe("ErrorCode: " + e.getErrorCode());
            e.printStackTrace();
        }
        return listaVoli;
    }

    /**
     * Metodo per controllare se un utente è un admin.
     * Effettua una query al database controllando se il flag "admin" è {@code true} oppure {@code false}
     * @param user da controllare
     * @return {@code true} se il flag "admin" nel database è {@code true}, {@code false} se il flag nel database è {@code false}
     */
    @Override
    public boolean isAdmin(Utenti user) {
        String username = user.getUsername();

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM \"Utenti\" WHERE \"Username\" = ? AND \"Admin\" IS TRUE")){
            ps.setString(1, username);

            try(ResultSet rs = ps.executeQuery()){
                return rs.next();
            }

        }
        catch (SQLException e){
            logger.severe("SQL Exception: " + e.getMessage());
            logger.severe("SQLState: " + e.getSQLState());
            logger.severe("ErrorCode: " + e.getErrorCode());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Metodo per aggiungere un utente al database.
     * @param utente l'oggetto utente da aggiungere al database
     * @return {@code true} se l'utente è stato aggiunto al database, {@code false} se non è stato aggiunto
     */
    @Override
    public boolean registrazioneUtente(Utenti utente) {
        String username = utente.getUsername();
        String password = utente.getPassword();

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("select * from \"Utenti\" where \"Username\" = ?")){

            ps.setString(1, username);

            try(ResultSet rs = ps.executeQuery()){
                if(!rs.next()){

                    try(PreparedStatement ps2 = conn.prepareStatement("insert into \"Utenti\" (\"Username\", \"Password\" , \"Admin\") values (?, ?, FALSE)")){
                        ps2.setString(1, username);
                        ps2.setString(2, password);

                        ps2.executeUpdate();
                    }

                    try(PreparedStatement id = conn.prepareStatement("select \"ID_Utente\" from \"Utenti\" where \"Username\" = ?")){
                        id.setString(1, username);

                        try(ResultSet rs2 = id.executeQuery()){
                            if(rs2.next()){
                                utente.setIdUtente(rs2.getInt(1));
                            }
                        }
                    }
                    return true;
                }
            }
        }
        catch(SQLException e){
            logger.severe("SQL Exception: " + e.getMessage());
            logger.severe("SQLState: " + e.getSQLState());
            logger.severe("ErrorCode: " + e.getErrorCode());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Metodo per effettuare l'accesso di un utente il quale è presente nel database.
     * @param utente l'oggetto utente da controllare nel database per verificare se è presente
     * @return {@code true} se l'utente è presente, {@code false} se l'utente non è presente
     * @throws SQLException
     */
    @Override
    public boolean accessoUtente(Utenti utente) throws SQLException{
        String username = utente.getUsername();
        String password = utente.getPassword();

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("select \"ID_Utente\" from \"Utenti\" where \"Username\" = ? and \"Password\" = ?")){

            ps.setString(1, username);
            ps.setString(2, password);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    utente.setIdUtente(rs.getInt("ID_Utente"));
                    return true;
                }
                else{
                    return false;
                }
            }

        }
    }
}
