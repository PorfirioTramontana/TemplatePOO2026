package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * Schermata principale per gli utenti con ruolo giudice.
 * Offre funzionalità per:
 * giudicare team assegnati,
 * visionare team giudicati,
 * cercare Hackathon a lui assegnati e non,
 * effettuare il logout.
 */
public class BenvenutoGiudice {
    private static final JFrame frame = new JFrame("Benvenuto Giudice");
    private JPanel panel1;
    private JLabel labelNomeGiudice;
    private JComboBox<String> comboBoxSceltaAzione;
    private JButton okButton;

    /**
     * Costruttore che crea un'istanza di una nuova pagina Benvenuto giudice.
     *
     * @param frameChiamante il frame precedente (ovvero quello chiamante)
     * @param emailGiudice l'email del giudice che ha effettuato l'accesso
     * @param controller il controller utilizzato per interagire con il model
     */
    public BenvenutoGiudice(JFrame frameChiamante, String emailGiudice, Controller controller) {
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        labelNomeGiudice.setText("Benvenutə " + controller.getUtentePiattaformaByEmail(emailGiudice).getNome() + " " + controller.getUtentePiattaformaByEmail(emailGiudice).getCognome());

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sceltaAzione = Objects.toString(comboBoxSceltaAzione.getSelectedItem(), "");
                switch (sceltaAzione) {
                    case "Giudica team assegnati" :
                        if (controller.getListaNomiTeamGiudicabiliByGiudice(emailGiudice).isEmpty()) {
                            paginaDiWarning("Non ci sono team assegnati da giudicare");
                        } else {
                            new GiudicaTeam(frame, emailGiudice, controller);
                            frame.setVisible(false);
                        }
                        break;
                    case "Visiona team giudicati" :
                        if (controller.getListaNomiTeamGiudicatiByGiudice(emailGiudice).isEmpty()) {
                            paginaDiWarning("Non ci sono team assegnati giudicati");
                        } else {
                            new VisionaTeam(frame, emailGiudice, controller);
                            frame.setVisible(false);
                        }
                        break;
                    case "Cerca Hackathon assegnati" :
                        if (controller.getListaTitoliHackathonAssegnatiToGiudice(emailGiudice).isEmpty()) {
                            paginaDiWarning("Non ci sono Hackathon assegnati");
                        } else {
                            new SelezionaHackathon(frame, emailGiudice, controller.getListaTitoliHackathonAssegnatiToGiudice(emailGiudice), controller);
                            frame.setVisible(false);
                        }
                        break;
                    case "Cerca Hackathon" :
                        new SelezionaHackathon(frame, emailGiudice, controller.getListaTitoliHackathon(), controller);
                        frame.setVisible(false);
                        break;
                    case "Logout" :
                        frameChiamante.setVisible(true);
                        frame.dispose();
                        break;
                    default :
                        paginaDiWarning("Scelta non valida!");
                }
            }
        });
    }

    private void paginaDiWarning(String messaggio) {
        JOptionPane.showMessageDialog(frame,
                messaggio,
                "Warning page",
                JOptionPane.WARNING_MESSAGE);
        frame.setVisible(true);
    }
}
