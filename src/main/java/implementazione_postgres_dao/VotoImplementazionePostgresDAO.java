package implementazione_postgres_dao;

import dao.VotoDAO;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione concreta dell'interfaccia VotoDAO per PostgreSQL.
 * Gestisce le operazioni WORM (Write-Once, Read-Many) per i voti assegnati ai team dai giudici durante gli Hackathon.
 */
public class VotoImplementazionePostgresDAO implements VotoDAO {

    private final Connection connessione;

    /**
     * Costruttore che inizializza la connessione al database.
     *
     * @param connessione la connessione al database PostgreSQL
     */
    public VotoImplementazionePostgresDAO(Connection connessione) {
        this.connessione = connessione;
    }

    @Override
    public void aggiungiVoto(Voto voto) {
        String sql = "INSERT INTO voto (emailGiudice, nomeTeam, titoloHackathon ,voto) VALUES (?, ?, ?,?)";

        try (PreparedStatement stmt = connessione.prepareStatement(sql)) {
            stmt.setString(1, voto.getGiudice().getEmail());
            stmt.setString(2, voto.getTeam().getNome());
            stmt.setString(3, voto.getTeam().getHackathon().getTitolo());
            stmt.setInt(4, voto.getValore());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Impossibile aggiungere il voto: " + voto, e);
        }
    }

    @Override
    public List<Voto> getTuttiVoti() {
        List<Voto> lista = new ArrayList<>();
        String sql = "SELECT * FROM voto";

        try (Statement stmt = connessione.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Giudice giudice = getGiudiceByEmail(rs.getString("emailgiudice"));
                Team team = getTeamByNomeAndHackathon(rs.getString("nometeam"), rs.getString("titolohackathon"));

                Voto v = new Voto(
                        giudice,
                        team,
                        rs.getInt("voto")
                );
                lista.add(v);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Impossibile prelevare dal DB i voti", e);
        }
        return lista;
    }

    private Giudice getGiudiceByEmail(String email) {
        String sql = "SELECT * FROM utentepiattaforma WHERE email = ?";
        try (PreparedStatement ps = connessione.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Giudice(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("pw")
                );
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Impossibile prelevare dal DB il giudice con email: " + email, e);
        }
        return null;
    }

    private Team getTeamByNomeAndHackathon(String nome, String titolo) {
        String sql = "SELECT * FROM team WHERE nome = ? AND titolohackathon = ?";
        try (PreparedStatement ps = connessione.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, titolo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Team(
                        rs.getString("nome"),
                        rs.getString("pw"),
                        getHackathonByTitolo(rs.getString("titolohackathon")),
                        getConcorrenteByEmail(rs.getString("creatore"))
                );
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Impossibile prelevare dal DB il team: " + nome + " dall'Hackathon: " + titolo, e);
        }
        return null;
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
}
