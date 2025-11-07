package gui;

import controller.Controller;
import model.StatoVolo;
import model.Voli;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Classe gui che rappresenta la finestra di modifica di un volo, chiamata dall'Amministratore.
 * Permette l'aggiornamento delle informazioni di un volo.
 */

@SuppressWarnings("unused")
public class ModificaVoloPanel extends JFrame{
    private JPanel titlePanel;
    private JLabel titleLabel;
    private JPanel optionPanel;
    private JTextField destinazioneField;
    private JTextField dataField;
    private JTextField timeField;
    private JButton salvaButton;
    private JLabel newDateLabel;
    private JLabel newTimeLabel;
    private JLabel newDestinazioneLabel;
    private JPanel mainPanel;
    private JLabel statoLabe;
    private JComboBox<String> statoComboBox;
    private JTextField partenzaTextField;
    private JLabel partenzaLabel;
    private final AdminPanel adminPanel;

    private String destinazione;
    private String partenza;
    private String dataFieldString;
    private String timeFieldString;
    private StatoVolo stato;


    public JPanel getPanel(){
        return mainPanel;
    }

    /**
     * Costruttore della finestra.
     * Permette di modificare le informazioni di un volo e salvarle.
     *
     * @param adminpanel riferimento alla precedente finestra dell'amministratore
     * @param vecchioVolo rappresenta il vecchio volo, selezionato dalla tabella, che va modificato/aggiornato
     * @param controller per la logica e l'ottenimento dei dati
     */
    public ModificaVoloPanel(AdminPanel adminpanel, Voli vecchioVolo, Controller controller) {
        this.adminPanel = adminpanel;
        List<String> listaStato;

        setSize(500,400);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        listaStato = controller.getStatoVolo(vecchioVolo);

        statoComboBox.addItem(null);
        for (String s : listaStato){
            statoComboBox.addItem(s);
        }

        salvaButton.addActionListener(e ->{
            destinazione = destinazioneField.getText().trim();
            partenza = partenzaTextField.getText().trim();
            dataFieldString = dataField.getText().trim();
            timeFieldString = timeField.getText().trim();
            String statoSelezionato = (String) statoComboBox.getSelectedItem();
            stato = (statoSelezionato == null) ? null : StatoVolo.fromString(statoSelezionato);

            boolean destinazioneNonVuota = destinazione != null && !destinazione.isEmpty();
            boolean partenzaNonVuota = partenza != null && !partenza.isEmpty();

            if (destinazioneNonVuota && partenzaNonVuota) {
                if (!destinazione.equals("Napoli") && !partenza.equals("Napoli")) {
                    JOptionPane.showMessageDialog(mainPanel,
                            "Se entrambe le destinazione e partenza sono compilate, almeno una deve essere Napoli.");
                    return;
                }
                else if(partenza.equalsIgnoreCase(destinazione)){
                    JOptionPane.showMessageDialog(mainPanel, "Non è possibile inserire un volo da "+ partenza +" a " + destinazione + ".", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            else if(!destinazioneNonVuota && !partenzaNonVuota){
                //continua ignorando gli altri if
            }
            else if (destinazioneNonVuota) {
                if (!destinazione.equals("Napoli") && !vecchioVolo.getProvenienza().equals("Napoli")) {
                    JOptionPane.showMessageDialog(mainPanel,
                            "Se la destinazione è diversa da Napoli, la partenza del volo vecchio deve essere Napoli.");
                    return;
                }
            } else {
                if (!partenza.equals("Napoli") && !vecchioVolo.getDestinazione().equals("Napoli")) {
                    JOptionPane.showMessageDialog(mainPanel,
                            "Se la partenza è diversa da Napoli, la destinazione del volo vecchio deve essere Napoli.");
                    return;
                }
            }

            if((dataFieldString == null || dataFieldString.isEmpty()) && (timeFieldString == null || timeFieldString.isEmpty())){
                try{
                    controller.modificaVolo(vecchioVolo, partenza, destinazione, null, null, stato);
                    JOptionPane.showMessageDialog(mainPanel, "Volo salvato con successo!", "Info", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
                catch(IllegalArgumentException ex){
                    JOptionPane.showMessageDialog(mainPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                adminPanel.aggiornaTable();

            }
            else {
                try {
                    LocalDate data = LocalDate.parse(dataFieldString);
                    LocalTime ora = LocalTime.parse(timeFieldString);

                    controller.modificaVolo(vecchioVolo, partenza, destinazione, data, ora, stato);
                    adminPanel.aggiornaTable();
                    JOptionPane.showMessageDialog(mainPanel, "Volo salvato con successo! (il secondo)", "Info", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
                catch (IllegalArgumentException ex){
                    JOptionPane.showMessageDialog(mainPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                catch (DateTimeParseException _) {
                    JOptionPane.showMessageDialog(mainPanel, "Formato di data o ora non valido.", "Error", JOptionPane.ERROR_MESSAGE);

                }

            }

        });
    }


}
