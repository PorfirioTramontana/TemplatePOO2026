package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * Schermata principale per gli utenti con ruolo concorrente.
 * Offre funzionalità per:
 * creare team,
 * partecipare ad un team,
 * accedere ad un team,
 * cercare un Hackathon,
 * effettuare il logout.
 */
public class BenvenutoConcorrente {
    private static final JFrame frame = new JFrame("Benvenuto Concorrente");
    private JPanel panel1;
    private JLabel labelNomeConcorrente;
    private JComboBox<String> comboBoxSceltaAzione;
    private JButton okButton;

    /**
     * Costruttore che crea un'istanza di una nuova pagina Benvenuto concorrente.
     *
     * @param frameChiamante il frame precedente (ovvero quello chiamante)
     *@param emailConcorrente l'email del concorrente che ha effettuato l'accesso
     * @param controller il controller utilizzato per interagire con il model
     */
    public BenvenutoConcorrente(JFrame frameChiamante, String emailConcorrente, Controller controller) {
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        labelNomeConcorrente.setText("Benvenutə " + controller.getUtentePiattaformaByEmail(emailConcorrente).getNome() + " " + controller.getUtentePiattaformaByEmail(emailConcorrente).getCognome());

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sceltaAzione = Objects.toString(comboBoxSceltaAzione.getSelectedItem(), "Seleziona");
                switch (sceltaAzione) {
                    case "Crea team" :
                        new CreaTeam(frame, emailConcorrente, controller);
                        frame.setVisible(false);
                        break;
                    case "Partecipa a un team" :
                        new PartecipaTeam(frame, emailConcorrente, controller);
                        frame.setVisible(false);
                        break;
                    case "Accedi a un team" :
                        new AccediTeam(frame, emailConcorrente, controller);
                        frame.setVisible(false);
                        break;
                    case "Cerca Hackathon" :
                        new SelezionaHackathon(frame, emailConcorrente, controller.getListaTitoliHackathon(), controller);
                        frame.setVisible(false);
                        break;
                    case "Logout" :
                        frameChiamante.setVisible(true);
                        frame.dispose();
                        break;
                    default :
                        JOptionPane.showMessageDialog(frame,
                                "Scelta non valida!",
                                "Warning page",
                                JOptionPane.WARNING_MESSAGE);
                        frame.setVisible(true);
                }
            }
        });
    }
}
