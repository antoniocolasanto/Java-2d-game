package com.game.utils;

public class Constants {
    // Dimensioni di base di uno sprite (es. blocco di terra)
    public static final int TILES_DEFAULT_SIZE = 64; 
    public static final float SCALE = 1.0f; // Utile se in futuro vorrai "zoomare" il gioco
    public static final int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    
    // Dimensioni della griglia del livello (20 colonne x 12 righe)
    public static final int TILES_IN_WIDTH = 20;
    public static final int TILES_IN_HEIGHT = 12;
    
    // Dimensioni finali della finestra del gioco (1280 x 768 pixel)
    public static final int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public static final int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
    
    // Framerate target
    public static final int FPS_SET = 60; // Quante volte lo schermo viene disegnato al secondo
    public static final int UPS_SET = 60; // Quante volte la logica (gravità, movimenti) si aggiorna al secondo
}