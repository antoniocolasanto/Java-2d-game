package com.game.core;

import com.game.entities.Entity;
import com.game.entities.Player;
import com.game.levels.LevelManager;
import com.game.utils.Constants;
import java.util.ArrayList;

/**
 * Gestisce la logica delle collisioni fisiche nel gioco.
 * Verifica le interazioni tra le entità (come il Player o i nemici)
 * e l'ambiente circostante (muri, pavimenti, elementi interagibili).
 */
public class CollisionChecker {
    
    private LevelManager levelManager;

    /**
     * Costruttore della classe CollisionChecker.
     * * @param levelManager L'istanza del gestore dei livelli usata per leggere i dati strutturali della mappa.
     */
    public CollisionChecker(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    /**
     * Controlla le collisioni di un'entità contro i blocchi solidi della griglia (muri, soffitti, pavimenti).
     * Calcola la potenziale posizione futura dell'entità in base alla velocità e alla direzione
     * per prevedere e bloccare il movimento prima che compenetri un ostacolo.
     * * @param entity L'entità fisica in movimento di cui validare il percorso.
     */
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

    /**
     * Verifica la collisione dell'entità contro elementi interattivi e dinamici della mappa,
     * come monete, fonti di danno o punti di checkpoint (salvataggio).
     * * @param entity L'entità che interagisce con gli elementi del livello.
     */
    public void checkCollision(Entity entity) {
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
        if (levelManager.isChekpoint(row, col)) {
                if (entity instanceof Player p) {
                    p.saveWinningSession();
                }
            }

    }
    
    /**
     * Controlla le interazioni fisiche sovrapposte tra il Player e i nemici.
     * Se rileva un'intersezione tra le rispettive hitbox, il Player subisce un danno.
     * * @param player L'istanza del giocatore.
     * @param nemici La lista delle entità nemiche attualmente attive nel livello.
     */
    public void checkEnemyCollision(Player player, ArrayList<Entity> nemici) {
        // Scorriamo tutti i nemici presenti nel livello
        for (Entity nemico : nemici) {
            // Se la hitbox del nemico non è nulla
            if (nemico.getHitbox() != null) {
                // Chiediamo se i due rettangoli si sovrappongono
                if (player.getHitbox().intersects(nemico.getHitbox())) {
                    player.rimuoviVita(); 
                }
            }
        }
    }
}