/**
 * CARICATORE DEI LIVELLI
 * - Legge come è fatta la mappa. Può leggere una matrice di numeri (int[][]) 
 * o decodificare i pixel di un'immagine in cui ogni colore rappresenta un blocco diverso.
 * - Crea i mattoni (Tiles) solidi, l'erba, gli ostacoli basandosi sulle risorse in /res/Sprites/Tiles/.
 * - Fornisce la struttura della mappa alla classe Player per il controllo delle collisioni.
 */

/**
 * Legenda della Mappa: 0 = Vuoto, 1 = Erba, 2 = Terra, 3 = Moneta, 4 = Spuntoni, 5 = Blocco Cassa, 6 = Ponte, 7 = Cartello Uscita.
 */
package com.game.levels;

import com.game.entities.Bee;
import com.game.entities.Entity;
import com.game.entities.Ladybug;
import com.game.utils.Constants;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class LevelManager {
    
    private ArrayList<Entity> nemici;
    private int[][] mapData;
    private BufferedImage[] tileSprites;

    public LevelManager() {
        // Matrice 12 righe x 20 colonne 
        // (per risoluzione 1280x720)
        mapData = new int[12][20];
        // Mappiamo fino a 20 tipi di blocchi diversi
        tileSprites = new BufferedImage[20]; 
        loadTileImages();
        nemici =new ArrayList<Entity>();
        loadMap("res/Map/level1.txt");
    }

    private void loadTileImages() {
        try {
            // Mappatura Terreno
            // 1 erba e 2 terreno sotto
            tileSprites[1] = ImageIO.read(new File("res/Sprites/Tiles/Double/terrain_grass_block_top.png"));
            tileSprites[2] = ImageIO.read(new File("res/Sprites/Tiles/Double/terrain_grass_block_center.png"));
            
            // Monete
            tileSprites[3] = ImageIO.read(new File("res/Sprites/Tiles/Double/coin_gold.png"));
            
            // Spuntoni
            tileSprites[4] = ImageIO.read(new File("res/Sprites/Tiles/Double/spikes.png"));
            
            // Blocco Sospeso
            tileSprites[5] = ImageIO.read(new File("res/Sprites/Tiles/Double/terrain_grass_cloud.png"));
            //Ponte
            tileSprites[6] = ImageIO.read(new File("res/Sprites/Tiles/Double/bridge_logs.png"));
            
            // Bandiera Checkpoint
            tileSprites[7] = ImageIO.read(new File("res/Sprites/Tiles/Double/flag_red_a.png"));

            // Acqua sopra e sotto
            tileSprites[10] = ImageIO.read(new File("res/Sprites/Tiles/Double/water_top.png"));
            tileSprites[11] = ImageIO.read(new File("res/Sprites/Tiles/Double/water.png"));

            // Catena
            tileSprites[12] = ImageIO.read(new File("res/Sprites/Tiles/Double/chain.png"));

            // Piattaforma
            tileSprites[13] = ImageIO.read(new File("res/Sprites/Tiles/Double/bridge.png"));
            
        } catch (IOException e) {
            System.out.println("ERRORE: Impossibile caricare le immagini");
            e.printStackTrace();
        }
    }

    private void loadMap(String filePath) {
        try {
            File file = new File(filePath);
            
            // Se il file .txt non esiste, carica la mappa di riserva
            if (!file.exists()) {
                System.out.println("ATTENZIONE: File della mappa non trovato in " + filePath + ". Carico la mappa di backup.");
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            for (int riga = 0; riga < mapData.length; riga++) {
                String line = br.readLine();
                if (line != null) {
                    // Divide i numeri separati dallo spazio
                    String[] numbers = line.split(" ");
                    for (int col = 0; col < mapData[riga].length && col < numbers.length; col++) {
                        mapData[riga][col] = Integer.parseInt(numbers[col]);
                    }
                }
            }
            br.close();
            
        } catch (Exception e) {
            System.out.println("ERRORE CRITICO: Eccezione durante la lettura della mappa!");
        }
    }

    public void draw(Graphics g) {
        // Scorre la griglia riga per riga e colonna per colonna
        for (int riga = 0; riga < mapData.length; riga++) {
            for (int col = 0; col < mapData[riga].length; col++) {
                
                int tileType = mapData[riga][col];
                
                // Disegna solo se non è vuoto (0) e se l'immagine è stata caricata con successo
                if (tileType != 0 && tileSprites[tileType] != null) {
                    int xAsse = col * Constants.SIZE_BLOCCO;
                    int yAsse = riga * Constants.SIZE_BLOCCO;
                    
                    g.drawImage(tileSprites[tileType], xAsse, yAsse, Constants.SIZE_BLOCCO, Constants.SIZE_BLOCCO, null);
                }
            }
        }
    }
    
    /**
     * getter permetterà di leggere dove sono i blocchi solidi per fermare la gravità del player.
     */
    public int[][] getLevelData() {
        return mapData;
    }

    /**
     * Se il tuo giocatore si trova alle coordinate X = 130 e Y = 70, e ogni
     * blocco (Constants.SIZE_BLOCCO) è grande 64 pixel, il gioco fa una 
     * divisione matematica per capire la sua posizione sulla griglia:
     * Colonna: 130 / 64 = 2 (Si trova nella terza colonna, partendo da 0)
     * Riga: 70 / 64 = 1 (Si trova nella seconda riga, partendo da 0)
     * Sapendo questo, il gioco va a guardare dentro la tua matrice mapData[1][2] 
     * per vedere che numero c'è. Se c'è un 1 (Erba), ti deve bloccare. 
     * Se c'è un 3 (Moneta), la deve raccogliere.
    
    /**
     * Restituisce il numero del blocco a una specifica riga e colonna.
     */
    public int getTileType(int riga, int col) {
        // Controllo di sicurezza: se il player cerca di uscire fuori dai 
        // bordi dello schermo,
        // fingiamo che ci sia un muro invisibile (ritorniamo 1)
        if (riga < 0 || riga >= 12 || col < 0 || col >= 20) {
            return 1; 
        }
        return mapData[riga][col];
    }

    /**
     * Ritorna true se il blocco NON si può attraversare (muri, pavimenti)
     * Legenda: 1 = Erba, 2 = Terra, 5 = Blocco solido, 6 = Ponte, 
     * 7= Checkpoint, 8= Coccinella, 9= Ape, 10= Acqua, 110 Acqua sotto 12= Catena
     * 13= Piattaforma
     */
    public boolean isSolid(int riga, int col) {
        // --- BLINDA I BORDI DELLO SCHERMO ---
        // Se cerchi di andare fuori dalla griglia a sinistra (col < 0) 
        // o in qualsiasi altra direzione, il gioco fa finta che ci sia un muro solido!
        if (col < 0 || col >= 20 || riga < 0 || riga >= 12) {
            return true; 
        }
        
        int tile = getTileType(riga, col);
        if (tile == 1 || tile == 2 || tile == 5 || tile == 6 || tile == 13) {
            return true; // È solido, il player sbatte!
        }
        return false; // Ci si può camminare attraverso (vuoto, monete, ecc.)
    }

    /**
     * Ritorna true se il blocco è una Moneta (3)
     */
    public boolean isCoin(int riga, int col) {
        int tile = getTileType(riga, col);
        return tile == 3;
    }

    public boolean isDamage(int riga, int col) {
        int tile = getTileType(riga, col);
        return tile == 4; // Spuntoni o Api(Api non funziona per ora)
    }

    public boolean isChekpoint(int riga, int col) {
        int tile = getTileType(riga, col);
        return tile == 7; // Spuntoni o Api(Api non funziona per ora)
    }
    
    /**
     * Sostituisce il blocco attuale con il vuoto (0). 
     * Utile quando il player raccoglie la moneta!
     */
    public void removeTile(int riga, int col) {
        if (riga >= 0 && riga < 12 && col >= 0 && col < 20) {
            mapData[riga][col] = 0; // Diventa vuoto
        }
    }

    //mappa i nemici e sostituisce con 0
    public ArrayList<Entity> getListaNemici(){
        for (int riga = 0; riga < mapData.length; riga++) {
            for (int col = 0; col < mapData[riga].length; col++) {
                
                int tileType = mapData[riga][col];
                switch(tileType){
                    //APE
                    case 9:nemici.add(new Bee(col*Constants.SIZE_BLOCCO, riga*Constants.SIZE_BLOCCO,70,70));
                    mapData[riga][col] = 0;
                        break;

                        default: break;
                    case 8:
                    //COCCINELLA
                    nemici.add(new Ladybug(col * Constants.SIZE_BLOCCO, riga * Constants.SIZE_BLOCCO, 60, 60));
                        mapData[riga][col] = 0;
                        break;
                }  
            }
        }
        return nemici;
    }
}