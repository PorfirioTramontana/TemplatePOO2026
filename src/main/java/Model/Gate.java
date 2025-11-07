package model;

/**
 * Classe che rappresenta un gate dell'aeroporto.
 * Possiede il suo identificativo e il codice del volo assegnato.
 */

public class Gate {
    private final int idGate;
    private String codiceVoloAssegnato;

    public int getidGate() {
        return idGate;
    }

    /**
     * Costruttore per la creazione del Gate.
     * @param idGate indica l'identificativo
     * @param codiceVoloAssegnato indica il volo a cui il gate è stato assegnato
     */
    public Gate(int idGate, String codiceVoloAssegnato) {
        this.idGate = idGate;
        this.codiceVoloAssegnato = codiceVoloAssegnato;
    }

    public String toString() {
        return Integer.toString(idGate);
    }


}
