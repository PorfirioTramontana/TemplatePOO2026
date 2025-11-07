package gui;
import controller.Controller;
import model.Passeggero;
import model.Prenotazione;
import util.Sessione;

import javax.swing.*;
import java.sql.SQLException;

/**
 * Classe gui per la finestra di modifica di una prenotazione da parte dell'utente.
 * Permette di modificare le credenziali e il posto assegnato, di una prenotazione.
 */

@SuppressWarnings("unused")
public class ModificaPrenotazionePanel extends JFrame{
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JPanel confermaPanel;
    private JPanel centralPanel;
    private JTextField nomeField;
    private JTextField cognomeField;
    private JButton confermaCambiamentiButton;
    private JTextField postoField;

    public JPanel getPanel(){
        return mainPanel;
    }

    /**
     * Costruttore della finestra.
     * Permette la modifica di una prenotazione, attendendo i campi di input dall'utente.
     * L'utente deve compilare almeno un campo
     * Se l'utente compila uno fra nome o cognome, è obblicato a completare anche con cognome/nome
     * Se l'utente vuole, può cambiare anche solo il posto assegnato, lasciando vuoti i campi delle credenziali
     *
     * @param controller per logica e ottenimento dati
     * @param prenotazione riferimento alla prenotazione da modificare
     * @param prenotazioniUtentePanel riferimento alla pagina precedente, che ha chiamato questa attuale
     */
    public ModificaPrenotazionePanel(Controller controller, Prenotazione prenotazione, PrenotazioniUtentePanel prenotazioniUtentePanel){

        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        titleLabel.setText("<html>Modifica della prenotazione.<br>Codice del volo: " + prenotazione.getCodiceVolo() + "</html>");

        confermaCambiamentiButton.addActionListener(e ->{
            String nuovoNome = nomeField.getText();
            String nuovoCognome = cognomeField.getText();
            String nuovoPosto = postoField.getText();

           if(nuovoNome.isEmpty() && nuovoCognome.isEmpty() && nuovoPosto.isEmpty()){
               JOptionPane.showMessageDialog(mainPanel, "Compila almeno un campo.", "Errore", JOptionPane.ERROR_MESSAGE);
           }
           else if((!nuovoNome.isEmpty() && nuovoCognome.isEmpty()) || (nuovoNome.isEmpty() && !nuovoCognome.isEmpty())){
               JOptionPane.showMessageDialog(mainPanel, "I campi \"Nome\" e \"Cognome\" devono essere entrambi compilati.", "Errore", JOptionPane.ERROR_MESSAGE);
           }
           else{
               try{
                   if(controller.modificaPrenotazione(new Passeggero(nuovoNome, nuovoCognome), prenotazione, nuovoPosto)){
                       JOptionPane.showMessageDialog(mainPanel, "Prenotazione modificata con successo!", "Info", JOptionPane.INFORMATION_MESSAGE);
                       prenotazioniUtentePanel.aggiornaTable();
                       dispose();
                   }
               }
               catch(SQLException ex){
                   JOptionPane.showMessageDialog(mainPanel, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
               }
           }
        });



    }

}
