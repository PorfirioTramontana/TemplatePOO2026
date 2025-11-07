package gui;

import controller.Controller;
import model.Gate;
import model.Voli;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Classe gui per la creazione di un nuovo volo da parte dell'amministratore.
 * Permette di inserire nel database un nuovo volo manualmente, fornendo tutte le informazioni necessarie.
 */

@SuppressWarnings("unused")
public class AggiungiVoloPanel extends JFrame{
    private JPanel headerPanel;
    private JLabel titleLabel;
    private JLabel compagniaLabel;
    private JLabel partenzaLabel;
    private JLabel destinazioneLabel;
    private JLabel dataLabel;
    private JLabel oraLabel;
    private JLabel gateLabel;
    private JTextField compagniaTextField;
    private JTextField partenzaTextField;
    private JTextField destinazioneTextField;
    private JTextField dataPartenzaTextField;
    private JTextField oraPartenzaTextField;
    private JComboBox<Gate> gateComboBox;
    private JButton aggiungiButton;
    private JPanel mainPanel;
    private JPanel optionPanel;
    private JPanel buttonPanel;
    private String destinazione;
    private String compagnia;
    private String partenza;
    private Gate gate;
    private Voli volo;

    public JPanel getPanel(){
        return mainPanel;
    }

    /**
     * Costruttore base.
     * Ottiene i valori dai campi input e i gate disponibili (qualora si vogliano aggiungere direttamente).
     * Permette quindi di creare un nuovo volo, al momento dell'inserimento di informazioni complete e corrette.
     * @param adminpanel riferimento alla precedente pagina
     * @param controller per ottenimento dati e logica complessa
     */
    public AggiungiVoloPanel(AdminPanel adminpanel, Controller controller) {
        List<Gate> listaGate;

        setSize(600, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        listaGate = controller.getGateLiberi();

        gateComboBox.addItem(null);
        for (Gate g : listaGate) {
            gateComboBox.addItem(g);
        }

        aggiungiButton.addActionListener(e -> {

            destinazione = destinazioneTextField.getText();
            compagnia = compagniaTextField.getText();
            partenza = partenzaTextField.getText();
            if(destinazione.isEmpty() || compagnia.isEmpty() || partenza.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Compilare tutti i campi.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else if(!partenza.equalsIgnoreCase("Napoli") && !destinazione.equalsIgnoreCase("Napoli")) {
                JOptionPane.showMessageDialog(mainPanel, "La partenza o la destinazione devono includere \"Napoli\".", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else if(partenza.equalsIgnoreCase(destinazione)){
                JOptionPane.showMessageDialog(mainPanel, "Non è possibile inserire un volo da " + partenza + " verso " + destinazione +"." , "Error", JOptionPane.ERROR_MESSAGE);
            }
            else{
                try{
                    LocalTime ora = LocalTime.parse(oraPartenzaTextField.getText());
                    LocalDate data = LocalDate.parse(dataPartenzaTextField.getText());

                    if(gateComboBox.getSelectedItem() == null){
                        volo = new Voli(controller.generaCodiceVolo(), partenza, destinazione, compagnia, data, ora, "programmato");
                    }
                    else{
                        gate = (Gate) gateComboBox.getSelectedItem();
                        volo = new Voli(controller.generaCodiceVolo(), partenza, destinazione, compagnia, data, ora, "programmato", gate);
                    }

                    if(controller.aggiungiVolo(volo)){
                        JOptionPane.showMessageDialog(mainPanel, "Volo aggiunto correttamente!", "Info", JOptionPane.INFORMATION_MESSAGE);
                        adminpanel.aggiornaTable();
                        compagniaTextField.setText("");
                        partenzaTextField.setText("");
                        destinazioneTextField.setText("");
                        dataPartenzaTextField.setText("");
                        oraPartenzaTextField.setText("");
                        gateComboBox.setSelectedItem(null);

                    }
                    else{
                        JOptionPane.showMessageDialog(mainPanel, "Errore nell'aggiunta del volo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                catch(DateTimeParseException _){
                    JOptionPane.showMessageDialog(mainPanel, "Formato data o ora non valido", "Error", JOptionPane.ERROR_MESSAGE);
                }
                catch(SQLException sql){
                    JOptionPane.showMessageDialog(mainPanel, sql.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


    }
}
