package com.game.core;

import com.game.entities.Player;
import com.game.inputs.KeyboardInputs;
import com.game.utils.Constants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    private Thread gameThread;
    private Player player; // <-- 1. Dichiariamo il giocatore

    public GamePanel() {
        setPanelSize();
        setBackground(Color.BLACK); 
        
        // <-- 2. Inizializziamo il giocatore (lo mettiamo alle coordinate x=100, y=100)
        player = new Player(100, 100, Constants.TILES_SIZE, Constants.TILES_SIZE);
        
        // <-- 3. Aggiungiamo i controlli
        addKeyListener(new KeyboardInputs(this));
        setFocusable(true); // FONDAMENTALE: Dice a Java che questa finestra può ricevere input
    }

    private void setPanelSize() {
        Dimension size = new Dimension(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        setPreferredSize(size);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        player.update(); // <-- 4. Aggiorniamo la logica del giocatore
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        player.render(g); // <-- 5. Disegniamo il giocatore a schermo
    }

    // Aggiungiamo questo metodo per permettere agli Input di parlare col Player
    public Player getPlayer() {
        return player;
    }

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
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }
}