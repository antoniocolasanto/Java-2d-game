package com.game.db;

import java.util.ArrayList;
import java.util.List;

/**
 * MODELLO DEI DATI (Data Transfer Object - DTO)
 * - Mappa i dati del giocatore per prepararli all'invio al database o per leggerli (simile a un JSON).
 * - Contiene il nome e la lista delle sue partite precedenti.
 * - Contiene metodi di business logic come getPerformanceRatio() per calcolare statistiche in tempo reale.
 */
public class PlayerProfile {
    
    private String nickname;
    private List<GameSession> sessions;

    public PlayerProfile(String nickname) {
        this.nickname = nickname;
        // ERRORE DI SINTASSI CORRETTO QUI:
        this.sessions = new ArrayList<>();
    }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    
    public List<GameSession> getSessions() { return sessions; }
    public void setSessions(List<GameSession> sessions) { this.sessions = sessions; }

    /**
     * Calcola il rapporto di prestazione tra monete raccolte e tempo impiegato.
     */
    public double getPerformanceRatio() {
        if (sessions.isEmpty()) return 0.0;
        
        int totalCoins = 0;
        long totalTimeSec = 0;
        
        for (GameSession s : sessions) {
            totalCoins += s.getCoins();
            totalTimeSec += s.getTimeSec();
        }
        
        // Evitiamo la divisione per zero nel caso in cui il tempo sia 0
        if (totalTimeSec == 0) return (double) totalCoins;
        
        return (double) totalCoins / totalTimeSec;
    }
}

/**
 * Sotto-classe di supporto che rappresenta la singola partita giocata.
 */
class GameSession {
    private int coins;
    private int livesRemaining;
    private long timeSec;

    public GameSession(int coins, int livesRemaining, long timeSec) {
        this.coins = coins;
        this.livesRemaining = livesRemaining;
        this.timeSec = timeSec;
    }

    public int getCoins() { return coins; }
    public int getLivesRemaining() { return livesRemaining; }
    public long getTimeSec() { return timeSec; }
}