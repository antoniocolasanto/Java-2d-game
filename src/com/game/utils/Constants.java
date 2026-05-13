package com.game.utils;

/**
 * VARIABILI GLOBALI
 * - Raccoglie tutte le costanti di gioco accessibili da chiunque senza istanziare la classe.
 * - Esempi: dimensioni fisse dei blocchi (public static final int TILE_SIZE = 32;), 
 * forza di gravità, velocità di default.
 * - Evita i "magic numbers" (numeri sparsi nel codice senza spiegazione).
 */
public class Constants {
    public static final int LARGHEZZA_FINESTRA = 1280;
    public static final int ALTEZZA_FINESTRA = 720;
    public static final int FPS_SET = 60;
    public static final int SIZE_BLOCCO = 64;
}