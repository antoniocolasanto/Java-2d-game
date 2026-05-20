package com.game.graphics;

import com.game.utils.Constants;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MainMenu {

    private Image immagineSfondo;

    public MainMenu() {
        try {
            immagineSfondo = ImageIO.read(new File("res/Sprites/Backgrounds/Double/sfondo.png"));
        } catch (IOException e) {
            System.out.println("Attenzione: Immagine di sfondo del menu non trovata!");
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Antialiasing per rendere i testi perfettamente lisci
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // --- GESTIONE SFONDO ---
        if (immagineSfondo != null) {
            g2.drawImage(immagineSfondo, 0, 0, Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA, null);
        } else {
            GradientPaint gp = new GradientPaint(0, 0, new Color(10, 15, 30), 0, Constants.ALTEZZA_FINESTRA, new Color(25, 42, 70));
            g2.setPaint(gp);
            g2.fillRect(0, 0, Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA);
        }

        // --- TITOLO ---
        String title = "SPACE BUG";
        g2.setFont(new Font("Verdana", Font.BOLD, 85));
        FontMetrics fmTitle = g2.getFontMetrics();
        int titoloX = (Constants.LARGHEZZA_FINESTRA - fmTitle.stringWidth(title)) / 2;
        int titoloY = 250;
        
        // Ombra semplice nera
        g2.setColor(Color.BLACK);
        g2.drawString(title, titoloX + 5, titoloY + 5);
        
        // Colore principale del titolo (Giallo acceso)
        g2.setColor(new Color(255, 215, 0)); 
        g2.drawString(title, titoloX, titoloY);

        // --- ISTRUZIONI ---
        g2.setFont(new Font("Arial", Font.BOLD, 22)); 
        
        drawCenteredText(g2, "Premi [INVIO] per Iniziare", 360, Color.CYAN);
        drawCenteredText(g2, "Premi [ C ] per la Classifica", 430, Color.MAGENTA);
        drawCenteredText(g2, "Premi [ESC] per Uscire", 500, Color.LIGHT_GRAY);
    }

    // Metodo semplificato per disegnare testo centrato con uno sfondo scuro leggibile
    private void drawCenteredText(Graphics2D g2, String text, int y, Color color) {
        FontMetrics fm = g2.getFontMetrics();
        int larghezzaTesto = fm.stringWidth(text);
        
        // Sfondo scuro semi-trasparente
        int paddingX = 30;
        int paddingY = 10;
        int larghezzaSfondo = larghezzaTesto + (paddingX * 2);
        int altezzaSfondo = 20 + (paddingY * 2);
        int rectX = (Constants.LARGHEZZA_FINESTRA - larghezzaSfondo) / 2;
        int rectY = y - fm.getAscent() - paddingY + 5;

        g2.setColor(new Color(0, 0, 0, 180)); // Nero trasparente
        g2.fillRoundRect(rectX, rectY, larghezzaSfondo, altezzaSfondo, 15, 15);

        // Testo
        int testoX = (Constants.LARGHEZZA_FINESTRA - larghezzaTesto) / 2;
        g2.setColor(color);
        g2.drawString(text, testoX, y);
    }
}