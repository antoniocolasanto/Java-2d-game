package com.game.graphics;

/**
 * GESTIONE DELLO SFONDO A LIVELLI (LAYERED)
 * - Gestisce molteplici immagini di sfondo in modo indipendente.
 * - Permette di posizionare ogni pezzo alle coordinate (X, Y) desiderate.
 * - Permette di ripetere (tiling) l'immagine orizzontalmente e/o verticalmente.
 * - Ottimo per preparare il terreno all'effetto Parallax.
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class LayeredBackground {

    /**
     * Classe interna che rappresenta un singolo "pezzo" o livello di sfondo.
     */
    private class Layer {
        BufferedImage image;
        int x, y;
        boolean repeatX, repeatY;

        public Layer(String imagePath, int x, int y, boolean repeatX, boolean repeatY) {
            this.x = x;
            this.y = y;
            this.repeatX = repeatX;
            this.repeatY = repeatY;
            try {
                this.image = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                System.err.println("Errore: Impossibile caricare il livello sfondo da: " + imagePath);
            }
        }
        
        // Disegna questo specifico livello
        public void draw(Graphics g, int screenWidth, int screenHeight) {
            if (image == null) return;
            
            int imgW = image.getWidth();
            int imgH = image.getHeight();
            
            // Calcoliamo da dove a dove dobbiamo disegnare
            int startX = repeatX ? 0 : x;
            int endX = repeatX ? screenWidth : x + imgW;
            
            int startY = repeatY ? 0 : y;
            int endY = repeatY ? screenHeight : y + imgH;

            // Se repeatX o repeatY sono true, questo ciclo riempie lo schermo
            for (int drawX = startX; drawX < endX; drawX += imgW) {
                for (int drawY = startY; drawY < endY; drawY += imgH) {
                    g.drawImage(image, drawX, drawY, imgW, imgH, null);
                }
            }
        }
    }

    // Lista che contiene tutti i nostri livelli sovrapposti
    private ArrayList<Layer> layers;

    public LayeredBackground() {
        layers = new ArrayList<>();
    }

    /**
     * Aggiunge un nuovo pezzo allo sfondo.
     * * @param imagePath Percorso dell'immagine
     * @param x Posizione X di partenza (es. 0)
     * @param y Posizione Y di partenza (es. 400 per metterlo in basso)
     * @param repeatX Se true, ripete l'immagine per tutta la larghezza della finestra
     * @param repeatY Se true, ripete l'immagine per tutta l'altezza della finestra
     */
    public void addLayer(String imagePath, int x, int y, boolean repeatX, boolean repeatY) {
        layers.add(new Layer(imagePath, x, y, repeatX, repeatY));
    }

    /**
     * Disegna tutti i livelli in ordine di inserimento.
     * * @param g Il pennello Graphics
     * @param width La larghezza della finestra
     * @param height L'altezza della finestra
     */
    public void draw(Graphics g, int width, int height) {
        for (Layer layer : layers) {
            layer.draw(g, width, height);
        }
    }
}