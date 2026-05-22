package com.game.graphics;

import com.game.utils.Constants;
import java.awt.*;

/**
 * GESTIONE DELLA SCHERMATA DI PAUSA
 * - Disegna un overlay semitrasparente per scurire l'azione di gioco in corso.
 * - Mostra un pannello centrale incorniciato.
 * - Fornisce le istruzioni visive per riprendere la partita o uscire al menu.
 */
public class PauseMenu {

    /**
     * Disegna il menu di pausa in sovrimpressione.
     * @param g Il pennello Graphics utilizzato per disegnare
     */
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Overlay semitrasparente (Nero all'80% di opacità) che scurisce il gioco
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, Constants.LARGHEZZA_FINESTRA, Constants.ALTEZZA_FINESTRA);

        // --- CORNICE CENTRALE ---
        // Impostazione delle dimensioni e calcolo per centrare il riquadro
        int w = 500;
        int h = 300;
        int x = (Constants.LARGHEZZA_FINESTRA - w) / 2;
        int y = (Constants.ALTEZZA_FINESTRA - h) / 2;

        // Disegna lo sfondo del riquadro (grigio scuro)
        g2.setColor(new Color(50, 50, 50));
        g2.fillRoundRect(x, y, w, h, 30, 30);
        
        // Disegna il bordo del riquadro (Azzurro, spessore 3px)
        g2.setColor(Color.CYAN);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, w, h, 30, 30);

        // --- TESTO PAUSA ---
        g2.setFont(new Font("Arial", Font.BOLD, 50));
        g2.drawString("PAUSA", x + 160, y + 80);

        // --- OPZIONI DI GIOCO ---
        g2.setFont(new Font("Arial", Font.PLAIN, 25));
        g2.setColor(Color.WHITE);
        g2.drawString("Premi [INVIO] per Continuare", x + 70, y + 160);
        g2.drawString("Premi [ S ] per Menu Principale", x + 70, y + 210);
    }
}