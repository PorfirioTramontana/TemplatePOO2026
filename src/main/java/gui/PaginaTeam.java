package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Schermata di dettaglio di un team partecipante ad un Hackathon.
 * Mostra informazioni sui membri, gara di appartenenza e punteggio (se disponibile).
 * Permette anche di accedere alle pagine di visualizzazione e aggiunta documenti
 */
public class PaginaTeam {
    private static final JFrame frame = new JFrame("Team");
    private JPanel panel1;
    private JLabel labelNomeTeam;
    private JLabel labelPunteggioTeam;
    private JLabel labelCapogruppoTeam;
    private JLabel labelMembri;
    private JLabel labelHackathonAppartenenza;
    private JLabel labelStatoGara;
    private JButton addDocButton;
    private JButton viewDocButton;
    private JPanel panelGiudice;
    private JSpinner spinnerVoto;
    private JButton confirmButton;

    /**
     * Costruttore che crea un'istanza di una nuova pagina Pagina team.
     *
     * @param frameChiamante il frame precedente (ovvero quello chiamante)
     * @param nomeTeam il nome del team da visualizzare
     * @param titoloHackathon il titolo Hackathon a cui appartiene tale team
     * @param emailUtente l'email dell'utente che vuole visualizzare la pagina del team
     * @param controller il controller utilizzato per interagire con il model
     */
    public PaginaTeam(JFrame frameChiamante, String nomeTeam, String titoloHackathon, String emailUtente, Controller controller) {
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

        labelNomeTeam.setText("Nome: " + nomeTeam);
        labelPunteggioTeam.setText("Punteggio: " + controller.getTeamByNomeAndHackathon(nomeTeam, titoloHackathon).getPunteggio());

        verificaVisualizzaPunteggio(controller, emailUtente, titoloHackathon, nomeTeam);

        labelCapogruppoTeam.setText("Capogruppo: " + controller.getTeamByNomeAndHackathon(nomeTeam, titoloHackathon).getCreatore().getNome() + " " + controller.getTeamByNomeAndHackathon(nomeTeam, titoloHackathon).getCreatore().getCognome());
        labelMembri.setText("Membri: " + controller.getListaNominativiMembriByTeam(nomeTeam, titoloHackathon));
        labelHackathonAppartenenza.setText("Hackathon appartenenza: " + controller.getTeamByNomeAndHackathon(nomeTeam, titoloHackathon).getHackathon().getTitolo());
        boolean statoGara = controller.getHackathonByTitolo(titoloHackathon).isTerminato();
        labelStatoGara.setText("Stato gara: " + (statoGara ? "terminata" : "in corso"));

        viewDocButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VisualizzaDocumenti(frame, nomeTeam, titoloHackathon, emailUtente, controller);
                frame.setVisible(false);
            }
        });

        addDocButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AggiungiDocumento(frame, nomeTeam, titoloHackathon, controller);
                frame.setVisible(false);
            }
        });

        verificaAggiungiDocumento(controller, emailUtente, titoloHackathon, nomeTeam);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.metodoAssegnaVoto(frame, emailUtente, (int) spinnerVoto.getValue(), nomeTeam, titoloHackathon);
                spinnerVoto.setValue(0);
                // ritorniamo alla pagina di seleziona documento (pagina precedente a questa)
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });

        verificaAssegnaVoto(controller, emailUtente, titoloHackathon, nomeTeam);
    }

    private void verificaVisualizzaPunteggio(Controller controller, String emailUtente, String titoloHackathon, String nomeTeam) {
        // verifica possibilità di visualizzare il punteggio ottenuto da un team (quando la gara è stata valutata e solo per i concorrenti nel team)
        // oppure da giudici e organizzatore appartenenti all'Hackathon, in qualsiasi momento della gara
        if (controller.isPartecipanteInTeam(nomeTeam, titoloHackathon, emailUtente)) {
            if (!controller.getHackathonByTitolo(titoloHackathon).verificaStatoGara("Valutata")) {
                labelPunteggioTeam.setVisible(false);
            }
        } else if (!(controller.isGiudice(emailUtente) || controller.isOrganizzatore(emailUtente))) {
            labelPunteggioTeam.setVisible(false);
        }
    }

    private void verificaAggiungiDocumento(Controller controller, String emailUtente, String titoloHackathon, String nomeTeam) {
        // verifica possibilità di aggiungere un documento (mentre la gara è in corso e solo per i concorrenti nel team)
        if (!(controller.getHackathonByTitolo(titoloHackathon).verificaStatoGara("In corso") && controller.isPartecipanteInTeam(nomeTeam, titoloHackathon, emailUtente))) {
            addDocButton.setVisible(false);
        }
    }

    private void verificaAssegnaVoto(Controller controller, String emailUtente, String titoloHackathon, String nomeTeam) {
        // verifica possibilità di assegnare un voto (solo al termine gara, solo tramite un giudice assegnato al team che non ha ancora giudicato tale team)
        if (!controller.getHackathonByTitolo(titoloHackathon).verificaStatoGara("Terminata") || !controller.isTeamGiudicabileByGiudice(nomeTeam, titoloHackathon, emailUtente)) {
            panelGiudice.setVisible(false);
        }
    }
}
