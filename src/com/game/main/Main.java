/**
 * ENTRY POINT DELL'APPLICAZIONE
 * - Contiene solo il metodo public static void main(String[] args).
 * - Il suo unico scopo è istanziare la GameWindow e avviare il gioco.
 * - Non deve contenere logica di gioco.
 */

package com.game.main;

import com.game.core.GamePanel;

public class Main {
    public static void main(String[] args) {
        GamePanel gamePanel = new GamePanel(); 

        // Istanziamo la GameWindow. Le passiamo il gamePanel in modo che la finestra sappia cosa deve mostrare.
        // Nota: non salviamo l'oggetto in una variabile (es. GameWindow gw = new...) 
        // perché ci basta che venga creata e mostrata a schermo.

        new GameWindow(gamePanel); 

        gamePanel.startGameThread(); // Controllare se in GamePanel è necessario chiamare questo metodo qui o se è già gestito internamente al costruttore.
    }
}