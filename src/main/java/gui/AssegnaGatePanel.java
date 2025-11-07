package gui;

import controller.Controller;
import model.Gate;
import model.Voli;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Classe gui per l'assegnazione dei gate ai voli.
 * Permette di selezionare i voli senza gate e assegnarne uno tra quelli liberi.
 */

@SuppressWarnings("unused")
public class AssegnaGatePanel extends JFrame{
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JComboBox<Voli> voliComboBox;
    private JComboBox<Gate> gateComboBox;
    private JPanel optionPanel;
    private JButton salvaButton;


    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Costruttore per la creazione della finestra.
     * Si ottiene la lista dei voli senza gate e la lista dei gate liberi.
     * E' quindi possibile effettuarne l'assegnazione.
     * @param adminPanel riferimento alla pagina precedente
     * @param controller per ottenimento dati e logica complessa
     */
    public AssegnaGatePanel(AdminPanel adminPanel, Controller controller) {
        List<Voli> listaVoli;
        List<Gate> listaGate;

        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        try{
            listaVoli = controller.getVoliGate();
            listaGate = controller.getGateLiberi();

            voliComboBox.addItem(null);
            for (Voli v : listaVoli) {
                voliComboBox.addItem(v);
            }
            gateComboBox.addItem(null);
            for (Gate g : listaGate) {
                gateComboBox.addItem(g);
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(mainPanel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        salvaButton.addActionListener(e -> {
            Voli voloSelezionato = (Voli) voliComboBox.getSelectedItem();
            Gate gateSelezionato = (Gate) gateComboBox.getSelectedItem();

            if(controller.salvaGate(gateSelezionato, voloSelezionato)){
                JOptionPane.showMessageDialog(mainPanel, "Gate assegnato correttamente al volo "
                        + voloSelezionato.getCodiceVolo(), "Info", JOptionPane.INFORMATION_MESSAGE);
                adminPanel.aggiornaTable();
            }
            else{
                JOptionPane.showMessageDialog(mainPanel, "Errore nella connessione al database.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

}
