package com.game.graphics;

import com.game.utils.Constants;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class PauseMenu {

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA);

        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        g.drawString("GIOCO IN PAUSA", Constants.LARGHEZZA_FINESTRA / 2 - 230, 250);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 40));
        g.drawString("Premi [INVIO] -> CONTINUA", Constants.LARGHEZZA_FINESTRA / 2 - 300, 400);
        g.drawString("Premi [ S ]   -> ESCI E SALVA", Constants.LARGHEZZA_FINESTRA / 2 - 300, 500);
    }
}