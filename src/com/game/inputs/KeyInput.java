package com.game.inputs;

import com.game.core.GamePanel;
import com.game.core.GameState;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Gestore dell'input da tastiera per tutto il gioco.
 * Implementa l'interfaccia KeyListener per intercettare la pressione e il rilascio 
 * dei tasti, dirottando le azioni in base al GameState corrente.
 */
public class KeyInput implements KeyListener { 
   
    private GamePanel gamePanel;

    /**
     * Costruttore della classe KeyInput.
     * * @param gamePanel Il pannello principale su cui agiscono gli input.
     */
    public KeyInput(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * Metodo ignorato, richiesto dall'interfaccia KeyListener.
     * * @param e L'evento da tastiera.
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Cattura il momento esatto in cui un tasto viene premuto.
     * Contiene le logiche per scorrere i menu, attivare abilità, identificare il giocatore
     * e impartire i comandi direzionali al Player.
     * * @param e L'evento da tastiera contenente il codice del tasto fisico premuto.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // GESTIONE MENU PRINCIPALE
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

        // GESTIONE IDENTIFICAZIONE 
        if (GamePanel.state == GameState.IDENTIFICATION) {
            String currentName = gamePanel.getCurrentNickname();
            
            if (key == KeyEvent.VK_BACK_SPACE && currentName.length() > 0) {
                gamePanel.setCurrentNickname(currentName.substring(0, currentName.length() - 1));
            } 
            else if (key == KeyEvent.VK_ENTER) { // Rimosso il controllo della lunghezza qui per poter debuggare
                
                System.out.println("Hai premuto INVIO. Nome attuale: '" + currentName + "'");
                
                if (currentName.trim().isEmpty()) {
                    System.out.println("Non puoi confermare un nome vuoto!");
                    return;
                }

                try {
                    System.out.println("Contatto il Database MongoDB...");
                    gamePanel.getPlayer().setNickname(currentName);
                    
                    if (gamePanel.getPlayerDAO().playerExists(currentName)) {
                        System.out.println("Giocatore trovato! Passo a CONFIRM_PLAYER.");
                        GamePanel.state = GameState.CONFIRM_PLAYER;
                    } else {
                        System.out.println("Nuovo giocatore! Lo creo e passo a PLAYING.");
                        gamePanel.getPlayerDAO().createNewPlayer(currentName);
                        
                        // FIX: Resettiamo la mappa prima di iniziare a giocare!
                        gamePanel.resetPartita();
                        GamePanel.state = GameState.PLAYING;
                    }
                } catch (Exception ex) {
                    System.err.println("ERRORE CRITICO DATABASE: MongoDB è acceso?");
                    System.err.println("Dettaglio errore: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } 
            else {
                char c = e.getKeyChar();
                if (Character.isLetterOrDigit(c) && currentName.length() < 15) {
                    gamePanel.setCurrentNickname(currentName + c);
                }
            }
            return;
        }

        // GESTIONE CONFERMA PROFILO 
        if (GamePanel.state == GameState.CONFIRM_PLAYER) {
            if (key == KeyEvent.VK_S) {
                // FIX: Resettiamo la mappa per il giocatore esistente prima di giocare!
                gamePanel.resetPartita();
                GamePanel.state = GameState.PLAYING;
            }
            if (key == KeyEvent.VK_N) {
                gamePanel.setCurrentNickname("");
                GamePanel.state = GameState.IDENTIFICATION;
            }
            return;
        }

        // GESTIONE PAUSA 
        if (key == KeyEvent.VK_ESCAPE) {
            if (GamePanel.state == GameState.PLAYING) GamePanel.state = GameState.PAUSE;
            else if (GamePanel.state == GameState.PAUSE) GamePanel.state = GameState.PLAYING; 
        }

        if (GamePanel.state == GameState.PAUSE) {
            if (key == KeyEvent.VK_ENTER) GamePanel.state = GameState.PLAYING; 
            if (key == KeyEvent.VK_S) GamePanel.state = GameState.MENU; 
            return;
        }

        // GESTIONE CLASSIFICA
        if (GamePanel.state == GameState.LEADERBOARD) {
            if (key == KeyEvent.VK_ESCAPE) GamePanel.state = GameState.MENU;
            return;
        }

        // MOVIMENTO PLAYER 
        if (GamePanel.state == GameState.PLAYING && gamePanel.getPlayer() != null) {
            if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) gamePanel.getPlayer().setLeft(true);
            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) gamePanel.getPlayer().setRight(true);
            if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP || key == KeyEvent.VK_SPACE) gamePanel.getPlayer().setJump(true);
            if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) gamePanel.getPlayer().setDown(true);
        }
        // GESTIONE GAME OVER (DEATH) 
        if (GamePanel.state == GameState.DEATH) {
            if (key == KeyEvent.VK_ENTER) {
                gamePanel.resetPartita();         // 1. Resetta mappa, monete, tempo e vite a 3
                GamePanel.state = GameState.MENU; // 2. Riporta il giocatore al Menu principale
            }
            return; 
        }
    }
    
    /**
     * Cattura il momento in cui il giocatore rilascia un tasto.
     * Serve prevalentemente per azzerare o interrompere le azioni cicliche (come il movimento o la corsa).
     * * @param e L'evento da tastiera.
     */
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