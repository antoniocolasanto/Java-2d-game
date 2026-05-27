package com.game.core;

/**
 * Enumerazione che rappresenta i possibili stati del gioco.
 * Utilizzata dal motore principale per determinare quale schermata o 
 * logica di aggiornamento (update/draw) eseguire ad ogni frame.
 */
public enum GameState {
    /** Stato iniziale: mostra il menu principale del gioco. */
    MENU, 
    
    /** Schermata di inserimento del nickname del giocatore. */
    IDENTIFICATION, 
    
    /** Schermata di conferma dell'identità se il giocatore esiste già nel Database. */
    CONFIRM_PLAYER,   // Domanda "Sei tu? SI/NO"
    
    /** Stato di gioco attivo: il livello è in esecuzione. */
    PLAYING, 
    
    /** Gioco in pausa: l'azione è congelata. */
    PAUSE, 
    
    /** Schermata che mostra i record dei top 10 giocatori. */
    LEADERBOARD,
    
    /** Schermata di fine partita in caso di sconfitta. */
    DEATH
}