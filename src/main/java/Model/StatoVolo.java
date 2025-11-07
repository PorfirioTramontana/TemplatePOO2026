package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Enum dello stato di un volo.
 */

public enum StatoVolo {
    programmato,
    inRitardo,
    atterrato,
    decollato,
    cancellato;

    public String getStato() {
        return name();
    }

    /**
     * Metodo statico per ottenere lo stato.
     * @param stato in forma StatoVolo
     * @return lo stato in versione String.
     */
    public static String getStatoStatic(StatoVolo stato) {
        if(stato == null){
            return null;
        }
        return stato.name();
    }

    /**
     * Metodo per ottenere lo stato da una stringa.
     * @param stato in versione String
     * @return lo stato in versione StatoVolo (Enum)
     */
    public static StatoVolo fromString(String stato) {
        if(stato == null){
            return null;
        }
        try{
            return StatoVolo.valueOf(stato);
        }
        catch(IllegalArgumentException _){
            return null;
        }
    }

    /**
     * Metodo per ottenere una lista degli stati escludendone quello indicato come parametro.
     * Serve ad ottenere gli stati che un volo può assumere, escludendo l'attuale.
     *
     * @param stato attuale del volo
     * @return la lista degli stati escluso quello già utilizzato.
     */
    public static List<String> getStatoVolo(StatoVolo stato) {
        List<String> statoVolo = new ArrayList<>();
        for (StatoVolo v : values()) {
            if(!v.equals(stato)){
                statoVolo.add(v.getStato());
            }
        }
        return statoVolo;
    }

    /**
     * Metodo per ottenere tutti gli stati in una lista.
     * @return una lista di tutti gli stati.
     */
    public static List<String> getStatoVolo(){
        List<String> statoVolo = new ArrayList<>();
        for (StatoVolo v : values()) {
            statoVolo.add(v.getStato());
        }
        return statoVolo;
    }
}
