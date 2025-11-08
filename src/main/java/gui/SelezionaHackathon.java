package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Finestra per la selezione di un Hackathon dalla lista disponibile.
 * Filtra gli Hackathon in base al ruolo e al tipo di ricerca (es: Hackathon e Hackathon creati).
 */
public class SelezionaHackathon {
    private static final String SDEFAULT = "Seleziona";
    private static final JFrame frame = new JFrame("Ricerca Hackathon");
    private JPanel panel1;
    private JComboBox<String> comboBoxTitoloHackathon;
    private JButton okButton;
    private JButton cancelButton;

    /**
     * Costruttore che crea un'istanza di una nuova pagina Seleziona Hackathon.
     *
     * @param frameChiamante il frame precedente (ovvero quello chiamante)
     * @param emailUtente l'email dell'utente che vuole selezionare un Hackathon
     * @param listaTitoliHackathon la lista dei titoli degli Hackathon selezionabili
     * @param controller il controller utilizzato per interagire con il model
     */
    public SelezionaHackathon(JFrame frameChiamante, String emailUtente, List<String> listaTitoliHackathon, Controller controller) {
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
        hackathonList.add(SDEFAULT);
        hackathonList.addAll(listaTitoliHackathon);
        comboBoxTitoloHackathon.setModel(new DefaultComboBoxModel<>(hackathonList.toArray(new String[0])));

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hackathonSelezionato = Objects.toString(comboBoxTitoloHackathon.getSelectedItem(), SDEFAULT);
                controller.metodoSelezionaHackathon(frame, hackathonSelezionato, emailUtente);
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
