/**
 * NEMICO APE (Estende Entity)
 * - Definisce l'Intelligenza Artificiale (IA) del nemico pattuglia.
 * - Calcola le collisioni con il Player per capire se infliggere danno o morire.
 */

package com.game.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Bee extends Entity {


    private BufferedImage[] sprites;
    
    // Variabili per gestire il tempo e i fotogrammi
    private int aniTick = 0;
    private int aniIndex = 0;
    private int aniSpeed = 10;
    
    // Variabili per il range di volo delle api
    private float startX;
    private float rangeX = 200.0f;

    // Variabili per il movimento dell ape
    private float beeSpeed = 2.0f;    // velocità ape
    private boolean movingRight = true;

    /**
     * Costruttore del nemico Ape
      * @param x ascissa di generazione
      * @param y ordinata di generazione
      * @param width larghezza dell immagine
      * @param height altezza dell immagine
     */
    public Bee(float x, float y, int width, int height) {
        super(x, y, width, height);
        startX = x;
        caricaImmagine();
    }
/**
 * Carica le immagini dell'ape e le memorizza nell'array sprites.
 */
private void caricaImmagine() {
        sprites = new BufferedImage[2];
        //gestiamo eccezione
        try {
            sprites[0] = ImageIO.read(new File("res/Sprites/Enemies/Double/bee_a.png"));
            sprites[1] = ImageIO.read(new File("res/Sprites/Enemies/Double/bee_b.png"));
        } catch (IOException e) {
            System.err.println("Errore: Immagini del Nemico non trovate!");
        }
    }
    /**
     * Aggiorna lo stato dell'ape, gestisce il movimento e le collisioni.
     */
    @Override
    public void update() {
        pattuglia();
        updateHitbox();
        aggiornaAnimazione();
    }
    /**
     * Aggiorna la posizione della hitbox in base alla posizione attuale dell'ape.
     */
    @Override 
    protected void updateHitbox(){
        hitbox.x = (int) x+5;
        hitbox.y = (int) y+20;

    }
        /**
         * inizializza il rettangolo dell hitbox adattandola all immagine
         */
    protected void initHitbox() {
        hitbox = new Rectangle((int) x+5, (int) y+20, width-20, height-30);
    }
    
    /**
     * implementa il movimento dell ape
     */
    private void pattuglia() {
        if (gamePanel == null) return; 

        this.speed = beeSpeed;         
        if (movingRight) {
            setDirection("RIGHT");
        } else {
            setDirection("LEFT");
        }

        setCollisionOn(false);
        gamePanel.getCollisionChecker().checkTile(this);

        if (isCollisionOn()) {
            movingRight = !movingRight;
        } else {
            if (movingRight) {
                if (x >= startX + rangeX) {
                    movingRight = false; 
                } else {
                    x += beeSpeed;
                }
            } else {
                if (x <= startX - rangeX) {
                    movingRight = true;
                } else {
                    x -= beeSpeed;
                }
            
            }
        }
    }
    /**
     * Disegna l'ape sullo schermo utilizzando l'immagine corrente dell'animazione.
      * @param g oggetto Graphics per disegnare l'ape
     */
    public void draw(Graphics g) {
        if (sprites != null && sprites[aniIndex] != null) {
            g.drawImage(sprites[aniIndex], (int) x, (int) y, width, height, null);
        }   
    }
    /**
     * Aggiorna l'animazione dell'ape cambiando l'immagine visualizzata in base al tempo trascorso.
     */
    private void aggiornaAnimazione() {  
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0; 
            aniIndex++;  
            if (aniIndex >= sprites.length) {
                aniIndex = 0;
            }
        }
    }
}