package gui;

import javax.swing.*;
import java.util.List;

/**
 * Schermata per la visualizzazione della classifica di un Hackathon.
 * Mostra i team partecipanti ordinati per punteggio (decrescente).
 */
public class ClassificaHackathon {
    private static final JFrame frame = new JFrame("Classifica Hackathon");;
    private JPanel panel1;
    private JLabel labelTitoloClassifica;
    private JTextArea textAreaClassifica;

    /**
     * Costruttore che crea un'istanza di una nuova pagina Classifica Hackathon.
     *
     * @param frameChiamante il frame precedente (ovvero quello chiamante)
     * @param classifica la classifica che si vuole visualizzare
     * @param titoloHackathon il titolo dell'Hackathon di cui si vuole visualizzare la classifica
     */
    public ClassificaHackathon(JFrame frameChiamante, List<String> classifica, String titoloHackathon) {
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

        labelTitoloClassifica.setText("Classifica Hackathon '" + titoloHackathon + "'");
        textAreaClassifica.setEditable(false);
        textAreaClassifica.setText("posizione \t| punteggio \t| nome team " + classifica);
    }
}
