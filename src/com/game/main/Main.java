package com.game.main;
import com.game.core.GamePanel;
/**
 * ENTRY POINT DELL'APPLICAZIONE
 * - Contiene solo il metodo public static void main(String[] args).
 * - Il suo unico scopo è istanziare la GameWindow e avviare il gioco.
 * - Non deve contenere logica di gioco.
 */
public class Main {
    public static void main(String[] args) {
        
        // --- 1. ATTIVA L'ACCELERAZIONE HARDWARE (OpenGL) E IL V-SYNC ---
        // questa  riga dice a Java di usare la Scheda Video invece del Processore
        System.setProperty("sun.java2d.opengl", "true");
        

        GamePanel gamePanel = new GamePanel(); 

        // Istanziamo la GameWindow. Le passiamo il gamePanel in modo che la finestra sappia cosa deve mostrare.
        new GameWindow(gamePanel); 

        // Avviamo il motore del gioco
        gamePanel.startGameThread(); 
    }
}