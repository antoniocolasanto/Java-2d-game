package com.game.inputs;

import com.game.core.GamePanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * ASCOLTATORE DELLA TASTIERA (Implementa KeyListener)
 * - Intercetta la pressione (keyPressed) e il rilascio (keyReleased) dei tasti (W,A,S,D o Frecce).
 * - Trasmette questi eventi boolean (true/false) direttamente al Player per farlo muovere fluidamente.
 */
public class KeyInput implements KeyListener { 
   
    private GamePanel gamePanel;

    // Costruttore che riceve il GamePanel al momento della creazione
    // così sa esattamente a quale partita collegarsi
    public KeyInput(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    // Questo metodo è obbligatorio per il KeyListener, 
    // ma per il movimento, lo lasciamo vuoto
    // non ci serve (si usa più che altro per i campi di testo)
    @Override
    public void keyTyped(KeyEvent e) {
    }


    //È un metodo speciale integrato in Java che viene attivato
    //automaticamente dal computer quando digiti un tasto
    @Override
    public void keyPressed(KeyEvent e) {
        // e.getKeyCode() ci restituisce un numero che 
        // corrisponde al tasto fisico premuto
        int key = e.getKeyCode();

        // Se il player non è ancora nato 
        // evitiamo errori (NullPointerException)
        if (gamePanel.getPlayer() == null) return; 

        // Se premiamo A o Freccia Sinistra, accendiamo 
        // l'interruttore "left"
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            gamePanel.getPlayer().setLeft(true);
        }
        // Se premiamo D o Freccia Destra, 
        // accendiamo l'interruttore "right"
        else if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            gamePanel.getPlayer().setRight(true);
        }
        // Se premiamo W, Freccia Su o Spazio, 
        // accendiamo l'interruttore "jump"
        else if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP || key == KeyEvent.VK_SPACE) {
            gamePanel.getPlayer().setJump(true);
        }
    }

    //È un metodo speciale integrato in Java che viene attivato
    //automaticamente dal computer quando rilasci un tasto
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (gamePanel.getPlayer() == null) return;

        // Quando rilasciamo il tasto, spegniamo l'interruttore (false)
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            gamePanel.getPlayer().setLeft(false);
        }
        else if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            gamePanel.getPlayer().setRight(false);
        }
        else if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP || key == KeyEvent.VK_SPACE) {
            gamePanel.getPlayer().setJump(false);
        }
    }
}