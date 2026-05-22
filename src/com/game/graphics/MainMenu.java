package com.game.graphics;

import com.game.utils.Constants;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * GESTIONE DEL MENU PRINCIPALE
 * - Gestisce il caricamento e il disegno dello sfondo del menu iniziale.
 * - Disegna il titolo del gioco con un effetto ombra.
 * - Mostra le istruzioni a schermo per navigare nei vari menu.
 * - Non ci sono bottoni cliccabili, tutto è gestito tramite input da tastiera.
 */
public class MainMenu {

    private Image immagineSfondo;

    /**
     * Costruttore del MainMenu.
     * Tenta di caricare l'immagine di sfondo specificata dal percorso.
     */
    public MainMenu() {
        try {
            immagineSfondo = ImageIO.read(new File("res/Sprites/Backgrounds/Double/sfondo.png"));
        } catch (IOException e) {
            System.out.println("Attenzione: Immagine di sfondo del menu non trovata!");
        }
    }

    /**
     * Disegna l'intero menu principale a schermo.
     * @param g Il pennello Graphics utilizzato per disegnare
     * variabile g = oggetto che fa parte della famiglia Graphics, accetta solo quell'oggetto specifico 
     * che contiene i metodi per colorare lo schermo (come drawLine, fillRect, drawString).
     */
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Antialiasing per rendere i testi perfettamente lisci e leggibili
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // --- GESTIONE SFONDO ---
        if (immagineSfondo != null) {
            // Se l'immagine è stata caricata, la adatta alle dimensioni della finestra
            g2.drawImage(immagineSfondo, 0, 0, Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA, null);
        }

        // --- TITOLO ---
        String title = "SPACE BUG";
        g2.setFont(new Font("Verdana", Font.BOLD, 85));
        FontMetrics fmTitle = g2.getFontMetrics();
        
        // Calcola le coordinate per centrare orizzontalmente il titolo
        int titoloX = (Constants.LARGHEZZA_FINESTRA - fmTitle.stringWidth(title)) / 2;
        int titoloY = 250;
        
        // Disegna l'ombra del titolo (nera, leggermente spostata)
        g2.setColor(Color.BLACK);
        g2.drawString(title, titoloX + 5, titoloY + 5);
        
        // Disegna il corpo principale del titolo (Giallo acceso)
        g2.setColor(new Color(255, 215, 0)); 
        g2.drawString(title, titoloX, titoloY);

        // --- ISTRUZIONI ---
        g2.setFont(new Font("Arial", Font.BOLD, 22)); 
        
        drawCenteredText(g2, "Premi [INVIO] per Iniziare", 360, Color.CYAN);
        drawCenteredText(g2, "Premi [ C ] per la Classifica", 430, Color.MAGENTA);
        drawCenteredText(g2, "Premi [ESC] per Uscire", 500, Color.LIGHT_GRAY);
    }

    /**
     * Metodo helper per disegnare un testo centrato orizzontalmente, 
     * circondato da un box scuro semitrasparente per migliorarne la leggibilità.
     * * @param g2 Il pennello Graphics2D
     * @param text Il testo da stampare a schermo
     * @param y L'ordinata (altezza) a cui posizionare il testo
     * @param color Il colore desiderato per il testo
     */
    private void drawCenteredText(Graphics2D g2, String text, int y, Color color) {
        FontMetrics fm = g2.getFontMetrics();
        int larghezzaTesto = fm.stringWidth(text);
        
        // Dimensioni e posizionamento dello sfondo scuro semi-trasparente
        int paddingX = 30;
        int paddingY = 10;
        int larghezzaSfondo = larghezzaTesto + (paddingX * 2);
        int altezzaSfondo = 20 + (paddingY * 2);
        int rectX = (Constants.LARGHEZZA_FINESTRA - larghezzaSfondo) / 2;
        int rectY = y - fm.getAscent() - paddingY + 5;

        // Disegna il rettangolo di sfondo con bordi arrotondati
        g2.setColor(new Color(0, 0, 0, 180)); // Nero trasparente
        g2.fillRoundRect(rectX, rectY, larghezzaSfondo, altezzaSfondo, 15, 15);

        // Disegna il testo colorato perfettamente centrato nel riquadro
        int testoX = (Constants.LARGHEZZA_FINESTRA - larghezzaTesto) / 2;
        g2.setColor(color);
        g2.drawString(text, testoX, y);
    }
}