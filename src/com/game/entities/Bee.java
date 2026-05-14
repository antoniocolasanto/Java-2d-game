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

// array che conterrà tutti i fotogrammi dell'animazione dell ape
    private BufferedImage[] sprites;
    
    // Variabili per gestire il tempo e i fotogrammi
    private int aniTick = 0;   // cronometro frame
    private int aniIndex = 0;  // indice fotogramma attuale 
    private int aniSpeed = 10; // velocità dell'animazione  
    
    
    // Variabili per il movimento del nemico
    private float beeSpeed = 2.0f;    // velocità ape
    private boolean movingRight = true;

    // Costruttore
    public Bee(float x, float y, int width, int height) {

        super(x, y, width, height);
        caricaImmagine();
    }

private void caricaImmagine() {
//array che contiene 2 immagini per l animazione dell ape
        sprites = new BufferedImage[2];
        //gestiamo eccezione
        try {
            sprites[0] = ImageIO.read(new File("res/Sprites/Enemies/Double/bee_a.png"));
            sprites[1] = ImageIO.read(new File("res/Sprites/Enemies/Double/bee_b.png"));
        } catch (IOException e) {
            System.err.println("Errore: Immagini del Nemico non trovate!");
        }
    }
    // Aggiorna le coordinate del nemico (Logica IA)
    @Override
    public void update() {
        pattuglia();
        updateHitbox();
        aggiornaAnimazione();
    }
    @Override 
    protected void updateHitbox(){
        hitbox.x = (int) x+5;
        hitbox.y = (int) y+20;

    }
        // Inizializza il rettangolo della hitbox sulle coordinate dell'entità
    protected void initHitbox() {
        hitbox = new Rectangle((int) x+5, (int) y+20, width-20, height-30);
    }
    

    private void pattuglia() {
        if (movingRight) {
            x += beeSpeed;
            
            // --- MURO PROVVISORIO A DESTRA ---
            // Quando arriva alla coordinata X = 800, inverte la direzione
            if (x >= 800) {
                movingRight = false;
            }
        } else {
            x -= beeSpeed;
            
            // --- MURO PROVVISORIO A SINISTRA ---
            // Quando torna alla coordinata X = 400, inverte di nuovo la direzione
            if (x <= 400) {
                movingRight = true;
            }
        }
    }

// Disegna il nemico sullo schermo in base all indice attuale aniIndex
    public void draw(Graphics g) {
        if (sprites != null && sprites[aniIndex] != null) {
            g.drawImage(sprites[aniIndex], (int) x, (int) y, width, height, null);
        }
        
        drawHitbox(g);
    }

    private void aggiornaAnimazione() {
        aniTick++; // Il cronometro aumenta di 1 ad ogni frame
        
        // Se il cronometro raggiunge la velocità desiderata (es. 15) resetto il cronometro e passo all immagine successiva
        if (aniTick >= aniSpeed) {
            aniTick = 0; 
            aniIndex++;  
            // Quando scorro tutto l array inizio da capo
            if (aniIndex >= sprites.length) {
                aniIndex = 0;
            }
        }
    }
}