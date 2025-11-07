package model;

/**
 * Classe Enum per la rappresentazione dello stato di una prenotazione.
 *
 */

public enum StatoPrenotazione {
    confermata,
    inAttesa,
    cancellata;

    @Override
    public String toString() {
        return name();
    }
}
