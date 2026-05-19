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
    public void keyTyped(KeyEvent e) {
        if (GamePanel.state == GameState.IDENTIFICATION) {
            char c = e.getKeyChar();
            String currentName = gamePanel.getCurrentNickname();
            
            if (Character.isLetterOrDigit(c) && currentName.length() < 15) {
                gamePanel.setCurrentNickname(currentName + c);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // --- GESTIONE MENU PRINCIPALE ---
        if (GamePanel.state == GameState.MENU) {
            if (key == KeyEvent.VK_ENTER) GamePanel.state = GameState.IDENTIFICATION;
            if (key == KeyEvent.VK_C) {
                // Prima di visualizzare la classifica, diciamo al GamePanel di interrogare il DB
                gamePanel.fetchLeaderboard();
                GamePanel.state = GameState.LEADERBOARD;
            }
            if (key == KeyEvent.VK_ESCAPE) System.exit(0);
            return; 
        }

        // --- GESTIONE IDENTIFICAZIONE ---
        if (GamePanel.state == GameState.IDENTIFICATION) {
            String currentName = gamePanel.getCurrentNickname();
            
            if (key == KeyEvent.VK_BACK_SPACE && currentName.length() > 0) {
                gamePanel.setCurrentNickname(currentName.substring(0, currentName.length() - 1));
            }
            
            if (key == KeyEvent.VK_ENTER && currentName.length() > 0) {
                if (gamePanel.getPlayerDAO().playerExists(currentName)) {
                    GamePanel.state = GameState.CONFIRM_PLAYER;
                } else {
                    gamePanel.getPlayerDAO().createNewPlayer(currentName);
                    GamePanel.state = GameState.PLAYING;
                }
            }
            return;
        }

        // --- GESTIONE CONFERMA PROFILO ---
        if (GamePanel.state == GameState.CONFIRM_PLAYER) {
            if (key == KeyEvent.VK_S) GamePanel.state = GameState.PLAYING;
            if (key == KeyEvent.VK_N) {
                gamePanel.setCurrentNickname("");
                GamePanel.state = GameState.IDENTIFICATION;
            }
            return;
        }

        // --- GESTIONE PAUSA ---
        if (key == KeyEvent.VK_ESCAPE) {
            if (GamePanel.state == GameState.PLAYING) GamePanel.state = GameState.PAUSE;
            else if (GamePanel.state == GameState.PAUSE) GamePanel.state = GameState.PLAYING; 
        }

        if (GamePanel.state == GameState.PAUSE) {
            if (key == KeyEvent.VK_ENTER) GamePanel.state = GameState.PLAYING; 
            if (key == KeyEvent.VK_S) GamePanel.state = GameState.MENU; 
            return;
        }

        // --- GESTIONE CLASSIFICA ---
        if (GamePanel.state == GameState.LEADERBOARD) {
            if (key == KeyEvent.VK_ESCAPE) GamePanel.state = GameState.MENU;
            return;
        }

        // --- MOVIMENTO PLAYER ---
        if (GamePanel.state == GameState.PLAYING && gamePanel.getPlayer() != null) {
            if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) gamePanel.getPlayer().setLeft(true);
            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) gamePanel.getPlayer().setRight(true);
            if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP || key == KeyEvent.VK_SPACE) gamePanel.getPlayer().setJump(true);
            if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) gamePanel.getPlayer().setDown(true);
        }
        // --- GESTIONE GAME OVER (DEATH) ---
        if (GamePanel.state == GameState.DEATH) {
            if (key == KeyEvent.VK_ENTER) {
                gamePanel.resetPartita();         // 1. Resetta mappa, monete, tempo e vite a 3
                GamePanel.state = GameState.MENU; // 2. Riporta il giocatore al Menu principale
            }
            return; 
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