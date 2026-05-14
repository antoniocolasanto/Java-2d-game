package com.game.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Entity {

    // L'array di immagini che rappresenta il giocatore (il suo sprite)
    private BufferedImage[] sprites;

    // Variabili per gli Input (Tastiera)
    private boolean left, right, jump, down;

        // Variabili per gestire il tempo e i fotogrammi
    private int aniTick = 0;   // Il nostro cronometro che conta i frame del gioco
    private int aniIndex = 0;  // L'indice del fotogramma attuale 
    private int aniSpeed = 10; // La velocità dell'animazione 
    
    // Costanti per il movimento orizzontale
    private float playerSpeed = 5.0f; // Velocità di camminata

    // Variabili per la fisica verticale (Salto e Gravità)
    private float ariaSpeed = 0f;      // La velocità sull'asse Y (che cambia saltando/cadendo)
    private float gravita = 0.5f;      // Quanto velocemente cade verso il basso
    private float forzaSalto = -10.0f; // Forza iniziale del salto (negativa perché in Java lo Y sale verso il basso)
    private boolean inAria = true;     // Ci dice se il giocatore sta cadendo o è sul pavimento



    public Player(float x, float y, int width, int height) {

        super(x, y, width, height);
        
        //polimorfismo
        caricaImmagine();
    }

private void caricaImmagine() {
//carichiamo un array che conterra 4 immagini per l animazione del Player
        sprites = new BufferedImage[5]; 
        if (!jump) {
            
        }
        try {
            // Carichiamo la prima immagine alla posizione 0 abbassato
            sprites[0] = ImageIO.read(new File("res/Sprites/Characters/Double/character_yellow_duck.png"));
            // Carichiamo la seconda immagine alla posizione fermo
            sprites[1] = ImageIO.read(new File("res/Sprites/Characters/Double/character_yellow_jump.png"));
            // Carichiamo la terza  immagine alla posizione salto
            sprites[2] = ImageIO.read(new File("res/Sprites/Characters/Double/character_yellow_idle.png"));
            // Carichiamo la quarta immagine alla posizione movimento1
            sprites[3] = ImageIO.read(new File("res/Sprites/Characters/Double/character_yellow_walk_a.png"));
            // Carichiamo la quinta immagine alla posizione movimento2
            sprites[4] = ImageIO.read(new File("res/Sprites/Characters/Double/character_yellow_walk_b.png"));

            File file = new File("res/Sprites/Characters/Double/character_yellow_idle.png");
            sprite = ImageIO.read(file);
        } catch (IOException e) {
            System.err.println("Errore: Immagini del Player non trovate!");
        }
    }

// restringiamo la heatbox rispetto al padre entity
    @Override
    protected void initHitbox() {
        hitbox = new Rectangle((int) x+23, (int) y+30, width-50, height-31);
    }
        @Override
    protected void updateHitbox() {
        hitbox.x = (int) x+23;
        hitbox.y = (int) y+30;

    }

    // Metodo per aggiornare le coordinate del giocatore (logica del movimento, gravità, ecc.)
    @Override
    public void update() {
        aggiornaPosizione();
        aggiornaAnimazione();
        updateHitbox();
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }
      public void setDown(boolean down) {
        this.down = down;
    }


    // Metodo per disegnare il giocatore sullo schermo in base all indice aniIndex
    public void draw(Graphics g) {
        if (sprites != null && sprites[aniIndex] != null) {
            g.drawImage(sprites[aniIndex], (int) x, (int) y, width, height, null);
        }
        
        drawHitbox(g);
    }

    private void aggiornaPosizione() {
        // 1. MOVIMENTO ORIZZONTALE
        // Azzera temporaneamente il movimento orizzontale per questo frame
        float xSpeed = 0; 
        
        if (left) xSpeed -= playerSpeed;
        if (right) xSpeed += playerSpeed;

        // 2. IL SALTO
        // Se premo salto e NON sono in aria, dammi la spinta verso l'alto!
        if (jump && !inAria) {
            ariaSpeed = forzaSalto;
            inAria = true;
        }

        // 3. APPLICARE LA GRAVITÀ
        // Se non stiamo toccando terra, continuiamo a cadere
        if (inAria) {
            ariaSpeed += gravita; // La gravità aumenta la velocità di caduta frame dopo frame
            y += ariaSpeed;       // Aggiorna la coordinata Y
            
            // --- PAVIMENTO PROVVISORIO ---
            // Siccome non hai ancora il codice per controllare se sbatti contro i blocchi di terra,
            // mettiamo un "pavimento invisibile" provvisorio a Y = 500.
            if (y >= 450) {
                y = 450;         // Fermalo a questa altezza
                ariaSpeed = 0;   // Azzera la velocità di caduta
                inAria = false;  // Fagli capire che ha toccato terra
            }
        } 
        
        // Infine, aggiorna la coordinata X
        x += xSpeed; 
    }
 
private void aggiornaAnimazione() {
        // 1. Se siamo in aria, mostra sempre l'immagine del salto
        if (inAria) {
            aniIndex = 1;
        } 
        // 2. Se stiamo premendo il tasto per abbassarci
        else if (down) {
            aniIndex = 0;
        } 
        // 3. Se ci stiamo muovendo a destra o a sinistra, facciamo l'animazione della camminata!
        else if (left || right) {
            aniTick++; // Facciamo partire il cronometro
            
            if (aniTick >= aniSpeed) {
                aniTick = 0; // Azzera il cronometro
                
                // Alterna tra l'immagine 3 (walk_a) e l'immagine 4 (walk_b)
                if (aniIndex == 3) {
                    aniIndex = 4;
                } else {
                    aniIndex = 3;
                }
            }
        } 
        // 4. Se non stiamo facendo nessuna di queste cose, siamo fermi (indice 2)
        else {
            aniIndex = 2;
        }
    }
    
}