package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controller.Controller;
import model.Prenotazione;
import model.StatoPrenotazione;
import util.Sessione;

import java.sql.SQLException;
import java.util.List;

/**
 * Classe gui che rappresenta la finestra con la tabella delle prenotazioni effettuate dall'utente in sessione.
 * Permette di vederle, modificarle o cancellarle.
 */

@SuppressWarnings("unused")
public class PrenotazioniUtentePanel extends JFrame{
    private JLabel titleLabel;
    private JTable tablePrenotazioni;
    private JScrollPane tableScrollPanel;
    private JLabel errorLabel;
    private JButton modificaPrenotazioneButton;
    private JButton cancellaPrenotazioneButton;
    private JPanel mainPanel;
    private List<Prenotazione> listaPrenotazioni;
    private final Controller controller;
    private ModificaPrenotazionePanel modificaPrenotazionePanel = null;

    public JPanel getPanel(){
        return mainPanel;
    }

    /**
     * Costruttore della finestra, inizializza i componenti e i listener.
     * Permette di modificare una prenotazione, solo se essa non è "cancellata".
     * Permette di cancellare una prenotazione.
     * @param controller per la logica più complessa e l'ottenimento dei dati.
     */
    public PrenotazioniUtentePanel(Controller controller){
        this.controller = controller;

        setSize(500,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        errorLabel.setVisible(false);
        aggiornaListener();

        modificaPrenotazioneButton.addActionListener(e -> {
            int rigaSelezionata = tablePrenotazioni.getSelectedRow();
            if(rigaSelezionata != -1){
                Prenotazione prenotazioneSelezionata = listaPrenotazioni.get(rigaSelezionata);
                if(prenotazioneSelezionata.getStato() == StatoPrenotazione.cancellata){
                    JOptionPane.showMessageDialog(mainPanel, "Non è possibile modificare una prenotazione cancellata."
                    , "Error", JOptionPane.ERROR_MESSAGE);
                }
                else{
                    if(modificaPrenotazionePanel == null || !modificaPrenotazionePanel.isDisplayable()){
                        modificaPrenotazionePanel = new ModificaPrenotazionePanel(controller, prenotazioneSelezionata, this);
                        modificaPrenotazionePanel.setContentPane(modificaPrenotazionePanel.getPanel());
                        modificaPrenotazionePanel.setVisible(true);
                    }
                    else{
                        modificaPrenotazionePanel.toFront();
                        modificaPrenotazionePanel.requestFocusInWindow();
                    }
                }
            }
        });


        cancellaPrenotazioneButton.addActionListener(e -> {
            int rigaSelezionata = tablePrenotazioni.getSelectedRow();
            if(rigaSelezionata != -1){
                Prenotazione prenotazioneSelezionata = listaPrenotazioni.get(rigaSelezionata);
                try{
                    if(controller.cancellaPrenotazione(prenotazioneSelezionata)){
                        JOptionPane.showMessageDialog(mainPanel, "Prenotazione cancellata con successo!", "Info",
                                JOptionPane.INFORMATION_MESSAGE);
                        aggiornaTable();
                    }
                }
                catch(SQLException ex){
                    JOptionPane.showMessageDialog(mainPanel, ex.getMessage(), "Errore di connessione", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        SwingUtilities.invokeLater(this::aggiornaTable);

    }

    /**
     * Metodo per aggiornare il listener dei bottoni che vengono mostrati alla selezione di una prenotazione, l'addove
     * la prenotazione precedentemente selezionata non sia più valida.
     */
    public void aggiornaListener(){
        cancellaPrenotazioneButton.setEnabled(false);
        modificaPrenotazioneButton.setEnabled(false);
        tablePrenotazioni.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()){
                int rigaSelezionata = tablePrenotazioni.getSelectedRow();
                cancellaPrenotazioneButton.setEnabled(rigaSelezionata != -1);
                modificaPrenotazioneButton.setEnabled(rigaSelezionata != -1);
            }
        });
    }

    /**
     * Metodo per l'aggiornamento in tempo reale della avvenuta modifica di una pernotazione, oppure della sua cancellazione
     */
    public void aggiornaTable(){
        String[] colonne = {"Stato", "Numero Biglietto", "Nome", "Cognome", "Codice Volo", "Partenza", "Destinazione", "Data", "Ora", "Posto", "Gate"};
        DefaultTableModel model = new DefaultTableModel(colonne, 0);
        model.setColumnIdentifiers(colonne);

        try{
            listaPrenotazioni = controller.visualizzaPrenotazioni(Sessione.user.getIdUtente());
            int counter = 0;

            for (Prenotazione p : listaPrenotazioni) {
                model.addRow(new Object[] {
                        p.getStato(), p.getNumeroBiglietto(), p.getPasseggero().getNome(), p.getPasseggero().getCognome(), p.getCodiceVolo(), p.getPartenza(),
                        p.getDestinazione(), p.getData(), p.getOra(), p.getPostoAssegnato(), controller.getGatePrenotazione(p)
                });
                counter++;
            }

            if(listaPrenotazioni.isEmpty()){
                errorLabel.setVisible(true);
            }
            else{
                errorLabel.setVisible(false);

                aggiornaListener();
            }
            tablePrenotazioni.setModel(model);
            tableScrollPanel.setViewportView(tablePrenotazioni);

            mainPanel.revalidate();
            mainPanel.repaint();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(mainPanel, e.getMessage()
                    , "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

}
