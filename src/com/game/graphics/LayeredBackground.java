package com.game.graphics;

/**
 * GESTIONE DELLO SFONDO LAYERED
 * - Concatena multiple immagini di sfondo (da res/Sprites/Backgrounds/Double)
 * - Supporta arrangiamenti orizzontali e verticali
 * - Crea un'unica immagine composita da layer singoli
 * - Utile per creare sfondi parallax stratificati
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LayeredBackground {

    private BufferedImage compositeImage;
    private int tilesHorizontal;
    private int tilesVertical;

    /**
     * Costruttore che carica e concatena le immagini di sfondo.
     * Carica le immagini in ordine, da sinistra a destra, da alto a basso.
     *
     * @param backgroundImages Array di percorsi alle immagini di sfondo
     * @param tilesHorizontal Numero di tile da disporre orizzontalmente
     * @param tilesVertical Numero di tile da disporre verticalmente
     */
    public LayeredBackground(String[] backgroundImages, int tilesHorizontal, int tilesVertical) {
        this.tilesHorizontal = tilesHorizontal;
        this.tilesVertical = tilesVertical;
        
        // Carica la prima immagine per ottenere le dimensioni
        BufferedImage firstImage = loadImage(backgroundImages[0]);
        if (firstImage == null) {
            System.err.println("Errore: Impossibile caricare la prima immagine da: " + backgroundImages[0]);
            return;
        }

        int imgWidth = firstImage.getWidth();
        int imgHeight = firstImage.getHeight();

        // Crea l'immagine composita
        compositeImage = new BufferedImage(
            imgWidth * tilesHorizontal,
            imgHeight * tilesVertical,
            BufferedImage.TYPE_INT_RGB
        );

        Graphics compositeGraphics = compositeImage.getGraphics();

        // Concatena le immagini nella griglia
        int imageIndex = 0;
        for (int y = 0; y < tilesVertical && imageIndex < backgroundImages.length; y++) {
            for (int x = 0; x < tilesHorizontal && imageIndex < backgroundImages.length; x++) {
                BufferedImage img = loadImage(backgroundImages[imageIndex]);
                if (img != null) {
                    compositeGraphics.drawImage(
                        img,
                        x * imgWidth,
                        y * imgHeight,
                        null
                    );
                }
                imageIndex++;
            }
        }
        compositeGraphics.dispose();
    }

    /**
     * Costruttore per un layout semplice: una riga di immagini orizzontale.
     *
     * @param backgroundImages Array di percorsi alle immagini
     */
    public LayeredBackground(String[] backgroundImages) {
        this(backgroundImages, backgroundImages.length, 1);
    }

    /**
     * Carica una singola immagine dal disco.
     *
     * @param imagePath Percorso dell'immagine
     * @return BufferedImage, o null se il file non esiste
     */
    private BufferedImage loadImage(String imagePath) {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("Avviso: Impossibile caricare immagine da: " + imagePath);
            return null;
        }
    }

    /**
     * Disegna lo sfondo composito scalato alla dimensione della finestra.
     *
     * @param g Il pennello Graphics fornito dal GamePanel
     * @param width La larghezza della finestra di gioco
     * @param height L'altezza della finestra di gioco
     */
    public void draw(Graphics g, int width, int height) {
        if (compositeImage != null) {
            g.drawImage(compositeImage, 0, 0, width, height, null);
        }
    }

    /**
     * Ottiene l'immagine composita completa (utile per debug o altre operazioni).
     *
     * @return BufferedImage dell'immagine composita
     */
    public BufferedImage getCompositeImage() {
        return compositeImage;
    }

    /**
     * Ottiene le dimensioni dell'immagine composita.
     *
     * @return Array [larghezza, altezza]
     */
    public int[] getCompositeDimensions() {
        if (compositeImage != null) {
            return new int[]{compositeImage.getWidth(), compositeImage.getHeight()};
        }
        return new int[]{0, 0};
    }
}
