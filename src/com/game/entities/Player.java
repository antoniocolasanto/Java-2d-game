package com.game.entities;

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



    public Player(float x, float y, int width, int height) {

        super(x, y, width, height);
        
        //polimorfismo
        caricaImmagine();
    }

    private void caricaImmagine() {
        try {
            File file = new File("res/Sprites/Characters/Default/character_green_idle.png");
            sprite = ImageIO.read(file);
        } catch (IOException e) {
            System.err.println("Errore: Immagine del Player non trovata!");
        }
    }

// restringiamo la heatbox rispetto al padre entity
    @Override
    protected void initHitbox() {
        hitbox = new Rectangle((int) x + 5, (int) y, width - 10, height);
    }

    // Metodo per aggiornare le coordinate del giocatore (logica del movimento, gravità, ecc.)
    @Override
    public void update() {
        aggiornaPosizione();
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
            // mettiamo un "pavimento invisibile" provvisorio a Y = 400.
            if (y >= 400) {
                y = 400;         // Fermalo a questa altezza
                ariaSpeed = 0;   // Azzera la velocità di caduta
                inAria = false;  // Fagli capire che ha toccato terra
            }
        } 
        
        // Infine, aggiorna la coordinata X
        x += xSpeed; 
    }

    
}