package gui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import controller.Controller;
import model.StatoVolo;
import model.Voli;

/**
 * Classe gui che rappresenta la finestra mostrata all'accesso di un utente generico.
 * L'utente può: cercare voli in base a destinazione o partenza, visualizzare la lista completa dei voli (filtrati di
 * default per cancellato, in ritardo, decollato, atterrato e programmato, oppure con filtro personalizzato), effettuare
 * una prenotazione cliccando su un volo (in entrambe le finestre sia di ricerca che principale) e vedere e modificare le prenotazioni.
 */

@SuppressWarnings("unused")
public class UserPanel extends JPanel{
    private final Controller controller;
    private JPanel panel1;
    private JButton logoutButton;
    private JButton vediModificaPrenotazioniButton;
    private JButton ricercaVoliButton;
    private JPanel headerPanel;
    private JPanel tablePanel;
    private JPanel buttonPanel;
    private JScrollPane scrollTable;
    private JTable voliTable;
    private JComboBox<String> filtroComboBox;
    private JButton applicaFiltroButton;
    private JButton prenotaButton;
    private RicercaVoliPanel ricercaPanel = null;
    private PrenotazioniUtentePanel prenotazionePanel = null;
    private String filtroStato;

    private CredenzialiPrenotazionePanel credenzialiPanel = null;
    private List<Voli> voliVisualizzati = new ArrayList<>();

    public JPanel getPanel() {
        return panel1;
    }

    /**
     * Costruttore di creazione della finestra e inizializzazione dei listener per i pulsanti e gli elementi.
     *
     * @param controller per ottenere i dati e fare operazioni complesse
     * @param loginWindow per tenere traccia della finestra di login, serve per il tasto "Logout"
     */

    public UserPanel(Controller controller, Window loginWindow) {
        this.controller = controller;

        List<String> listaStati = controller.getStatoVolo();

        aggiornaTable(null);

        filtroComboBox.addItem("Nessuno filtro");
        for (String s : listaStati){
            filtroComboBox.addItem(s);
        }

        applicaFiltroButton.addActionListener(e -> {
            filtroStato = (String) filtroComboBox.getSelectedItem();
            if("Nessuno filtro".equals(filtroStato)){
                filtroStato = null;
            }
            aggiornaTable(filtroStato);
        });

        logoutButton.addActionListener(e ->{
            SwingUtilities.getWindowAncestor(panel1).dispose();
            if(loginWindow != null){
                loginWindow.setVisible(true);
            }
        });

        ricercaVoliButton.addActionListener(e ->{
            if(ricercaPanel == null || !ricercaPanel.isDisplayable()){
                ricercaPanel = new RicercaVoliPanel(controller);
                ricercaPanel.setContentPane(ricercaPanel.getPanel());
                ricercaPanel.setVisible(true);
            }
            else{
                ricercaPanel.toFront();
                ricercaPanel.requestFocusInWindow();
            }
        });

        vediModificaPrenotazioniButton.addActionListener(e ->{
            if(prenotazionePanel == null || !prenotazionePanel.isDisplayable()){
                prenotazionePanel = new PrenotazioniUtentePanel(controller);
                prenotazionePanel.setContentPane(prenotazionePanel.getPanel());
                prenotazionePanel.setVisible(true);
            }
            else{
                prenotazionePanel.toFront();
                prenotazionePanel.requestFocusInWindow();
            }
        });

        prenotaButton.setEnabled(false);
        voliTable.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()){
                int rigaSelezionata = voliTable.getSelectedRow();
                prenotaButton.setEnabled(rigaSelezionata != -1);
            }
        });

        prenotaButton.addActionListener(actionEvent -> {
            int rigaSelezionata = voliTable.getSelectedRow();
            if(rigaSelezionata != -1){
                Voli voloSelezionato = voliVisualizzati.get(rigaSelezionata);
                if(voloSelezionato.getStatoEnum() == StatoVolo.programmato){
                    if(credenzialiPanel == null || !credenzialiPanel.isDisplayable()){
                        credenzialiPanel = new CredenzialiPrenotazionePanel(controller, voloSelezionato);
                        credenzialiPanel.setContentPane(credenzialiPanel.getPanel());
                        credenzialiPanel.setVisible(true);
                    }
                    else{
                        credenzialiPanel.toFront();
                        credenzialiPanel.requestFocusInWindow();
                    }
                }
                else{
                    JOptionPane.showMessageDialog(panel1, "Non è possibile prenotare il volo in stato: " +
                            voloSelezionato.getStato(), "Errore", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
    }

    /**
     * Metodo per aggiornare in tempo reale la tabella dei voli in base al filtro passato.
     *
     * @param stato il filtro, se il valore è {@code null}, viene impostato il filtro default, se non lo è,
     *              viene impostato il filtro personalizzato escludendo i voli che non lo rispettano.
     */
    public void aggiornaTable(String stato){
        List<Voli> tuttiVoli;
        String[] colonne = {"Codice Volo", "Compagnia", "Partenza", "Destinazione", "Data", "Ora", "Stato"};
        DefaultTableModel model = new DefaultTableModel(colonne,0);
        model.setColumnIdentifiers(colonne);

        try{
            tuttiVoli = controller.visualizzaVoli();
        }
        catch (SQLException _){
            JOptionPane.showMessageDialog(panel1, "Errore nella connessione al database"
                    , "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(stato == null){

            List<String> ordineStatiDefault = List.of("cancellato", "inRitardo", "decollato", "atterrato", "programmato");

            tuttiVoli.sort((v1, v2) -> {
                String stato1 = v1.getStato();
                String stato2 = v2.getStato();
                return Integer.compare(
                        ordineStatiDefault.indexOf(stato1),
                        ordineStatiDefault.indexOf(stato2)
                );
            });
        }
        else{
            tuttiVoli = tuttiVoli.stream()
                    .filter(v -> v.getStato().equalsIgnoreCase(stato))
                    .collect(Collectors.toList());
        }

        voliVisualizzati = tuttiVoli;
        model.setRowCount(0);

        for(Voli v : tuttiVoli){
            model.addRow(new Object[]{
                    v.getCodiceVolo(), v.getCompagniaAerea(), v.getProvenienza(), v.getDestinazione(), v.getDataPartenza(),
                    v.getOrarioPartenza(), v.getStato()
            });
        }
        voliTable.setModel(model);
        scrollTable.setViewportView(voliTable);
    }
}
