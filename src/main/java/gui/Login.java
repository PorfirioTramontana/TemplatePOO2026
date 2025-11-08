package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Schermata di autenticazione per gli utenti registrati.
 * Gestisce il processo di login con validazione delle credenziali.
 */
public class Login {
    private static final JFrame frame = new JFrame("Login");
    private JPanel panel1;
    private JTextField fieldEmailUtente;
    private JPasswordField fieldPassword;
    private JButton okButton;
    private JButton cancelButton;

    /**
     * Costruttore che crea un'istanza di una nuova pagina Login.
     *
     * @param frameChiamante il frame precedente (ovvero quello chiamante)
     * @param controller il controller utilizzato per interagire con il model
     */
    public Login(JFrame frameChiamante, Controller controller) {
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.metodoLogin(frame, fieldEmailUtente.getText(), fieldPassword.getPassword());
                azzeraCampi();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                azzeraCampi();
            }
        });
    }

    private void azzeraCampi() {
        fieldEmailUtente.setText("");
        fieldPassword.setText("");
        // sovrascrizione della password (per renderla inaccessibile in modo non autorizzato)
        Arrays.fill(fieldPassword.getPassword(), '\0');
    }
}