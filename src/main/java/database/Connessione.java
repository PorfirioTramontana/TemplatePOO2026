package com.progettooo.rubrica.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connessione {


    private static Connessione instance;
    private Connection connection = null;
    private String nome ;
    private String password ;
    private String url ;
    private String driver ;


    public Connessione() throws SQLException {
        nome="postgres";
        password= "alex";
        url="jdbc:postgresql://127.0.0.1:5432/test";
        driver="org.postgresql.Driver";
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, nome, password);

        } catch (ClassNotFoundException ex) {
            System.out.println("Database Connection Fallita: " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    public Connection getConnection() {
        return connection;
    }

    public static Connessione getInstance() throws SQLException {
        if (instance == null) {
            instance = new Connessione();
        } else if (instance.getConnection().isClosed()) {
            instance = new Connessione();
        }
        return instance;
    }
}
