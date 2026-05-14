package com.game.graphics;

// IMPORTANTE: Senza questa riga, la classe non sa dove trovare LARGHEZZA_FINESTRA
import com.game.utils.Constants; 
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class MainMenu {

    public void draw(Graphics g) {
        g.setColor(new Color(20, 20, 20));
        g.fillRect(0, 0, Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA);

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 70));
        g.drawString("SUPER NICOLO", Constants.LARGHEZZA_FINESTRA / 2 - 250, 200);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 40));
        g.drawString("Premi [INVIO] -> START", Constants.LARGHEZZA_FINESTRA / 2 - 200, 350);
        g.drawString("Premi [ C ]   -> CLASSIFICA", Constants.LARGHEZZA_FINESTRA / 2 - 200, 450);
        g.drawString("Premi [ESC]   -> ESCI", Constants.LARGHEZZA_FINESTRA / 2 - 200, 550);
    }
}