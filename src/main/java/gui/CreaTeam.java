package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Schermata per la creazione di un nuovo team per partecipare ad un Hackathon.
 * Permette di selezionare l'Hackathon a cui si vuole partecipare e definire nome e password del team da creare.
 */
public class CreaTeam {
    private static final JFrame frame = new JFrame("Crea team");
    private JPanel panel1;
    private JComboBox<String> comboBoxTitoloHackathon;
    private JTextField fieldNomeTeam;
    private JPasswordField fieldPasswordTeam;
    private JButton okButton;
    private JButton cancelButton;

    /**
     * Costruttore che crea un'istanza di una nuova pagina Crea team.
     *
     * @param frameChiamante il frame precedente (ovvero quello chiamante)
     * @param emailConcorrente l'email del concorrente che vuole creare un nuovo team
     * @param controller il controller utilizzato per interagire con il model
     */
    public CreaTeam(JFrame frameChiamante, String emailConcorrente, Controller controller) {
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

        // comboBox Hackathon con default
        List<String> hackathonList = new ArrayList<>();
        hackathonList.add("Seleziona");
        hackathonList.addAll(controller.getListaTitoliHackathonNonTerminati());
        comboBoxTitoloHackathon.setModel(new DefaultComboBoxModel<>(hackathonList.toArray(new String[0])));

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hackathonSelezionato = Objects.toString(comboBoxTitoloHackathon.getSelectedItem(), "Seleziona");
                controller.metodoCreaTeam(frame, hackathonSelezionato, fieldNomeTeam.getText(), fieldPasswordTeam.getPassword(), emailConcorrente);
                // sovrascrizione della password (per renderla inaccessibile in modo non autorizzato)
                Arrays.fill(fieldPasswordTeam.getPassword(), '\0');
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
        comboBoxTitoloHackathon.setSelectedIndex(0);
        fieldNomeTeam.setText("");
        fieldPasswordTeam.setText("");
    }
}