package com.game.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Ladybug extends Entity {

    //Array per i fotogrammi dell'animazione

    private BufferedImage[] sprites;

    //Variabili per gestire l'aniamazione
    private int aniTick = 0;
    private int aniIndex = 0;
    //Velocità battito ali
    private int aniSpeed = 15;

    //Variabili pe ril range di volo della coccinella
    private float startY;
    private float rangeY = 100.0f;

    //Variabili per il movimento verticale
    //Velocità volo
    private float ladybugSpeed = 2.0f;
    //Direzione di volo (verso l'alto)
    private boolean movingUp = true;

    public Ladybug(float x, float y, int width, int height) {
        super(x, y, width, height);
        //Si salva il punto di partenza della coccinella
        this.startY = y;
        //carica le immagini per l'animazione
        caricaImmagine();
    }

    private void caricaImmagine(){
        sprites = new BufferedImage[2];
        try {
            // Assicurati che i nomi dei file e i percorsi corrispondano a dove hai salvato le immagini
            sprites[0] = ImageIO.read(new File("res/Sprites/Enemies/Double/ladybug_rest.png"));
            sprites[1] = ImageIO.read(new File("res/Sprites/Enemies/Double/ladybug_fly.png"));
        } catch (IOException e) {
            System.err.println("Errore: Immagini della Coccinella non trovate!");
        }
    }

    @Override
    public void update() {
        volaSuEGiù();
        updateHitbox();
        aggiornaAnimazione();
    }

    // Aggiorniamo la hitbox
    @Override 
    protected void updateHitbox() {
        hitbox.x = (int) x + 10;
        hitbox.y = (int) y + 20;
    }

    //Creiamo la hitbox
    @Override
    protected void initHitbox() {
        hitbox = new Rectangle((int) x + 10, (int) y + 20, width - 20, height - 30);
    }

    private void volaSuEGiù() {
        // Se il GamePanel non è ancora caricato, non fare nulla (evita crash)
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

    @Override
    public void draw(Graphics g) {
        if (sprites != null && sprites[aniIndex] != null) {
            g.drawImage(sprites[aniIndex], (int) x, (int) y, width, height, null);
        }
        
        // drawHitbox(g); // Decommenta questa riga se vuoi vedere la hitbox rosa per il debug
    }

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
