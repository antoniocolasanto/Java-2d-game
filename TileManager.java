import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TileManager {
    public BufferedImage[] tiles;

    public TileManager() {
        // La spritesheet di Kenney ha 132 tile (12 colonne x 11 righe)
        tiles = new BufferedImage[132];
        caricaTiles();
    }

    private void caricaTiles() {
        try {
            // ATTENZIONE: Assicurati che "tilemap.png" sia nella cartella principale del tuo progetto
            BufferedImage spritesheet = ImageIO.read(new File("Grafica/tilemap.png"));
            
            int tileSize = 16;
            int spacing = 1;
            int cols = 12;
            int rows = 11;

            int index = 0;
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    
                    // Calcolo esatto per saltare lo spazio di 1 pixel
                    int x = col * (tileSize + spacing);
                    int y = row * (tileSize + spacing);

                    // Taglia il singolo tile e salvalo nell'array
                    tiles[index] = spritesheet.getSubimage(x, y, tileSize, tileSize);
                    index++;
                }
            }
            System.out.println("Tutte le grafiche sono state caricate con successo!");

        } catch (IOException e) {
            System.out.println("Errore: Immagine tilemap.png non trovata!");
            e.printStackTrace();
        }
    }
}