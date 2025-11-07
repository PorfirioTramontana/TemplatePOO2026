package gui;
import controller.Controller;
import model.StatoVolo;
import model.Voli;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Classe gui per la finestra di ricerca dei voli, chiamata dall'utente.
 * Mostra in base ai criteri di ricerca, la tabella dei voli, permettendo una prenotazione.
 */

@SuppressWarnings("unused")
public class RicercaVoliPanel extends JFrame{
    private JPanel panel1;
    private JTextField partenzaTextField;
    private JTextField destinazioneTextField;
    private JButton cercaButton;
    private JTable tabellaVoli;
    private JScrollPane scrollPanel1;
    private JPanel searchPanel;
    private JPanel prenotaPanel;
    private JButton prenotaButton;
    private JLabel noteLabel;
    private List<Voli> listaVoliRicercati;
    private CredenzialiPrenotazionePanel prenotazionePanel = null;


    public JPanel getPanel(){
        return panel1;
    }

    /**
     * Costruttore della finestra.
     * Inizializza i componenti e i listener e attende l'input dei criteri di ricerca.
     * Se un campo viene lasciato vuoto, la ricerca lo tratta come "Napoli".
     * Se entrambi i campi sono vuoti, la ricerca non è valida.
     * @param controller serve per ottenere dati e fare operazioni di logica più complesse.
     */
    public RicercaVoliPanel(Controller controller) {
        setSize(600, 400);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cercaButton.addActionListener(actionEvent -> {

            String[] colonne = {"Codice Volo", "Provenienza", "Destinazione", "Compagnia", "Data Partenza", "Orario Partenza", "Stato del volo"};
            DefaultTableModel model = new DefaultTableModel(colonne, 0);
            model.setColumnIdentifiers(colonne);

            String partenza = partenzaTextField.getText();
            String destinazione = destinazioneTextField.getText();
            try{
                if(partenza.isEmpty()){
                    listaVoliRicercati = controller.ricercaVoli("Napoli", destinazione);
                }
                else{
                    listaVoliRicercati = controller.ricercaVoli(partenza, "Napoli");
                }

                int counter = 0;

                for (Voli voli : listaVoliRicercati) {
                    model.addRow(new Object[] {
                            voli.getCodiceVolo(), voli.getProvenienza(), voli.getDestinazione(), voli.getCompagniaAerea(),
                            voli.getDataPartenza(), voli.getOrarioPartenza(), voli.getStato()
                    });
                    counter++;
                }

                if(counter != 0){
                    tabellaVoli.setModel(model);
                    scrollPanel1.setViewportView(tabellaVoli);
                }
                else{
                    JOptionPane.showMessageDialog(panel1, "Nessun volo trovato con questi parametri.",
                            "Errore Ricerca", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
            scrollPanel1.setVisible(true);
        });

        prenotaButton.setEnabled(false);
        tabellaVoli.getSelectionModel().addListSelectionListener(e -> {
           if(!e.getValueIsAdjusting()){
               int rigaSelezionata = tabellaVoli.getSelectedRow();
               prenotaButton.setEnabled(rigaSelezionata != -1);
           }
        });

        prenotaButton.addActionListener(actionEvent -> {
            int rigaSelezionata = tabellaVoli.getSelectedRow();
            if(rigaSelezionata != -1){
                Voli voloSelezionato = listaVoliRicercati.get(rigaSelezionata);
                if(voloSelezionato.getStatoEnum() == StatoVolo.programmato){
                    if(prenotazionePanel == null || !prenotazionePanel.isDisplayable()){
                        prenotazionePanel = new CredenzialiPrenotazionePanel(controller, voloSelezionato);
                        prenotazionePanel.setContentPane(prenotazionePanel.getPanel());
                        prenotazionePanel.setVisible(true);
                    }
                    else{
                        prenotazionePanel.toFront();
                        prenotazionePanel.requestFocusInWindow();
                    }
                }
                else{
                    JOptionPane.showMessageDialog(panel1, "Non è possibile prenotare il volo in stato: " +
                            voloSelezionato.getStato(), "Errore", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
    }
}
