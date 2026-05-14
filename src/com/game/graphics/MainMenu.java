package com.game.graphics;

import com.game.utils.Constants;
import java.awt.*;

public class MainMenu {

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Sfondo con gradiente scuro
        GradientPaint gp = new GradientPaint(0, 0, new Color(20, 30, 48), 0, Constants.ALTEZZA_FINESTRA, new Color(36, 59, 85));
        g2.setPaint(gp);
        g2.fillRect(0, 0, Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA);

        // Disegno Titolo con Ombra
        String title = "SUPER NICOLO'";
        g2.setFont(new Font("Verdana", Font.BOLD, 80));
        
        // Ombra
        g2.setColor(Color.BLACK);
        g2.drawString(title, Constants.LARGHEZZA_FINESTRA / 2 - 325, 205);
        
        // Testo Principale (Giallo acceso)
        g2.setColor(new Color(255, 215, 0));
        g2.drawString(title, Constants.LARGHEZZA_FINESTRA / 2 - 330, 200);

        // Istruzioni
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