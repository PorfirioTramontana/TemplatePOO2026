package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Schermata per la convocazione di giudici ad un Hackathon, da parte dell'organizzatore di quest'ultimo.
 * Permette di selezionare un giudice e associarlo a un Hackathon specifico (precedentemente creato).
 */
public class ConvocaGiudice {
    private static final String SDEFAULT = "Seleziona";
    private static final JFrame frame = new JFrame("Convoca giudice");
    private JPanel panel1;
    private JComboBox<String> comboBoxEmailGiudice;
    private JComboBox<String> comboBoxTitoloHackathon;
    private JButton okButton;
    private JButton cancelButton;

    /**
     * Costruttore che crea un'istanza di una nuova pagina Convoca giudice.
     *
     * @param frameChiamante il frame precedente (ovvero quello chiamante)
     * @param emailOrganizzatore la email dell'organizzatore dell'Hackathon
     * @param controller il controller utilizzato per interagire con il model
     */
    public ConvocaGiudice(JFrame frameChiamante, String emailOrganizzatore, Controller controller) {
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
        hackathonList.addAll(controller.getListaTitoliHackathonAnnunciatiByCreatore(emailOrganizzatore));
        comboBoxTitoloHackathon.setModel(new DefaultComboBoxModel<>(hackathonList.toArray(new String[0])));

        // secondo comboBox Giudice con default
        // utilizzo di liste parallele per non perdere l'associazione nome-email
        List<String> giudiceNominativoList = new ArrayList<>();
        List<String> giudiceEmailList = new ArrayList<>();
        giudiceNominativoList.add(SDEFAULT);
        giudiceEmailList.add("");
        giudiceNominativoList.addAll(controller.getListaNominativiGiudici());
        giudiceEmailList.addAll(controller.getListaEmailGiudici());
        comboBoxEmailGiudice.setModel(new DefaultComboBoxModel<>(giudiceNominativoList.toArray(new String[0])));

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ricavo l'indice del nominativo selezionato, per accedere alla rispettiva email
                int selectedIndex = comboBoxEmailGiudice.getSelectedIndex();
                String hackathonSelezionato = Objects.toString(comboBoxTitoloHackathon.getSelectedItem(), SDEFAULT);
                controller.metodoConvocaGiudice(frame, hackathonSelezionato, giudiceEmailList.get(selectedIndex), emailOrganizzatore);
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
        comboBoxEmailGiudice.setSelectedIndex(0);
    }
}
