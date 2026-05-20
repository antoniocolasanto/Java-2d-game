package com.game.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Ladybug extends Entity {

    private BufferedImage[] sprites;

    //Variabili per gestire l'aniamazione
    private int aniTick = 0;
    private int aniIndex = 0;
    //Velocità battito ali
    private int aniSpeed = 15;

    //Variabili per il volo della coccinella
    private float startY;
    private float rangeY = 500.0f;
    private float ladybugSpeed = 2.0f;
    private boolean movingUp = true;
/**
 * costruttore coccinella
 * @param x ascissa di generazione
 * @param y ordinata di generazione
 * @param width
 * @param height
 */
    public Ladybug(float x, float y, int width, int height) {
        super(x, y, width, height);
        this.startY = y;
        caricaImmagine();
    }

    private void caricaImmagine(){
        sprites = new BufferedImage[2];
        try {
            sprites[0] = ImageIO.read(new File("res/Sprites/Enemies/Double/ladybug_rest.png"));
            sprites[1] = ImageIO.read(new File("res/Sprites/Enemies/Double/ladybug_fly.png"));
        } catch (IOException e) {
            System.err.println("Errore: Immagini della Coccinella non trovate!");
        }
    }
    /**
     * Aggiorna lo stato della coccinella, gestisce il movimento e le collisioni.
     */
    @Override
    public void update() {
        volaSuEGiù();
        updateHitbox();
        aggiornaAnimazione();
    }

    // Aggiorniamo la hitbox
    @Override 
    protected void updateHitbox() {
        hitbox.x = (int) x + 5;
        hitbox.y = (int) y + 20;
    }

    //Creiamo la hitbox
    @Override
    protected void initHitbox() {
        hitbox = new Rectangle((int) x + 10, (int) y + 20, width - 10, height - 20);
    }
/**
 * Gestisce il volo della coccinella su e giù, controllando le collisioni con muri, pavimenti e soffitti.
 */
    private void volaSuEGiù() {
        if (gamePanel == null) return; 

        // 1. Diciamo al CollisionChecker a che velocità andiamo
        this.speed = ladybugSpeed;
        // 2. Impostiamo la direzione sull'asse Y
        if (movingUp) {
            setDirection("UP");
        } else {
            setDirection("DOWN");
        }

        // 3. Resettiamo la collisione e chiediamo al GamePanel di controllare i muri/pavimenti/soffitti
        setCollisionOn(false);
        gamePanel.getCollisionChecker().checkTile(this);

        // 4. Se sbattiamo (soffitto o pavimento), invertiamo la rotta
        if (isCollisionOn()) {
            movingUp = !movingUp; 
        } else {
            // Se la strada è libera, continuiamo a volare fino ad un certo range
            if (movingUp) {
                // Se la sua Y attuale è minore o uguale al punto di partenza meno 
                // il range
                if (y <= startY - rangeY) {
                    // Ha raggiunto i massimo range e scende
                    movingUp = false; 
                } else {
                    y -= ladybugSpeed;
                }
            } else {
                // Se la sua Y attuale è maggiore o uguale al punto di partenza più 
                // il range
                if (y >= startY + rangeY) {
                    // Ha raggiunto il minimo range e scende
                    movingUp = true;
                } else {
                    y += ladybugSpeed;
                }
            }
        }
    }

    /**
     * Disegna la coccinella sullo schermo utilizzando l'immagine corrente dell'animazione.
     * @param g oggetto Graphics per disegnare la coccinella
     */
    @Override
    public void draw(Graphics g) {
        if (sprites != null && sprites[aniIndex] != null) {
            g.drawImage(sprites[aniIndex], (int) x, (int) y, width, height, null);
        }
        
        drawHitbox(g);
    }
    /**
     * Aggiorna l'animazione della coccinella cambiando l'immagine visualizzata in base al tempo trascorso.
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
