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

import com.game.utils.Constants;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LevelManager {

    private int[][] mapData;
    private BufferedImage[] tileSprites;

    public LevelManager() {
        // Matrice 12 righe x 20 colonne (per risoluzione 1280x720 con TILE_SIZE = 64)
        mapData = new int[12][20]; 
        tileSprites = new BufferedImage[10]; // Mappiamo fino a 10 tipi di blocchi diversi
        
        loadTileImages();
        loadMap("res/Maps/level1.txt");
    }

    private void loadTileImages() {
        try {
            // Mappatura Terreno (Numeri 1 e 2)
            tileSprites[1] = ImageIO.read(new File("res/Sprites/Tiles/Default/terrain_grass_block.png"));
            tileSprites[2] = ImageIO.read(new File("res/Sprites/Tiles/Default/terrain_dirt_block.png"));
            
            // Collezionabili (Numero 3)
            tileSprites[3] = ImageIO.read(new File("res/Sprites/Tiles/Default/coin_gold.png"));
            
            // Ostacoli Pericolosi (Numero 4)
            tileSprites[4] = ImageIO.read(new File("res/Sprites/Tiles/Default/spikes.png"));
            
            // Blocchi e Piattaforme sospese (Numeri 5 e 6)
            tileSprites[5] = ImageIO.read(new File("res/Sprites/Tiles/Default/block_coin.png"));
            tileSprites[6] = ImageIO.read(new File("res/Sprites/Tiles/Default/bridge.png"));
            
            // Decorazioni / Fine Livello (Numero 7)
            tileSprites[7] = ImageIO.read(new File("res/Sprites/Tiles/Default/sign_exit.png"));
            
        } catch (IOException e) {
            System.out.println("ERRORE: Impossibile caricare alcune immagini dei blocchi! Verifica i percorsi nella cartella res.");
            e.printStackTrace();
        }
    }

    private void loadMap(String filePath) {
        try {
            File file = new File(filePath);
            
            // SISTEMA DI SICUREZZA: Se il file .txt non esiste, carica la mappa di riserva
            if (!file.exists()) {
                System.out.println("ATTENZIONE: File della mappa non trovato in " + filePath + ". Carico la mappa di backup.");
                loadDefaultMap();
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
            loadDefaultMap(); // Carica comunque la mappa di emergenza se c'è un errore
        }
    }

    // MAPPA DI EMERGENZA (Hardcoded in Java nel caso manchi il file testuale)
    private void loadDefaultMap() {
         int[][] defaultMap = {
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
            {0,0,0,0,0,5,0,5,0,0,0,0,0,0,0,3,3,0,2,2},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,2,2},
            {0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,2,2},
            {0,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0,0,0,2,2},
            {0,0,1,2,2,2,0,0,0,1,2,2,2,1,0,0,0,0,2,2},
            {1,1,2,2,2,2,4,4,4,2,2,2,2,2,1,1,0,0,2,2},
            {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,2,2},
            {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,2,2}
        };
        this.mapData = defaultMap;
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
     * Getter vitale per lo Studente A (Antonio).
     * Gli permetterà di leggere dove sono i blocchi solidi per fermare la gravità del player.
     */
    public int[][] getLevelData() {
        return mapData;
    }
}