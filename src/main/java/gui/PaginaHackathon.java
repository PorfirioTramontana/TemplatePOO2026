package gui;

import  controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

/**
 * Schermata di dettaglio di un Hackathon con tutte le informazioni rilevanti.
 * Offre funzionalità specifiche in base al ruolo dell'utente e allo stato dell'evento.
 */
public class PaginaHackathon {
    private static final JFrame frame = new JFrame("Hackathon");
    private JPanel panel1;
    private JLabel labelNomeHackathon;
    private JLabel labelStatoGara;
    private JLabel labelInizioIscrizioni;
    private JLabel labelFineIscrizioni;
    private JLabel labelDataInizio;
    private JLabel labelDataFine;
    private JLabel labelNumIscritti;
    private JLabel labelNumMaxIscritti;
    private JLabel labelDimMaxTeam;
    private JLabel labelIndirizzoSede;
    private JLabel labelDescrizioneProblema;
    private JTextArea textAreaDescrizione;
    private JPanel panelUtente;
    private JButton joinWithATeamButton;
    private JButton joinWithNewTeamButton;
    private JPanel panelGiudice;
    private JTextArea textAreaInserimentoDescrizione;
    private JButton confirmButton;
    private JButton judgeTeamButton;
    private JButton addJudgeButton;
    private JButton viewRankButton;

