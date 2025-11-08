package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Schermata per l'accesso a un team esistente da parte di un concorrente.
 * Permette di selezionare un Hackathon e visualizzare il team a cui si è iscritti.
 */
public class AccediTeam {
    private static final String SDEFAULT = "Seleziona";
    private static final JFrame frame = new JFrame("Accedi team");
    private JPanel panel1;
    private JComboBox<String> comboBoxTitoloHackathon;
    private JComboBox<String> comboBoxNomeTeam;
    private JButton okButton;
    private JButton cancelButton;

    /**
     * Costruttore che crea un'istanza di una nuova pagina Accedi team.
     *
     * @param frameChiamante il frame precedente (ovvero quello chiamante)
     * @param emailConcorrente la email del concorrente
     * @param controller il controller utilizzato per interagire con il model
     */
    public AccediTeam(JFrame frameChiamante, String emailConcorrente , Controller controller) {
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
        hackathonList.addAll(controller.getListaTitoliHackathon());
        comboBoxTitoloHackathon.setModel(new DefaultComboBoxModel<>(hackathonList.toArray(new String[0])));

        // secondo comboBox Team solo default
        comboBoxNomeTeam.addItem(SDEFAULT);

        comboBoxTitoloHackathon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedHackathon = (String) comboBoxTitoloHackathon.getSelectedItem();
                // evita di aggiornare se è selezionato (SDEFAULT)
                if (selectedHackathon != null && !selectedHackathon.equals(SDEFAULT)) {
                    // secondo comboBox Team con default
                    List<String> teamList = new ArrayList<>();
                    teamList.add(SDEFAULT);
                    String nomeTeam = controller.getNomeTeamByHackathonAndConcorrente(selectedHackathon, emailConcorrente);
                    if (nomeTeam != null) {
                        teamList.add(nomeTeam);
                    }
                    comboBoxNomeTeam.setModel(new DefaultComboBoxModel<>(teamList.toArray(new String[0])));
                } else {
                    // reset comboBoxNomeTeam se non è selezionato un Hackathon valido
                    comboBoxNomeTeam.setModel(new DefaultComboBoxModel<>(new String[] {SDEFAULT}));
                }
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // se getSelectedItem() restituisce null, il metodo toString di Object restituisce il secondo parametro, ovvero: SDEFAULT
                String hackathonSelezionato = Objects.toString(comboBoxTitoloHackathon.getSelectedItem(), SDEFAULT);
                String teamSelezionato = Objects.toString(comboBoxNomeTeam.getSelectedItem(), SDEFAULT);
                controller.metodoAccediTeam(frame, hackathonSelezionato, teamSelezionato, emailConcorrente);
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
    }
}
