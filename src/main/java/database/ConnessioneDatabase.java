package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe singleton che gestisce la connessione al database PostgreSQL.
 * Garantisce un'unica istanza attiva della connessione durante il ciclo di vita dell'applicazione.
 * Implementa il pattern WORM (Write-Once, Read-Many) per la connessione.
 */
public class ConnessioneDatabase {

    // singleton instance (serve a mantenere una sola istanza della classe)
    private static ConnessioneDatabase instance;

    // oggetto connessione al database (è l'oggetto che permette di eseguire query sul db)
    private Connection connection = null;

    // credenziali di connessione
    String nome = "alessiapicari";
    String password = "pdb2025!";
    String url = "jdbc:postgresql://localhost:5432/projectHackathon";
    String driver = "org.postgresql.Driver";

    /**
     * Costruttore privato per il pattern Singleton.
     * Stabilisce la connessione al database PostgreSQL.
     *
     * @throws SQLException se si verifica un errore durante la connessione
     * @throws IllegalStateException se il driver JDBC non è trovato
     */
    public ConnessioneDatabase() throws SQLException {
        try {
            Class.forName(driver);
            setConnection(DriverManager.getConnection(url, nome, password));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Impossibile effettuare la connessione al DB!", e);
        }
    }

    /**
     * Restituisce l'istanza singleton della connessione al database.
     * Se l'istanza non esiste o la connessione è chiusa, ne crea una nuova.
     *
     * @return l'istanza singleton di ConnessioneDatabase
     * @throws SQLException se non è possibile stabilire una nuova connessione
     */
    public static ConnessioneDatabase getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new ConnessioneDatabase();
        }
        return instance;
    }

    /**
     * Restituisce l'oggetto Connection attivo.
     *
     * @return l'oggetto Connection per eseguire query sul database
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Imposta manualmente una connessione (usato principalmente per testing).
     *
     * @param connection la connessione da impostare
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
