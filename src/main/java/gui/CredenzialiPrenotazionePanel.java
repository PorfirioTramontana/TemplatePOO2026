package gui;

import javax.swing.*;
import controller.Controller;
import model.Voli;
import util.Sessione;

/**
 * Classe gui per la creazione di una Prenotazione.
 * In questa finestra vanno inseriti i dati della prenotazione, come le credenziali e la preferenza sul posto.
 */

@SuppressWarnings("unused")
public class CredenzialiPrenotazionePanel extends JFrame{
    private JPanel panel1;
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField postoField;
    private JLabel titleLabel;
    private JButton confermaButton;

    public JPanel getPanel() {
        return panel1;
    }

    /**
     * Costruttore base per la creazione della finestra.
     * Si effettua un controllo sul rispetto del formato del posto e si salvano le informazioni delle credenziali.
     * @param controller per logica complessa e salvataggio della prenotazione.
     * @param volo indica il volo da prenotare.
     */
    public CredenzialiPrenotazionePanel(Controller controller, Voli volo) {

        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        confermaButton.addActionListener(e ->{
            try{
                if(postoField.getText().matches("[1-6]-[A-K]") &&
                        controller.creaPrenotazione(Sessione.user.getIdUtente(), volo, nomeField.getText(), cognomeField.getText(), postoField.getText())){
                    JOptionPane.showMessageDialog(null, "Prenotazione creata con successo!");
                    dispose();

                }
                else{
                    JOptionPane.showMessageDialog(null, "Formato del posto non valido.");
                }
            }
            catch(IllegalArgumentException ex){
                JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }

        });



    }


}
