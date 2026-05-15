package com.game.db;

/**
 * ENTITÀ DI SESSIONE DI GIOCO (Model)
 * - Questa classe rappresenta una singola partita vinta da un giocatore.
 * - È un oggetto di supporto (Data Transfer Object) che viene memorizzato 
 * all'interno della lista 'sessions' della classe PlayerProfile.
 * - Quando salviamo i dati su MongoDB, le variabili di questa classe 
 * (monete, vite rimanenti, tempo impiegato) diventano i campi del documento.
 */
public class GameSession {
    
    private int coins;
    private int livesRemaining;
    private long timeSec;

    /**
     * Costruttore della sessione.
     * Viene richiamato nel momento esatto in cui il giocatore vince la partita,
     * passando i valori attuali registrati dal GamePanel.
     */
    public GameSession(int coins, int livesRemaining, long timeSec) {
        this.coins = coins;
        this.livesRemaining = livesRemaining;
        this.timeSec = timeSec;
    }

    // --- Metodi Getter per leggere i dati ---
    
    public int getCoins() { 
        return coins; 
    }
    
    public int getLivesRemaining() { 
        return livesRemaining; 
    }
    
    public long getTimeSec() { 
        return timeSec; 
    }
}