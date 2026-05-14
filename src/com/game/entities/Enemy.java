/**
 * NEMICO BASE (Estende Entity)
 * - Definisce l'Intelligenza Artificiale (IA) del nemico (es. pattuglia a destra finché non tocca un muro, poi gira a sinistra).
 * - Calcola le collisioni con il Player per capire se infliggere danno o morire.
 */

package com.game.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * NEMICO BASE (Estende Entity)
 * - Definisce l'Intelligenza Artificiale (IA) del nemico (pattuglia a destra finché non tocca un muro, poi gira a sinistra).
 * - Calcola le collisioni con il Player per capire se infliggere danno o morire.
 */
public class Enemy extends Entity {

// L'array che conterrà tutti i fotogrammi dell'animazione dell ape
    private BufferedImage[] sprites;
    
    // Variabili per gestire il tempo e i fotogrammi
    private int aniTick = 0;   // Il nostro cronometro che conta i frame del gioco
    private int aniIndex = 0;  // L'indice del fotogramma attuale 
    private int aniSpeed = 10; // La velocità dell'animazione  
    
    
    // Variabili per il movimento del nemico
    private float enemySpeed = 2.0f;    // I nemici di solito sono un po' più lenti del player
    private boolean movingRight = true; // All'inizio lo facciamo camminare verso destra

    // Costruttore
    public Enemy(float x, float y, int width, int height) {
        super(x, y, width, height); // Chiama il costruttore di Entity per impostare x, y, ecc.
        
        caricaImmagine();
    }

private void caricaImmagine() {
//carichiamo un array che conterra 2 immagini per l animazione dell ape
        sprites = new BufferedImage[2]; 
        
        try {
            // Carichiamo la prima immagine alla posizione 0
            sprites[0] = ImageIO.read(new File("res/Sprites/Enemies/Double/bee_a.png"));
            // Carichiamo la seconda immagine alla posizione 1
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
        // Se movingRight è true, aumenta la X, altrimenti diminuiscila
        if (movingRight) {
            x += enemySpeed;
            
            // --- MURO PROVVISORIO A DESTRA ---
            // Quando arriva alla coordinata X = 800, inverte la direzione
            if (x >= 800) {
                movingRight = false;
            }
        } else {
            x -= enemySpeed;
            
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
        aniTick++; // Il cronometro aumenta di 1 ad ogni giro del gioco
        
        // Se il cronometro raggiunge la velocità desiderata (es. 15)
        if (aniTick >= aniSpeed) {
            aniTick = 0; // Azzera il cronometro
            aniIndex++;  // Passa all'immagine successiva
            
            // Se siamo andati oltre le nostre 2 immagini, torna alla prima (indice 0)
            if (aniIndex >= sprites.length) {
                aniIndex = 0;
            }
        }
    }
}