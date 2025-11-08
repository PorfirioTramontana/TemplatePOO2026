package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Finestra per la selezione di un documento dalla lista disponibile.
 * Mostra tutti i documenti caricati dal team precedentemente selezionato.
 */
public class VisualizzaDocumenti {
    private static final JFrame frame = new JFrame("Visualizza Documenti");
    private JPanel panel1;
    private JComboBox<String> comboBoxDocumento;
    private JButton okButton;
    private JButton cancelButton;

    /**
     * Costruttore che crea un'istanza di una nuova pagina Visualizza documenti.
     *
     * @param frameChiamante il frame precedente (ovvero quello chiamante)
     * @param nomeTeam il nome del team di cui si vogliono visualizzare i documenti
     * @param titoloHackathon il titolo dell'Hackathon a cui partecipa tale team
     * @param emailUtente l'email dell'utente che vuole visualizzare un documento
     * @param controller il controller utilizzato per interagire con il model
     */
    public VisualizzaDocumenti(JFrame frameChiamante, String nomeTeam, String titoloHackathon, String emailUtente, Controller controller) {
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

        List<String> docList = new ArrayList<>();
        docList.add("Seleziona");
        docList.addAll(controller.getListaDocumentiByTeam(nomeTeam, titoloHackathon));
        comboBoxDocumento.setModel(new DefaultComboBoxModel<>(docList.toArray(new String[0])));

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedFile = (String) comboBoxDocumento.getSelectedItem();
                controller.metodoVisualizzaDocumenti(frame, selectedFile, nomeTeam, titoloHackathon, emailUtente);
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
        comboBoxDocumento.setSelectedIndex(0);
    }
}
