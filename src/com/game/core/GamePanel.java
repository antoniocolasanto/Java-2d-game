package com.game.core;

/**
 * MOTORE DEL GIOCO E GAMELOOP (JPanel + Runnable)
 * - È la tela su cui viene disegnato tutto (View) e il motore che aggiorna i dati (Controller).
 * - Contiene il GameLoop (es. un thread che gira a 60 FPS costanti).
 * - Ad ogni "tick" (frame) chiama due metodi fondamentali:
 * 1. update(): aggiorna le posizioni di Player, Nemici e Mappa.
 * 2. repaint(): pulisce lo schermo e ridisegna tutto nelle nuove posizioni.
 * - Gestisce le liste dinamiche (es. ArrayList<Entity> per i nemici).
 */
public class GamePanel {
    // ...
}