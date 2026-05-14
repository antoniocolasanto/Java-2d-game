package com.game.entities;
/**
 * SUPERCLASSE ASTRATTA
 * - Definisce le proprietà comuni di ogni cosa che si muove: x, y, width, height, speed.
 * - Impone la creazione di una Hitbox (Rectangle) per calcolare le collisioni.
 * - Dichiara i metodi astratti update() e render(Graphics g) che le sottoclassi 
 * dovranno obbligatoriamente implementare.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
// classe astratta che definisce un entita del nostro videogioco
public abstract class Entity {

    protected float x, y;
    protected int width, height;
    
    // AGGIUNTE PER LE COLLISIONI (a tutte le entità)
    public float speed;
    public String direction = "IDLE";
    public boolean collisionOn = false;

    // creo una heatbox per inizaire 
    protected Rectangle hitbox;

    // Costruttore: chi crea il player inserira' dove nascera',x,y e quanto sara' grande
    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        //polimorfismo
        initHitbox();
    }

    // --- GETTER ---
    public float getX() { return x; }
    public float getY() { return y; }
    public float getSpeed() { return speed; }
    public String getDirection() { return direction; }
    public boolean isCollisionOn() { return collisionOn; }

    // --- SETTER ---
    public void setCollisionOn(boolean collisionOn) {
        this.collisionOn = collisionOn;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }
    
        // polimorfismo
    // Inizializza il rettangolo della hitbox sulle coordinate dell'entità
    protected void initHitbox() {
        hitbox = new Rectangle((int) x, (int) y, width, height);
    }

    // Metodo che aggiorna la posizione della hitbox seguendo le X e Y dell'entità
    // Questo andrà chiamato ogni volta che il personaggio si muove!
    protected void updateHitbox() {
        hitbox.x = (int) x;
        hitbox.y = (int) y;
    } 

    public Rectangle getHitbox() {
        return hitbox;
    }

    // funzione che aggiorna le coordinate del personaggio
    public abstract void update();
    // funzione per stampare a schermo l entità
    public abstract void draw(Graphics g);

    // Questo metodo disegnerà un rettangolo rosa attorno al personaggio
    //metodo per il debug
    protected void drawHitbox(Graphics g) {
        g.setColor(Color.PINK);
        g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }

}