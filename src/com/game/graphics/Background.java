package com.game.graphics;

/**
 * GESTIONE DELLO SFONDO SINGOLO
 * - Carica le immagini di background dalla cartella /res/.
 * - (Opzionale per Fase 2) Gestisce l'effetto Parallax: lo sfondo si muove più 
 *   lentamente rispetto al primo piano quando il Player avanza, creando profondità.
 * - Utile per sfondi statici o precomposti. Per sfondi tiled/layered, usare LayeredBackground.
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Background {

    private BufferedImage image;

    /**
     * Costruttore della classe Background.
     * Carica un'immagine singola di sfondo.
     *
     * @param path Percorso dell'immagine di sfondo
     */
    public Background(String path) {
        try {
            // Carichiamo l'immagine dal disco alla memoria RAM
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Errore: Impossibile caricare lo sfondo al percorso: " + path);
        }
    }

    /**
     * Disegna lo sfondo scalato alla dimensione della finestra.
     *
     * @param g Il pennello Graphics fornito dal GamePanel
     * @param width La larghezza della finestra di gioco
     * @param height L'altezza della finestra di gioco
     */
    public void draw(Graphics g, int width, int height) {
        if (image != null) {
            // Disegniamo l'immagine partendo dall'angolo in alto a sinistra (0,0)
            // e forziamo la sua dimensione a coprire tutto il pannello (width, height)
            g.drawImage(image, 0, 0, width, height, null);
        }
    }

    /**
     * Ottiene l'immagine di sfondo caricata.
     *
     * @return BufferedImage dell'immagine di sfondo
     */
    public BufferedImage getImage() {
        return image;
    }
}