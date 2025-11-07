package gui;
import controller.Controller;
import model.Voli;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Classe gui per la pagina principale dell'Amministratore.
 * Permette di: aggiungere un volo, modificare un volo e assegnare i gate.
 */

@SuppressWarnings("unused")
public class AdminPanel extends JPanel{
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JButton logoutButton;
    private JLabel titleLabel;
    private JPanel tablePanel;
    private JScrollPane tableScrollPanel;
    private JTable voliTable;
    private JPanel optionPanel;
    private JButton aggiungiUnVoloButton;
    private JButton modificaVoloButton;
    private JButton assegnaGateButton;
    private Controller controller;
    private Window loginWindow;
    private List<Voli> tuttiVoli;
    private ModificaVoloPanel modificaVoloPanel = null;
    private AssegnaGatePanel assegnaGatePanel = null;
    private AggiungiVoloPanel aggiungiVoloPanel = null;

    public JPanel getPanel1() {
        return mainPanel;
    }

    /**
     * Costruttore base per la creazione della finestra.
     * Ottiene e mostra una tabella completa dei voli.
     * Permette inoltre di modificare il volo selezionato dalla tabella, aggiungerne uno nuovo o assegnare i gate.
     * @param controller per ottenimento dati e logica complessa
     * @param loginWindow riferimento alla pagina di login, utile per il "Logout"
     */
    public AdminPanel(Controller controller, Window loginWindow) {
        this.controller = controller;
        this.loginWindow = loginWindow;


        logoutButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(mainPanel).dispose();
            if(loginWindow != null){
                loginWindow.setVisible(true);
            }
        });

        String[] colonne = {"Codice Volo", "Compagnia", "Partenza", "Destinazione", "Data", "Ora", "Stato", "Gate"};
        DefaultTableModel model = new DefaultTableModel(colonne,0);
        model.setColumnIdentifiers(colonne);

        try{
            tuttiVoli = controller.visualizzaVoli();

            for(Voli v : tuttiVoli){
                model.addRow(new Object[]{
                        v.getCodiceVolo(), v.getCompagniaAerea(), v.getProvenienza(), v.getDestinazione(), v.getDataPartenza(),
                        v.getOrarioPartenza(), v.getStato(), (v.getGate() != null ? v.getGate().getidGate() : "Non Assegnato")
                });
            }
            voliTable.setModel(model);
            tableScrollPanel.setViewportView(voliTable);

        }
        catch(SQLException _){
            JOptionPane.showMessageDialog(null, "Errore nella connessione al database"
                    , "Errore", JOptionPane.ERROR_MESSAGE);

        }

        modificaVoloButton.setEnabled(false);
        voliTable.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()){
                int rigaSelezionata = voliTable.getSelectedRow();
                modificaVoloButton.setEnabled(rigaSelezionata != -1);
            }
        });

        modificaVoloButton.addActionListener(e -> {
            int rigaSelezionata = voliTable.getSelectedRow();
            if(rigaSelezionata != -1){
                Voli editVolo = tuttiVoli.get(rigaSelezionata);
                if(modificaVoloPanel == null || !modificaVoloPanel.isDisplayable()){
                    modificaVoloPanel = new ModificaVoloPanel(this, editVolo, controller);
                    modificaVoloPanel.setContentPane(modificaVoloPanel.getPanel());
                    modificaVoloPanel.setVisible(true);
                }
                else{
                    modificaVoloPanel.toFront();
                    modificaVoloPanel.requestFocusInWindow();
                }
            }
        });

        assegnaGateButton.addActionListener(e -> {
            if(assegnaGatePanel == null || !assegnaGatePanel.isDisplayable()){
                assegnaGatePanel = new AssegnaGatePanel(this, controller);
                assegnaGatePanel.setContentPane(assegnaGatePanel.getMainPanel());
                assegnaGatePanel.setVisible(true);
            }
            else{
                assegnaGatePanel.toFront();
                assegnaGatePanel.requestFocusInWindow();
            }
        });

        aggiungiUnVoloButton.addActionListener(e -> {
            if(aggiungiVoloPanel == null || !aggiungiVoloPanel.isDisplayable()){
                aggiungiVoloPanel = new AggiungiVoloPanel(this, controller);
                aggiungiVoloPanel.setContentPane(aggiungiVoloPanel.getPanel());
                aggiungiVoloPanel.setVisible(true);
            }
            else{
                aggiungiVoloPanel.toFront();
                aggiungiVoloPanel.requestFocusInWindow();
            }

        });

    }

    /**
     * Metodo di aggiornamento dinamico della tabella dei voli
     * Permette l'aggiornamento della tabella.
     */
    public void aggiornaTable(){
        String[] colonne = {"Codice Volo", "Compagnia", "Partenza", "Destinazione", "Data", "Ora", "Stato", "Gate"};
        DefaultTableModel model = new DefaultTableModel(colonne,0);
        model.setColumnIdentifiers(colonne);

        try{
            tuttiVoli = controller.visualizzaVoli();

            for(Voli v : tuttiVoli){
                model.addRow(new Object[]{
                        v.getCodiceVolo(), v.getCompagniaAerea(), v.getProvenienza(), v.getDestinazione(), v.getDataPartenza(),
                        v.getOrarioPartenza(), v.getStato(), (v.getGate() != null ? v.getGate().getidGate() : "Non Assegnato")
                });
            }
            voliTable.setModel(model);
            tableScrollPanel.setViewportView(voliTable);

        }
        catch(SQLException _){
            JOptionPane.showMessageDialog(null, "Errore nella connessione al database"
                    , "Errore", JOptionPane.ERROR_MESSAGE);

        }
    }


}
