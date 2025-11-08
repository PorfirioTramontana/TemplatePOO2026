package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Schermata iniziale dell'applicazione con opzioni di login e registrazione.
 * Punto di ingresso per gli utenti autenticati e non.
 */
public class Home {
    private static final JFrame frameHome = new JFrame("Home");
    private JPanel panel1;
    private JButton loginButton;
    private JButton signupButton;

    private static Controller controller;

    /**
     * Punto di ingresso principale dell'applicazione.
     * Inizializza la finestra principale e il controller.
     *
     * @param args gli argomenti della riga di comando (non utilizzati)
     */
    public static void main(String[] args) {
        try {
            frameHome.setContentPane(new Home().panel1);
            frameHome.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frameHome.pack();

            controller = new Controller();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Costruttore che crea un'istanza di una nuova pagina Home.
     * Inizializza l'interfaccia utente e configura i listener per i pulsanti.
     * - Il pulsante login apre la schermata di autenticazione
     * - Il pulsante signup apre la schermata di registrazione
     */
    public Home() {
        frameHome.setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login(frameHome, controller);
                frameHome.setVisible(false);
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Signup(frameHome, controller);
                frameHome.setVisible(false);
            }
        });
    }
}
