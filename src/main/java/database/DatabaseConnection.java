package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

/**
 * Classe fondamentale per la connessione al database.
 * Effettua la connessione al database, utilizzando le informazioni riportate.
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/Aeroporto";
    private static final String USER = "postgres";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    /**
     * Metodo per stabilire la connessione.
     * @return la connessione tra il progetto e il database.
     * @throws SQLException se la connessione non è stata stabilita.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);

    }


}
