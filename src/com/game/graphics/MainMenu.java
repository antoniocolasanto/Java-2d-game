package com.game.graphics;

import com.game.utils.Constants;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MainMenu {

    // Dichiaro l'immagine dello sfondo
    private Image immagineSfondo;

    // Costruttore: carico l'immagine una sola volta all'avvio
    // carichiamo qui l'immagine perchè in draw ci metterebbe troppo tempo
    public MainMenu() {
        try {
            // Percorso dell'immagine
            immagineSfondo = ImageIO.read(new File("res/Sprites/Backgrounds/Double/sfondo.jpg"));
            
        } catch (IOException e) {
            System.out.println("Attenzione: Immagine di sfondo del menu non trovata! Verrà usato il gradiente di default.");
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // --- GESTIONE SFONDO ---
        if (immagineSfondo != null) {
            // Se l'immagine è stata caricata correttamente, la disegno usando le costanti
            g2.drawImage(immagineSfondo, 0, 0, Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA, null);
        } else {
            // Se c'è stato un errore, uso il gradiente originale
            GradientPaint gp = new GradientPaint(0, 0, new Color(20, 30, 48), 0, Constants.ALTEZZA_FINESTRA, new Color(36, 59, 85));
            g2.setPaint(gp);
            g2.fillRect(0, 0, Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA);
        }

        // --- DISEGNO TITOLO CON OMBRA ---
        String title = "SUPER NICOLO'";
        g2.setFont(new Font("Verdana", Font.BOLD, 80));
        
        // Centriamo il titolo dinamicamente calcolando la larghezza della costante
        FontMetrics fmTitle = g2.getFontMetrics();
        int titoloX = (Constants.LARGHEZZA_FINESTRA - fmTitle.stringWidth(title)) / 2;
        
        // Ombra
        g2.setColor(Color.BLACK);
        g2.drawString(title, titoloX + 5, 205);
        
        // Testo Principale (Giallo acceso)
        g2.setColor(new Color(255, 215, 0));
        g2.drawString(title, titoloX, 200);

        // --- ISTRUZIONI ---
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        drawCenteredText(g2, "Premi [INVIO] per Iniziare", 380, Color.WHITE);
        drawCenteredText(g2, "Premi [ C ] per la Classifica", 450, new Color(200, 200, 200));
        drawCenteredText(g2, "Premi [ESC] per Uscire", 520, new Color(255, 80, 80));
    }

    private void drawCenteredText(Graphics2D g2, String text, int y, Color color) {
        FontMetrics fm = g2.getFontMetrics();
        int x = (Constants.LARGHEZZA_FINESTRA - fm.stringWidth(text)) / 2;
        g2.setColor(color);
        g2.drawString(text, x, y);
    }
}