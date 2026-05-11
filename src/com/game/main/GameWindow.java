package com.game.main;

import com.game.core.GamePanel;
import javax.swing.JFrame;

public class GameWindow {
    private JFrame jframe;

    public GameWindow(GamePanel gamePanel) {
        jframe = new JFrame();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Chiude il programma quando chiudi la finestra
        jframe.setTitle("Il mio Platform 2D"); // Titolo della finestra
        jframe.add(gamePanel); // Aggiunge il nostro foglio da disegno alla finestra
        jframe.setResizable(false); // Impedisce di ridimensionare la finestra e rompere le proporzioni
        jframe.pack(); // Adatta la finestra alle dimensioni del GamePanel
        jframe.setLocationRelativeTo(null); // Fa apparire la finestra al centro dello schermo
        jframe.setVisible(true); // Rende la finestra visibile
    }
}