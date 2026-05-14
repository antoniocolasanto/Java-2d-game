package com.game.entities;

import com.game.core.GamePanel;
import com.game.utils.Constants;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Entity {

    // L'immagine che rappresenta il giocatore (il suo sprite)
    private BufferedImage sprite;
    // Variabili per gli Input (Tastiera)
    private boolean left, right, jump;
    
    // Costanti per il movimento orizzontale
    private float playerSpeed = 5.0f; // Velocità di camminata

    // Variabili per la fisica verticale (Salto e Gravità)
    private float ariaSpeed = 0f;      // La velocità sull'asse Y (che cambia saltando/cadendo)
    private float gravita = 0.5f;      // Quanto velocemente cade verso il basso
    private float forzaSalto = -10.0f; // Forza iniziale del salto (negativa perché in Java lo Y sale verso il basso)
    private boolean inAria = true;     // Ci dice se il giocatore sta cadendo o è sul pavimento

    //Inizializza GamePanel per poterlo utilizzare (es. collisioni)
    private GamePanel gamePanel;

    public Player(float x, float y, int width, int height, GamePanel gp) {

        super(x, y, width, height);
        this.gamePanel = gp;

        //polimorfismo
        caricaImmagine();
    }

    private void caricaImmagine() {
        try {
            File file = new File("res/Sprites/Characters/Double/character_yellow_idle.png");
            sprite = ImageIO.read(file);
        } catch (IOException e) {
            System.err.println("Errore: Immagine del Player non trovata!");
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
        // 1. Controlla se hai preso monete
        gamePanel.getCollisionChecker().checkCoins(this);
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

    // Metodo per disegnare il giocatore sullo schermo
    public void draw(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, (int) x, (int) y, width, height, null);
        }
        
        // --- SOLO PER TEST ---
        // Richiamiamo il metodo del padre per vedere la hitbox rosa. 
        // Una volta finito il gioco, basterà cancellare questa riga.
        drawHitbox(g);
    }

    private void aggiornaPosizione() {
        // --- PARTE 1: MOVIMENTO ORIZZONTALE (X) ---
        if (left || right) {
            // Diciamo all'entità da che parte stiamo andando
            if (left) setDirection("LEFT");
            if (right) setDirection("RIGHT");

            // FONDAMENTALE 1: Diciamo al CollisionChecker a che velocità andiamo in orizzontale!
            speed = playerSpeed;

            // Resettiamo la collisione e chiediamo al GamePanel di controllare
            setCollisionOn(false);
            gamePanel.getCollisionChecker().checkTile(this);

            // Muoviti sull'asse X SOLO SE il checker dice che non c'è un muro
            if (!isCollisionOn()) {
                if (left) x -= playerSpeed;
                if (right) x += playerSpeed;
            }
        }else {
            setDirection("IDLE");
        }

        updateHitbox();

        // --- PARTE 2: SALTO E GRAVITÀ (Y) ---
        // Se premo salto e sono a terra, inizio a volare
        if (jump && !inAria) {
            ariaSpeed = forzaSalto; // -10.0f mi dà la spinta verso l'alto
            inAria = true;
        }

        // Se sono in aria (o sto saltando in su, o sto cadendo in giù)
        if (inAria) {
            // Applichiamo la gravità alla velocità (ad ogni frame vado sempre più veloce verso il basso)
            ariaSpeed += gravita;

            // Diciamo all'entità se stiamo andando SU o GIÙ per far controllare la testa o i piedi
            if (ariaSpeed < 0) setDirection("UP");
            else setDirection("DOWN");

            // FONDAMENTALE 2: Diciamo al CollisionChecker la nostra velocità di caduta.
            // Usiamo Math.abs() per trasformare numeri negativi (salto) in positivi per il Checker.
            speed = Math.abs(ariaSpeed);

            // Controllo collisioni verticale
            setCollisionOn(false);
            gamePanel.getCollisionChecker().checkTile(this);

            if (!isCollisionOn()) {
                // Se non sbatto, mi muovo verticalmente
                y += ariaSpeed;
            } else {
                // SE SBATTO:
                if (getDirection().equals("DOWN")) {
                    // Sbattevo in GIÙ: sono atterrato sul pavimento!
                    inAria = false;
                    ariaSpeed = 0;
                    // Calcola dove effettivamente stai atterrando (posizione proiettata)
                    int bottomPixel = (int)(hitbox.y + hitbox.height + speed);
                     int tileRow = bottomPixel / Constants.SIZE_BLOCCO;
                    // Allinea la Y al bordo superiore del tile
                    y = (tileRow * Constants.SIZE_BLOCCO) - hitbox.height - 30;
                } else if (getDirection().equals("UP")) {
                    // Sbattevo in SU: ho picchiato la testa sul soffitto!
                    ariaSpeed = 0; // Azzero la velocità di salita, inizio a cadere
                }
            }
        } else {
            // --- CONTROLLO BORDI (EDGE DETECTION) ---
            // Se non sono in aria (cammino a terra), devo assicurarmi che ci sia 
            // ancora il terreno sotto di me. Se cammino oltre un burrone, devo cadere!
            setDirection("DOWN");
            
            // FONDAMENTALE 3: Simulo di cadere di 1 solo pixel per vedere se c'è il vuoto sotto di me
            speed = 1.0f; 
            
            setCollisionOn(false);
            gamePanel.getCollisionChecker().checkTile(this); 
            
            if (!isCollisionOn()) {
                inAria = true; // C'è il vuoto, cadi!
            }
        }
    }   
}