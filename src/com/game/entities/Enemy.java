/**
 * NEMICO BASE (Estende Entity)
 * - Definisce l'Intelligenza Artificiale (IA) del nemico (es. pattuglia a destra finché non tocca un muro, poi gira a sinistra).
 * - Calcola le collisioni con il Player per capire se infliggere danno o morire.
 */

package com.game.entities;

import java.awt.Graphics;
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

    // L'immagine che rappresenta il nemico
    private BufferedImage sprite;
    
    // Variabili per il movimento del nemico
    private float enemySpeed = 2.0f;    // I nemici di solito sono un po' più lenti del player
    private boolean movingRight = true; // All'inizio lo facciamo camminare verso destra

    // Costruttore
    public Enemy(float x, float y, int width, int height) {
        super(x, y, width, height); // Chiama il costruttore di Entity per impostare x, y, ecc.
        
        caricaImmagine();
    }

    private void caricaImmagine() {
        try {
            // NOTA: Controlla che questo file esista nella tua cartella del progetto!
            // Ho inserito un nome generico basato sulla struttura delle tue cartelle.
            File file = new File("res/Sprites/Enemies/Default/bee_a.png");
            sprite = ImageIO.read(file);
        } catch (IOException e) {
            System.err.println("Errore: Immagine del Nemico non trovata al percorso specificato!");
        }
    }

    // Aggiorna le coordinate del nemico (Logica IA)
    @Override
    public void update() {
        pattuglia();
        updateHitbox(); // Aggiorna la hitbox rosa seguendo le nuove coordinate
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

    // Disegna il nemico sullo schermo
    public void draw(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, (int) x, (int) y, width, height, null);
        }
        
        // --- SOLO PER TEST ---
        // Mostriamo la hitbox rosa per verificare che segua il nemico
        drawHitbox(g);
    }
}
