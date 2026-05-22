package com.game.main;

import com.game.core.GamePanel;

public class Main {
    public static void main(String[] args) {
        
        // --- 1. ATTIVA L'ACCELERAZIONE HARDWARE (OpenGL) E IL V-SYNC ---
        // Queste due righe dicono a Java di usare la Scheda Video invece del Processore
        System.setProperty("sun.java2d.opengl", "true");
        

        GamePanel gamePanel = new GamePanel(); 

        // Istanziamo la GameWindow. Le passiamo il gamePanel in modo che la finestra sappia cosa deve mostrare.
        new GameWindow(gamePanel); 

        // Avviamo il motore del gioco
        gamePanel.startGameThread(); 
    }
}