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
 * Finestra per partecipare a un team esistente in un Hackathon.
 * Richiede la password del team per l'accesso.
 */
public class PartecipaTeam {
    private static final String SDEFAULT = "Seleziona";
    private static final JFrame frame = new JFrame("Partecipa team");
    private JPanel panel1;
    private JComboBox<String> comboBoxTitoloHackathon;
    private JComboBox<String> comboBoxNomeTeam;
    private JPasswordField fieldPasswordTeam;
    private JButton okButton;
    private JButton cancelButton;

    /**
     * Costruttore che crea un'istanza di una nuova pagina Login.
     *
     * @param frameChiamante il frame precedente (ovvero quello chiamante)
     * @param emailConcorrente l'email del concorrente che vuole partecipare al team
     * @param controller il controller utilizzato per interagire con il model
     */
    public PartecipaTeam(JFrame frameChiamante, String emailConcorrente, Controller controller) {
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

        // primo comboBox Hackathon con default
        List<String> hackathonList = new ArrayList<>();
        hackathonList.add(SDEFAULT);
        hackathonList.addAll(controller.getListaTitoliHackathonNonTerminati());
        comboBoxTitoloHackathon.setModel(new DefaultComboBoxModel<>(hackathonList.toArray(new String[0])));

        // secondo comboBox Team solo default
        comboBoxNomeTeam.addItem(SDEFAULT);

        comboBoxTitoloHackathon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedHackathon = (String) comboBoxTitoloHackathon.getSelectedItem();
                // evita di aggiornare se è selezionato SDEFAULT
                if (selectedHackathon != null && !selectedHackathon.equals(SDEFAULT)) {
                    // secondo comboBox Team con default
                    List<String> teamList = new ArrayList<>();
                    teamList.add(SDEFAULT);
                    teamList.addAll(controller.getListaNomiTeamLiberiByHackathon(selectedHackathon));
                    comboBoxNomeTeam.setModel(new DefaultComboBoxModel<>(teamList.toArray(new String[0])));
                } else {
                    // reset comboBox Team se non è selezionato un Hackathon valido
                    comboBoxNomeTeam.setModel(new DefaultComboBoxModel<>(new String[]{SDEFAULT}));
                }
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hackathonSelezionato = Objects.toString(comboBoxTitoloHackathon.getSelectedItem(), SDEFAULT);
                String nomeTeam = Objects.toString(comboBoxNomeTeam.getSelectedItem(), SDEFAULT);
                controller.metodoPartecipaTeam(frame, hackathonSelezionato, nomeTeam, fieldPasswordTeam.getPassword(), emailConcorrente);
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
        fieldPasswordTeam.setText("");
    }
}
