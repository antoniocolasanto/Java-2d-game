package com.game.inputs;

import com.game.core.GamePanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {
    private GamePanel gamePanel;

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Non ci serve per i movimenti
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Quando PREMIAMO un tasto, attiviamo il movimento
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: gamePanel.getPlayer().setUp(true); break;
            case KeyEvent.VK_A: gamePanel.getPlayer().setLeft(true); break;
            case KeyEvent.VK_S: gamePanel.getPlayer().setDown(true); break;
            case KeyEvent.VK_D: gamePanel.getPlayer().setRight(true); break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Quando RILASCIAMO un tasto, fermiamo il movimento
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: gamePanel.getPlayer().setUp(false); break;
            case KeyEvent.VK_A: gamePanel.getPlayer().setLeft(false); break;
            case KeyEvent.VK_S: gamePanel.getPlayer().setDown(false); break;
            case KeyEvent.VK_D: gamePanel.getPlayer().setRight(false); break;
        }
    }
}