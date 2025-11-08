package model;

/**
 * Classe che rappresenta un voto assegnato da un giudice a un team.
 * Gestisce il valore del voto e le relazioni con giudice e team.
 */
public class Voto {
    // attributi
    private int valore;

    // rappresentazione relazioni
    private final Team team; // team che riceve il voto
    private final Giudice giudice; // giudice che assegna il voto

    // metodi
    /**
   * Costruttore che crea un'istanza di un nuovo Voto.
   *
   * @param giudice il giudice che assegna il voto
   * @param team il team che riceve il voto
   * @param valore il valore del voto
   */
    public Voto(Giudice giudice, Team team, int valore) {
        this.giudice = giudice;
        this.team = team;
        setValore(valore); // utilizza il set per effettuare la verifica del valore
    }

    /**
   * Restituisce il valore del voto.
   *
   * @return valore del voto
   */
    public int getValore() { return valore; }

    /**
   * Imposta il valore del voto con verifica.
   *
   * @param valore nuovo valore (0-10)
   * @throws IllegalArgumentException se il valore non è valido
   */
    public void setValore(int valore) {
        // verifica che il nuovo voto abbia un valore valido
        if (valore < 0 || valore > 10) {
            throw new IllegalArgumentException("Voto non valido");
        }
        this.valore = valore;
    }

    /**
   * Restituisce il team che ha ricevuto il voto.
   *
   * @return team votato
   */
    public Team getTeam() { return team; }

    /**
   * Restituisce il giudice che ha assegnato il voto.
   *
   * @return giudice votante
   */
    public Giudice getGiudice() { return giudice; }

    /**
   * Override del metodo toString per rappresentare il voto in lettere.
   *
   * @return rappresentazione in lettere del voto
   */
    @Override
    public String toString() {
        return (switch (getValore()) {
                    case 0 -> "zero";
                    case 1 -> "uno";
                    case 2 -> "due";
                    case 3 -> "tre";
                    case 4 -> "quattro";
                    case 5 -> "cinque";
                    case 6 -> "sei";
                    case 7 -> "sette";
                    case 8 -> "otto";
                    case 9 -> "nove";
                    case 10 -> "dieci";
                    default -> "non valido";
                }
        );
    }
}
