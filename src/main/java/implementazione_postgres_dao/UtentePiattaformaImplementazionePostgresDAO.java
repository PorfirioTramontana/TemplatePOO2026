package implementazione_postgres_dao;

import dao.UtentePiattaformaDAO;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione concreta dell'interfaccia UtentePiattaformaDAO per PostgreSQL.
 * Gestisce le operazioni WORM (Write-Once, Read-Many) per gli utenti della piattaforma
 */
public class  UtentePiattaformaImplementazionePostgresDAO implements UtentePiattaformaDAO {

    private final Connection connessione;

    /**
     * Costruttore che inizializza la connessione al database.
     *
     * @param connessione la connessione al database PostgreSQL
     */
    public UtentePiattaformaImplementazionePostgresDAO(Connection connessione) {
        this.connessione = connessione;
    }

    // metodi dell'utente
    @Override
    public void aggiungiUtente(UtentePiattaforma utente) {
        // scriviamo la query per inserire i dati
        String sql = "INSERT INTO utentePiattaforma (nome, cognome, email, pw, ruolo) VALUES (?, ?, ?, ?, ?)";

        // prepara la query evitando SQL injection
        try (PreparedStatement stmt = connessione.prepareStatement(sql)) {
            // inserisce i valori dell'utente
            stmt.setString(1, utente.getNome());
            stmt.setString(2, utente.getCognome());
            stmt.setString(3, utente.getEmail());
            stmt.setString(4, utente.getPw());
            stmt.setString(5, utente.getRuolo());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Impossibile aggiungere l'utente " + utente.getCognome() + " " + utente.getNome(), e);
        }
    }

    @Override
    public List<UtentePiattaforma> getTuttiUtenti() {
        List<UtentePiattaforma> lista = new ArrayList<>();
        String sql = "SELECT * FROM utentePiattaforma";

        try (Statement stmt = connessione.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                UtentePiattaforma u = new UtentePiattaforma(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("pw"),
                        rs.getString("ruolo")
                );
                lista.add(u);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Impossibile prelevare dal DB gli utenti", e);
        }
        return lista;
    }

    // metodi dell'organizzatore
    @Override
    public void convocaGiudice(Organizzatore organizzatore, Giudice giudice, Hackathon hackathon) {
        // scriviamo la query per inserire i dati
        String sql = "INSERT INTO convocazione (emailorganizzatore, emailgiudice, titolohackathon) VALUES (?, ?, ?)";

        // prepara la query evitando SQL injection
        try (PreparedStatement stmt = connessione.prepareStatement(sql)) {
            // inserisce i valori dell'utente
            stmt.setString(1, organizzatore.getEmail());
            stmt.setString(2, giudice.getEmail());
            stmt.setString(3, hackathon.getTitolo());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Impossibile aggiungere la convocazione effettuata dall'organizzatore: " + organizzatore.getCognome() + " " + organizzatore.getNome() + "verso il giudice: " + giudice.getCognome() + " " + giudice.getNome(), e);
        }
    }

    @Override
    public List<Hackathon> getHackathonAssegnatiToGiudice(Giudice giudice) {
        List<Hackathon> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT titolohackathon FROM convocazione WHERE emailgiudice = ?";

        try (PreparedStatement stmt = connessione.prepareStatement(sql)) {
            stmt.setString(1, giudice.getEmail());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Hackathon h = getHackathonByTitolo(rs.getString("titolohackathon"));
                    lista.add(h);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Impossibile prelevare dal DB la lista di Hackathon assegnati al giudice: " +
                    giudice.getCognome() + " " + giudice.getNome(), e);
        }
        return lista;
    }

    private Hackathon getHackathonByTitolo(String titolo) {
        Hackathon hackathon = null;
        String sql = "SELECT * FROM hackathon WHERE titolo = ?";

        try (PreparedStatement ps = connessione.prepareStatement(sql)) {
            ps.setString(1, titolo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Ricostruisci l'oggetto Hackathon con tutti i dati
                String emailOrganizzatore = rs.getString("creatore");
                Organizzatore organizzatore = getOrganizzatoreByEmail(emailOrganizzatore);
                hackathon = new Hackathon(
                        organizzatore,
                        rs.getString("titolo"),
                        rs.getInt("nummaxiscritti"),
                        rs.getInt("dimmaxteam"),
                        rs.getDate("inizioiscrizioni").toLocalDate(),
                        rs.getDate("fineiscrizioni").toLocalDate(),
                        rs.getDate("datainizio").toLocalDate(),
                        rs.getDate("datafine").toLocalDate(),
                        rs.getString("descrizioneproblema"),
                        rs.getString("classifica"),
                        rs.getString("indirizzosede"),
                        rs.getInt("numiscritti"),
                        rs.getInt("numvotiassegnati")
                );
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Impossibile prelevare dal DB l'Hackathon: " + titolo, e);
        }
        return hackathon;
    }

    private Organizzatore getOrganizzatoreByEmail(String email) {
        String sql = "SELECT * FROM utentepiattaforma WHERE email = ?";
        try (PreparedStatement ps = connessione.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Organizzatore(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("pw")
                );
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Impossibile prelevare dal DB l'organizzatore con email: " + email, e);
        }
        return null;
    }

    @Override
    public List<Organizzatore> getOrganizzatoriInvitantiToGiudice(Giudice giudice) {
        List<Organizzatore> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT emailorganizzatore FROM convocazione WHERE emailgiudice = ?";

        try (PreparedStatement stmt = connessione.prepareStatement(sql)) {
            stmt.setString(1, giudice.getEmail()); // Presupponendo che esista il metodo getEmail()
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Organizzatore o = getOrganizzatoreByEmail(rs.getString("emailorganizzatore"));
                    lista.add(o);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Impossibile prelevare dal DB gli organizzatori invitanti il giudice: " +
                    giudice.getCognome() + " " + giudice.getNome(), e);
        }
        return lista;
    }

    // metodi del concorrente
    @Override
    public void partecipaTeam(Concorrente concorrente, String nomeTeam, String titoloHackathon) {
        // scriviamo la query per inserire i dati
        String sql = "INSERT INTO concorrente_team (emailconcorrente, nometeam, titolohackathon) VALUES (?, ?, ?)";

        // prepara la query evitando SQL injection
        try (PreparedStatement stmt = connessione.prepareStatement(sql)) {
            // inserisce i valori dell'utente
            stmt.setString(1, concorrente.getEmail());
            stmt.setString(2, nomeTeam);
            stmt.setString(3, titoloHackathon);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Impossibile aggiungere la partecipazione del concorrente: " + concorrente.getCognome() + " " + concorrente.getCognome() + " al team: " + nomeTeam + " dell'Hackathon: " + titoloHackathon, e);
        }
    }
}
