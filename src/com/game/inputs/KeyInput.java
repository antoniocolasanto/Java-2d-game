package com.game.inputs;

import com.game.core.GamePanel;
import com.game.core.GameState;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener { 
   
    private GamePanel gamePanel;

    public KeyInput(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // --- GESTIONE MENU PRINCIPALE ---
        if (GamePanel.state == GameState.MENU) {
            if (key == KeyEvent.VK_ENTER) GamePanel.state = GameState.PLAYING;
            if (key == KeyEvent.VK_C) GamePanel.state = GameState.LEADERBOARD;
            if (key == KeyEvent.VK_ESCAPE) System.exit(0);
            return; 
        }

        // --- GESTIONE PAUSA ---
        if (key == KeyEvent.VK_ESCAPE) {
            if (GamePanel.state == GameState.PLAYING) GamePanel.state = GameState.PAUSE;
            else if (GamePanel.state == GameState.PAUSE) GamePanel.state = GameState.PLAYING; // ESC per riprendere
        }

        if (GamePanel.state == GameState.PAUSE) {
            if (key == KeyEvent.VK_ENTER) GamePanel.state = GameState.PLAYING; // INVIO per continuare (RISOLTO)
            if (key == KeyEvent.VK_S) GamePanel.state = GameState.MENU; // S per tornare al menu
            return;
        }

        // --- GESTIONE CLASSIFICA ---
        if (GamePanel.state == GameState.LEADERBOARD) {
            if (key == KeyEvent.VK_ESCAPE) GamePanel.state = GameState.MENU;
            return;
        }

        // --- MOVIMENTO PLAYER (Solo se stiamo giocando) ---
        if (GamePanel.state == GameState.PLAYING && gamePanel.getPlayer() != null) {
            if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) gamePanel.getPlayer().setLeft(true);
            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) gamePanel.getPlayer().setRight(true);
            if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP || key == KeyEvent.VK_SPACE) gamePanel.getPlayer().setJump(true);
            if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) gamePanel.getPlayer().setDown(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (gamePanel.getPlayer() != null) {
            if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) gamePanel.getPlayer().setLeft(false);
            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) gamePanel.getPlayer().setRight(false);
            if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP || key == KeyEvent.VK_SPACE) gamePanel.getPlayer().setJump(false);
            if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) gamePanel.getPlayer().setDown(false);
        }
    }
}