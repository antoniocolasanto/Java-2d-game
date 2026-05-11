package com.game.main;

import com.game.core.GamePanel;

public class Main {
    public static void main(String[] args) {
        // Crea le varie componenti
        GamePanel gamePanel = new GamePanel();
        new GameWindow(gamePanel); // Creiamo la finestra e le passiamo il pannello
        
        // Avvia il motore del gioco
        gamePanel.startGameThread();
    }
}