package com.game.core;

import com.game.entities.Entity;
import com.game.entities.Player;
import com.game.levels.LevelManager;
import com.game.utils.Constants;
import java.util.ArrayList;

public class CollisionChecker {
    
    private LevelManager levelManager;

    public CollisionChecker(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    public void checkTile(Entity entity) {
        // Usiamo getHitbox() per le posizioni
        int entityLeftX = (int) entity.getHitbox().getX();
        int entityRightX = (int) entity.getHitbox().getX() + (int) entity.getHitbox().getWidth() - 1;
        int entityTopY = (int) entity.getHitbox().getY();
        int entityBottomY = (int) entity.getHitbox().getY() + (int) entity.getHitbox().getHeight() - 1;

        int entityLeftCol = entityLeftX / Constants.SIZE_BLOCCO;
        int entityRightCol = entityRightX / Constants.SIZE_BLOCCO;
        int entityTopRow = entityTopY / Constants.SIZE_BLOCCO;
        int entityBottomRow = entityBottomY / Constants.SIZE_BLOCCO;

        switch (entity.getDirection()) {
            case "UP":
                int projectedUp = (int)(entityTopY - entity.getSpeed());
                // Controllo Muro Invisibile per il Soffitto (Y < 0)
                if (projectedUp < 0) {
                    entity.setCollisionOn(true);
                } else {
                    entityTopRow = projectedUp / Constants.SIZE_BLOCCO;
                    if (levelManager.isSolid(entityTopRow, entityLeftCol) || levelManager.isSolid(entityTopRow, entityRightCol)) {
                        entity.setCollisionOn(true); 
                    }
                }
                break;
                
            case "DOWN":
                entityBottomRow = (int)(entityBottomY + entity.getSpeed()) / Constants.SIZE_BLOCCO;
                if (levelManager.isSolid(entityBottomRow, entityLeftCol) || levelManager.isSolid(entityBottomRow, entityRightCol)) {
                    entity.setCollisionOn(true);
                }
                break;
                
            case "LEFT":
                int projectedLeft = (int)(entityLeftX - entity.getSpeed());
                // LA MAGIA È QUI: Muro invisibile istantaneo se cerchi di superare il pixel 0 a sinistra
                if (projectedLeft < 0) {
                    entity.setCollisionOn(true);
                } else {
                    entityLeftCol = projectedLeft / Constants.SIZE_BLOCCO;
                    if (levelManager.isSolid(entityTopRow, entityLeftCol) || 
                        levelManager.isSolid(entityBottomRow, entityLeftCol)) {
                        
                        entity.setCollisionOn(true);
                    }
                }
                break;
                
            case "RIGHT":
                entityRightCol = (int)(entityRightX + entity.getSpeed()) / Constants.SIZE_BLOCCO;
                if (levelManager.isSolid(entityTopRow, entityRightCol) || levelManager.isSolid(entityBottomRow, entityRightCol)) {
                    entity.setCollisionOn(true);
                }
                break;
        }
    }

    public void checkCoins(Entity entity) {
        // Usiamo getHitbox() per trovare il centro
        int entityCenterX = (int) (entity.getHitbox().getX() + (entity.getHitbox().getWidth() / 2));
        int entityCenterY = (int) (entity.getHitbox().getY() + (entity.getHitbox().getHeight() / 2));
        
        int col = entityCenterX / Constants.SIZE_BLOCCO;
        int row = entityCenterY / Constants.SIZE_BLOCCO;

        if (levelManager.isCoin(row, col)) {
            levelManager.removeTile(row, col); 
            if (entity instanceof Player p) {
                p.aggiungiMoneta();
            }
        }
        if (levelManager.isDamage(row, col)) {
                if (entity instanceof Player p) {
                    p.rimuoviVita();
                }
            }

    }
    public void checkEnemyCollision(Player player, ArrayList<Entity> nemici) {
        // Scorriamo tutti i nemici presenti nel livello
        for (Entity nemico : nemici) {
            // Se la hitbox del nemico non è nulla...
            if (nemico.getHitbox() != null) {
                // ...ecco la magia di Java! Chiediamo se i due rettangoli si sovrappongono
                if (player.getHitbox().intersects(nemico.getHitbox())) {
                    player.rimuoviVita(); 
                }
            }
        }
    }
}