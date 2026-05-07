import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;

public class GamePanel extends JPanel {
    
    // IMPOSTAZIONI DELLO SCHERMO
    public final int originalTileSize = 16;
    public final int scale = 3; 
    public final int tileSize = originalTileSize * scale; // I tile a schermo saranno 48x48 pixel
    
    // Dimensioni basate sul tuo file Tiled (32x20)
    public final int maxScreenCol = 32;
    public final int maxScreenRow = 20;
    public final int screenWidth = tileSize * maxScreenCol;   // 1536 pixel
    public final int screenHeight = tileSize * maxScreenRow;  // 960 pixel

    // COMPONENTI DEL GIOCO
    TileManager tileM = new TileManager();
    int[][] mapData;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true); // Migliora le performance grafiche
        
        mapData = new int[maxScreenRow][maxScreenCol];
        loadDummyMap(); // Riempiamo la mappa per fare un test
    }

    // Metodo provvisorio per testare le grafiche senza leggere il CSV
    public void loadDummyMap() {
        // Riempiamo tutto lo schermo con il tile della terra (Indice 14)
        for (int row = 0; row < maxScreenRow; row++) {
            for (int col = 0; col < maxScreenCol; col++) {
                mapData[row][col] = 14; 
            }
        }
        // Piazziamo un cavaliere (Indice 85) e un pipistrello (Indice 120) al centro
        mapData[10][15] = 85; 
        mapData[10][17] = 120;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Disegna l'intera mappa basandosi sui numeri nell'array mapData
        for (int row = 0; row < maxScreenRow; row++) {
            for (int col = 0; col < maxScreenCol; col++) {
                
                int tileIndex = mapData[row][col];
                
                // Calcola le coordinate X e Y dove disegnare il tile
                int x = col * tileSize;
                int y = row * tileSize;
                
                // Se l'indice è valido, disegna l'immagine
                if (tileIndex >= 0 && tileIndex < tileM.tiles.length) {
                    g.drawImage(tileM.tiles[tileIndex], x, y, tileSize, tileSize, null);
                }
            }
        }
        g.dispose(); // Libera la memoria del componente grafico
    }
}