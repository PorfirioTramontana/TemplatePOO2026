package implementazione_postgres_dao;

import dao.DocumentoDAO;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione concreta dell'interfaccia DocumentoDAO per PostgreSQL.
 * Gestisce le operazioni WORM (Write-Once, Read-Many) per i documenti nel database.
 */
public class DocumentoImplementazionePostgresDAO implements DocumentoDAO {

    private final Connection connessione;

    /**
     * Costruttore che inizializza la connessione al database.
     *
     * @param connessione la connessione al database PostgreSQL
     */
    public DocumentoImplementazionePostgresDAO(Connection connessione) {
        this.connessione = connessione;
    }

    @Override
    public void aggiungiDocumento(Documento documento) {
        String sql = "INSERT INTO documento (nomeFile,nomeTeam, titoloHackathon, dataaggiornamento) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connessione.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, documento.getNomeFile());
            stmt.setString(2, documento.getTeam().getNome());
            stmt.setString(3, documento.getTeam().getHackathon().getTitolo());
            stmt.setDate(4, java.sql.Date.valueOf(documento.getDataAggiornamento()));

            stmt.executeUpdate();

            // recupera id generato
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    documento.setIdDocumento(id);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Impossibile aggiungere il documento: " + documento.getNomeFile(), e);
        }
    }

    @Override
    public List<Documento> getTuttiDocumenti(){
        List<Documento> lista = new ArrayList<>();
        String sql = "SELECT * FROM documento";

        try (Statement stmt = connessione.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String nomeTeam = rs.getString("nometeam");
                String titoloHackathon = rs.getString("titoloHackathon");
                Team team = getTeamByNomeAndHackathon(nomeTeam, titoloHackathon);

                Documento d = new Documento(
                        // query che restituisce un organizzatore tramite email
                        rs.getInt("iddocumento"),
                        rs.getString("nomefile"),
                        team,
                        rs.getDate("dataaggiornamento").toLocalDate()
                );
                lista.add(d);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Impossibile prelevare dal DB i documenti", e);
        }
        return lista;
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

    @Override
    public void addCommento(Giudice giudice, Documento documento, String commento) {
        // scriviamo la query per inserire i dati
        String sql = "INSERT INTO esamina (emailgiudice, iddocumento, commento) VALUES (?,?,?)";

        // prepara la query evitando SQL injection
        try (PreparedStatement stmt = connessione.prepareStatement(sql)) {
            // inserisce i valori
            stmt.setString(1, giudice.getEmail());
            stmt.setInt(2, documento.getIdDocumento());
            stmt.setString(3, commento);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Impossibile aggiungere il commento: '" + commento + "' al documento: " + documento.getNomeFile(), e);
        }
    }

    @Override
    public List<String> getTuttiCommentiByDocumento(Documento documento) {
        List<String> commenti = new ArrayList<>();
        String sql = "SELECT commento FROM esamina WHERE iddocumento = ?";

        try (PreparedStatement ps = connessione.prepareStatement(sql)) {
            ps.setInt(1,documento.getIdDocumento());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    commenti.add(rs.getString("commento"));
                }
            }
        }
        catch (SQLException e) {
            throw new IllegalStateException("Impossibile prelevare dal DB i commenti al documento: " + documento.getNomeFile(), e);
        }
        return commenti;
    }
}