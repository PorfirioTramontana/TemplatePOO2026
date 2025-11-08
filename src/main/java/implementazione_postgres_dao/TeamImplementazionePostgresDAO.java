package implementazione_postgres_dao;

import dao.TeamDAO;
import model.Concorrente;
import model.Hackathon;
import model.Organizzatore;
import model.Team;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione concreta dell'interfaccia TeamDAO per PostgreSQL.
 * Gestisce le operazioni WORM (Write-Once, Read-Many) per i team nel database.
 */
public class TeamImplementazionePostgresDAO implements TeamDAO {

    private final Connection connessione;

    /**
     * Costruttore che inizializza la connessione al database.
     *
     * @param connessione la connessione al database PostgreSQL
     */
    public TeamImplementazionePostgresDAO(Connection connessione) {
        this.connessione = connessione;
    }

    @Override
    public void aggiungiTeam(Team team) {
        String sql = "INSERT INTO team (nome, titoloHackathon, pw, creatore) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connessione.prepareStatement(sql)) {
            stmt.setString(1, team.getNome());
            stmt.setString(2, team.getHackathon().getTitolo());
            stmt.setString(3, team.getPw());
            stmt.setString(4, team.getCreatore().getEmail());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Impossibile aggiungere il team: " + team.getNome() + " per l''Hackathon: " + team.getHackathon().getTitolo(), e);
        }
    }

    @Override
    public List<Team> getTuttiTeam() {
        List<Team> lista = new ArrayList<>();
        String sql = "SELECT * FROM team";

        try (Statement stmt = connessione.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String nome = rs.getString("nome");
                String titoloHackathon = rs.getString("titoloHackathon");
                String pw = rs.getString("pw");
                String emailConcorrente = rs.getString("creatore");

                Hackathon hackathon = getHackathonByTitolo(titoloHackathon);

                Concorrente concorrente = getConcorrenteByEmail(emailConcorrente);

                Team team = new Team(nome, pw, hackathon, concorrente);

                lista.add(team);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Impossibile prelevare dal DB i team", e);
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
                // ricostruisci l'oggetto Hackathon con tutti i dati
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

    private Concorrente getConcorrenteByEmail(String email) {
        String sql = "SELECT * FROM utentepiattaforma WHERE email = ?";
        try (PreparedStatement ps = connessione.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Concorrente(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("pw")
                );
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Impossibile prelevare dal DB il concorrente con email: " + email, e);
        }
        return null;
    }

    public List<Concorrente> getConcorrentiOfTeam(Team team) {
        List<Concorrente> membri = new ArrayList<>();
        String sql = "SELECT emailconcorrente FROM concorrente_team WHERE nometeam = ? AND titolohackathon = ?";
        try (PreparedStatement ps = connessione.prepareStatement(sql)) {
            ps.setString(1, team.getNome());
            ps.setString(2, team.getHackathon().getTitolo());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Concorrente c = getConcorrenteByEmail(rs.getString("emailconcorrente"));
                membri.add(c);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Impossibile prelevare dal DB i concorrenti del team: " + team.getNome(), e);
        }
        return membri;
    }
}
