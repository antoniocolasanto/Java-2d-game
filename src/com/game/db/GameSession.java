package com.game.db;

/**
 * ENTITÀ DI SESSIONE DI GIOCO (Model)
 * - Questa classe rappresenta un contenitore di dati per una singola partita vinta del giocatore.
 * - È un oggetto di supporto (Data Transfer Object) che viene memorizzato 
 * all'interno della lista 'sessions' della classe PlayerProfile.
 * Memorizza in modo immutabile (poiché ha solo i getter e viene valorizzata nel costruttore) il numero di monete raccolte (coins), 
 * le vite con cui si è conclusa la partita (livesRemaining) e il tempo totale impiegato in secondi (timeSec). 
 * Questi dati corrispondono ai campi del documento che verrà salvato su MongoDB
 */
public class GameSession {
    
    private int coins;
    private int livesRemaining;
    private long timeSec;

    /**
     * Costruttore della sessione.
     * Viene richiamato nel momento esatto in cui il giocatore vince la partita,
     * passando i valori attuali registrati dal GamePanel.
     * @parm coins - il numero di monete raccolte in questa partita
     * @param livesRemaining - le vite rimanenti alla fine della partita
     * @param timesec - il tempo totale impiegato in secondi per completare la partita
     */
    public GameSession(int coins, int livesRemaining, long timeSec) {
        this.coins = coins;
        this.livesRemaining = livesRemaining;
        this.timeSec = timeSec;
    }

    /**
     * Getter per il numero di monete raccolte in queta sessione.
     * @return coins - il numero di monete raccolte
     */    
    public int getCoins() { 
        return coins; 
    }
    /**
     * Getter per le vite rimanenti alla fine della partita..
     * @return livesRemaining - le vite rimanenti.
     */    
    public int getLivesRemaining() { 
        return livesRemaining; 
    }
    /**
     * Getter per il tempo totale impiegato in secondi per completare la partita.
     * @return timeSec - il tempo totale impiegato in secondi
     */    
    public long getTimeSec() { 
        return timeSec; 
    }
}