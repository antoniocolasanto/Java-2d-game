package com.game.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Entity {
    private BufferedImage image;
    private boolean left, right, up, down; // Direzioni
    private float playerSpeed = 4.0f;      // Velocità di movimento

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadImage();
    }

    private void loadImage() {
        try {
            // Assicurati che il percorso combaci con la tua cartella res!
            File file = new File("res/Sprites/Characters/Default/character_green_idle.png");
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERRORE: Impossibile trovare l'immagine del personaggio!");
        }
    }

    public void update() {
        // Aggiorna le coordinate se il tasto corrispondente è premuto
        if (left) x -= playerSpeed;
        if (right) x += playerSpeed;
        if (up) y -= playerSpeed;
        if (down) y += playerSpeed;
    }

    public void render(Graphics g) {
        // Disegna l'immagine alle coordinate correnti
        g.drawImage(image, (int) x, (int) y, width, height, null);
    }

    // Metodi Set per cambiare lo stato del movimento (usati dalla tastiera)
    public void setLeft(boolean left) { this.left = left; }
    public void setRight(boolean right) { this.right = right; }
    public void setUp(boolean up) { this.up = up; }
    public void setDown(boolean down) { this.down = down; }
}