package com.game.graphics;

import com.game.utils.Constants;
import java.awt.*;

public class PauseMenu {

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Overlay semitrasparente che scurisce il gioco
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA);

        // Cornice centrale
        int w = 500;
        int h = 300;
        int x = (Constants.LARGHEZZA_FINESTRA - w) / 2;
        int y = (Constants.ALTEZZA_FINESTRA - h) / 2;

        g2.setColor(new Color(50, 50, 50));
        g2.fillRoundRect(x, y, w, h, 30, 30);
        g2.setColor(Color.CYAN);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, w, h, 30, 30);

        // Testo Pausa
        g2.setFont(new Font("Arial", Font.BOLD, 50));
        g2.drawString("PAUSA", x + 160, y + 80);

        // Opzioni
        g2.setFont(new Font("Arial", Font.PLAIN, 25));
        g2.setColor(Color.WHITE);
        g2.drawString("Premi [INVIO] per Continuare", x + 70, y + 160);
        g2.drawString("Premi [ S ] per Menu Principale", x + 70, y + 210);
    }
}