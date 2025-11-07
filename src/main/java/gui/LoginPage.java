package gui;
import controller.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import model.Amministratori;
import model.Utenti;
import util.Sessione;

/**
 * Classe gui per la prima finestra, la finestra di Login
 * In base alle credenziali inserite, si viene reindirizzati alla pagina di riferimento.
 */

@SuppressWarnings("unused")
public class LoginPage extends JPanel{

    private JPanel panel1;
    private JLabel passwordLabel;
    private JLabel usernameLabel;
    private JTextField usernameField1;
    private JPasswordField passwordField1;
    private JButton accediButton;
    private JLabel titleField;
    private JLabel errorLabel;
    private JButton registrazioneButton;

    public JPanel mainPanel(){
        return panel1;
    }

    /**
     * Costruttore base.
     * Prendendo in input username e password, viene creato un oggetto Utenti generico.
     * L'oggetto viene confrontato con i dati del database per vedere se è presente e in caso contrario avvisare e consigliare
     * la registrazione.
     * In caso positivo, viene effettuato un controllo interno per capirne il grado (Admin o Utente generico) e, in base
     * a ciò, viene mostrata la pagina di riferimento
     * @param controller per ottenimento dati e operazioni complesse.
     */
    public LoginPage(Controller controller) {

        errorLabel.setVisible(false);

        registrazioneButton.addActionListener(e -> {
            String username = usernameField1.getText();
            String password = String.valueOf(passwordField1.getPassword());

            Utenti utente = new Utenti(username, password);

            if(controller.registraUtente(utente)){
                errorLabel.setVisible(true);
                errorLabel.setText("Utente creato con successo!");
                errorLabel.setForeground(Color.BLACK);
            }

        });

        accediButton.addActionListener(e -> {
           String username = usernameField1.getText();
           String password = String.valueOf(passwordField1.getPassword());
           Utenti utente = new Utenti(username, password);

           if(!controller.accessoUtente(utente)) {
               errorLabel.setVisible(true);
               errorLabel.setText("<html>Nessun utente trovato.<br>Prova a Registrarti!</html>");

           }
           else{

               Utenti accessoUtente = controller.getAdminFlag(utente);

               Sessione.user = accessoUtente;

               if(accessoUtente instanceof Amministratori){
                   Window finestraCorrente = SwingUtilities.getWindowAncestor(panel1);

                   if(finestraCorrente != null){
                       finestraCorrente.setVisible(false);
                   }
                   JFrame adminPage = new JFrame("Admin Page");
                   AdminPanel adminpanel = new AdminPanel(controller, finestraCorrente);

                   adminPage.setSize(600, 400);
                   adminPage.setLocationRelativeTo(null);
                   adminPage.setVisible(true);
                   adminPage.setResizable(false);
                   adminPage.setLocationRelativeTo(null);
                   adminPage.setContentPane(adminpanel.getPanel1());
                   adminPage.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                   adminPage.addWindowListener(new WindowAdapter() {
                       @Override
                       public void windowClosing(WindowEvent e) {
                           if(finestraCorrente != null){
                               finestraCorrente.dispose();
                           }
                       }
                   });
               }
               else{
                   Window finestraCorrente = SwingUtilities.getWindowAncestor(panel1);
                   if(finestraCorrente != null){
                       finestraCorrente.setVisible(false);
                   }
                   JFrame userPage = new JFrame("User Page");
                   UserPanel userpanel = new UserPanel(controller, finestraCorrente);

                   userPage.setSize(600, 400);
                   userPage.setLocationRelativeTo(null);
                   userPage.setVisible(true);
                   userPage.setResizable(false);
                   userPage.setContentPane(userpanel.getPanel());
                   userPage.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                   userPage.addWindowListener(new WindowAdapter() {
                       @Override
                       public void windowClosing(WindowEvent e) {
                           if(finestraCorrente != null){
                               finestraCorrente.dispose();
                           }
                       }
                   });
               }
           }
        });
    }
}
