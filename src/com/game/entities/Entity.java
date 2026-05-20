package com.game.entities;
/**
 * SUPERCLASSE ASTRATTA
 * - Definisce le proprietà comuni di ogni cosa che si muove: x, y, width, height, speed.
 * - Impone la creazione di una Hitbox (Rectangle) per calcolare le collisioni.
 * - Dichiara i metodi astratti update() e draw(Graphics g) che le sottoclassi 
 * dovranno obbligatoriamente implementare.
 */

import com.game.core.GamePanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
/**
 * Classe astratta che definisce un entità del nostro videogioco
 */
public abstract class Entity {

    protected float x, y;
    protected int width, height;
    
    /**
     *  AGGIUNTE PER LE COLLISIONI
     */ 
    public float speed;
    public String direction = "IDLE";//entità ferma di default
    public boolean collisionOn = false;

    protected GamePanel gamePanel;
    protected Rectangle hitbox;

 /**
  * Costruttore della nostra entità 
  * @param x ascissa di generazione
  * @param y ordinata di generazione
  * @param width largheza dell immagine
  * @param height altezza dell immagine
  */
    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        //polimorfismo
        initHitbox();
    }
/**
 *  Funzioni Getter
 **/ 
    public float getX() { return x; }
    public float getY() { return y; }
    public float getSpeed() { return speed; }
    public Rectangle getHitbox() { return hitbox;}
    public String getDirection() { return direction; }
    public boolean isCollisionOn() { return collisionOn; }

    /**
     * Funzioni Setter
     */
    public void setCollisionOn(boolean collisionOn) {
        this.collisionOn = collisionOn;
    }
    public void setGamePanel(GamePanel gp) {
        this.gamePanel = gp;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * riporta l entità alla posizione indicata
     * @param position_x ascissa  x
     * @param position_y  ordinata y
     */
    public void resetPosition(int position_x, int position_y){
        this.x = position_x;
        this.y = position_y;
    }


    /**
     * Inizializza la hitbox sulle coordinate dell'entità
     */
    protected void initHitbox() {
        hitbox = new Rectangle((int) x, (int) y, width, height);
    }
    /**
     * Aggiorna la posizione della hitbox in base alle coordinate attuali dell'entità
     */
    protected void updateHitbox() {
        hitbox.x = (int) x;
        hitbox.y = (int) y;
    } 
    
  
    /**
     * Metodo astratto per aggiornare lo stato dell'entità.
     */
    public abstract void update();
    /**
     * metodo astratto per disegnare l'entità sullo schermo.
     * @param g oggetto Graphics per disegnare l'entità
     */
    public abstract void draw(Graphics g);


    protected void drawHitbox(Graphics g) {
        g.setColor(Color.PINK);
        g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }

}