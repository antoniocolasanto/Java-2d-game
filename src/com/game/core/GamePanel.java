package com.game.core;

import com.game.utils.Constants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    private Thread gameThread;

    public GamePanel() {
        setPanelSize();
        setBackground(Color.BLACK); // Impostiamo uno sfondo nero per ora
    }

    private void setPanelSize() {
        Dimension size = new Dimension(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        setPreferredSize(size);
    }

    // Avvia il ciclo infinito del gioco
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Qui aggiorneremo le posizioni di giocatori e nemici
    public void update() {
        // Al momento vuoto, lo riempiremo nella prossima fase
    }

    // Metodo speciale di Java Swing per disegnare sullo schermo
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Disegniamo un testo di prova
        g.setColor(Color.WHITE);
        g.drawString("Il Game Loop funziona! (Guarda la console per gli FPS)", 10, 20);
    }

    // Il cuore del gioco (Il Game Loop vero e proprio)
    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / Constants.FPS_SET;
        double timePerUpdate = 1000000000.0 / Constants.UPS_SET;
        
        long previousTime = System.nanoTime();
        
        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();
        
        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update(); // 1. Aggiorna la logica
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                repaint(); // 2. Disegna la nuova scena (chiama paintComponent)
                frames++;
                deltaF--;
            }

            // Stampa le prestazioni nella console ogni secondo
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }
}