    /**
     * Costruttore che crea un'istanza di una nuova pagina Pagina Hackathon.
     *
     * @param frameChiamante il frame precedente (ovvero quello chiamante)
     * @param emailUtente l'email dell'utente che vuole visualizzare la pagina dell'Hackathon
     * @param titoloHackathon il titolo dell'Hackathon da visualizzare
     * @param controller il controller utilizzato per interagire con il model
     */
    public PaginaHackathon(JFrame frameChiamante, String emailUtente, String titoloHackathon, Controller controller) {
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

        labelNomeHackathon.setText("Titolo: " + titoloHackathon);
        labelStatoGara.setText("Stato gara: " + controller.getHackathonByTitolo(titoloHackathon).statoGara());
        labelInizioIscrizioni.setText("Inizio iscrizioni: " + controller.getHackathonByTitolo(titoloHackathon).getInizioIscrizioni());
        labelFineIscrizioni.setText("Fine iscrizioni: " + controller.getHackathonByTitolo(titoloHackathon).getFineIscrizioni());
        labelDataInizio.setText("Inizio gara: " + controller.getHackathonByTitolo(titoloHackathon).getDataInizio());
        labelDataFine.setText("Fine gara: " + controller.getHackathonByTitolo(titoloHackathon).getDataFine());
        labelNumIscritti.setText("Numero di iscritti: " + controller.getHackathonByTitolo(titoloHackathon).getNumIscritti());
        labelNumMaxIscritti.setText("Numero massimo di partecipanti: " + controller.getHackathonByTitolo(titoloHackathon).getNumMaxIscritti());
        labelDimMaxTeam.setText("Numero massimo di partecipanti per singolo team: " + controller.getHackathonByTitolo(titoloHackathon).getDimMaxTeam());
        labelIndirizzoSede.setText("Indirizzo sede gara: " + controller.getHackathonByTitolo(titoloHackathon).getIndirizzoSede(controller.getUtentePiattaformaByEmail(emailUtente)));
        labelDescrizioneProblema.setText("Descrizione problema: ");
        textAreaDescrizione.setText(controller.getHackathonByTitolo(titoloHackathon).getDescrizioneProblema());
        textAreaDescrizione.setEditable(false);

        verificaDescrizioneProblema(controller, emailUtente, titoloHackathon);

        joinWithATeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PartecipaTeam(frame, emailUtente, controller);
                frame.setVisible(false);
            }
        });

        joinWithNewTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreaTeam(frame, emailUtente, controller);
                frame.setVisible(false);
            }
        });

        verificaIscrizioneHackathon(controller, emailUtente, titoloHackathon);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.metodoSetDescrizioneProblemaHackathon(frame, textAreaInserimentoDescrizione.getText(), titoloHackathon, emailUtente);
                textAreaInserimentoDescrizione.setText("");
                // ritorniamo alla pagina di seleziona documento (pagina precedente a questa)
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });

        judgeTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GiudicaTeam(frame, emailUtente, controller);
                frame.setVisible(false);
            }
        });

        verificaGiudice(controller, emailUtente, titoloHackathon);

        addJudgeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ConvocaGiudice(frame, emailUtente, controller);
                frame.setVisible(false);
            }
        });

        verificaConvocazioneGiudice(controller, emailUtente, titoloHackathon);

        viewRankButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.metodoVisualizzaClassifica(frame, titoloHackathon, emailUtente);
            }
        });

        verificaVisualizzaClassifica(controller, titoloHackathon);
    }

    private void verificaDescrizioneProblema(Controller controller, String emailUtente, String titoloHackathon) {
        // verifica possibilità di visualizzare la descrizione del problema (visibile in qualsiasi momento ma solo dai giudici e organizzatore)
        // oppure (non visibile fino all'inizio della gara, ossia il periodo in cui viene creato l'Hackathon e quando viene inserita dai giudici)
        if (controller.isGiudice(emailUtente) || controller.isOrganizzatore(emailUtente)) {
            if (textAreaDescrizione.getText().isEmpty()) {
                textAreaDescrizione.setText("*ASSENTE*\nInserire la descrizione prima dell'inizio gara");
            }
        } else if (LocalDate.now().isBefore(controller.getHackathonByTitolo(titoloHackathon).getDataInizio())) {
            textAreaDescrizione.setText("Non ancora disponibile");
        }
    }

    private void verificaIscrizioneHackathon(Controller controller, String emailUtente,  String titoloHackathon) {
        // verifica possibilità di iscrizione all'Hackathon (durante il periodo di iscrizioni e solo per i concorrenti)
        if (!(controller.getHackathonByTitolo(titoloHackathon).verificaStatoGara("Iscrizioni aperte") && controller.isConcorrente(emailUtente))) {
            panelUtente.setVisible(false);
        }
    }

    private void verificaGiudice(Controller controller, String emailUtente, String titoloHackathon) {
        // verifica possibilità di fare azioni solo per i giudici assegnati (inserire descrizione problema Hackathon, giudicare team)
        if (controller.isGiudice(emailUtente)) {
            if (controller.getListaTitoliHackathonAssegnatiToGiudice(emailUtente).contains(titoloHackathon)) {
                // inserire/reinserire la descrizione del problema (dopo la fine delle iscrizioni, prima dell'inizio della gara)
                if (!controller.getHackathonByTitolo(titoloHackathon).verificaStatoGara("Iscrizioni terminate. In attesa dell'inizio della gara")) {
                    panelGiudice.setVisible(false);
                }
                // giudicare team (aggiungere commenti ai documenti, durante la fase di gara oppure assegnare i voti ai team, subito dopo la fine della gara)
                if (!(controller.getHackathonByTitolo(titoloHackathon).verificaStatoGara("In corso") || controller.getHackathonByTitolo(titoloHackathon).verificaStatoGara("Terminata"))) {
                    judgeTeamButton.setVisible(false);
                }
            } else {
                panelGiudice.setVisible(false);
                judgeTeamButton.setVisible(false);
            }
        } else {
            panelGiudice.setVisible(false);
            judgeTeamButton.setVisible(false);
        }
    }

    private void verificaConvocazioneGiudice(Controller controller, String emailUtente, String titoloHackathon) {
        // verifica possibilità di convocare giudice (prima dell'inizio delle iscrizioni e solo per l'organizzatore)
        if (!(controller.getHackathonByTitolo(titoloHackathon).statoGara().equals("Iscrizioni non ancora aperte") && emailUtente.toLowerCase().contains("@organizzatore.com"))) {
            addJudgeButton.setVisible(false);
        }
    }

    private void verificaVisualizzaClassifica(Controller controller, String titoloHackathon) {
        // verifica possibilità di visualizzazione classifica (solo in fase di gara conclusa e valutata)
        if (!controller.getHackathonByTitolo(titoloHackathon).verificaStatoGara("Valutata")) {
            viewRankButton.setVisible(false);
        }
    }
}
