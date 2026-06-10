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

/**
 * CARICATORE DEI LIVELLI
 * - Legge come è fatta la mappa. Può leggere una matrice di numeri (int[][]) 
 * o decodificare i pixel di un'immagine in cui ogni colore rappresenta un blocco diverso.
 * - Crea i mattoni (Tiles) solidi, l'erba, gli ostacoli basandosi sulle risorse in /res/Sprites/Tiles/.
 * - Fornisce la struttura della mappa alla classe Player per il controllo delle collisioni.
 * * Legenda della Mappa: 0 = Vuoto, 1 = Erba, 2 = Terra, 3 = Moneta, 4 = Spuntoni, 5 = Blocco Cassa, 6 = Ponte, 7 = Cartello Uscita.
 */
public class LevelManager {
    
    private ArrayList<Entity> nemici;
    private int[][] mapData;
    private BufferedImage[] tileSprites;

    /**
     * Costruttore della classe LevelManager.
     * Inizializza le dimensioni della griglia di gioco, alloca l'array per le immagini 
     * dei tasselli e avvia le routine di caricamento degli asset visivi e del file di testo del livello.
     */
    public LevelManager() {
        // Matrice 12 righe x 20 colonne 
        mapData = new int[12][20];
        // Mappiamo fino a 20 tipi di blocchi diversi
        tileSprites = new BufferedImage[20]; 
        loadTileImages();
        nemici = new ArrayList<Entity>();
        loadMap("res/Map/level1.txt");
    }

    /**
     * Carica in memoria RAM tutte le immagini dei singoli elementi grafici (blocchi, monete, ostacoli)
     * prelevandole dalle cartelle delle risorse del progetto.
     */
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

    /**
     * Legge e decodifica il file di configurazione testuale (.txt) associato al livello,
     * popolando riga per riga e colonna per colonna la matrice numerica di gioco.
     * @param filePath Il percorso relativo del file di testo della mappa da caricare.
     */
    private void loadMap(String filePath) {
        try {
            File file = new File(filePath);
            
            // Se il file .txt non esiste, carica la mappa di riserva
            if (!file.exists()) {
                System.out.println("ATTENZIONE: File della mappa non trovato in " + filePath);
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            for (int riga = 0; riga < mapData.length; riga++) {
                String line = br.readLine();
                if (line != null) {
                    //Non può leggere la matrice nel file ma se lo ricrea e se la salva nella matrice 
                    // instanziata nella classe mapData
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

    /**
     * Disegna la griglia visiva del livello a schermo. Scansiona l'intera matrice e,
     * per ogni valore diverso da zero, effettua il rendering dello sprite corrispondente convertendo
     * gli indici della griglia in coordinate pixel effettive.
     * @param g L'oggetto Graphics fornito dal sistema per l'esecuzione del disegno grafico.
     */
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
     * Getter per ottenere i dati grezzi della griglia logica del livello.
     * Permetterà di leggere dove sono i blocchi solidi per fermare la gravità del player.
     * @return La matrice bidimensionale di interi mapData.
     */
    public int[][] getLevelData() {
        return mapData;
    }

    /**
     * Restituisce il numero identificativo del blocco situato a una specifica riga e colonna.
     * Include una gestione di sicurezza per simulare un muro perimetrale invalicabile qualora si tenti
     * di esaminare coordinate al di fuori dei limiti formali dello schermo.
     * @param riga L'indice della riga della griglia da ispezionare.
     * @param col L'indice della colonna della griglia da ispezionare.
     * @return L'identificatore numerico del blocco in quella specifica cella.
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
     * Determina se una cella specifica della mappa è un ostacolo solido non attraversabile.
     * Legenda solidità: 1 = Erba, 2 = Terra, 5 = Blocco solido, 6 = Ponte, 13 = Piattaforma.
     * @param riga La riga della cella da verificare.
     * @param col La colonna della cella da verificare.
     * @return true se il blocco è classificato come solido e provoca un arresto fisico, false altrimenti.
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
     * Verifica se il blocco alla riga e colonna fornite corrisponde a una moneta d'oro raccoglibile.
     * @param riga La riga da analizzare.
     * @param col La colonna da analizzare.
     * @return true se la cella ospita una moneta (valore 3), false in caso contrario.
     */
    public boolean isCoin(int riga, int col) {
        int tile = getTileType(riga, col);
        return tile == 3;
    }

    /**
     * Verifica se il blocco alla riga e colonna fornite rappresenta un pericolo ambientale mortale.
     * @param riga La riga da analizzare.
     * @param col La colonna da analizzare.
     * @return true se la cella contiene un ostacolo dannoso come gli spuntoni (valore 4), false altrimenti.
     */
    public boolean isDamage(int riga, int col) {
        int tile = getTileType(riga, col);
        return tile == 4; // Spuntoni o Api(Api non funziona per ora)
    }

    /**
     * Verifica se la cella ospita la bandiera di traguardo o di salvataggio intermedia.
     * @param riga La riga da analizzare.
     * @param col La colonna da analizzare.
     * @return true se il tassello corrisponde al Checkpoint (valore 7), false altrimenti.
     */
    public boolean isChekpoint(int riga, int col) {
        int tile = getTileType(riga, col);
        return tile == 7; // Spuntoni o Api(Api non funziona per ora)
    }
    
    /**
     * Sostituisce il blocco attuale con il vuoto (0). 
     * Utile quando il player raccoglie la moneta!
     * @param riga La riga logica del blocco da azzerare.
     * @param col La colonna logica del blocco da azzerare.
     */
    public void removeTile(int riga, int col) {
        if (riga >= 0 && riga < 12 && col >= 0 && col < 20) {
            mapData[riga][col] = 0; // Diventa vuoto
        }
    }

    /**
     * Scansiona l'intera matrice alla ricerca di indicatori numerici riservati ai nemici dinamici (8 e 9).
     * Quando ne individua uno, istanzia l'oggetto della classe specifica passandogli le coordinate di spawn calcolate,
     * ripulisce la casella della mappa impostandola a vuoto (0) e aggiunge il nemico alla lista ritornata al motore grafico.
     * @return L'ArrayList popolata con tutte le istanze delle entità nemiche pronte all'azione nel livello.
     */
    public ArrayList<Entity> getListaNemici(){
        for (int riga = 0; riga < mapData.length; riga++) {
            for (int col = 0; col < mapData[riga].length; col++) {
                
                int tileType = mapData[riga][col];
                switch(tileType){
                    //APE
                    case 9:
                        nemici.add(new Bee(col*Constants.SIZE_BLOCCO, riga*Constants.SIZE_BLOCCO,70,70));
                        mapData[riga][col] = 0;
                        break;

                    case 8:
                        //COCCINELLA
                        nemici.add(new Ladybug(col * Constants.SIZE_BLOCCO, riga * Constants.SIZE_BLOCCO, 60, 60));
                        mapData[riga][col] = 0;
                        break;
                        
                    default: 
                        break;
                }  
            }
        }
        return nemici;
    }
}